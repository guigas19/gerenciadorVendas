package gerenciador.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import gerenciador.model.dao.ItemDeVendaDAO;
import gerenciador.model.dao.ProdutoDAO;
import gerenciador.model.dao.VendaDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import gerenciador.model.domain.Cliente;
import gerenciador.model.domain.ItemDeVenda;
import gerenciador.model.domain.Produto;
import gerenciador.model.domain.Venda;
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

public class AnchorPaneProcessoVendasController implements Initializable {

	@FXML
	private TableView<Venda> tableViewVendas;

	@FXML
	private TableColumn<Venda, Integer> tableVendaColumnCodigo;

	@FXML
	private TableColumn<Venda, LocalDate> tableVendaColumnData;

	@FXML
	private TableColumn<Venda, Venda> tableVendaColumnCliente;

	@FXML
	private Label labelVendaCodigo;

	@FXML
	private Label labelVendaData;

	@FXML
	private Label labelVendaValor;

	@FXML
	private Label labelVendaPago;

	@FXML
	private Label labelVendaCliente;

	@FXML
	private Button buttonInserir;

	@FXML
	private Button buttonAlterar;

	@FXML
	private Button buttonRemover;
	
	private List<Venda> listVendas;
	private ObservableList<Venda> observableListVendas;
	

	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final VendaDAO vendaDAO = new VendaDAO();
	private final ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
	private final ProdutoDAO produtoDAO = new ProdutoDAO();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		vendaDAO.setConnection(connection);
		carregarTableViewVendas();
		
		selecionarItemTableViewVendas(null);

		tableViewVendas.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> selecionarItemTableViewVendas(newValue));

	}
	
	public void selecionarItemTableViewVendas(Venda venda) {
        if (venda != null) {
            labelVendaCodigo.setText(String.valueOf(venda.getCdVenda()));
            labelVendaData.setText(String.valueOf(venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelVendaValor.setText(String.format("%.2f", venda.getValor()));
            labelVendaPago.setText(String.valueOf(venda.getPago()));
            labelVendaCliente.setText(venda.getCliente().toString());
        } else {
            labelVendaCodigo.setText("");
            labelVendaData.setText("");
            labelVendaValor.setText("");
            labelVendaPago.setText("");
            labelVendaCliente.setText("");
        }
    }
	
	
	
	public void carregarTableViewVendas() {
		tableVendaColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("cdVenda"));
		tableVendaColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));
		tableVendaColumnCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		
		listVendas = vendaDAO.listar();
		
		observableListVendas = FXCollections.observableArrayList(listVendas);
		tableViewVendas.setItems(observableListVendas);
	}
	
	@FXML
	public void handleButtonInserir() throws IOException {
		Venda venda = new Venda();
        List<ItemDeVenda> listItensDeVenda = new ArrayList<>();
        venda.setItensDeVenda(listItensDeVenda);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendasDialog(venda);
        if (buttonConfirmarClicked) {
            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                vendaDAO.inserir(venda);
                itemDeVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);
                for (ItemDeVenda listItemDeVenda : venda.getItensDeVenda()) {
                    Produto produto = listItemDeVenda.getProduto();
                    listItemDeVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemDeVendaDAO.inserir(listItemDeVenda);
                    produto.setQuantidade(produto.getQuantidade() - listItemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                }
                connection.commit();
                carregarTableViewVendas();
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(AnchorPaneProcessoVendasController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(AnchorPaneProcessoVendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
	
	 @FXML
	    public void handleButtonRemover() throws IOException, SQLException {
	        Venda venda = tableViewVendas.getSelectionModel().getSelectedItem();
	        if (venda != null) {
	            connection.setAutoCommit(false);
	            vendaDAO.setConnection(connection);
	            itemDeVendaDAO.setConnection(connection);
	            produtoDAO.setConnection(connection);
	            for (ItemDeVenda listItemDeVenda : venda.getItensDeVenda()) {
	                Produto produto = listItemDeVenda.getProduto();
	                produto.setQuantidade(produto.getQuantidade() + listItemDeVenda.getQuantidade());
	                produtoDAO.alterar(produto);
	                itemDeVendaDAO.remover(listItemDeVenda);
	            }
	            vendaDAO.remover(venda);
	            connection.commit();
	            carregarTableViewVendas();
	        } else {
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setContentText("Por favor, escolha uma venda na Tabela!");
	            alert.show();
	        }
	    }
	
	public boolean showFXMLAnchorPaneProcessosVendasDialog(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneProcessosVendasDialogController.class.getResource("/gerenciador/view/FXMLAnchorPaneProcessosVendasDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Registro de Vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AnchorPaneProcessosVendasDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();

    }

}
