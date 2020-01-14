package gerenciador.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import gerenciador.model.dao.ClienteDAO;
import gerenciador.model.dao.ProdutoDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Cliente;
import gerenciador.model.domain.ItemDeVenda;
import gerenciador.model.domain.Produto;
import gerenciador.model.domain.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AnchorPaneProcessosVendasDialogController implements Initializable {
	
	@FXML
    private ComboBox<Cliente> comboBoxVendaCliente;

    @FXML
    private DatePicker datePickerVendaData;

    @FXML
    private CheckBox checkBoxVendaPago;

    @FXML
    private TableView<ItemDeVenda> tableViewItensDeVenda;
    
    @FXML
    private TableColumn<ItemDeVenda, Produto> tableViewItensDeVendaProduto;

    @FXML
    private TableColumn<ItemDeVenda, Integer> tableViewItensDeVendaQuantidade;

    @FXML
    private TableColumn<ItemDeVenda, Double> tableViewItensDeVendaValor;

    @FXML
    private HBox buttonAdicionar;

    @FXML
    private ComboBox<Produto> comboBoxVendaProduto;

    @FXML
    private TextField textFieldVendaQuantidade;

    @FXML
    private TextField textFieldVendaValor;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;
    
    private List<Cliente> listClientes;
    private List<Produto> listProdutos;
    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemDeVenda> observableListItensDeVenda;

 
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;   
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clienteDAO.setConnection(connection);
		produtoDAO.setConnection(connection);
		carregarComboBoxCliente();
		carregarComboBoxProdutos();
		tableViewItensDeVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
		tableViewItensDeVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableViewItensDeVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		
	}


	public Stage getDialogStage() {
		return dialogStage;
	}


	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}


	public boolean isButtonConfirmarClicked() {
		return buttonConfirmarClicked;
	}


	public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
		this.buttonConfirmarClicked = buttonConfirmarClicked;
	}


	public Venda getVenda() {
		return venda;
	}


	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
	public void carregarComboBoxCliente() {
		listClientes = clienteDAO.listar();
		observableListClientes  = FXCollections.observableArrayList(listClientes);
		comboBoxVendaCliente.setItems(observableListClientes);
	}
	
	public void carregarComboBoxProdutos() {
		listProdutos = produtoDAO.listar();
		observableListProdutos = FXCollections.observableArrayList(listProdutos);
		comboBoxVendaProduto.setItems(observableListProdutos);
	}
	
	@FXML
    public void handleButtonAdicionar() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();

        if (comboBoxVendaProduto.getSelectionModel().getSelectedItem() != null) {
            produto = (Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem();

            if (produto.getQuantidade() >= Integer.parseInt(textFieldVendaQuantidade.getText())) {
                itemDeVenda.setProduto((Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem());
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldVendaQuantidade.getText()));
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());

                venda.getItensDeVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());

                observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                tableViewItensDeVenda.setItems(observableListItensDeVenda);

                textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto!");
                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
                alert.show();
            }
        }
    }
	
	@FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            venda.setCliente((Cliente) comboBoxVendaCliente.getSelectionModel().getSelectedItem());
            venda.setPago(checkBoxVendaPago.isSelected());
            venda.setData(datePickerVendaData.getValue());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        getDialogStage().close();
    }


    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (comboBoxVendaCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido!\n";
        }
        if (datePickerVendaData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }
        if (observableListItensDeVenda == null) {
            errorMessage += "Itens de Venda inválidos!\n";
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
