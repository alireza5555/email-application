package aut.ap.graphical;

import aut.ap.command.Service;
import aut.ap.framework.UserSession;
import aut.ap.model.Email;
import aut.ap.model.Recipients;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendController {
    @FXML
    private TextField recipients;
    @FXML
    private TextField subject;
    @FXML
    private TextArea body;

    @FXML
    private Label recipientsError;

    @FXML
    private Label subjectError;


    public void sendEmail(){

            recipientsError.setText("");
            subjectError.setText("");

            if(recipients.getText().isBlank()){
                recipientsError.setText("Enter at least one recipient");
                return;
            }
            else{
                recipientsError.setText("");
            }

            List<String> recipientsStr = recipientCheck(recipients.getText());
            String subjectStr = subject.getText();
            String bodyStr = body.getText();


            if (subjectStr == null || subjectStr.isBlank()) {
                subjectError.setText("Subject can't be empty.");
                return;
            } else {
                subjectError.setText("");
            }

            Email email = Service.addEmailToDB(UserSession.get(), subjectStr, bodyStr, recipientsStr);

            if (email != null)
                SceneController.showPopup("Code: " + email.getCode(), "Email sent successfully");

            else {
                SceneController.showPopup("Please try again", "Failed to send email");
            }



    }


    public List<String> recipientCheck(String recipients){
        List<String> emails = List.of(recipients.split(" "));
        List<String> trueEmails = new ArrayList<>();
        emails.forEach(email-> {
            email = Service.normalizeEmail(email);
            if(Service.emailCheck(email)){
            trueEmails.add(email);
        }});
        return trueEmails;
    }

    public void setSubject(String subject) {
        this.subject.setText(subject);
    }

    public void setBody(String body) {
        this.body.setText(body);
    }

    public void setRecipients(String recipients) {
        this.recipients.setText(recipients);
        this.recipients.setEditable(false);
    }
}
