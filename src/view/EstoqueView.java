package view;

//Classes de controle e de produtos
import controller.ProdutoController;
import model.Produto;
import model.Usuario;
import model.UsuarioDAO;
import model.Fornecedor;
import model.FornecedorDAO;

//Imports para interface:
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class EstoqueView extends JFrame{
	
	private ProdutoController controller;
	private Usuario usuarioLogado;
	private UsuarioDAO usuarioDAO;
	private FornecedorDAO fornecedorDAO;
	
	//Componentes de interface:
	private JTextField txtId;
	private JTextField txtNome;
	private JTextField txtQuantidade;
	private JCheckBox chkPerecivel;
	private JComboBox<String> cbFornecedor;
	private JButton btnCadastrar;
	private JTable tabelaProdutos;
	private DefaultTableModel modeloTabela;
	private boolean modoAtualizacaoAtivo = false;
	
	// Componentes para gerenciamento de usuários (apenas para administradores)
	private JTabbedPane abas;
	private JPanel painelGerenciarUsuarios;
	

	
	public EstoqueView(Usuario usuario) {
		this.usuarioLogado = usuario;
		this.controller = new ProdutoController();
		this.usuarioDAO = new UsuarioDAO();
		this.fornecedorDAO = new FornecedorDAO();
		
		//Configurações de screen:
		setTitle("Controle de Estoque - " + usuario.getNome() + " (" + usuario.getTipo() + ")");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 1000);
		setLocationRelativeTo(null);
		
		//Painel com abas:
		abas = new JTabbedPane();
		abas.setPreferredSize(new Dimension(330, getHeight())); 
		
		abas.addChangeListener(e->{
			int abaSelecionada = abas.getSelectedIndex();
			String tituloAba = abas.getTitleAt(abaSelecionada);
			modoAtualizacaoAtivo = true; // Sempre permitir edição
			
			// Atualizar a lista de acordo com a aba selecionada
			switch (tituloAba) {
				case "Produtos":
					atualizarLista(1); // Lista de produtos
					break;
				case "Gerenciar Usuários":
					atualizarLista(2); // Lista de gerentes
					break;
				case "Fornecedores":
					atualizarLista(3); // Lista de fornecedores
					break;
			}
		});
		
		//Aba de produtos:
		JPanel painelProdutos = criarPainelProdutos();
		abas.addTab("Produtos", painelProdutos);

		// Aba de gerenciamento de usuários (apenas para administradores)
		if (usuarioLogado.podeGerenciarUsuarios()) {
			painelGerenciarUsuarios = criarPainelGerenciarUsuarios();
			abas.addTab("Gerenciar Usuários", painelGerenciarUsuarios);
			JPanel painelFornecedores = criarPainelFornecedores();
			abas.addTab("Fornecedores", painelFornecedores);
		}
		
		//Área de exibição dos produtos + interação com a tabela:
		modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Quantidade", "Perecível", "Status", "Fornecedor"}, 0) {
		    
			@Override
		    public boolean isCellEditable(int row, int column) {
				if (column == 1 || column == 2) {
					// Verificar permissão para atualizar produtos
					if (!usuarioLogado.podeAtualizarProduto()) {
		        		JOptionPane.showMessageDialog(null,
								"Você não tem permissão para atualizar produtos.",
								"Permissão Negada", JOptionPane.WARNING_MESSAGE);
		        		return false;
		        	}
		        	return true;
		        }
		        return false;
		    }
		};

		tabelaProdutos = new JTable(modeloTabela);
		
		// Adicionar listener para clique na coluna de fornecedor
		tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = tabelaProdutos.rowAtPoint(evt.getPoint());
				int col = tabelaProdutos.columnAtPoint(evt.getPoint());
				
				if (row >= 0 && col == 5) { // Coluna do fornecedor
					String fornecedorNome = (String) modeloTabela.getValueAt(row, col);
					if (fornecedorNome != null && !fornecedorNome.equals("Não informado")) {
						mostrarDetalhesFornecedor(fornecedorNome);
					}
				}
			}
		});
		
		modeloTabela.addTableModelListener(e -> {
		    int row = e.getFirstRow();
		    int column = e.getColumn();

		    if (row >= 0 && (column == 1 || column == 2)) {
		        int id = (int) modeloTabela.getValueAt(row, 0);
		        Produto produto = controller.buscarPorId(id);

		        if (produto != null) {
		        	boolean alterado = false;
		        	
		            if (column == 1) {
		                // Atualiza o nome
		                produto.setNome((String) modeloTabela.getValueAt(row, column));
		                alterado = true;
		            } else if (column == 2) {
		                try {
		                    int novaQuantidade = Integer.parseInt(modeloTabela.getValueAt(row, column).toString());
		                    produto.setQuantidade(novaQuantidade);
		                    alterado = true;
		                } catch (NumberFormatException ex) {
		                    JOptionPane.showMessageDialog(this, "Quantidade inválida.");
		                    atualizarLista(1); // Restaurar valor anterior
		                    return;
		                }
		            }
		            
		            if(alterado) {
		            	boolean sucesso = controller.atualizarProduto(produto);
		            	if(!sucesso) {
		            		JOptionPane.showMessageDialog(this, "Erro ao atualizar produto no banco de dados");
		            	}
		            }
		            
		            atualizarLista(1); // Atualiza o status visualmente
		        }
		    }
		});

		JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, abas, scrollPane);
		splitPane.setDividerLocation(330);
		splitPane.setResizeWeight(0.3);
		add(splitPane);
				
		atualizarLista(1);
	}
	
	// Construtor padrão para compatibilidade
	public EstoqueView() {
		this(new model.Administrador("admin", "admin123"));
	}
	
	private JPanel criarPainelProdutos() {
		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
		painel.setBorder(BorderFactory.createTitledBorder("Gerenciar Produtos"));
		
		//Campo de ID:
		txtId = ajustarCampo(new JTextField(40));
		painel.add(criarLinha("ID:", txtId));
		
		//Campo de nome:
		txtNome = ajustarCampo(new JTextField(5));
		painel.add(criarLinha("Nome:", txtNome));
		
		//Campo de quantidade:
		txtQuantidade = ajustarCampo(new JTextField(20));
		painel.add(criarLinha("Quantidade:", txtQuantidade));
		
		//Campo perecível:
		chkPerecivel = new JCheckBox();
		painel.add(criarLinha("Perecível:", chkPerecivel));
		
		// Campo fornecedor
		cbFornecedor = new JComboBox<>();
		cbFornecedor.addItem("Selecione um fornecedor");
		for (Fornecedor f : fornecedorDAO.listarTodosFornecedores()) {
			cbFornecedor.addItem(f.getNome());
		}
		painel.add(criarLinha("Fornecedor:", cbFornecedor));
		
		//Botões:
		JPanel painelBotoes = new JPanel();
		painelBotoes.setLayout(new FlowLayout());
		
		btnCadastrar = new JButton("Cadastrar Produto");
		btnCadastrar.addActionListener(e -> cadastrarProduto());
		painelBotoes.add(btnCadastrar);
		
		painel.add(painelBotoes);
		
		// Botão para remover produto selecionado (separado)
		JButton btnRemoverSelecionado = new JButton("Remover Produto Selecionado");
		btnRemoverSelecionado.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRemoverSelecionado.addActionListener(e -> removerProdutoSelecionado());
		painel.add(btnRemoverSelecionado);
		
		// Adicionar informação sobre edição
		JLabel lblInfo = new JLabel("<html><body style='width: 200px; text-align: center;'>" +
				"<p>Para editar um produto, clique diretamente sobre o nome ou quantidade na tabela.</p>" +
				"<p>Para remover, selecione o produto na tabela e clique em 'Remover Produto Selecionado'.</p>" +
				"</body></html>");
		lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblInfo.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		painel.add(lblInfo);
		
		return painel;
	}
	
	private void removerProdutoSelecionado() {
		int linhaSelecionada = tabelaProdutos.getSelectedRow();
		if (linhaSelecionada >= 0) {
			if (!usuarioLogado.podeRemoverProduto()) {
				JOptionPane.showMessageDialog(this,
						"Você não tem permissão para remover produtos.",
						"Permissão Negada", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
			int confirmacao = JOptionPane.showConfirmDialog(this,
					"Tem certeza que deseja remover este produto?",
					"Confirmar Remoção",
					JOptionPane.YES_NO_OPTION);
			
			if (confirmacao == JOptionPane.YES_OPTION) {
				boolean sucesso = controller.excluiProduto(id);
				if (sucesso) {
					JOptionPane.showMessageDialog(this,
							"Produto removido com sucesso!",
							"Sucesso",
							JOptionPane.INFORMATION_MESSAGE);
					atualizarLista(1);
				} else {
					JOptionPane.showMessageDialog(this,
							"Erro ao remover o produto.",
							"Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"Selecione um produto para remover.",
					"Aviso",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void cadastrarProduto() {
		// Verificar permissão para adicionar produtos
		if (!usuarioLogado.podeAdicionarProduto()) {
			JOptionPane.showMessageDialog(this, 
					"Você não tem permissão para adicionar produtos.",
					"Permissão Negada", 
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			int id = Integer.parseInt(txtId.getText());
			String nome = txtNome.getText();
			int quantidade = Integer.parseInt(txtQuantidade.getText());
			boolean perecivel = chkPerecivel.isSelected();
			// Obter fornecedor selecionado
			Fornecedor fornecedorSelecionado = null;
			if (cbFornecedor != null && cbFornecedor.getSelectedIndex() > 0) { // Se não for "Selecione um fornecedor"
				String fornecedorNome = (String) cbFornecedor.getSelectedItem();
				fornecedorSelecionado = fornecedorDAO.buscarPorNome(fornecedorNome);
			}
			Produto novo = new Produto(id, nome, quantidade, perecivel, fornecedorSelecionado);
			boolean sucesso = controller.cadastrarProduto(novo);
			if(sucesso) {
				atualizarLista(1);
				limparCampos();
				JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao cadastrar produto.\nVerifique se o ID já existe ou se os dados estão corretos.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "ID e quantidade devem ser números inteiros.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Erro inesperado ao cadastrar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
	
	private JPanel criarPainelGerenciarUsuarios() {
		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
		painel.setBorder(BorderFactory.createTitledBorder("Gerenciar Usuários"));
		
		// Campos para adicionar usuário
		JTextField txtNomeUsuario = new JTextField(20);
		JPasswordField txtSenhaUsuario = new JPasswordField(20);
		JComboBox<String> cbTipoUsuario = new JComboBox<>(new String[]{"GERENTE", "ADMINISTRADOR"});
		
		painel.add(criarLinha("Nome:", ajustarCampo(txtNomeUsuario)));
		painel.add(criarLinha("Senha:", ajustarCampo(txtSenhaUsuario)));
		painel.add(criarLinha("Tipo:", cbTipoUsuario));
		
		JButton btnAdicionarUsuario = new JButton("Adicionar Usuário");
		btnAdicionarUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		painel.add(btnAdicionarUsuario);
		
		JButton btnRemoverUsuario = new JButton("Remover Usuário Selecionado");
		btnRemoverUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		painel.add(btnRemoverUsuario);
		
		// Eventos
		btnAdicionarUsuario.addActionListener(e -> {
			String nome = txtNomeUsuario.getText().trim();
			String senha = new String(txtSenhaUsuario.getPassword());
			String tipo = (String) cbTipoUsuario.getSelectedItem();
			
			if (nome.isEmpty() || senha.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
				return;
			}
			
			Usuario novoUsuario;
			if ("ADMINISTRADOR".equals(tipo)) {
				novoUsuario = new model.Administrador(nome, senha);
			} else {
				novoUsuario = new model.Gerente(nome, senha);
			}
			
			if (usuarioDAO.adicionarUsuario(novoUsuario)) {
				JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
				txtNomeUsuario.setText("");
				txtSenhaUsuario.setText("");
				atualizarLista(2); // Atualizar a lista de gerentes na tabela principal
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao adicionar usuário. Nome já pode existir.");
			}
		});
		
		btnRemoverUsuario.addActionListener(e -> {
			// Verificar se há uma linha selecionada na tabela principal
			int linha = tabelaProdutos.getSelectedRow();
			if (linha >= 0) {
				// Verificar se estamos na aba de gerenciar usuários
				String tituloAba = abas.getTitleAt(abas.getSelectedIndex());
				if ("Gerenciar Usuários".equals(tituloAba)) {
					// Obter dados da tabela principal
					int id = (int) modeloTabela.getValueAt(linha, 0);
					String nome = (String) modeloTabela.getValueAt(linha, 1);
					String tipo = (String) modeloTabela.getValueAt(linha, 3);
					
					// Não permitir remover o próprio usuário logado
					if (id == usuarioLogado.getId()) {
						JOptionPane.showMessageDialog(this, "Você não pode remover a si mesmo.");
						return;
					}
					
					int confirmacao = JOptionPane.showConfirmDialog(this,
							"Tem certeza que deseja remover o usuário '" + nome + "' (" + tipo + ")?",
							"Confirmar Remoção",
							JOptionPane.YES_NO_OPTION);
					
					if (confirmacao == JOptionPane.YES_OPTION) {
						if (usuarioDAO.removerUsuario(id)) {
							JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!");
							atualizarLista(2); // Atualizar a lista de gerentes na tabela principal
						} else {
							JOptionPane.showMessageDialog(this, "Erro ao remover usuário.");
						}
					}
				} else {
					JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela à direita para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela à direita para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		return painel;
	}

	private JPanel criarPainelFornecedores() {
		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
		painel.setBorder(BorderFactory.createTitledBorder("Gerenciar Fornecedores"));

		// Painel de formulário
		JPanel painelFormulario = new JPanel();
		painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
		JTextField txtNomeF = new JTextField();
		JTextField txtCnpjF = new JTextField();
		JTextField txtContatoF = new JTextField();
		JTextField txtEmailF = new JTextField();
		JTextField txtTelefoneF = new JTextField();
		JButton btnAdicionarF = new JButton("Adicionar Fornecedor");

		painelFormulario.add(criarLinha("Nome:", ajustarCampo(txtNomeF)));
		painelFormulario.add(criarLinha("CNPJ:", ajustarCampo(txtCnpjF)));
		painelFormulario.add(criarLinha("Contato:", ajustarCampo(txtContatoF)));
		painelFormulario.add(criarLinha("Email:", ajustarCampo(txtEmailF)));
		painelFormulario.add(criarLinha("Telefone:", ajustarCampo(txtTelefoneF)));
		
		btnAdicionarF.setAlignmentX(Component.CENTER_ALIGNMENT);
		painelFormulario.add(btnAdicionarF);

		painel.add(painelFormulario);

		// Botão para remover fornecedor
		JButton btnRemoverF = new JButton("Remover Fornecedor Selecionado");
		btnRemoverF.setAlignmentX(Component.CENTER_ALIGNMENT);
		painel.add(btnRemoverF);

		// Evento de adicionar fornecedor
		btnAdicionarF.addActionListener(e -> {
			String nome = txtNomeF.getText().trim();
			String cnpj = txtCnpjF.getText().trim();
			String contato = txtContatoF.getText().trim();
			String email = txtEmailF.getText().trim();
			String telefone = txtTelefoneF.getText().trim();
			if (nome.isEmpty() || cnpj.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Nome e CNPJ são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Fornecedor novo = new Fornecedor(nome, cnpj, contato, email, telefone);
			boolean sucesso = fornecedorDAO.adicionarFornecedor(novo);
			if (sucesso) {
				JOptionPane.showMessageDialog(this, "Fornecedor adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				txtNomeF.setText(""); txtCnpjF.setText(""); txtContatoF.setText(""); txtEmailF.setText(""); txtTelefoneF.setText("");
				atualizarLista(3); // Atualizar a lista de fornecedores na tabela principal
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao adicionar fornecedor. Verifique se o CNPJ já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento de remover fornecedor
		btnRemoverF.addActionListener(e -> {
			// Verificar se há uma linha selecionada na tabela principal
			int linha = tabelaProdutos.getSelectedRow();
			if (linha >= 0) {
				// Verificar se estamos na aba de fornecedores
				String tituloAba = abas.getTitleAt(abas.getSelectedIndex());
				if ("Fornecedores".equals(tituloAba)) {
					// Obter dados da tabela principal
					int id = (int) modeloTabela.getValueAt(linha, 0);
					String nome = (String) modeloTabela.getValueAt(linha, 1);
					
					int confirm = JOptionPane.showConfirmDialog(this, 
						"Tem certeza que deseja remover o fornecedor '" + nome + "'?", 
						"Confirmar", JOptionPane.YES_NO_OPTION);
					
					if (confirm == JOptionPane.YES_OPTION) {
						boolean sucesso = fornecedorDAO.removerFornecedor(id);
						if (sucesso) {
							JOptionPane.showMessageDialog(this, "Fornecedor removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							atualizarLista(3); // Atualizar a lista de fornecedores na tabela principal
						} else {
							JOptionPane.showMessageDialog(this, "Erro ao remover fornecedor. Verifique se ele está vinculado a algum produto.", "Erro", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela à direita para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela à direita para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
			}
		});

		return painel;
	}
	
	/////////////////////////////////////METODOS PARA ESTILIZAÇÃO/PERSONALIZAÇÃO////////////////////////////////////
	
	//Ajusta a largura das input
	private JTextField ajustarCampo(JTextField campo) {
		
		campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, campo.getPreferredSize().height));
		return campo;
		
	}
	
	//Sobrecarça para "quebra de linha"
	private JPanel criarLinha(String rotulo, JComponent campo) {
		
		JPanel linha = new JPanel();
		linha.setLayout(new BorderLayout());
		linha.setBorder(BorderFactory.createEmptyBorder(3,3,3,3)); // top, left, bottom, right
		
		JLabel label = new JLabel(rotulo);
		label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));// largura do rótulo
		
		linha.add(label, BorderLayout.WEST);
		linha.add(campo, BorderLayout.CENTER);
		return linha;
		
	}
	private JPanel criarLinha(String rotulo) {
		
		JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
		linha.add(new JLabel(rotulo));
		return linha;
		
	}

	
	//Atualiza área de texto com a lista de produtos
	private void atualizarLista(int tipo) {
		inicializarTabela(); // Garante que a tabela está inicializada
		
		// Limpa a tabela atual
		while (modeloTabela.getRowCount() > 0) {
			modeloTabela.removeRow(0);
		}

		switch (tipo) {
			case 1: // Lista de Produtos
				atualizarListaProdutos();
				break;
			case 2: // Lista de Gerentes
				atualizarListaGerentes();
				break;
			case 3: // Lista de Fornecedores
				atualizarListaFornecedores();
				break;
			default:
				JOptionPane.showMessageDialog(this, "Tipo de lista inválido");
				break;
		}
		
		centralizarTextoTabela();
	}
	
	private void atualizarListaProdutos() {
		modeloTabela.setColumnIdentifiers(new Object[]{"ID", "Nome", "Quantidade", "Perecível", "Status", "Fornecedor"});
		java.util.List<Produto> produtos = controller.listarProdutos();
		
		// Ordenar produtos por ID em ordem crescente
		produtos.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
		
		for (Produto produto : produtos) {
			String fornecedorNome = produto.getFornecedor() != null ? produto.getFornecedor().getNome() : "Não informado";
			modeloTabela.addRow(new Object[]{
				produto.getId(),
				produto.getNome(),
				produto.getQuantidade(),
				produto.isPerecivel() ? "Sim" : "Não",
				produto.getStatus(),
				fornecedorNome
			});
		}
	}

	private void atualizarListaGerentes() {
		modeloTabela.setColumnIdentifiers(new Object[]{"ID", "Nome", "Login", "Tipo"});
		java.util.List<Usuario> usuarios = usuarioDAO.listarTodosUsuarios();
		
		// Filtrar apenas gerentes e ordenar por ID em ordem crescente
		java.util.List<Usuario> gerentes = usuarios.stream()
			.filter(usuario -> usuario.getTipo().equals("GERENTE"))
			.sorted((u1, u2) -> Integer.compare(u1.getId(), u2.getId()))
			.collect(java.util.stream.Collectors.toList());
		
		for (Usuario usuario : gerentes) {
			modeloTabela.addRow(new Object[]{
				usuario.getId(),
				usuario.getNome(),
				usuario.getLogin(),
				usuario.getTipo()
			});
		}
	}

	private void atualizarListaFornecedores() {
		modeloTabela.setColumnIdentifiers(new Object[]{"ID", "Nome", "CNPJ", "Contato", "Email", "Telefone"});
		java.util.List<Fornecedor> fornecedores = fornecedorDAO.listarTodosFornecedores();
		
		// Ordenar fornecedores por ID em ordem crescente
		fornecedores.sort((f1, f2) -> Integer.compare(f1.getId(), f2.getId()));
		
		for (Fornecedor fornecedor : fornecedores) {
			modeloTabela.addRow(new Object[]{
				fornecedor.getId(),
				fornecedor.getNome(),
				fornecedor.getCnpj(),
				fornecedor.getContato(),
				fornecedor.getEmail(),
				fornecedor.getTelefone()
			});
		}
	}
	
	//Centralizar o texto dentro da tabela:
	private void centralizarTextoTabela() {
		
		DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
		centro.setHorizontalAlignment(SwingConstants.CENTER);
		
		for(int i = 0; i < tabelaProdutos.getColumnCount(); i++){
			
			tabelaProdutos.getColumnModel().getColumn(i).setCellRenderer(centro);			
		}
	}
	
	//Limpa os campos do formulário
	private void limparCampos() {
		
		txtId.setText("");
		txtNome.setText("");
		txtQuantidade.setText("");
		chkPerecivel.setSelected(false);
		if (cbFornecedor != null) {
			cbFornecedor.setSelectedIndex(0); // Voltar para "Selecione um fornecedor"
		}
		
	}
	
	private void mostrarDetalhesFornecedor(String nomeFornecedor) {
		Fornecedor fornecedor = fornecedorDAO.buscarPorNome(nomeFornecedor);
		
		if (fornecedor != null) {
			// Criar diálogo de detalhes do fornecedor
			JDialog dialog = new JDialog(this, "Detalhes do Fornecedor", true);
			dialog.setLayout(new BorderLayout());
			
			// Título
			JLabel lblTitulo = new JLabel("Detalhes do Fornecedor: " + fornecedor.getNome(), SwingConstants.CENTER);
			lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
			lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			dialog.add(lblTitulo, BorderLayout.NORTH);
			
			// Tabela com informações do fornecedor
			DefaultTableModel modeloFornecedor = new DefaultTableModel(new Object[]{"Campo", "Valor"}, 0);
			JTable tabelaFornecedor = new JTable(modeloFornecedor);
			
			// Adicionar dados do fornecedor
			modeloFornecedor.addRow(new Object[]{"ID", fornecedor.getId()});
			modeloFornecedor.addRow(new Object[]{"Nome", fornecedor.getNome()});
			modeloFornecedor.addRow(new Object[]{"CNPJ", fornecedor.getCnpj()});
			modeloFornecedor.addRow(new Object[]{"Contato", fornecedor.getContato() != null ? fornecedor.getContato() : "Não informado"});
			modeloFornecedor.addRow(new Object[]{"Email", fornecedor.getEmail() != null ? fornecedor.getEmail() : "Não informado"});
			modeloFornecedor.addRow(new Object[]{"Telefone", fornecedor.getTelefone() != null ? fornecedor.getTelefone() : "Não informado"});
			
			// Centralizar texto da tabela
			DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
			centro.setHorizontalAlignment(SwingConstants.CENTER);
			for(int i = 0; i < tabelaFornecedor.getColumnCount(); i++){
				tabelaFornecedor.getColumnModel().getColumn(i).setCellRenderer(centro);
			}
			
			JScrollPane scrollFornecedor = new JScrollPane(tabelaFornecedor);
			dialog.add(scrollFornecedor, BorderLayout.CENTER);
			
			// Botão para fechar
			JButton btnFechar = new JButton("Fechar");
			btnFechar.addActionListener(e -> {
				dialog.dispose();
				// Atualizar o cbFornecedor
				if (cbFornecedor != null) {
					cbFornecedor.removeAllItems();
					cbFornecedor.addItem("Selecione um fornecedor");
					for (Fornecedor f : fornecedorDAO.listarTodosFornecedores()) {
						cbFornecedor.addItem(f.getNome());
					}
				}
			});
			
			JPanel painelBotao = new JPanel();
			painelBotao.add(btnFechar);
			dialog.add(painelBotao, BorderLayout.SOUTH);
			
			// Configurar e mostrar o diálogo
			dialog.setSize(400, 300);
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
			
		} else {
			JOptionPane.showMessageDialog(this, 
					"Fornecedor não encontrado.", 
					"Erro", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void voltarParaProdutos() {
		// Restaurar a interface original
		getContentPane().removeAll();
		
		// Recriar o painel com abas
		abas = new JTabbedPane();
		abas.setPreferredSize(new Dimension(330, getHeight())); 
		
		abas.addChangeListener(e->{
			int abaSelecionada = abas.getSelectedIndex();
			String tituloAba = abas.getTitleAt(abaSelecionada);
			modoAtualizacaoAtivo = true; // Sempre permitir edição
			
			// Atualizar a lista de acordo com a aba selecionada
			switch (tituloAba) {
				case "Produtos":
					atualizarLista(1); // Lista de produtos
					break;
				case "Gerenciar Usuários":
					atualizarLista(2); // Lista de gerentes
					break;
				case "Fornecedores":
					atualizarLista(3); // Lista de fornecedores
					break;
			}
		});
		
		//Aba de produtos:
		JPanel painelProdutos = criarPainelProdutos();
		abas.addTab("Produtos", painelProdutos);

		// Aba de gerenciamento de usuários (apenas para administradores)
		if (usuarioLogado.podeGerenciarUsuarios()) {
			painelGerenciarUsuarios = criarPainelGerenciarUsuarios();
			abas.addTab("Gerenciar Usuários", painelGerenciarUsuarios);
			JPanel painelFornecedores = criarPainelFornecedores();
			abas.addTab("Fornecedores", painelFornecedores);
		}
		
		inicializarTabela(); // Garante que a tabela está inicializada
		
		// Recriar o split pane
		JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, abas, scrollPane);
		splitPane.setDividerLocation(330);
		splitPane.setResizeWeight(0.3);
		add(splitPane);
		
		revalidate();
		repaint();
	}
	

	
	//Executar a interface
	public static void main(String [] args) {
		
		SwingUtilities.invokeLater(() ->{
			
			// Iniciar com a tela de login
			LoginView loginView = new LoginView();
			loginView.setVisible(true);
			
		});
		
	}
	
	private void inicializarTabela() {
		if (modeloTabela == null) {
			modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Quantidade", "Perecível", "Status", "Fornecedor"}, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column == 1 || column == 2) {
						// Modo de atualização sempre ativo
						if (!modoAtualizacaoAtivo) {
							return false;
						}
						
						if (!usuarioLogado.podeAtualizarProduto()) {
							JOptionPane.showMessageDialog(null,
									"Você não tem permissão para atualizar produtos.",
									"Permissão Negada", JOptionPane.WARNING_MESSAGE);
							return false;
						}
						return true;
					}
					return false;
				}
			};
			tabelaProdutos = new JTable(modeloTabela);
		}
	}

}
