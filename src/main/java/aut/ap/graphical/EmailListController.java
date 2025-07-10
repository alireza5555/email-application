package aut.ap.graphical;

import aut.ap.model.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

public class EmailListController {
    @FXML
    private ListView<Email> emailListView;

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

    public void setEmailList(List<Email> emails) {
        emailListView.getItems().setAll(emails);
    }
}
