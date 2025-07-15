package aut.ap.graphical;

import aut.ap.command.Service;
import aut.ap.model.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemController {


    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label codeLabel;

    @FXML
    private Button replyButton;

    @FXML
    private Button forwardButton;

    private Email currentEmail;

    private SceneController sceneController;


    public void setData(Email email) {
        this.currentEmail = email;
        senderLabel.setText("From: " + email.getSender());
        subjectLabel.setText("Subject: " + email.getSubject());
        codeLabel.setText("Code: " + email.getCode());
    }


    @FXML
    private void handleReply() {
        try {
            SendController sendController = sceneController.loadView("/email/send-email.fxml");
            sendController.setSubject("[RE] " + currentEmail.getSubject());

            List<String> recipients = Service.getRecipients(currentEmail.getCode());
            recipients.add(currentEmail.getSender());
            Set<String> uniqueRecipientsSet = new HashSet<>(recipients);
            sendController.setRecipients(String.join(" ",uniqueRecipientsSet));
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }


    @FXML
    private void handleForward() {

        try {
           SendController sendController = sceneController.loadView("/email/send-email.fxml");
           sendController.setSubject("[FW] " + currentEmail.getSubject());
           sendController.setBody(currentEmail.getBody());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

    }


    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
