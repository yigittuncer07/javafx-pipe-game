import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

public class PipeGame extends Application {

    private static ImageView[][] imageViews = new ImageView[4][4];

    private static File levelFile;
    private static GridPane gridPane = new GridPane();

    private static ComboBox<String> comboBox = new ComboBox<>();


    private static int numberOfMoves = 0;
    private static int unlockedLevel = 1;
    private static int currentLevel;
    private static final int numberOfLevels = folderSize(new File("src/levels"));


    private static Label displayLabel = new Label("Welcome to PipeGame!");
    private static Label movesLabel = new Label("Number of moves: " + numberOfMoves);


    private static final Image corner00 = new Image("images/00.png", 120, 120, false, false);
    private static final Image corner01 = new Image("images/01.png", 120, 120, false, false);
    private static final Image corner10 = new Image("images/10.png", 120, 120, false, false);
    private static final Image corner11 = new Image("images/11.png", 120, 120, false, false);
    private static final Image empty = new Image("images/Empty.png", 120, 120, false, false);
    private static final Image emptyFree = new Image("images/EmptyFree.png", 120, 120, false, false);
    private static final Image endH = new Image("images/EndH.png", 120, 120, false, false);
    private static final Image endV = new Image("images/EndV.png", 120, 120, false, false);
    private static final Image pipeH = new Image("images/PipeH.png", 120, 120, false, false);
    private static final Image pipeStaticH = new Image("images/PipeStaticH.png", 120, 120, false, false);
    private static final Image pipeStaticV = new Image("images/PipeStaticV.png", 120, 120, false, false);
    private static final Image pipeV = new Image("images/PipeV.png", 120, 120, false, false);
    private static final Image starterH = new Image("images/StarterH.png", 120, 120, false, false);
    private static final Image starterV = new Image("images/StarterV.png", 120, 120, false, false);
    private static final Image pipeStatic00 = new Image("images/00Static.png", 120, 120, false, false);
    private static final Image pipeStatic01 = new Image("images/01Static.png", 120, 120, false, false);
    private static final Image pipeStatic10 = new Image("images/10Static.png", 120, 120, false, false);
    private static final Image pipeStatic11 = new Image("images/11Static.png", 120, 120, false, false);


