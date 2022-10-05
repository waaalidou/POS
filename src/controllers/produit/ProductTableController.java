package controllers.produit;

import dao.DBConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.SelectProduit;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductTableController implements Initializable {
    @FXML
    private TableView<SelectProduit> productTable;
    @FXML
    private TableColumn<SelectProduit, String> idCol,codeCol,nomCol,quantiteCol,AjoutSupp,minCol,categoryCol,fornisseurCol;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterComboBox,filterComboBox1;

    private ObservableList<SelectProduit> ListProduits = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<String> fournisseurs = FXCollections.observableArrayList();

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private SelectProduit selectProduit;
    static int id;
    private Alert alert = new Alert(Alert.AlertType.NONE);
    private Image supplierImg = new Image(getClass().getResourceAsStream("/resources/images/supplier.png"));
    private Image customerIcon = new Image(getClass().getResourceAsStream("/resources/images/customer.png"));
    @FXML
    private ImageView supplierIcon = new ImageView(supplierImg);
    @FXML
    private ImageView CustomerIcon = new ImageView(customerIcon);
    public void refreshTable() {
        populateComboBox();
        populateFourniseur();
        ListProduits.clear();
        searchField.setText("");
        filterComboBox.getSelectionModel().clearSelection();
        filterComboBox1.getSelectionModel().clearSelection();
        try {
            String query = "SELECT Produit.id AS Id,\n" +
                    "Produit.nom AS Nom, \n" +
                    "Produit.code_produit AS 'Code',\n" +
                    "\tProduit.quantit_stock AS Quantite,\n" +
                    "    Produit.quantit_mininmal As Minimum,\n" +
                    "    IFNULL(category.nom, 'Aucune') AS CategoryController ,\n" +
                    "    IFNULL(fournisseur.nom, 'Aucun') AS Fournisseur\n" +
                    "    FROM Produit LEFT JOIN fournisseur  \n" +
                    "    ON Produit.code_fourniseur = fournisseur.code \n" +
                    "    LEFT JOIN category \n" +
                    "    ON Produit.code_category = category.code ORDER BY id";
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ListProduits.add(new SelectProduit(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Code"),
                        rs.getInt("Quantite"),
                        rs.getInt("Minimum"),
                        rs.getString("CategoryController"),
                        rs.getString("Fournisseur")
                ));
            }
            productTable.setItems(ListProduits);
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void addProductScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/products/AddProduct.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Ajouter Produit");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static public int selectedId() {
        return id;
    }
    @FXML
    private void categoryScreen() {
        Parent root ;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/category/Categorie.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Categorie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void UpdateProductScreen() {
        Parent root;
        SelectProduit sp = productTable.getSelectionModel().getSelectedItem();
        try {
            if (sp == null) {
                alert.setTitle("Erreur");
                alert.setHeaderText("Selectionner an element");
                alert.setContentText("Selectionner Un element Pour le mis a jour");
                alert.setAlertType(Alert.AlertType.ERROR);
                if (alert.showAndWait().get() == ButtonType.OK) {
                    refreshTable();
                    return;
                }
                return;
            }
            id = sp.getId();
            String name = sp.getNom();
            String code = sp.getCode();
            String quantite = String.valueOf(sp.getQuantite());
            String minimum = String.valueOf(sp.getQuantite_min());
            FXMLLoader Loader = new FXMLLoader(getClass().getResource("/resources/views/products/DeleteProduct.fxml"));
            root = Loader.load();
            UpdateProductController upc = Loader.getController();
            upc.fillFields(name,code,quantite,minimum);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("MaJ Produit");
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    @FXML
    private void searchByName() {
        String Name = searchField.getText();
        if (Name.isEmpty()) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Nom non valid");
            alert.setContentText("Entrer un Nom ");
            alert.setAlertType(Alert.AlertType.ERROR);
            if (alert.showAndWait().get() == ButtonType.OK) {
                refreshTable();
                return;
            }
        }
        try {
            ListProduits.clear();
            String query = "SELECT Produit.id AS Id,\n" +
                    "Produit.nom AS Nom,\n" +
                    "Produit.code_produit AS 'Code',\n" +
                    "Produit.quantit_stock AS Quantite,\n" +
                    "Produit.quantit_mininmal As Minimum, \n" +
                    "IFNULL(category.nom, 'Aucune') AS CategoryController ,\n" +
                    "IFNULL(fournisseur.nom, 'Aucun') AS Fournisseur \n" +
                    "FROM Produit LEFT JOIN fournisseur ON \n" +
                    "Produit.code_fourniseur = fournisseur.code  \n" +
                    "LEFT JOIN category  ON Produit.code_category = category.code \n" +
                    "HAVING Nom = " + "'" + Name + "' ORDER BY id;";
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                ListProduits.add(new SelectProduit(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Code"),
                        rs.getInt("Quantite"),
                        rs.getInt("Minimum"),
                        rs.getString("CategoryController"),
                        rs.getString("Fournisseur")
                ));
                productTable.setItems(ListProduits);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        refreshTable();
        populateComboBox();
        populateFourniseur();
        filterComboBox.setOnAction(this::filterByCategory);
        filterComboBox1.setOnAction(this::filterByFournisseur);
    }

    private void loadData() {
        connection = DBConnection.getConnection();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        minCol.setCellValueFactory(new PropertyValueFactory<>("quantite_min"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        fornisseurCol.setCellValueFactory(new PropertyValueFactory<>("fournisseur"));
        Callback<TableColumn<SelectProduit, String>, TableCell<SelectProduit, String>> cellFoctory = (TableColumn<SelectProduit, String> param) ->
        {
            // make cell containing buttons
            final TableCell<SelectProduit, String> cell = new TableCell<SelectProduit, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    }
                    else {
                        FontAwesomeIconView upIcon =  new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_UP);
                        FontAwesomeIconView downIcon =  new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_DOWN);
                        downIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                                        +"-fx-left-margin : 10px"
                        );
                        upIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        downIcon.setOnMouseClicked((MouseEvent event) -> {
                            try{
                                selectProduit = productTable.getSelectionModel().getSelectedItem();
                                int min = selectProduit.getQuantite_min();
                                int newQuantite = selectProduit.getQuantite() - 1;
                                int quantite = newQuantite;
                                int id = selectProduit.getId();
                                if(min > newQuantite) {
                                        Alert alert = new Alert(Alert.AlertType.NONE);
                                        alert.setTitle("Attention");
                                        alert.setHeaderText("La quantitte est inferieur au minimum");
                                        alert.setContentText("La quantitte est inferieur au minimum Charger le stock!");
                                        alert.setAlertType(Alert.AlertType.INFORMATION);
                                        alert.showAndWait();
                                } else if(quantite == 0) {
                                    Alert alert = new Alert(Alert.AlertType.NONE);
                                    alert.setTitle("Attention");
                                    alert.setHeaderText("La quantitte est 0");
                                    alert.setContentText("Cette article est out of stock recharger le");
                                    alert.setAlertType(Alert.AlertType.INFORMATION);
                                    alert.showAndWait();
                                }
                                String query = "UPDATE `Produit` SET `quantit_stock`= " +newQuantite+ " WHERE id = " + id;
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            } catch (SQLException Se) {
                                Se.printStackTrace();
                            }
                        });
                        upIcon.setOnMouseClicked((MouseEvent event) -> {
                            try{
                                selectProduit = productTable.getSelectionModel().getSelectedItem();
                                int newQuantite = selectProduit.getQuantite() + 1;
                                int id = selectProduit.getId();
                                String query = "UPDATE `Produit` SET `quantit_stock`= " +newQuantite+ " WHERE id = " + id;
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            } catch (SQLException Se) {
                                Se.printStackTrace();
                            }
                        });
                        HBox managebtn = new HBox(upIcon, downIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(downIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(upIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);
                    }
                }
            };
            return cell;
        };
        AjoutSupp.setCellFactory(cellFoctory);
    }
    @FXML
    public void deleteProduct() {

        try {
            if (productTable.getSelectionModel().getSelectedItem() == null) {
                alert.setTitle("Erreur");
                alert.setHeaderText("Pas de element est selectionner");
                alert.setContentText("Selectionner Un element pour le supprimer ");
                alert.setAlertType(Alert.AlertType.ERROR);
                if (alert.showAndWait().get() == ButtonType.OK) {
                    refreshTable();
                    return;
                }
                return;
            }
            SelectProduit produit = productTable.getSelectionModel().getSelectedItem();
            String queryDelete = "DELETE FROM `Produit` WHERE id = " + produit.getId();
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(queryDelete);
            preparedStatement.execute();
            refreshTable();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Supprimer");
            a.setContentText("L'operation est termine");
            a.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateComboBox() {
        categories.clear();
        connection = DBConnection.getConnection();
        String query = "SELECT category.nom AS 'Nom' FROM category";
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                categories.add(rs.getString("Nom"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        filterComboBox.setItems(null); //beh yjou mratbin machhi jdid yatle3 foug lgdim ki t'inserer mn lapp
        filterComboBox.setItems(categories);
    }

    private void populateFourniseur() {
        fournisseurs.clear();
        try {
            String query = "SELECT fournisseur.nom AS 'Nom' FROM fournisseur";
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                fournisseurs.add(rs.getString("Nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        filterComboBox1.setItems(null);
        filterComboBox1.setItems(fournisseurs);
    }

    private void filterByCategory(ActionEvent event) {
        String category = filterComboBox.getSelectionModel().getSelectedItem();
        connection = DBConnection.getConnection();
        try {
            ListProduits.clear();
            String query = "SELECT Produit.id AS Id,\n" +
                    "Produit.nom AS Nom,\n" +
                    "Produit.code_produit AS 'Code',\n" +
                    "Produit.quantit_stock AS Quantite,\n" +
                    "Produit.quantit_mininmal As Minimum, \n" +
                    "IFNULL(category.nom, 'Aucune') AS CategoryController ,\n" +
                    "IFNULL(fournisseur.nom, 'Aucun') AS Fournisseur \n" +
                    "FROM Produit LEFT JOIN fournisseur ON \n" +
                    "Produit.code_fourniseur = fournisseur.code  \n" +
                    "LEFT JOIN category  ON Produit.code_category = category.code \n" +
                    "HAVING CategoryController = " + "'" + category + "' ORDER BY id;";
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                ListProduits.add(new SelectProduit(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Code"),
                        rs.getInt("Quantite"),
                        rs.getInt("Minimum"),
                        rs.getString("CategoryController"),
                        rs.getString("Fournisseur")
                ));
            }
            productTable.setItems(ListProduits);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterByFournisseur(ActionEvent event) {
        String fournisseur = filterComboBox1.getSelectionModel().getSelectedItem();
        connection = DBConnection.getConnection();
        try {
            ListProduits.clear();
            String query = "SELECT Produit.id AS Id,\n" +
                    "Produit.nom AS Nom,\n" +
                    "Produit.code_produit AS 'Code',\n" +
                    "Produit.quantit_stock AS Quantite,\n" +
                    "Produit.quantit_mininmal As Minimum, \n" +
                    "IFNULL(category.nom, 'Aucune') AS CategoryController ,\n" +
                    "IFNULL(fournisseur.nom, 'Aucun') AS Fournisseur \n" +
                    "FROM Produit LEFT JOIN fournisseur ON \n" +
                    "Produit.code_fourniseur = fournisseur.code  \n" +
                    "LEFT JOIN category  ON Produit.code_category = category.code \n" +
                    "HAVING Fournisseur = " + "'" + fournisseur + "' ORDER BY id;";
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                ListProduits.add(new SelectProduit(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Code"),
                        rs.getInt("Quantite"),
                        rs.getInt("Minimum"),
                        rs.getString("CategoryController"),
                        rs.getString("Fournisseur")
                ));
                productTable.setItems(ListProduits);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void fournScreen(MouseEvent even) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/fournisseur/fournisseurScreen.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)even.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Fournisseurs");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void logout(MouseEvent event) {
        Stage stage;
        Parent root;
        try {
             root = FXMLLoader.load(getClass().getResource("/resources/views/login/login.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    public void ClientScreen(MouseEvent even) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/clients/ClientScreen.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)even.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Clients");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    public void StaticsPage(MouseEvent ev) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/statics/products/ProductsStaticsScreen.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Statistique");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
