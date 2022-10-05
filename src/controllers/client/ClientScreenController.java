package controllers.client;


import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import models.Client;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientScreenController implements Initializable {

    @FXML
    private TextField searchField,nameField,CodeField,teleField,AdressF,codeAbonField;
    @FXML
    private TableView<Client> clients;
    @FXML
    private TableColumn<Client, String> idCol;
    @FXML
    private TableColumn<Client, String> NomCol;
    @FXML
    private TableColumn<Client, String> NumCol;
    @FXML
    private TableColumn<Client, String> CodeCol;
    @FXML
    private TableColumn<Client, String> AddressCol;
    @FXML
    private TableColumn<Client, String> AbonementCol;
    @FXML
    private ComboBox<String> filterAbonement,AbonementChoice;
    private ObservableList<Client> ListClients = FXCollections.observableArrayList();
    private ObservableList<String> abonnement = FXCollections.observableArrayList();
    private ObservableList<String> Updateabonnement = FXCollections.observableArrayList();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    void Ajouter() {
        connection  = DBConnection.getConnection();
        String name = nameField.getText();
        String code = CodeField.getText();
        String phone = teleField.getText();
        String adress = AdressF.getText();
        String abonement = codeAbonField.getText();
        if (name.isEmpty() || code.isEmpty() || phone.isEmpty() || adress.isEmpty() || abonement.isEmpty()) {
            alert.setHeaderText("Information Non Complete");
            alert.setContentText("Entrer tous les informations");
            alert.showAndWait();
        }
        else if (!phone.matches("[0-9]{10}+")) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Numero Telephone Non Valid");
            alert.setContentText("Entrer un numero de telephone Valid !");
            alert.showAndWait();
        }
        else if (!name.matches("[a-zA-Z0-9_ ]{3,}")) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Nom non Valid");
            alert.setContentText("Entrer un Nom Valid !");
            alert.showAndWait();
        } else {
            update();
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("MaJ");
            alert.setHeaderText("operation est termine");
            alert.setContentText("Client est Mise a jour !");
            alert.showAndWait();
            nameField.setText("");
            teleField.setText("");
            CodeField.setText("");
            AdressF.setText("");
            codeAbonField.setText("");
            refreshTable();
        }
    }

    @FXML
    void addClientScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/clients/AddClient.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Ajouter Client");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private int checkSelected() {
        Client client = clients.getSelectionModel().getSelectedItem();
        if(client == null){
            return 0;
        }
        return 1;
    }
    @FXML
    void deleteClient() {
        int selected = checkSelected();
        //depending on the result of checkSelected do :
        switch (selected)  {
            //0 means that no item selected
            case 0 : {
                alert.setTitle("Erreur");
                alert.setContentText("Choissisiez Un Client pour supprimer");
                if(alert.showAndWait().get() == ButtonType.OK) {
                    return;
                }
            }
            //1 means there is a selected item
            case 1 : {
                try {
                    Client client = clients.getSelectionModel().getSelectedItem();
                    String queryDelete = "DELETE FROM `client` WHERE id = " + client.getId();
                    connection = DBConnection.getConnection();
                    preparedStatement = connection.prepareStatement(queryDelete);
                    preparedStatement.execute();
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Supprime !");
                    alert.setContentText("Le Client est supprimer");
                    refreshTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
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

    @FXML
    void refreshTable() {
        populateUpdateComboBox();
        abonnement.clear();
        ListClients.clear();
        populateComboBox();
        searchField.setText("");
        nameField.setText("");
        CodeField.setText("");
        teleField.setText("");
        AdressF.setText("");
        codeAbonField.setText("");
        filterAbonement.getSelectionModel().clearSelection();
        String query = "SELECT CLIENT.id AS Id, CLIENT.nom AS nom, CLIENT.num_tele AS NumeroTele,\n" +
                "CLIENT.Code AS 'Code', CLIENT.Adress AS Adress, IFNULL(abonement.nom, 'Non-Abonne') AS Abonement\n" +
                "FROM CLIENT LEFT JOIN abonement ON CLIENT.code_abonement = abonement.code ORDER BY id";
        try {

            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ListClients.add(new Client(
                        rs.getInt("Id"),
                        rs.getString("nom"),
                        rs.getString("NumeroTele"),
                        rs.getString("Code"),
                        rs.getString("Adress"),
                        rs.getString("Abonement")
                ));

            }
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        clients.setItems(ListClients);
    }

    @FXML
    void searchByName() {
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
            ListClients.clear();
            String query = "SELECT CLIENT.id AS Id, CLIENT.nom AS nom, CLIENT.num_tele AS NumeroTele,\n" +
                    "CLIENT.Code AS 'Code', CLIENT.Adress AS Adress, IFNULL(abonement.nom, 'Aucune') AS Abonement\n" +
                    "FROM CLIENT LEFT JOIN abonement ON CLIENT.code_abonement = abonement.code HAVING Nom = '" + Name + "'ORDER BY id";
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                ListClients.add(new Client(
                        rs.getInt("Id"),
                        rs.getString("nom"),
                        rs.getString("NumeroTele"),
                        rs.getString("Code"),
                        rs.getString("Adress"),
                        rs.getString("Abonement")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clients.setItems(ListClients);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        refreshTable();
        populateComboBox();
        filterAbonement.setOnAction(this::filterByAbonement);
        tableClicked();
        populateUpdateComboBox();
    }

    private void filterByAbonement(ActionEvent actionEvent) {
        String abonement = filterAbonement.getSelectionModel().getSelectedItem();
        System.out.println(abonement);
        connection = DBConnection.getConnection();

        try {
            ListClients.clear();
            String query = "SELECT CLIENT.id AS Id, CLIENT.nom AS nom, CLIENT.num_tele AS NumeroTele,\n" +
                    "CLIENT.Code AS 'Code', CLIENT.Adress AS Adress, IFNULL(abonement.nom, 'Aucune') AS Abonement\n" +
                    "FROM CLIENT LEFT JOIN abonement ON CLIENT.code_abonement = abonement.code HAVING Abonement = '" + abonement +"' ORDER BY id";
            System.out.println("Hrer");
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                ListClients.add(new Client(
                        rs.getInt("Id"),
                        rs.getString("nom"),
                        rs.getString("NumeroTele"),
                        rs.getString("Code"),
                        rs.getString("Adress"),
                        rs.getString("Abonement")
                ));
            }
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        clients.setItems(ListClients);
    }

    private void populateComboBox() {
        abonnement.clear();
        connection = DBConnection.getConnection();
        String query = "SELECT abonement.nom AS 'Nom' FROM abonement";
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                abonnement.add(rs.getString("Nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        filterAbonement.setItems(null); //beh yjou mratbin machhi jdid yatle3 foug lgdim ki t'inserer mn lapp
        filterAbonement.setItems(abonnement);
    }

    private void populateUpdateComboBox() {
        Updateabonnement.clear();
        connection = DBConnection.getConnection();
        String query = "SELECT CONCAT(abonement.code, '- ',abonement.nom) AS 'Code-Nom' FROM abonement";
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                Updateabonnement.add(rs.getString("Code-Nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        AbonementChoice.setItems(null);
        AbonementChoice.setItems(Updateabonnement);
    }

    private void loadData() {
        connection = DBConnection.getConnection();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        CodeCol.setCellValueFactory(new PropertyValueFactory<>("Code"));
        NomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        NumCol.setCellValueFactory(new PropertyValueFactory<>("num_tele"));
        AddressCol.setCellValueFactory(new PropertyValueFactory<>("Adress"));
        AbonementCol.setCellValueFactory(new PropertyValueFactory<>("Abonement"));
    }

    private void tableClicked() {
        clients.setOnMouseClicked(e -> tableEvent());
    }

    private void tableEvent() {
        Client f = clients.getSelectionModel().getSelectedItem();
        if(f == null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Pas de element selectionner");
            alert.setContentText("Svp selectionner un element !");
            alert.showAndWait();
        }
        else {
            nameField.setText(f.getNom());
            CodeField.setText(f.getCode());
            AdressF.setText(f.getAdress());
            teleField.setText(f.getNum_tele());
        }
    }

    private void update() {
        int id = clients.getSelectionModel().getSelectedItem().getId();
        String query = "UPDATE `client` SET `nom`=?,`num_tele`=?,`Adress`=?,`Code`=?,`code_abonement`= ? WHERE id = " + id ;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,nameField.getText());
            preparedStatement.setString(2,teleField.getText());
            preparedStatement.setString(4,CodeField.getText());
            preparedStatement.setString(3, AdressF.getText());
            preparedStatement.setString(5,codeAbonField.getText());
            preparedStatement.execute();
        } catch (SQLException sq) {
            sq.printStackTrace();
        }
    }

    @FXML
    public void StaticsPage(MouseEvent e) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/statics/clients/ClientsStaticsScreen.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Statistiques");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void addAbonement() {
        Parent root ;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/abonement/Abonement.fxml"));
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
}

