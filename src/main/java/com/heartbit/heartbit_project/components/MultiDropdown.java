package com.heartbit.heartbit_project.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import java.io.IOException;
import java.util.List;

public class MultiDropdown extends AnchorPane {

    @FXML private HBox header;
    @FXML private ImageView arrow;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox contentBox;

    private static final String packagePath = "/com/heartbit/heartbit_project";
    private boolean open = false;

    public MultiDropdown() {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource(packagePath + "/components/MultiDropdown.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Populate the dropdown with your list of labels. */
    public void setItems(List<String> items) {
        contentBox.getChildren().clear();
        for (String name : items) {
            CheckBox cb = new CheckBox(name);
            cb.setFont(javafx.scene.text.Font.font("Tahoma", 15));
            VBox.setMargin(cb, new Insets(0, 0, 20, 0));
            contentBox.getChildren().add(cb);
        }
    }

    /** Programmatically check the given items (by text). */
    public void setSelectedItems(List<String> toSelect) {
        for (Node node : contentBox.getChildren()) {
            if (node instanceof CheckBox cb) {
                cb.setSelected(toSelect.contains(cb.getText()));
            }
        }
    }

    /** Returns all the texts from selected CheckBoxes. */
    public List<String> getValues() {
        return contentBox.getChildren().stream()
                .filter(n -> n instanceof CheckBox cb && cb.isSelected())
                .map(n -> ((CheckBox) n).getText())
                .toList();
    }

    @FXML
    private void initialize() {
        scrollPane.setVisible(false);
        scrollPane.setManaged(false);
        header.setOnMouseClicked(evt -> {
            open = !open;
            scrollPane.setVisible(open);
            scrollPane.setManaged(open);
            arrow.setRotate(open ? 180 : 0);
        });
    }
}
