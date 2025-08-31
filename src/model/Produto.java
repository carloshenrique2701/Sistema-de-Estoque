package model;

public class Produto {
	
	private int id;
	private String nome;
	private int quantidade;
	private boolean perecivel;
	private StatusProduto status;
	private Fornecedor fornecedor;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
		atualizarStatus();
	}
	public boolean isPerecivel() {
		return perecivel;
	}
	public void setPerecivel(boolean perecivel) {
		this.perecivel = perecivel;
	}
	public StatusProduto getStatus() {
		return status;
	}
	public void setStatus(StatusProduto status) {
		this.status = status;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public Produto(int id, String nome, int quantidade, boolean perecivel, Fornecedor fornecedor) {
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
		this.perecivel = perecivel;
		this.fornecedor = fornecedor;
		atualizarStatus();
	}
	
	public Produto(int id, String nome, int quantidade, boolean perecivel) {
		this.id = id;
		this.nome = nome;
		this.quantidade = quantidade;
		this.perecivel = perecivel;
		atualizarStatus();
	}
	
	public Produto(int i, String string, double d) {
		// TODO Auto-generated constructor stub
	}
	
	public void atualizarStatus() {
		
		if(quantidade == 0) {
			
			status = StatusProduto.EM_FALTA;
			
		} else if(quantidade < 10) {
			
			status = StatusProduto.BAIXO_ESTOQUE;
			
		} else if (quantidade >= 10 && quantidade <= 30) {
			
			status = StatusProduto.A_VENDA;
			
		} else {
			
			status = StatusProduto.ESTOQUE_COMPLETO;
			
		}
		
	}
	
	@Override
	public String toString() {
		
		return "Produto ID= " + id 
				+ "\n Nome= " + nome 
				+ "\n Quantidade= " + quantidade + 
				"\n Perecível= " +(perecivel ? "Sim" : "Não") 
				+ "\n Status= " + status 
				+ "\n Fornecedor= " + (fornecedor != null ? fornecedor.getNome() : "Não informado");
		
	}
	

	
	
}