    @Override
    public void start(Stage stage) {

        //Create combo box
        comboBox.setValue("Select Level");
        ObservableList<String> list = comboBox.getItems();
        list.add("Level 1");
        for (int i = 2; i <= numberOfLevels; i++) {
            list.add("(LOCKED) Level " + i);
        }


        //Create h Box
        HBox hBox = new HBox();
        comboBox.setPrefWidth(140);

        displayLabel.setPrefWidth(140);

        movesLabel.setPrefWidth(140);

        hBox.getChildren().addAll(comboBox, displayLabel, movesLabel);
        hBox.setSpacing(35);

        //Create grid pane
        gridPane.setGridLinesVisible(true);

        //Create v Box
        VBox pane = new VBox();
        Line line = new Line();

        //Create a line to separate the game from the labels
        line.setStartX(0);
        line.setEndX(480);
        line.setStartY(30);
        line.setEndY(30);

        //Sets the start scene
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                ImageView empty1 = new ImageView(empty);
                gridPane.add(empty1, i, k);
            }
        }

        comboBox.setOnAction(event -> {
            //Resets number of moves
            setNumberOfMoves(0);

            //Gets the chosen value
            String value = comboBox.getValue();

            //Checks if the level is a locked level
            if (value.contains("LOCKED")) {
                displayLabel.setText("That level is Locked!");
            } else {
                try {
                    currentLevel = Integer.parseInt(value.substring(6));
                    displayLabel.setText("Level " + currentLevel);
                    //Sets the grid according to the level
                    setGrid(currentLevel);

                } catch (Exception ignored) {
                    System.out.println("Error at get Substring");
                }
            }

        });


        gridPane.setOnMousePressed(event1 -> {
            int x1 = (int) event1.getSceneX();
            int y1 = (int) event1.getSceneY();

            int[] pressedGrid1 = getWhichTileIsClicked(x1, y1);

            gridPane.setOnMouseReleased(event2 -> {
                int x2 = (int) event2.getSceneX();
                int y2 = (int) event2.getSceneY();

                int[] pressedGrid2 = getWhichTileIsClicked(x2, y2);

                if (isTileMovable(pressedGrid1[0], pressedGrid1[1], pressedGrid2[0], pressedGrid2[1])) {
                    moveTile(pressedGrid1[0], pressedGrid1[1], pressedGrid2[0], pressedGrid2[1]);
                    if (isWon()) {
                        if (currentLevel == unlockedLevel) {
                            unlockLevel();
                            System.out.println("Unlock Level Called");
                        }
                    }
                }


            });
        });


        //Adds elements to the main pane
        pane.getChildren().add(hBox);
        pane.getChildren().add(line);
        pane.getChildren().add(gridPane);

        //Sets up the scene
        Scene scene = new Scene(pane, 480, 510);

        //Sets up the stage
        stage.setTitle("Pipe Game v1.0");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    //Launches the program
    public static void main(String[] args) {
        launch(args);
    }

    /*-----------------------------------Methods-----------------------------------*/

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

    //This method sets the grid according to the given level
    private static void setGrid(int level) {
        //Checks if level has actually been chosen
        if (level == 0) {
            return;
        }
        Scanner scanner = null;
        try {
            //Gets the file according to the level
            String fileName = "Level" + level + ".txt";
            levelFile = new File("src/levels/" + fileName);

            scanner = new Scanner(levelFile);
            //Creates a scanner for the file
        } catch (Exception e) {
            System.out.println("File not found");
        }
        //Clears the grid
        clearGrid();

        //Loops for each part of the grid
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                assert scanner != null;
                String line = scanner.nextLine();

                //This following part makes sure the line isn't empty
                boolean quit = false;
                while (!quit) {
                    if (line.isEmpty()) {
                        line = scanner.nextLine();
                    } else {
                        quit = true;
                    }
                }

                //This part gets the values from the line
                String[] temp = line.split(",");
                String type = temp[1];
                String property = temp[2];

                //Add images according to the given file
                switch (type) {
                    case "Starter":
                        if (property.equals("Vertical")) {
                            ImageView imageView = new ImageView(starterV);
                            gridPane.add(imageView, x, y);
                            imageViews[x][y] = imageView;
                        } else {
                            ImageView imageView = new ImageView(starterH);
                            gridPane.add(imageView, x, y);
                            imageViews[x][y] = imageView;
                        }
                        break;
                    case "End":
                        if (property.equals("Vertical")) {
                            ImageView imageView = new ImageView(endV);
                            gridPane.add(imageView, x, y);
                            imageViews[x][y] = imageView;
                        } else {
                            ImageView imageView = new ImageView(endH);
                            gridPane.add(imageView, x, y);
                            imageViews[x][y] = imageView;
                        }
                        break;
                    case "Empty":
                        if (property.equals("Free")) {
                            ImageView imageView = new ImageView(emptyFree);
                            gridPane.add(imageView, x, y);
                            imageViews[x][y] = imageView;
                        } else {
                            ImageView imageView = new ImageView(empty);
                            gridPane.add(imageView, x, y);
                            imageViews[x][y] = imageView;
                        }
                        break;
                    case "Pipe":
                        switch (property) {
                            case "Vertical":
                                ImageView imageView = new ImageView(pipeV);
                                gridPane.add(imageView, x, y);
                                imageViews[x][y] = imageView;
                                break;
                            case "Horizontal":
                                ImageView imageView1 = new ImageView(pipeH);
                                gridPane.add(imageView1, x, y);
                                imageViews[x][y] = imageView1;
                                break;
                            case "01":
                                ImageView imageView2 = new ImageView(corner01);
                                gridPane.add(imageView2, x, y);
                                imageViews[x][y] = imageView2;
                                break;
                            case "00":
                                ImageView imageView3 = new ImageView(corner00);
                                gridPane.add(imageView3, x, y);
                                imageViews[x][y] = imageView3;
                                break;
                            case "10":
                                ImageView imageView4 = new ImageView(corner10);
                                gridPane.add(imageView4, x, y);
                                imageViews[x][y] = imageView4;
                                break;
                            case "11":
                                ImageView imageView5 = new ImageView(corner11);
                                gridPane.add(imageView5, x, y);
                                imageViews[x][y] = imageView5;
                                break;
                        }
                        break;
                    case "PipeStatic":
                        if (property.equals("Vertical")) {
                            gridPane.add(new ImageView(pipeStaticV), x, y);
                            imageViews[x][y] = new ImageView(pipeStaticV);
                        } else if (property.equals("Horizontal")) {
                            gridPane.add(new ImageView(pipeStaticH), x, y);
                            imageViews[x][y] = new ImageView(pipeStaticH);
                        } else if (property.equals("01")) {
                            ImageView imageView2 = new ImageView(pipeStatic01);
                            gridPane.add(imageView2, x, y);
                            imageViews[x][y] = imageView2;
                        } else if (property.equals("00")) {
                            ImageView imageView2 = new ImageView(pipeStatic00);
                            gridPane.add(imageView2, x, y);
                            imageViews[x][y] = imageView2;
                        } else if (property.equals("11")) {
                            ImageView imageView2 = new ImageView(pipeStatic11);
                            gridPane.add(imageView2, x, y);
                            imageViews[x][y] = imageView2;
                        } else if (property.equals("10")) {
                            ImageView imageView2 = new ImageView(pipeStatic10);
                            gridPane.add(imageView2, x, y);
                            imageViews[x][y] = imageView2;
                        }
                        break;
                }
            }
        }

    }

    //This method clears the grid of any nodes
    private static void clearGrid() {
        gridPane.getChildren().clear();
    }

    //This method moves tiles, according to the x y values and also checks if the game is won.
    private static boolean moveTile(int x1, int y1, int x2, int y2) {
        numberOfMoves++;
        movesLabel.setText("Number of moves: " + numberOfMoves);

        //Sets up place where tile is moved
        gridPane.getChildren().remove(imageViews[x2][y2]);
        ImageView movedImage = new ImageView(getImageType(x1, y1));
        imageViews[x2][y2] = movedImage;
        gridPane.add(movedImage, x2, y2);

        //Removes old tile
        gridPane.getChildren().remove(imageViews[x1][y1]);
        ImageView emptyImage = new ImageView(emptyFree);
        imageViews[x1][y1] = emptyImage;
        gridPane.add(emptyImage, x1, y1);

        //Check if game is won
        return isWon();
    }

    //Checks if the tiles are movable
    private static boolean isTileMovable(int x1, int y1, int x2, int y2) {
        Image startImage = getImageType(x1, y1);
        Image endImage = getImageType(x2, y2);
        assert startImage != null;
        boolean b = !((startImage.equals(pipeStaticH) || startImage.equals(pipeStaticV) || startImage.equals(starterH) || startImage.equals(starterV) || startImage.equals(endH) || startImage.equals(endV) || startImage.equals(emptyFree)) || startImage.equals(pipeStatic01) || startImage.equals(pipeStatic11) || startImage.equals(pipeStatic10) || startImage.equals(pipeStatic00)) && endImage.equals(emptyFree);
        if ((Math.abs(x1 - x2) == 1) && (y1 - y2 == 0)) {
            return b;

        } else if ((x1 - x2 == 0) && (Math.abs(y1 - y2) == 1)) {
            return b;
        }
        return false;


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

    //Returns the type of image of a place in the grid
    private static Image getImageType(final int column, final int row) {
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                ImageView imageView = (ImageView) node;
                return imageView.getImage();
            }
        }
        return null;
    }

    private static void setNumberOfMoves(int moves) {
        numberOfMoves = moves;
        movesLabel.setText("Number of moves: " + moves);
    }

    private static void setDisplayLabel(String s) {
        displayLabel.setText(s);
    }

    //This method sets the elements of the combo box
    private static void unlockLevel() {
        unlockedLevel++;
        ObservableList<String> list = comboBox.getItems();
        list.set(unlockedLevel - 1, "Level " + unlockedLevel);
        setDisplayLabel("Level " + (unlockedLevel) + " unlocked!");

    }

    //Checks if the game is won
    private static boolean isWon() {

        
        return true;
    }
}
