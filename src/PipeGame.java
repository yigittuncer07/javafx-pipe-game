/*
 *
 * NAME: Yiğit Tuncer, STUDENT ID: 150121073
 * NAME: ARDACAN ÖZENER, STUDENT ID: 150120026
 *
 */

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PipeGame extends Application {

    static Tile[][] gridArray = new Tile[4][4];
    static ArrayList<Integer> answerList = new ArrayList<>();
    static ArrayList<Integer> unusedList = new ArrayList<>();
    static File file;
    static Scanner scanner;

    //Creates the grid pane
    private static final GridPane gridPane = new GridPane();

    //Creates the combo box
    private final ComboBox<String> comboBox = new ComboBox<>();

    //Integer fields
    private static int numberOfMoves = 0;
    private static int unlockedLevel = 1;
    private static int currentLevel;
    private static int end;
    private static final int numberOfLevels = folderSize(new File("src/levels"));
    private static double ballSpeed = 8;

    //Creates the labels
    private final Label displayLabel = new Label("Welcome to PipeGame!");
    private final Label movesLabel = new Label("Number of moves: " + numberOfMoves);

    //Creates the ball
    private final ImageView ball = new ImageView(new Image("extraImages/Ball.png", 30, 30, false, false));

    //Creates booleans
    private static boolean isBallRolled;
    private static boolean isSoundOn = true;

    //Creates a variable for each image in the images directory
    public static final Image corner00 = new Image("images/00.png", 120, 120, false, false);
    public static final Image corner01 = new Image("images/01.png", 120, 120, false, false);
    public static final Image corner10 = new Image("images/10.png", 120, 120, false, false);
    public static final Image corner11 = new Image("images/11.png", 120, 120, false, false);
    public static final Image empty = new Image("images/Empty.png", 120, 120, false, false);
    public static final Image emptyFree = new Image("images/EmptyFree.png", 120, 120, false, false);
    public static final Image endH = new Image("images/EndH.png", 120, 120, false, false);
    public static final Image endV = new Image("images/EndV.png", 120, 120, false, false);
    public static final Image pipeH = new Image("images/PipeH.png", 120, 120, false, false);
    public static final Image pipeStaticH = new Image("images/PipeStaticH.png", 120, 120, false, false);
    public static final Image pipeStaticV = new Image("images/PipeStaticV.png", 120, 120, false, false);
    public static final Image pipeV = new Image("images/PipeV.png", 120, 120, false, false);
    public static final Image starterH = new Image("images/StarterHBall.png", 120, 120, false, false);
    public static final Image starterV = new Image("images/StarterVBall.png", 120, 120, false, false);
    public static final Image pipeStatic00 = new Image("images/00Static.png", 120, 120, false, false);
    public static final Image pipeStatic01 = new Image("images/01Static.png", 120, 120, false, false);
    public static final Image pipeStatic10 = new Image("images/10Static.png", 120, 120, false, false);
    public static final Image pipeStatic11 = new Image("images/11Static.png", 120, 120, false, false);


    @Override
    public void start(Stage primaryStage) {

        /*Sets up combo box so that the first level
         * is not locked and the rest to be normal.
         * The game works by checking if the title of
         * the level has "(LOCKED)" in it. When a level
         * is won the LOCKED is removed in the setComboBox method*/
        comboBox.setValue("Select Level");
        comboBox.setStyle("-fx-background-color: #d1d1d1;");
        ObservableList<String> list = comboBox.getItems();
        list.add("Level 1");
        for (int i = 2; i <= numberOfLevels; i++) {
            list.add("(LOCKED) Level " + i);
        }

        //Sets up the music
        File file = new File("src/sounds/karinka.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.4);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);

        //Create upper hBox
        HBox upperHBox = new HBox();

        //Sets the preferred sizes of the nodes so that they don't move
        comboBox.setPrefWidth(140);
        displayLabel.setPrefWidth(150);
        movesLabel.setPrefWidth(140);

        //Adds nodes to the upper hBox and set color
        upperHBox.setStyle("-fx-background-color: #464a52;");
        upperHBox.getChildren().addAll(comboBox, displayLabel, movesLabel);
        upperHBox.setSpacing(25);

        //Creates speed slider and its image
        ImageView speedImageView = new ImageView(new Image("extraImages/speed.png", 50, 30, false, false));
        Slider speedSlider = new Slider(0, 9.7, 8);
        speedSlider.setPrefHeight(8);
        speedSlider.setPrefWidth(100);

        //Creates the sound slider
        Slider soundSlider = new Slider(0, 1, 0.4);
        soundSlider.setPrefHeight(8);
        soundSlider.setPrefWidth(100);

        //Creates sound button
        Button soundButton = new Button();
        soundButton.setPrefWidth(50);
        ImageView soundImageView = new ImageView(new Image("extraImages/SoundOn.png", 50, 20, false, false));
        soundButton.setGraphic(soundImageView);

        //Sets up info button
        Button infoButton = new Button();
        infoButton.setGraphic(new ImageView(new Image("extraImages/info.png", 20, 20, false, false)));
        VBox vBox1 = new VBox();
        Text title = new Text();
        Text names = new Text();
        Text credits = new Text();
        vBox1.setSpacing(30);
        vBox1.setAlignment(Pos.CENTER);
        title.setText("PIPE GAME");
        title.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        names.setText("Made by: \n" + "Ardacan Özener \n" + "Yiğit Tuncer");
        credits.setText("Thanks to JetBrains and StackOverflow");
        names.setStyle("-fx-text-fill: blue; -fx-font-size: 12px;");
        vBox1.getChildren().addAll(title, names, credits);
        Scene infoScene = new Scene(vBox1, 300, 300);
        vBox1.setStyle("-fx-background-color: #464a52;");
        Stage infoStage = new Stage();
        infoStage.setScene(infoScene);
        infoStage.setResizable(false);

        //Sets up helpButton
        Button helpButton = new Button("Exit Game");

        //Create lower hBox
        HBox lowerHBox = new HBox();

        //Set color of lower hBox and add nodes
        lowerHBox.setStyle("-fx-background-color: #464a52;");
        lowerHBox.getChildren().addAll(soundButton, soundSlider, speedImageView, speedSlider, helpButton, infoButton);
        lowerHBox.setSpacing(10);

        //Create vBox
        VBox vBox = new VBox();
        Line line = new Line();

        //Create a line to separate the game from the labels
        line.setStartX(0);
        line.setEndX(480);
        line.setStartY(30);
        line.setEndY(30);

        //Sets the style of the labels
        movesLabel.setStyle("-fx-text-fill: #a8bce6; -fx-font-size: 15px;");
        displayLabel.setStyle("-fx-text-fill: #a8bce6; -fx-font-size: 15px;");

        //Sets the start scene
        Image a = new Image("extraImages/A.png", 120, 120, false, false);
        Image e = new Image("extraImages/E.png", 120, 120, false, false);
        Image g = new Image("extraImages/G.png", 120, 120, false, false);
        Image i = new Image("extraImages/I.png", 120, 120, false, false);
        Image m = new Image("extraImages/M.png", 120, 120, false, false);
        Image p = new Image("extraImages/P.png", 120, 120, false, false);

        gridPane.add(new ImageView(empty), 0, 0);
        gridPane.add(new ImageView(empty), 1, 0);
        gridPane.add(new ImageView(empty), 2, 0);
        gridPane.add(new ImageView(empty), 3, 0);

        gridPane.add(new ImageView(p), 0, 1);
        gridPane.add(new ImageView(i), 1, 1);
        gridPane.add(new ImageView(p), 2, 1);
        gridPane.add(new ImageView(e), 3, 1);

        gridPane.add(new ImageView(g), 0, 2);
        gridPane.add(new ImageView(a), 1, 2);
        gridPane.add(new ImageView(m), 2, 2);
        gridPane.add(new ImageView(e), 3, 2);

        gridPane.add(new ImageView(empty), 0, 3);
        gridPane.add(new ImageView(empty), 1, 3);
        gridPane.add(new ImageView(empty), 2, 3);
        gridPane.add(new ImageView(empty), 3, 3);


        /*-----------------------------------Events-----------------------------------*/

        //This part works when an item is chosen from the combo box
        comboBox.setOnAction(event -> {
            //Resets ball roll status
            isBallRolled = false;

            //Resets number of moves
            resetNumberOfMoves();

            //Gets the chosen value
            String value = comboBox.getValue();

            //Checks if the level is a locked level
            if (value.contains("LOCKED")) {
                displayLabel.setText("That level is Locked!");
                setGridLocked();
            } else {
                try {
                    //Gets which level is chosen
                    currentLevel = Integer.parseInt(value.substring(6));
                    displayLabel.setText("Level " + currentLevel);

                    //Sets the grid according to the level
                    setGrid();

                } catch (Exception ignored) {
                }
            }

        });

        /*The following code calls the methods needed to move
         * the tiles the user has clicked*/
        gridPane.setOnMousePressed(event1 -> {

            //Gets the coordinates of the first click
            int x1 = (int) event1.getSceneX();
            int y1 = (int) event1.getSceneY();

            //Gets which grid is clicked according to the click
            int[] pressedGrid1 = getWhichTileIsClicked(x1, y1);

            //Same thing but for when the mouse is released
            gridPane.setOnMouseReleased(event2 -> {

                //Gets coordinates for the release
                int x2 = (int) event2.getSceneX();
                int y2 = (int) event2.getSceneY();

                //Gets which grid is chosen when mouse is released
                int[] pressedGrid2 = getWhichTileIsClicked(x2, y2);

                //Moves the chosen tiles
                boolean isWon = moveTile(pressedGrid1[0], pressedGrid1[1], pressedGrid2[0], pressedGrid2[1]);
                if (isWon) {
                    animateBall();
                    isBallRolled = true;
                }

            });
        });

        //This part makes the sound button work
        soundButton.setOnAction(event2 -> {
            ImageView soundOffImageView = new ImageView(new Image("extraImages/SoundOff.png", 50, 20, false, false));

            if (isSoundOn) {
                mediaPlayer.pause();
                soundButton.setGraphic(soundOffImageView);
                isSoundOn = false;
            } else {
                mediaPlayer.play();
                soundButton.setGraphic(soundImageView);
                isSoundOn = true;
            }
        });

        //This part makes the hint button work
        helpButton.setOnAction(event4 -> primaryStage.close());

        //This makes the sound slider work
        soundSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(soundSlider.getValue()));

        //This makes the speed slider work
        speedSlider.valueProperty().addListener(observable -> ballSpeed = speedSlider.getValue());

        //This makes the info button work
        infoButton.setOnAction(event5 -> infoStage.show());

        //Adds elements to the main pane
        vBox.getChildren().add(upperHBox);
        vBox.getChildren().add(line);
        vBox.getChildren().add(gridPane);
        vBox.getChildren().add(lowerHBox);

        //Sets up the scene
        Scene scene = new Scene(vBox, 480, 538);

        //Sets up the stage
        primaryStage.setTitle("Pipe Game v1.1");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /*-----------------------------------Methods-----------------------------------*/

    //This method launches the program
    public static void main(String[] args) {
        launch(args);
    }

    //This returns the size of any directory
    public static int folderSize(File directory) {
        int length = 0;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                length++;
            }
        }
        return length;
    }

    //This method clears the grid of any nodes
    private static void clearGrid() {
        gridPane.getChildren().clear();
    }

    //This method return which tile is pressed according to x and y
    private static int[] getWhichTileIsClicked(int x, int y) {
        int[] positions = new int[2];

        //Determines where x lands
        int posX;
        if (x <= 120) {
            posX = 0;
        } else if (x <= 240) {
            posX = 1;
        } else if (x <= 360) {
            posX = 2;
        } else {
            posX = 3;
        }

        //Determines where y lands
        int posY;
        if (y <= 150 && y >= 30) {
            posY = 0;
        } else if (y <= 270) {
            posY = 1;
        } else if (y <= 390) {
            posY = 2;
        } else {
            posY = 3;
        }

        positions[0] = posX;
        positions[1] = posY;
        return positions;
    }

    //This method checks whether the game is won
    public boolean isGameWon() {
        int index;
        int x;
        int y;
        try {
            while (true) {
                index = answerList.get(answerList.size() - 1);
                x = index % 4;
                y = index / 4;
                if (gridArray[x][y].connections[unusedList.get(unusedList.size() - 1)] == 'u') {
                    if (gridArray[x][y - 1].connections[0] == 'd') {
                        answerList.add(index - 4);
                        unusedList.add(1);
                    } else if (gridArray[x][y - 1].connections[1] == 'd') {
                        answerList.add(index - 4);
                        unusedList.add(0);
                    } else {
                        break;
                    }
                } else if (gridArray[x][y].connections[unusedList.get(unusedList.size() - 1)] == 'd') {
                    if (gridArray[x][y + 1].connections[0] == 'u') {
                        answerList.add(index + 4);
                        unusedList.add(1);
                    } else if (gridArray[x][y + 1].connections[1] == 'u') {
                        answerList.add(index + 4);
                        unusedList.add(0);
                    } else {
                        break;
                    }
                } else if (gridArray[x][y].connections[unusedList.get(unusedList.size() - 1)] == 'l') {
                    if (gridArray[x - 1][y].connections[0] == 'r') {
                        answerList.add(index - 1);
                        unusedList.add(1);
                    } else if (gridArray[x - 1][y].connections[1] == 'r') {
                        answerList.add(index - 1);
                        unusedList.add(0);
                    } else {
                        break;
                    }
                } else if (gridArray[x][y].connections[unusedList.get(unusedList.size() - 1)] == 'r') {
                    if (gridArray[x + 1][y].connections[0] == 'l') {
                        answerList.add(index + 1);
                        unusedList.add(1);
                    } else if (gridArray[x + 1][y].connections[1] == 'l') {
                        answerList.add(index + 1);
                        unusedList.add(0);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        if (answerList.contains(end)) {
            if (currentLevel == unlockedLevel) {
                unlockLevel();
            } else {
                setDisplayLabel("Correct! You Won!");
            }
            return true;
        }
        return false;

    }

    //Sets the grid for when the user chooses a locked level
    public void setGridLocked() {
        Image cross1 = new Image("extraImages/cross.png", 120, 120, false, false);
        Image cross2 = new Image("extraImages/cross1.png", 120, 120, false, false);

        gridPane.add(new ImageView(cross1), 0, 0);
        gridPane.add(new ImageView(empty), 1, 0);
        gridPane.add(new ImageView(empty), 2, 0);
        gridPane.add(new ImageView(cross2), 3, 0);

        gridPane.add(new ImageView(empty), 0, 1);
        gridPane.add(new ImageView(cross1), 1, 1);
        gridPane.add(new ImageView(cross2), 2, 1);
        gridPane.add(new ImageView(empty), 3, 1);

        gridPane.add(new ImageView(empty), 0, 2);
        gridPane.add(new ImageView(cross2), 1, 2);
        gridPane.add(new ImageView(cross1), 2, 2);
        gridPane.add(new ImageView(empty), 3, 2);

        gridPane.add(new ImageView(cross2), 0, 3);
        gridPane.add(new ImageView(empty), 1, 3);
        gridPane.add(new ImageView(empty), 2, 3);
        gridPane.add(new ImageView(cross1), 3, 3);


    }

    //This method checks whether the tiles are movable and also moves said tiles
    public boolean moveTile(int x1, int y1, int x2, int y2) {

        //Checks if the game is currently at the starter page or if the level has already been completed
        if (displayLabel.getText().contains("Welcome") || isBallRolled) {
            return false;
        }

        //Checks if the tiles are movable
        if ((gridArray[x1][y1].isMovable) && (gridArray[x2][y2].isFree) && (((Math.abs(x1 - x2) == 1) && (y1 == y2)) || ((Math.abs(y1 - y2) == 1) && (x1 == x2)))) {

            //Updates number of moves
            numberOfMoves++;
            movesLabel.setText("Number of moves: " + numberOfMoves);

            //Moves the tiles
            gridPane.getChildren().remove(gridArray[x1][y1].imageview);
            gridPane.getChildren().remove(gridArray[x2][y2].imageview);

            Tile temp = gridArray[x1][y1];
            gridArray[x1][y1] = gridArray[x2][y2];
            gridArray[x2][y2] = temp;

            gridPane.add(gridArray[x1][y1].imageview, x1, y1);
            gridPane.add(gridArray[x2][y2].imageview, x2, y2);

            if (answerList.contains((4 * y1) + x1)) {
                while (answerList.contains((4 * y1) + x1)) {
                    unusedList.remove(answerList.size() - 1);
                    answerList.remove(answerList.size() - 1);
                }
            }
        }
        //Checks if the game is won
        return isGameWon();
    }

    //This method sets the grid according to the level files
    public static void setGrid() throws FileNotFoundException {

        //Clears the lists
        clearGrid();
        answerList.clear();
        unusedList.clear();

        int index;
        int x;
        int y;

        //Finds the level file according to current level
        file = new File("src/levels/Level" + currentLevel + ".txt");
        scanner = new Scanner(file);
        //setting the map
        while (scanner.hasNextLine()) {

            String[] line = (scanner.nextLine()).split(",");

            //Checks if the line is empty
            if (line.length < 3) {
                continue;
            }

            index = Integer.parseInt(line[0]);
            index--;

            x = index % 4;
            y = index / 4;

            switch (line[1]) {
                case "Pipe":
                    gridArray[x][y] = new CornerPipe(line[2], true);
                    gridPane.add(gridArray[x][y].imageview, x, y);
                    break;
                case "PipeStatic":
                    gridArray[x][y] = new CornerPipe(line[2], false);
                    gridPane.add(gridArray[x][y].imageview, x, y);
                    break;
                case "Empty":
                    gridArray[x][y] = new EmptyC(line[2].equals("Free"));
                    gridPane.add(gridArray[x][y].imageview, x, y);
                    break;
                case "Starter":

                    gridArray[x][y] = new StartPipe(line[2]);
                    answerList.add(index);
                    unusedList.add(0);
                    gridPane.add(gridArray[x][y].imageview, x, y);
                    break;
                case "End":
                    gridArray[x][y] = new EndPipe(line[2]);
                    end = index;
                    gridPane.add(gridArray[x][y].imageview, x, y);
                    break;
                default:
                    System.out.println("Invalid input for" + line[0]);
                    break;
            }

        }
    }

    //This method sets the elements of the combo box
    private void unlockLevel() {
        if (currentLevel == folderSize(new File("src/levels"))) {
            return;
        }
        unlockedLevel++;
        ObservableList<String> list = comboBox.getItems();
        list.set(unlockedLevel - 1, "Level " + unlockedLevel);
        setDisplayLabel("Level " + (unlockedLevel) + " unlocked!");

    }

    //This method sets the display label
    private void setDisplayLabel(String s) {
        displayLabel.setText(s);
    }

    //This method sets the number of moves
    private void resetNumberOfMoves() {
        numberOfMoves = 0;
        movesLabel.setText("Number of moves: " + 0);
    }

    //This method animates the ball
    private void animateBall() {

        //This part removes the ball from the image
        ImageView starterNoBallH = new ImageView(new Image("images/StarterH.png", 120, 120, false, false));
        ImageView starterNoBallV = new ImageView(new Image("images/StarterV.png", 120, 120, false, false));

        int indexOfStarter = answerList.get(0);
        int x = indexOfStarter % 4;
        int y = indexOfStarter / 4;

        if (gridArray[x][y].imageview.getImage().equals(starterV)) {
            gridPane.add(starterNoBallV, x, y);
        } else if (gridArray[x][y].imageview.getImage().equals(starterH)) {
            gridPane.add(starterNoBallH, x, y);
        }

        //This adds the rolling ball
        gridPane.add(ball, 0, 0);

        //This creates the path that the ball will roll
        Path path = new Path();

        path.getElements().add(new MoveTo(x * 120 + 55, y * 120 + 20));

        int index;
        int x1;
        int y1;

        for (int i = 0; i < answerList.size(); i++) {

            index = answerList.get(i);
            x1 = index % 4;
            y1 = index / 4;

            Image image = gridArray[x1][y1].imageview.getImage();

            if (image.equals(corner00) || image.equals(corner01) || image.equals(corner10) || image.equals(corner11) || image.equals(pipeStatic00) || image.equals(pipeStatic01) || image.equals(pipeStatic10) || image.equals(pipeStatic11)) {

                if (image.equals(corner00) || image.equals(pipeStatic00)) {
                    if (unusedList.get(i) == 1) {
                        path.getElements().add(new CubicCurveTo(x1 * 120, y1 * 120 + 25, x1 * 120 + 60, 25 + y1 * 120, 60 + x1 * 120, y1 * 120));
                    } else {
                        path.getElements().add(new CubicCurveTo(60 + x1 * 120, y1 * 120, x1 * 120 + 60, 25 + y1 * 120, x1 * 120, y1 * 120 + 25));
                    }
                } else if (image.equals(corner01) || image.equals(pipeStatic01)) {
                    if (unusedList.get(i) == 0) {
                        path.getElements().add(new CubicCurveTo(x1 * 120 + 60, y1 * 120, x1 * 120 + 60, 25 + y1 * 120, 120 + x1 * 120, 25 + y1 * 120));
                    } else {
                        path.getElements().add(new CubicCurveTo(x1 * 120 + 120, 25 + y1 * 120, x1 * 120 + 60, 25 + y1 * 120, x1 * 120 + 60, y1 * 120));
                    }
                } else if (image.equals(corner10) || image.equals(pipeStatic10)) {
                    if (unusedList.get(i) == 1) {
                        path.getElements().add(new CubicCurveTo(x1 * 120, y1 * 120 + 25, x1 * 120 + 60, 25 + y1 * 120, 60 + x1 * 120, 50 + y1 * 120));
                    } else {
                        path.getElements().add(new CubicCurveTo(x1 * 120 + 60, y1 * 120 + 50, x1 * 120 + 60, 25 + y1 * 120, x1 * 120, 25 + y1 * 120));
                    }
                } else if (image.equals(corner11) || image.equals(pipeStatic11)) {
                    if (unusedList.get(i) == 0) {
                        path.getElements().add(new CubicCurveTo(60 + x1 * 120, 50 + y1 * 120, x1 * 120 + 60, 25 + y1 * 120, 120 + x1 * 120, 25 + y1 * 120));
                    } else {
                        path.getElements().add(new CubicCurveTo(120 + x1 * 120, 25 + y1 * 120, x1 * 120 + 60, 25 + y1 * 120, 60 + x1 * 120, 50 + y1 * 120));
                    }
                }

            } else {
                path.getElements().add(new LineTo(55 + x1 * 120, 20 + y1 * 120));
            }

            //Creates the transition
            PathTransition pathTransition = new PathTransition();

            //Sets the animation time
            pathTransition.setDuration(Duration.seconds(10 - ballSpeed));

            //Sets the path of the ball
            pathTransition.setPath(path);

            pathTransition.setNode(ball);
            pathTransition.play();

        }
    }
}

