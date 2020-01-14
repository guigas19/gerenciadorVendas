package gerenciador.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import gerenciador.model.dao.CategoriaDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Categoria;

public class AnchorPaneCadastrosCategoriasController implements Initializable {

	@FXML
	private TableView<Categoria> tableViewCategorias;
	@FXML
	private TableColumn<Categoria, Integer> tableColumnCategoriasCodigo;
	@FXML
	private TableColumn<Categoria, String> tableColumnCategoriasDescricao;

	@FXML
	private Label labelCategoriasSelecionado;

	@FXML
	private Button buttonNovo;

	@FXML
	private Button buttonRemover;

	private List<Categoria> listCategorias;
	private ObservableList<Categoria> observableListCategorias;

	
	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final CategoriaDAO categoriaDAO = new CategoriaDAO();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		categoriaDAO.setConnection(connection);
		carregarTableViewCategorias();

		tableViewCategorias.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
	}

	public void carregarTableViewCategorias() {
		tableColumnCategoriasCodigo.setCellValueFactory(new PropertyValueFactory<>("cdCategoria"));
		tableColumnCategoriasDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

		listCategorias = categoriaDAO.listar();

		observableListCategorias = FXCollections.observableArrayList(listCategorias);
		tableViewCategorias.setItems(observableListCategorias);
	}

	public void selecionarItemTableViewCategorias(Categoria categoria) {
		if (categoria != null) {
			labelCategoriasSelecionado.setText(categoria.getDescricao());

		} else {
			labelCategoriasSelecionado.setText("");
		}
	}

	@FXML
	public void handleButtonNovo() throws IOException {
		Categoria categoria = new Categoria();
		boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
		if (buttonConfirmarClicked) {
			categoriaDAO.inserir(categoria);
			carregarTableViewCategorias();
		}
	}

	@FXML
	public void handleButtonRemover() throws IOException {
		Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
		if (categoria != null) {
			categoriaDAO.remover(categoria);
			carregarTableViewCategorias();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Por favor, escolha uma categoria na Tabela!");
			alert.show();
		}
	}

	public boolean showFXMLAnchorPaneCadastrosCategoriasDialog(Categoria categoria) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AnchorPaneCadastrosCategoriasDialogController.class
				.getResource("/gerenciador/view/FXMLAnchorPaneCadastrosCategoriasDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();

		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Cadastro de Categorias");
		
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		
		AnchorPaneCadastrosCategoriasDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setCategoria(categoria);

		
		dialogStage.showAndWait();

		return controller.isButtonConfirmarClicked();

	}



}
