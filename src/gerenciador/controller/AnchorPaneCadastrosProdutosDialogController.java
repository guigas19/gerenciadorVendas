
package gerenciador.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import gerenciador.model.dao.CategoriaDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Categoria;
import gerenciador.model.domain.Produto;

public class AnchorPaneCadastrosProdutosDialogController implements Initializable {

	@FXML
	private Label labelProdutosCodigo;
	@FXML
	private TextField textFieldCadastrosProdutosNome;
	@FXML
	private ComboBox comboBoxCadastrosProdutosCategoria;
	@FXML
	private TextField textFieldCadastrosProdutosPreco;
	@FXML
	private TextField textFieldCadastrosProdutosQtd;
	@FXML
	private Button buttonConfirma;
	@FXML
	private Button buttonCancela;

	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final CategoriaDAO categoriaDAO = new CategoriaDAO();
	private List<Categoria> listCategorias;
	private ObservableList<Categoria> observableListCategorias;

	private Stage dialogStage;
	private boolean buttonConfirmarClicked = false;
	private Produto produto;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		categoriaDAO.setConnection(connection);
		carregarComboBoxCategorias();

	}

	public void carregarComboBoxCategorias() {

		listCategorias = categoriaDAO.listar();

		observableListCategorias = FXCollections.observableArrayList(listCategorias);
		comboBoxCadastrosProdutosCategoria.setItems(observableListCategorias);

	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		this.textFieldCadastrosProdutosNome.setText(produto.getNome()); 
		this.textFieldCadastrosProdutosPreco.setText(String.valueOf(produto.getPreco())); 
		this.textFieldCadastrosProdutosQtd.setText(String.valueOf(produto.getQuantidade())); 
		
		
	}

	public Stage getDialogStage() {
		return dialogStage;
	}


	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	boolean isButtonConfirmarClicked() {
		return buttonConfirmarClicked;
	}

	public void handleButtonConfirmar() {
		if (validarEntradaDeDados()) {
			produto.setNome(textFieldCadastrosProdutosNome.getText());
			produto.setCategoria((Categoria) comboBoxCadastrosProdutosCategoria.getSelectionModel().getSelectedItem());
			produto.setPreco(Double.parseDouble(textFieldCadastrosProdutosPreco.getText().replaceAll(",", ".")));
			produto.setQuantidade(Integer.parseInt(textFieldCadastrosProdutosQtd.getText()));
			buttonConfirmarClicked = true;
			dialogStage.close();
		}
	}

	public void handleButtonCancelar() {
		getDialogStage().close();
	}

	private boolean validarEntradaDeDados() {
		String errorMessage = "";

		if (textFieldCadastrosProdutosNome.getText() == null
				|| textFieldCadastrosProdutosNome.getText().length() == 0) {
			errorMessage += "Nome inválido!\n";
		}
		if (comboBoxCadastrosProdutosCategoria.getSelectionModel().getSelectedItem() == null) {
			errorMessage += "Selecione uma Categoria!\n";
		}
		if (textFieldCadastrosProdutosPreco.getText() == null || textFieldCadastrosProdutosPreco.getText().length() == 0
				|| Double.parseDouble(textFieldCadastrosProdutosPreco.getText().replaceAll(",", ".")) < 0) {
			errorMessage += "Preco invalido inválido!\n";
		}
		if (textFieldCadastrosProdutosQtd.getText() == null || textFieldCadastrosProdutosQtd.getText().length() == 0
				|| Integer.parseInt(textFieldCadastrosProdutosQtd.getText()) < 0) {
			errorMessage += "Quantidade invalida inválido!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Erro no cadastro");
			alert.setHeaderText("Campos inválidos, por favor, corrija...");
			alert.setContentText(errorMessage);
			alert.show();
			return false;
		}
	}

}
