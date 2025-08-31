package model;

public class Administrador extends Usuario {
    
    public Administrador(int id, String nome, String senha) {
        super(id, nome, senha, "ADMINISTRADOR");
    }
    
    public Administrador(String nome, String senha) {
        super(nome, senha, "ADMINISTRADOR");
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
        return true; // Administrador pode gerenciar usuários
    }
} 