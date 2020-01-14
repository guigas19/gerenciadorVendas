package gerenciador.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gerenciador.model.domain.Usuario;

public class UsuarioDAO {

	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean inserir(Usuario usuario) {
		String sql = "INSERT INTO usuarios VALUES(?,?,?,?)";		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getLogin());
			stmt.setString(3, usuario.getSenha());
			stmt.setString(4, usuario.getEmail());
			stmt.execute();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}

	}
	
	public List<Usuario> listar() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setNome(resultado.getString("nome"));
                usuario.setLogin(resultado.getString("login"));
                usuario.setSenha(resultado.getString("senha"));
                usuario.setEmail(resultado.getString("email"));
                retorno.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
	
	 public boolean remover(Usuario usuario) {
	        String sql = "DELETE FROM usuarios WHERE nome=?";
	        try {
	            PreparedStatement stmt = connection.prepareStatement(sql);
	            stmt.setString(1, usuario.getNome());
	            stmt.execute();
	            return true;
	        } catch (SQLException ex) {
	            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
	            return false;
	        }
	    }
	

	public Usuario buscarLogin(Usuario usuario) {
		String sql = "SELECT * FROM usuarios WHERE login = '"+usuario.getLogin()+"'";
		Usuario retorno = new Usuario();		
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			ResultSet resultado = stm.executeQuery();			
			if (resultado.next()) {
                usuario.setNome(resultado.getString("nome"));
                usuario.setLogin(resultado.getString("login"));
                usuario.setSenha(resultado.getString("senha"));
                usuario.setEmail(resultado.getString("email"));
                retorno = usuario;
             
            }
			
		} catch (SQLException ex) {
			Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		return retorno;
	}

}
