package model;

public class Gerente extends Usuario {
    
    public Gerente(int id, String nome, String senha) {
        super(id, nome, senha, "GERENTE");
    }
    
    public Gerente(String nome, String senha) {
        super(nome, senha, "GERENTE");
    }
    
    @Override
    public boolean podeAdicionarProduto() {
        return true;
    }
    
    @Override
    public boolean podeRemoverProduto() {
        return true;
    }
    
    @Override
    public boolean podeAtualizarProduto() {
        return true;
    }
    
    @Override
    public boolean podeGerenciarUsuarios() {
        return false; // Gerente não pode gerenciar usuários
    }
} 