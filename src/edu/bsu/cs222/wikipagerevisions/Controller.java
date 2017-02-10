package edu.bsu.cs222.wikipagerevisions;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Document;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Controller {
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Revisions, String> userColumn;
    @FXML
    private TableColumn<Revisions, String> timestampColumn;
    @FXML
    private TableView<Revisions> revisionsTable;
    @FXML
    private Label Notify;

    private Model model = new Model();
    private boolean hasBeenSearched = false;

    @FXML
    public void handleSearchButton() {
        if (!searchField.getText().equals("")) {
            timestampColumn.setText("Timestamp");
            Platform.runLater(() -> {
                clear();
                model.clear();
                hasBeenSearched = true;
                model.executeModel(searchField.getText());
                checkNotifications();
                loadListToGUI(model.getRevisionsList(), "timestamp");
            });
        }
    }

    @FXML
    public void handleUserCountButton() {
        if (hasBeenSearched) {
            timestampColumn.setText("Revision Count");
            Platform.runLater(() -> {
                clear();
                System.out.println(model.getUniqueUserRevisionsList().toString());
                loadListToGUI(model.getUniqueUserRevisionsList(), "RevisionsCount");
            });
        }
    }

    @FXML
    private void loadListToGUI(List<Revisions> rev, String secondColumn) {
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>(secondColumn));
        for(Revisions temp: rev) {
            revisionsTable.getItems().add(temp);
        }
    }

    private void clear() {
        revisionsTable.getItems().removeAll(model.getRevisionsList());
        revisionsTable.getItems().removeAll(model.getUniqueUserRevisionsList());
        Notify.setText("");
    }

    public void checkNotifications() {
        if (model.isGoodConnection()) {
            handleRedirection();
            handlePageDoesNotExist();
        }
        else {
            handleBadConnection();
        }
    }

    private void handleRedirection(){
        if (model.isRedirection()) {
            Notify.setText(model.getRedirection());
        }
    }

    private void handlePageDoesNotExist() {
        if (!model.doesPageExist()) {
            Notify.setText("Page does not exist");
        }
    }

    private boolean handleBadConnection() {
        if (!model.isGoodConnection()) {
            Notify.setText("Page could not be Reached. Check internet connection");
            return false;
        }
        return true;
    }
}