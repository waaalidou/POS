package controllers.fournisseur;


import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Fournisseur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class fournisseurScreenController implements Initializable {

    @FXML
    private TableView<Fournisseur> fournisseur;
    @FXML
    private TableColumn<Fournisseur, String> idCol,NomCol,CodeCol,NumCol,AddressCol;
    @FXML
    private TextField nameField,CodeField,teleField,searchField,AdressF;
    private ObservableList<Fournisseur> ListFournisseur = FXCollections.observableArrayList();
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet rs;
    private Alert a = new Alert(Alert.AlertType.ERROR);
    //function used to check if there is a selected item or not
    private int checkSelected() {
        Fournisseur fournisseurObj = fournisseur.getSelectionModel().getSelectedItem();
        if(fournisseurObj == null){
            return 0;
        }
        return 1;
    }
    @FXML
    private void deleteFournisseur() {
        int selected = checkSelected();
        //depending on the result of checkSelected do :
        switch (selected)  {
            //0 means that no item selected
            case 0 : {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Erreur");
                a.setContentText("Choissisiez Un fournisseur pour supprimer");
                if(a.showAndWait().get() == ButtonType.OK) {
                    return;
                }
            }
            //1 means there is a selected item
            case 1 : {
                try {
                    Fournisseur fournisseurObj = fournisseur.getSelectionModel().getSelectedItem();
                    String queryDelete = "DELETE FROM `fournisseur` WHERE id = " + fournisseurObj.getId();
                    connection = DBConnection.getConnection();
                    preparedStatement = connection.prepareStatement(queryDelete);
                    preparedStatement.execute();
                    a.setTitle("Supprimer");
                    a.setHeaderText("operation est termine");
                    a.setContentText("Element est supprimer!");
                    refreshTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    @FXML
    void refreshTable() {
        ListFournisseur.clear();
        searchField.setText("");
        nameField.setText("");
        CodeField.setText("");
        teleField.setText("");
        AdressF.setText("");
        try {
            String query = "SELECT fournisseur.id AS 'Id', fournisseur.nom AS 'Nom', fournisseur.num_tele AS 'Num Telephone', fournisseur.code AS 'code', fournisseur.adress AS 'Adresse' FROM fournisseur";
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ListFournisseur.add(new Fournisseur(
                    rs.getInt("Id"),
                    rs.getString("Nom"),
                    rs.getString("Num Telephone"),
                    rs.getString("Code"),
                    rs.getString("Adresse")
                        ));
                fournisseur.setItems(ListFournisseur);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    void addFournisseurScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/fournisseur/AddFournisseur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Ajouter Fournisseuur");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    void searchByName() {
        String Name = searchField.getText();
        if (Name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE);
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
            ListFournisseur.clear();
            String query = "SELECT fournisseur.id AS 'Id', " +
                    "fournisseur.nom AS 'Nom', fournisseur.num_tele " +
                    "AS 'Num Telephone', fournisseur.code AS 'code', " +
                    "fournisseur.adress AS 'Adresse' FROM fournisseur HAVING Nom = '"+Name+"'" ;
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                ListFournisseur.add(new Fournisseur(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Num Telephone"),
                        rs.getString("Code"),
                        rs.getString("Adresse")
                ));
            }
            rs.close();
            fournisseur.setItems(ListFournisseur);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goBackFunction(MouseEvent event) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/products/ProductView.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Produits");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    void logout(MouseEvent event) {
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        refreshTable();
        tableClicked();
    }
    private void tableClicked() {
        fournisseur.setOnMouseClicked(e -> tableEvent());
    }

    private void tableEvent() {
        Fournisseur f = fournisseur.getSelectionModel().getSelectedItem();
        if(f == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur");
            a.setHeaderText("Pas de element selectionner");
            a.setContentText("Svp selectionner un element !");
            a.showAndWait();
        }
        else {
            nameField.setText(f.getName());
            CodeField.setText(f.getCode());
            AdressF.setText(f.getAdresse());
            teleField.setText(f.getNum_tele());
        }
    }

    private void loadData() {
        connection = DBConnection.getConnection();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        NomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        NumCol.setCellValueFactory(new PropertyValueFactory<>("num_tele"));
        CodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        AddressCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
    }

    @FXML
    void Ajouter() {
        connection  = DBConnection.getConnection();
        String name = nameField.getText();
        String code = CodeField.getText();
        String phone = teleField.getText();
        String adress = AdressF.getText();
        if (name.isEmpty() || code.isEmpty() || phone.isEmpty() || adress.isEmpty()) {
            a.setTitle("Erreur");
            a.setHeaderText("Information Non Complete");
            a.setContentText("Entrer tous les informations");
            a.showAndWait();
        }
        else if (!phone.matches("[0-9]{10}+")) {
            a.setTitle("Erreur");
            a.setHeaderText("Numero Telephone Non Valid");
            a.setContentText("Entrer un numero de telephone Valid !");
            a.showAndWait();
        } else if (!name.matches("[a-zA-Z0-9_ ]{3,}")) {
            a.setTitle("Erreur");
            a.setHeaderText("Nom non Valid");
            a.setContentText("Entrer un Nom Valid !");
            a.showAndWait();
        }else {
            Update();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("MaJ");
            a.setHeaderText("operation est termine");
            a.setContentText("Les informations sont mis a jour!");

            a.showAndWait();
            nameField.setText("");
            teleField.setText("");
            CodeField.setText("");
            AdressF.setText("");
            refreshTable();
        }
    }
    private void Update() {
        int id =  fournisseur.getSelectionModel().getSelectedItem().getId();
        String code = fournisseur.getSelectionModel().getSelectedItem().getCode();
        String q = "UPDATE `fournisseur` SET `nom`=? ,`num_tele`=? ,`code`= ? ,`adress`=? WHERE id = " +  id ;
        String q2 = " UPDATE `produit` SET `code_fourniseur`=? WHERE code_fourniseur = '"+code+"'";
        try {
            PreparedStatement ps = connection.prepareStatement(q2);
            ps.setString(1,CodeField.getText());
            ps.execute();
            preparedStatement = connection.prepareStatement(q);
            preparedStatement.setString(1,nameField.getText());
            preparedStatement.setString(2,teleField.getText());
            preparedStatement.setString(3,CodeField.getText());
            preparedStatement.setString(4, AdressF.getText());
            preparedStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
