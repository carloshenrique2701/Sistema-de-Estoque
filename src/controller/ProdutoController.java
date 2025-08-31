package controller;

import model.Produto;
import java.util.List;
import model.ProdutoDAO;

public class ProdutoController {
	
	private ProdutoDAO produtoDAO;
	
	public ProdutoController() {
		this.produtoDAO = new ProdutoDAO();
	}
	
	public boolean cadastrarProduto(Produto produto) {
		try {
			if(produtoDAO.buscarPorId(produto.getId()) != null) {
				System.out.println("[ERRO] Produto com ID " + produto.getId() + " já existe.");
				return false;
			}
			boolean sucesso = produtoDAO.inserir(produto);
			if(sucesso) {
				System.out.println("[OK] Produto cadastrado no banco de dados: " + produto);
			} else {
				System.out.println("[ERRO] Falha ao cadastrar produto no banco de dados: " + produto);
			}
			return sucesso;
		} catch (Exception ex) {
			System.out.println("[EXCEPTION] Erro ao cadastrar produto: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	//Excluir Produto po ID
	public boolean excluiProduto(int id) {
		
		return produtoDAO.deletarPorId(id);
		
	}
	

	
	
	
	//Buscar um produto por ID
	public Produto buscarPorId(int id) {
		
		Produto produto = produtoDAO.buscarPorId(id);
		
		if(produto == null) {
			
			System.out.println("Produto não encontrado.");
			
		}
		
		return produto;
		
	}
	
	//Atualiza o produto
	public boolean atualizarProduto(Produto produto) {
		
		return produtoDAO.atualiza(produto);
		
	}

	//Listar todos os produtos
	public List<Produto> listarProdutos() {
		return produtoDAO.ListarTodos();
	}

}
