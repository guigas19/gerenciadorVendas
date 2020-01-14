package gerenciador.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import gerenciador.Main;
import gerenciador.model.dao.ClienteDAO;
import gerenciador.model.dao.UsuarioDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnchorPaneLoginController implements Initializable {

	@FXML
	private TextField textFieldEntrarLogin;

	@FXML
	private PasswordField passwordFieldEntrar;


	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usuarioDAO.setConnection(connection);

	}

	@FXML
	public void handleButtonEntrar() throws IOException {
		Usuario usuario = new Usuario();
		usuario.setLogin(textFieldEntrarLogin.getText());
		usuario.setSenha(passwordFieldEntrar.getText());



		Usuario usuarioValidado = usuarioDAO.buscarLogin(usuario);
		try {
			if (usuarioValidado.getLogin().equals(textFieldEntrarLogin.getText())) {
				if(usuarioValidado.getSenha().equals(passwordFieldEntrar.getText())) {
					Main.changeScreen("VBoxMain");
					System.out.println("Usuário Validado");
				}else {
					throw  new NullPointerException();
				}
				
			}
		} catch (NullPointerException ex) {

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Erro ao realizar o Login!");
			alert.setHeaderText("Campos inválidos, por favor, corrija...");
			alert.setContentText("Login e/ou senha inválido");
			alert.show();
		}

	}



}
