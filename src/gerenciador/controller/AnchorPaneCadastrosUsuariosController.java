package gerenciador.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import gerenciador.model.dao.ClienteDAO;
import gerenciador.model.dao.UsuarioDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Cliente;
import gerenciador.model.domain.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AnchorPaneCadastrosUsuariosController implements Initializable {

	@FXML
	private TableView<Usuario> tableViewUsuarios;

	@FXML
	private TableColumn<Usuario, String> tableColumnUsuariosNome;

	@FXML
	private TableColumn<Usuario, String> tableColumnUsuariosLogin;

	@FXML
	private Label labelUsuarioLogin;

	@FXML
	private Label labelUsuarioEmail;

	@FXML
	private Label labelUsuarioNome;

	private List<Usuario> listUsuarios;
	private ObservableList<Usuario> observableListUsuarios;

	
	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usuarioDAO.setConnection(connection);

		carregarTableViewUsuarios();

		selecionarItemTableViewUsuarios(null);

		tableViewUsuarios.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewUsuarios(newValue));

	}

	public void carregarTableViewUsuarios() {
		tableColumnUsuariosNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnUsuariosLogin.setCellValueFactory(new PropertyValueFactory<>("login"));

		listUsuarios = usuarioDAO.listar();

		observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
		tableViewUsuarios.setItems(observableListUsuarios);
	}
	
	public void selecionarItemTableViewUsuarios(Usuario usuario) {
		if (usuario != null) {
			labelUsuarioNome.setText(usuario.getNome());
			labelUsuarioLogin.setText(usuario.getLogin());
			labelUsuarioEmail.setText(usuario.getEmail());
			
		} else {
			labelUsuarioNome.setText("");
			labelUsuarioLogin.setText("");
			labelUsuarioEmail.setText("");			
		}
	}

	@FXML
	void handleButtonInserir() throws IOException {
		Usuario usuario = new Usuario();
		boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosUsuariosDialog(usuario);
		if (buttonConfirmarClicked) {
			usuarioDAO.inserir(usuario);
			carregarTableViewUsuarios();
		}

	}

	@FXML
	void handleButtonRemover() {
		Usuario usuario = tableViewUsuarios.getSelectionModel().getSelectedItem();
		if (usuario != null) {
			usuarioDAO.remover(usuario);
			carregarTableViewUsuarios();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Por favor, escolha um usuário na Tabela!");
			alert.show();
		}

	}
	
	public boolean showFXMLAnchorPaneCadastrosUsuariosDialog(Usuario usuario) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AnchorPaneCadastrosUsuariosDialogController.class
				.getResource("/gerenciador/view/FXMLAnchorPaneCadastrosUsuariosDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();


		Stage dialogStage = new Stage();
		dialogStage.setTitle("Cadastro de Usuarios");

		Scene scene = new Scene(page);
		dialogStage.setScene(scene);


		AnchorPaneCadastrosUsuariosDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setUsuario(usuario);

		dialogStage.showAndWait();

		return controller.isButtonConfirmarClicked();

	}

}
