package gerenciador.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
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
import gerenciador.model.dao.ProdutoDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Produto;

    
public class AnchorPaneCadastrosProdutosController implements Initializable {
    
    
    
    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, Integer> tableColumnProdutoCodigo;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    
    
    
    @FXML
    private Label labelProdutoCategoria;
    @FXML
    private Label labelProdutoPreço;
    @FXML
    private Label labelProdutoQuantidade;
    
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    
    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;
    
   
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        produtoDAO.setConnection(connection);
        carregarTableViewProdutos();
        
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue));
    }    
     public void carregarTableViewProdutos() {
        tableColumnProdutoCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
        tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        listProdutos = produtoDAO.listar();

        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewProdutos.setItems(observableListProdutos);
    }
     
     public void selecionarItemTableViewProdutos(Produto produto) {
        if (produto != null) {
            labelProdutoCategoria.setText(String.valueOf(produto.getCategoria()));
            labelProdutoPreço.setText(String.valueOf(produto.getPreco()));
            labelProdutoQuantidade.setText(String.valueOf(produto.getQuantidade()));
           
        } else {
            labelProdutoCategoria.setText("");
            labelProdutoPreço.setText("");
            labelProdutoQuantidade.setText("");
        }
    }
     @FXML
    public void handleButtonInserir() throws IOException {
        Produto produto = new Produto();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
        if (buttonConfirmarClicked) {
            produtoDAO.inserir(produto);
            carregarTableViewProdutos();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();//Obtendo produto selecionado
        if (produto != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
            if (buttonConfirmarClicked) {
                produtoDAO.alterar(produto);
                carregarTableViewProdutos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Produto na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto != null) {
            produtoDAO.remover(produto);
            carregarTableViewProdutos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um Produto na Tabela!");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneCadastrosClientesDialogController.class.getResource("/gerenciador/view/FXMLAnchorPaneCadastrosProdutosDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Produtos");

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

 
        AnchorPaneCadastrosProdutosDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);

         dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();

    }

    
}
