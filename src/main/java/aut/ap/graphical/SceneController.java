package aut.ap.graphical;

import aut.ap.command.Service;
import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.Email;
import aut.ap.model.User;
import jakarta.persistence.NoResultException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;


public class SceneController {
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField age;
    @FXML
    private TextField lastName;


    @FXML
    private Label ageErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Label emailErrorLabel;

    @FXML
    private ListView<Email> emailListView;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private AnchorPane contentArea;

    public void loadView(String fxml) {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void switchToLoginScene(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSignUpScene(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/signup.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void backToScene1(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/scene1.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent e) throws IOException {
        Window window = ((MenuItem) e.getSource()).getParentPopup().getOwnerWindow();
        Stage stage = (Stage) window;

        Parent root = FXMLLoader.load(getClass().getResource("/scene1.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent event) {
        String emailStr = Service.normalizeEmail(email.getText());
        String passwordStr = password.getText();

        Boolean conditions = true;
        User user = null;

        try {
            user = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery
                    ("SELECT * FROM user " + "WHERE email = :userName", User.class).setParameter("userName", emailStr).getSingleResult());
        } catch (NoResultException e) {
            conditions = false;
            emailErrorLabel.setText("Wrong email");
        }

        try {
            if (user != null && !user.getPassword().equals(passwordStr)) {
                passwordErrorLabel.setText("Wrong password");

                conditions = false;
            }
        }
            catch(NullPointerException _){}


        if(conditions) {

            showPopup("Welcome back, " + user.getName(), "Login succeeded");

            try {

                root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void signUp(ActionEvent event) {

        String ageStr = age.getText();
        String nameStr = name.getText();
        String lastNameStr = lastName.getText();
        String passwordStr = password.getText();
        String emailStr = Service.normalizeEmail(email.getText());

        Boolean isDuplicate = true;
        Boolean conditions = true;
        try {
            Service.emailCheck(emailStr);
        } catch (Exception e) {
            isDuplicate = false;
        }

        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        ageErrorLabel.setText("");

        if (isDuplicate) {
            conditions = false;
            emailErrorLabel.setText("This email already exist");
        }

        try {
            Service.isStrongPass(passwordStr);
        } catch (IllegalArgumentException e) {
            conditions = false;
            passwordErrorLabel.setText("Password must contain at least 8 character");
        }

        int intAge = 18;
        try {
           intAge = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            conditions = false;
            ageErrorLabel.setText("Age must be number");
        }

        if (conditions) {
            showPopup(Service.submit(nameStr, lastNameStr, passwordStr, intAge, emailStr),
                    "Sign up was successful");

            try {

                root = FXMLLoader.load(getClass().getResource("/scene1.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (Exception e){
                System.err.println(e.getMessage());
            }
        }


    }

    public void showPopup(String text, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();


    }

    @FXML
    public void initialize() {
        if (emailListView != null) {
            emailListView.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(Email item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/email/email-item.fxml"));
                            Parent root = loader.load();
                            ItemController controller = loader.getController();
                            controller.setData(item);
                            setGraphic(root);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }



}
