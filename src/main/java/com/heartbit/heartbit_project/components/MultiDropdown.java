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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /**
     * Call this to populate the dropdown with your list of labels.
     */
    public void setItems(List<String> items) {
        contentBox.getChildren().clear();
        for (String name : items) {
            CheckBox cb = new CheckBox(name);
            cb.setFont(javafx.scene.text.Font.font("Tahoma", 15));
            VBox.setMargin(cb, new Insets(0, 0, 20, 0));
            contentBox.getChildren().add(cb);
        }
    }

    /**
     * Returns zero-based indexes of checked items.
     */
    public List<Integer> getSelectedIndexes() {
        return IntStream.range(0, contentBox.getChildren().size())
                .filter(i -> {
                    var node = contentBox.getChildren().get(i);
                    return node instanceof CheckBox && ((CheckBox) node).isSelected();
                })
                .boxed()
                .collect(Collectors.toList());
    }

    @FXML
    private void initialize() {
        // start hidden
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
