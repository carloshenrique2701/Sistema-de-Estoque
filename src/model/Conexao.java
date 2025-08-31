package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	
	private static final String URL = "jdbc:mysql://localhost:3306/SistemaEstoque";
	private static final String USUARIO = "root";
	private static final String SENHA = "123";
	
	public static Connection getConexao() throws SQLException{
		
		return DriverManager.getConnection(URL, USUARIO, SENHA);
		
	}

}
