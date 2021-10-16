package dev.cinomod.MarkdownGenerator;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class StartViewController {

    private Stage stage;

    @FXML
    private TextField numRows;

    private int numberOfRows;

    @FXML
    private TextField numCols;

    private int numberOfColumns;

    private Map<String, TextField> tableTextFields;

    private TextField saveLocationTextField;

    @FXML
    public void reactToClick(Event e) {
        // get the current stage
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        tableTextFields = new LinkedHashMap<>();

        numberOfColumns = Integer.parseInt(numCols.getText());
        numberOfRows = Integer.parseInt(numRows.getText());

        // When the next button is clicked here, this should now create a
        // numRowsxnumCols table with the defined column names
        VBox tableVBox = new VBox();

        for (int row = 1; row <= numberOfRows + 1; row++) {
            HBox hbox = new HBox();
            for (int col = 1; col <= numberOfColumns; col++) {
                TextField rowEntry = new TextField();
                rowEntry.setId(row + "-" + col);

                tableTextFields.put(row + "-" + col, rowEntry);
                hbox.getChildren().add(rowEntry);
            }
            tableVBox.getChildren().add(hbox);
        }

        saveLocationTextField = new TextField();
        saveLocationTextField.setId("saveLocation");

        // Use filechooser to allow a user to specify the save location for the Markdown file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("test.md");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MD Files", "*.md")
        );

        Button specifySaveLocationButton = new Button("Save location");
        specifySaveLocationButton.setOnAction(event -> {

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile == null) {
                saveLocationTextField.setText(System.getProperty("user.home") + "/test.md");
            } else {
                saveLocationTextField.setText(selectedFile.getPath());
            }
        });

        HBox saveLocationSection = new HBox(specifySaveLocationButton, saveLocationTextField);

        // Save button
        Button saveButton = new Button("Save file");
        saveButton.setOnAction(new SaveButtonHanlder());

        tableVBox.getChildren().add(saveLocationSection);
        tableVBox.getChildren().add(saveButton);

        Scene scene = new Scene(tableVBox);
        stage.setScene(scene);
        stage.show();

    }

    // Save the table to a file. The saved file needs to be in markdown format
    private class SaveButtonHanlder implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            Path file = Path.of(saveLocationTextField.getText());
            Charset charset = Charset.forName("US-ASCII");

            try {
                BufferedWriter writer = Files.newBufferedWriter(file, charset);

                StringBuilder tableDivider = new StringBuilder();
                for (int row = 1; row <= numberOfRows + 1; row++) {

                    for (int col = 1; col <= numberOfColumns; col++) {
                        String cellValue = tableTextFields.get(row + "-" + col).getText();

                        if (col != numberOfColumns) {
                            String rowEntryValue = "| " + cellValue + " | ";
                            writer.write(rowEntryValue, 0, rowEntryValue.length());

                            if (row == 1) tableDivider.append(("| --- | "));
                        } else {
                            String rowEntryValue = cellValue + " |";
                            writer.write(rowEntryValue, 0, rowEntryValue.length());
                            writer.newLine();

                            if (row == 1) tableDivider.append(("--- |\n"));
                        }
                    }

                    if (row == 1) writer.write(tableDivider.toString(), 0, tableDivider.toString().length());
                }

                writer.close();
                System.out.println("Done writing");

            } catch (IOException ioException) {
                System.err.format("IOException: %s%n", ioException);
            }

        }
    }
}
