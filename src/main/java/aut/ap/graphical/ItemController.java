package aut.ap.graphical;

import aut.ap.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

        public void setData(Email email) {
            this.currentEmail = email;
            senderLabel.setText("From: " + email.getSender());
            subjectLabel.setText("Subject: " + email.getSubject());
            codeLabel.setText("Code: " + email.getCode());
        }

        @FXML
        private void handleReply() {
            System.out.println("Reply to: " + currentEmail.getCode());
            // اینجا می‌تونی یه پنجره پاسخ یا کدی برای ارسال ریپلای باز کنی
        }

        @FXML
        private void handleForward() {
            System.out.println("Forward email: " + currentEmail.getCode());
            // اینجا هم می‌تونی پنجره فوروارد بزنی یا کد بزاری
        }
    }
