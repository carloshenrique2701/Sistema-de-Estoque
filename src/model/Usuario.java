package model;

public abstract class Usuario {
    protected int id;
    protected String nome;
    protected String senha;
    protected String tipo;
    
    public Usuario(int id, String nome, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }
    
    public Usuario(String nome, String senha, String tipo) {
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }
    
    // Getters e Setters
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
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    // Método para obter o login (usando o nome como login)
    public String getLogin() {
        return nome;
    }
    
    // Métodos abstratos que devem ser implementados pelas subclasses
    public abstract boolean podeAdicionarProduto();
    public abstract boolean podeRemoverProduto();
    public abstract boolean podeAtualizarProduto();
    public abstract boolean podeGerenciarUsuarios();
    
    // Método para autenticação
    public boolean autenticar(String nome, String senha) {
        return this.nome.equals(nome) && this.senha.equals(senha);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
} 