package M;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {

    private PlaylistLinkedList playlist = new PlaylistLinkedList();
    private MediaPlayer mediaPlayer;
    
    private Label trackTitleLabel = new Label("No Playlist Loaded");
    private ListView<String> musicListView = new ListView<>();
    private ProgressBar musicProgressBar = new ProgressBar(0.0);

    @Override
    public void start(Stage primaryStage) {
        
        // 1. Create File & Folder Action Buttons
        Button btnCreate = new Button("➕ New Playlist");
        Button btnImport = new Button("📂 Import Folder");
        Button btnSave = new Button("💾 Save Playlist");
        Button btnLoadFile = new Button("📥 Load Saved Playlist");

        // 2. Create Media Navigation Buttons
        Button btnPrev = new Button("⏮ Prev");
        Button btnPlay = new Button("▶ Play");
        Button btnPause = new Button("⏸ Pause");
        Button btnNext = new Button("⏭ Next");

        // ==========================================
        // ACTION LISTENERS (The Core Logic Engine)
        // ==========================================

        // CREATE: Wipes the screen and resets RAM nodes completely
        btnCreate.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            playlist.clear();
            musicListView.getItems().clear();
            trackTitleLabel.setText("Created Empty Playlist. Add songs via Import!");
            musicProgressBar.setProgress(0.0);
        });

        // IMPORT FOLDER: Opens a native system directory browser pop-up to scan MP3s
        btnImport.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select your Music Folder");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            
            if (selectedDirectory != null) {
                File[] listOfFiles = selectedDirectory.listFiles();
                if (listOfFiles != null) {
                    for (File file : listOfFiles) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                            String title = file.getName().replace(".mp3", ""); 
                            playlist.addTrack(new Track(title, file.getAbsolutePath()));
                            musicListView.getItems().add("🎵  " + title);
                        }
                    }
                }
                loadCurrentTrack();
            }
        });

        // SAVE PLAYLIST: Exports active linked list data array structures to a text destination path
        btnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Playlist Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Playlist Files (*.txt)", "*.txt"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    playlist.saveToFile(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // LOAD SAVED PLAYLIST: Reconstructs internal linked list by importing text record files
        btnLoadFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Saved Playlist Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Playlist Files (*.txt)", "*.txt"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    if (mediaPlayer != null) mediaPlayer.stop();
                    musicListView.getItems().clear();
                    
                    playlist.loadFromFile(file);
                    
                    // Repopulate View list dynamically matching loaded linked nodes
                    PlaylistLinkedList.Node temp = playlist.getCurrent();
                    playlist.getAtIndex(0); // reset pointer to head row safely
                    PlaylistLinkedList.Node parseNode = playlist.getCurrent();
                    while (parseNode != null) {
                        musicListView.getItems().add("🎵  " + parseNode.track.getTitle());
                        parseNode = parseNode.next;
                    }
                    playlist.getAtIndex(0); // return pointer index baseline back to front
                    loadCurrentTrack();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Media Button Control bindings
        btnPlay.setOnAction(e -> handlePlay());
        btnPause.setOnAction(e -> handlePause());
        btnNext.setOnAction(e -> handleNext());
        btnPrev.setOnAction(e -> handlePrev());

        // Dynamic Interactive List Viewer selection tracking
        musicListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = newValue.intValue();
            if (selectedIndex >= 0) {
                playlist.getAtIndex(selectedIndex); 
                loadCurrentTrack();
                handlePlay();
            }
        });

        // ==========================================
        // CSS COOSMECTIC PRESENTATION LAYOUTS
        // ==========================================
        trackTitleLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        musicListView.setPrefHeight(160);
        musicListView.setStyle("-fx-background-color: #1A1A1E; -fx-control-inner-background: #1A1A1E; -fx-text-fill: #FFFFFF; -fx-background-radius: 10px;");
        musicProgressBar.setMaxWidth(Double.MAX_VALUE);
        musicProgressBar.setStyle("-fx-accent: #00E5FF; -fx-control-inner-background: #2D2D35;");

        String genericBtnStyle = "-fx-background-color: #1E1E24; -fx-text-fill: #00E5FF; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-cursor: hand; -fx-padding: 6 14;";
        String folderBtnStyle = "-fx-background-color: #00E5FF; -fx-text-fill: #121214; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-cursor: hand; -fx-padding: 6 14;";

        Button[] dataManagementButtons = {btnCreate, btnImport, btnSave, btnLoadFile};
        for (Button b : dataManagementButtons) b.setStyle(folderBtnStyle);

        Button[] playbackButtons = {btnPrev, btnPlay, btnPause, btnNext};
        for (Button b : playbackButtons) b.setStyle(genericBtnStyle);

        // Grid Box grouping assignments
        HBox dataBarLayout = new HBox(10, btnCreate, btnImport, btnSave, btnLoadFile);
        dataBarLayout.setAlignment(Pos.CENTER);

        HBox playbackControlLayout = new HBox(15, btnPrev, btnPlay, btnPause, btnNext);
        playbackControlLayout.setAlignment(Pos.CENTER);

        VBox coreApplicationStack = new VBox(20, dataBarLayout, musicListView, trackTitleLabel, playbackControlLayout, musicProgressBar);
        coreApplicationStack.setAlignment(Pos.CENTER);
        coreApplicationStack.setStyle("-fx-background-color: linear-gradient(to bottom right, #121214, #202026); -fx-padding: 25px;");

        // Open window frame view container definitions
        primaryStage.setTitle("Neon Database-Free Playlist Desk");
        primaryStage.setScene(new Scene(coreApplicationStack, 620, 440));
        primaryStage.show();
    }

    private void loadCurrentTrack() {
        if (mediaPlayer != null) { mediaPlayer.stop(); }

        PlaylistLinkedList.Node currentNode = playlist.getCurrent();
        if (currentNode != null) {
            trackTitleLabel.setText("Playing: " + currentNode.track.getTitle());
            try {
                Media media = new Media(new File(currentNode.track.getFilePath()).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.currentTimeProperty().addListener((observable, oldDur, currentDur) -> {
                    if (mediaPlayer.getTotalDuration() != null) {
                        double progress = currentDur.toMillis() / mediaPlayer.getTotalDuration().toMillis();
                        Platform.runLater(() -> musicProgressBar.setProgress(progress));
                    }
                });
                mediaPlayer.setOnEndOfMedia(() -> handleNext());
            } catch (Exception e) {
                trackTitleLabel.setText("❌ File Path target could not run!");
            }
        }
    }

    private void handlePlay() { if (mediaPlayer != null) mediaPlayer.play(); }
    private void handlePause() { if (mediaPlayer != null) mediaPlayer.pause(); }
    private void handleNext() { playlist.nextSong(); loadCurrentTrack(); handlePlay(); }
    private void handlePrev() { playlist.prevSong(); loadCurrentTrack(); handlePlay(); }

    public static void main(String[] args) { launch(args); }
}
