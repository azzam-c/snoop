package com.azzam.snoop_one;

import com.azzam.snoop_one.model.MemoryLog;
import com.azzam.snoop_one.model.Snoop;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;

public class Main extends Application {

    private TableView<MemoryLog> table;

    @Override
    public void start(Stage stage) {
        Label appTitle = new Label("S.N.O.O.P");
        appTitle.getStyleClass().add("app-title");

        Label subtitle = new Label("System Navigation & Overhead Observation Panel");
        subtitle.getStyleClass().add("subtitle");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField snapshotCountField = new TextField();
        snapshotCountField.setPromptText("Snapshots");

        TextField intervalField = new TextField();
        intervalField.setPromptText("Interval (seconds)");

        Button startButton = new Button("Start Logging");
        startButton.getStyleClass().add("primary-button");

        Label statusLabel = new Label("Ready.");
        statusLabel.getStyleClass().add("status-label");

        table = createMemoryTable();

        HBox inputRow = new HBox(12);
        inputRow.getChildren().addAll(snapshotCountField, intervalField, startButton);
        inputRow.getStyleClass().add("input-row");

        VBox headerBox = new VBox(4);
        headerBox.getChildren().addAll(appTitle, subtitle);

        VBox card = new VBox(18);
        card.getChildren().addAll(headerBox, inputRow, table, statusLabel);
        card.getStyleClass().add("main-card");

        StackPane root = new StackPane(card);
        root.getStyleClass().add("root-pane");

        startButton.setOnAction(event -> {
            try {
                int snapshotCount = Integer.parseInt(snapshotCountField.getText());
                int intervalSeconds = Integer.parseInt(intervalField.getText());

                if (snapshotCount <= 0 || intervalSeconds <= 0) {
                    statusLabel.setText("Enter numbers greater than 0.");
                    return;
                }

                table.getItems().clear();
                startButton.setDisable(true);
                statusLabel.setText("Logging started...");

                startMemoryLogging(snapshotCount, intervalSeconds, startButton, statusLabel);

            } catch (NumberFormatException e) {
                statusLabel.setText("Enter valid whole numbers.");
            }
        });

        Scene scene = new Scene(root, 850, 520);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("Snoop");
        stage.setScene(scene);
        stage.show();
    }

    private TableView<MemoryLog> createMemoryTable() {
        TableView<MemoryLog> memoryTable = new TableView<>();
        memoryTable.getStyleClass().add("memory-table");

        TableColumn<MemoryLog, String> snapshotColumn = new TableColumn<>("Snapshot");
        snapshotColumn.setCellValueFactory(cellData -> {
            int index = memoryTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty("SNAPSHOT #" + index);
        });

        TableColumn<MemoryLog, String> timestampColumn = new TableColumn<>("Timestamp");
        timestampColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp().toString())
        );

        TableColumn<MemoryLog, String> usedColumn = new TableColumn<>("Used RAM (GiB)");
        usedColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", MemoryLog.toGiB(cellData.getValue().getUsed())))
        );

        TableColumn<MemoryLog, String> totalColumn = new TableColumn<>("Total RAM (GiB)");
        totalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", MemoryLog.toGiB(cellData.getValue().getTotal())))
        );

        snapshotColumn.setPrefWidth(130);
        timestampColumn.setPrefWidth(340);
        usedColumn.setPrefWidth(160);
        totalColumn.setPrefWidth(160);

        memoryTable.getColumns().addAll(snapshotColumn, timestampColumn, usedColumn, totalColumn);
        memoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        memoryTable.setPlaceholder(new Label("No snapshots recorded yet."));

        return memoryTable;
    }

    private void startMemoryLogging(int snapshotCount, int intervalSeconds, Button startButton, Label statusLabel) {    //setup & start, subroutine is meant to split the work
        Task<Snoop> loggingTask = new Task<>() {
            @Override
            protected Snoop call() throws Exception {
                ArrayList<MemoryLog> snoop = new ArrayList<>();
                Snoop s = new Snoop("Snoop 1", 1, snoop, intervalSeconds, snapshotCount);
                SystemInfo si = new SystemInfo();
                HardwareAbstractionLayer hal = si.getHardware();
                GlobalMemory memory = hal.getMemory();

                for (int i = 1; i <= snapshotCount; i++) {
                    Thread.sleep(intervalSeconds * 1000L);

                    long total = memory.getTotal();
                    long used = total - memory.getAvailable();

                    MemoryLog log = new MemoryLog(used, total, Instant.now());
                    s.addLog(log);

                    int snapshotNumber = i;
                    Platform.runLater(() -> {
                        table.getItems().add(log);
                        statusLabel.setText("Recorded snapshot " + snapshotNumber + " of " + snapshotCount + ".");
                    });
                }

                Platform.runLater(() -> {
                    statusLabel.setText("Logging complete.");
                    startButton.setDisable(false);
                });

                return s;
            }
        };

        Thread thread = new Thread(loggingTask);
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}