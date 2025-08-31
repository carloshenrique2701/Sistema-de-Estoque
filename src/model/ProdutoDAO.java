package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
	
	private FornecedorDAO fornecedorDAO;
	
	public ProdutoDAO() {
		this.fornecedorDAO = new FornecedorDAO();
	}
	
	public boolean inserir(Produto produto) {
		
		String sql = "INSERT INTO Produto(id, nome, qnt_estoque, perecivel, status, fornecedor_id) VALUES (?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = Conexao.getConexao();
			PreparedStatement stmt = conn.prepareStatement(sql)){
				
				stmt.setInt(1, produto.getId());
				stmt.setString(2, produto.getNome());
				stmt.setInt(3, produto.getQuantidade());
				stmt.setBoolean(4, produto.isPerecivel());
				stmt.setString(5, produto.getStatus().toString());
				
				// Definir fornecedor_id
				if (produto.getFornecedor() != null) {
					stmt.setInt(6, produto.getFornecedor().getId());
				} else {
					stmt.setNull(6, Types.INTEGER);
				}
				
				stmt.executeUpdate();
				
				return true;
				
		}catch(SQLException e) {
				
				e.printStackTrace();
				return false;
				
		}
	
	}

	
	public boolean atualiza(Produto produto) {
		
		String sql = "UPDATE Produto SET nome=?, qnt_estoque=?, perecivel=?, status=?, fornecedor_id=? WHERE id=?";
		
		try(Connection conn = Conexao.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setString(1, produto.getNome());
			stmt.setInt(2, produto.getQuantidade());
			stmt.setBoolean(3, produto.isPerecivel());
			stmt.setString(4, produto.getStatus().toString());
			
			// Definir fornecedor_id
			if (produto.getFornecedor() != null) {
				stmt.setInt(5, produto.getFornecedor().getId());
			} else {
				stmt.setNull(5, Types.INTEGER);
			}
			
			stmt.setInt(6, produto.getId());
			
			return stmt.executeUpdate() > 0;
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			return false;
			
		}
		
	}
	
	public boolean deletarPorId(int id) {
		
		String sql = "DELETE FROM Produto WHERE id = ?";
		
		try(Connection conn = Conexao.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, id);
			
			return stmt.executeUpdate() > 0;
			
		} catch(SQLException e) { 
			
			e.printStackTrace();
			return false;
			
		}
	}
	
	public List<Produto> ListarTodos(){
		
		List<Produto> lista = new ArrayList<>();
		String sql = "SELECT p.*, f.nome as fornecedor_nome FROM Produto p LEFT JOIN fornecedores f ON p.fornecedor_id = f.id";
		
		try(Connection conn = Conexao.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()){
			
			while (rs.next()) {
				
				Produto p = new Produto(
							rs.getInt("id"),
							rs.getString("nome"),
							rs.getInt("qnt_estoque"),
							rs.getBoolean("perecivel")
						);
				
				// Definir status
				p.setStatus(StatusProduto.valueOf(rs.getString("status")));
				
				// Definir fornecedor se existir
				String fornecedorNome = rs.getString("fornecedor_nome");
				if (fornecedorNome != null) {
					Fornecedor fornecedor = fornecedorDAO.buscarPorNome(fornecedorNome);
					p.setFornecedor(fornecedor);
				}
				
				lista.add(p);
				
			}
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return lista;
		
	}
	
	public Produto buscarPorId(int id) {
		
		String sql = "SELECT p.*, f.nome as fornecedor_nome FROM Produto p LEFT JOIN fornecedores f ON p.fornecedor_id = f.id WHERE p.id = ?";
		
		try(Connection conn = Conexao.getConexao(); 
				PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, id);
			
			try(ResultSet rs = stmt.executeQuery()){
				
				if(rs.next()) {
					
					Produto p = new Produto(
								rs.getInt("id"),
								rs.getString("nome"),
								rs.getInt("qnt_estoque"),
								rs.getBoolean("perecivel")
							);
					p.setStatus(StatusProduto.valueOf(rs.getString("status")));
					
					// Definir fornecedor se existir
					String fornecedorNome = rs.getString("fornecedor_nome");
					if (fornecedorNome != null) {
						Fornecedor fornecedor = fornecedorDAO.buscarPorNome(fornecedorNome);
						p.setFornecedor(fornecedor);
					}
					
					return p;
							
					
				}
				
			}
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return null;
		
	}
}



























