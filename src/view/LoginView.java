package view;

import model.Usuario;
import model.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginView extends JFrame {
    
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;
    private UsuarioDAO usuarioDAO;
    
    public LoginView() {
        usuarioDAO = new UsuarioDAO();
        
        configurarJanela();
        criarComponentes();
        configurarEventos();
    }
    
    private void configurarJanela() {
        setTitle("Login - Sistema de Controle de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void criarComponentes() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Sistema de Controle de Estoque", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        // Painel de login
        JPanel painelLogin = new JPanel();
        painelLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        painelLogin.add(new JLabel("Usuário:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtUsuario = new JTextField(20);
        painelLogin.add(txtUsuario, gbc);
        
        // Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        painelLogin.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtSenha = new JPasswordField(20);
        painelLogin.add(txtSenha, gbc);
        
        painelPrincipal.add(painelLogin, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        btnEntrar = new JButton("Entrar");
        btnEntrar.setPreferredSize(new Dimension(100, 30));
        btnSair = new JButton("Sair");
        btnSair.setPreferredSize(new Dimension(100, 30));
        
        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);
        
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelPrincipal);
    }
    
    private void configurarEventos() {
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Permitir login com Enter
        KeyListener enterListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        };
        
        txtUsuario.addKeyListener(enterListener);
        txtSenha.addKeyListener(enterListener);
    }
    
    private void realizarLogin() {
        String nomeUsuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos.",
                    "Campos Vazios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarioDAO.buscarPorNome(nomeUsuario);
        
        if (usuario != null && usuario.autenticar(nomeUsuario, senha)) {
            // Login bem-sucedido
            JOptionPane.showMessageDialog(this,
                    "Login realizado com sucesso!\nBem-vindo, " + usuario.getNome() + " (" + usuario.getTipo() + ")",
                    "Login Bem-sucedido",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir a tela principal do sistema
            abrirTelaPrincipal(usuario);
            
        } else {
            // Login falhou
            JOptionPane.showMessageDialog(this,
                    "Usuário não cadastrado no sistema.\nFale com o administrador para mais informações.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
            
            // Limpar campos
            txtSenha.setText("");
            txtUsuario.requestFocus();
        }
    }
    
    private void abrirTelaPrincipal(Usuario usuario) {
        // Fechar a tela de login
        this.dispose();
        
        // Abrir a tela principal com o usuário logado
        SwingUtilities.invokeLater(() -> {
            EstoqueView estoqueView = new EstoqueView(usuario);
            estoqueView.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
} 