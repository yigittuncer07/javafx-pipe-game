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
import sun.invoke.empty.Empty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PipeGame extends Application {

    static int end;
    //4*4 map array
    static Tile[][] mp = new Tile[4][4];
    static ArrayList<Integer> ans = new ArrayList<Integer>();
    static ArrayList<Integer> unused = new ArrayList<Integer>();
    static File file;
    static String tx;
    static Scanner sc;

    private static ImageView[][] imageViews = new ImageView[4][4];

    private static File levelFile;
    private static GridPane gridPane = new GridPane();

    private static int numberOfMoves = 0;

    private static Label displayLabel = new Label("Welcome to PipeGame!");
    private static Label movesLabel = new Label("Number of moves: " + numberOfMoves);


    static final Image corner00 = new Image("images/00.png", 120, 120, false, false);
    static final Image corner01 = new Image("images/01.png", 120, 120, false, false);
    static final Image corner10 = new Image("images/10.png", 120, 120, false, false);
    static final Image corner11 = new Image("images/11.png", 120, 120, false, false);
    static final Image empty = new Image("images/Empty.png", 120, 120, false, false);
    static final Image emptyFree = new Image("images/EmptyFree.png", 120, 120, false, false);
    static final Image endH = new Image("images/EndH.png", 120, 120, false, false);
    static final Image endV = new Image("images/EndV.png", 120, 120, false, false);
    static final Image pipeH = new Image("images/PipeH.png", 120, 120, false, false);
    static final Image pipeStaticH = new Image("images/PipeStaticH.png", 120, 120, false, false);
    static final Image pipeStaticV = new Image("images/PipeStaticV.png", 120, 120, false, false);
    static final Image pipeV = new Image("images/PipeV.png", 120, 120, false, false);
    static final Image starterH = new Image("images/StarterH.png", 120, 120, false, false);
   static final Image starterV = new Image("images/StarterV.png", 120, 120, false, false);
    static final Image pipeStatic00 = new Image("images/00Static.png", 120, 120, false, false);
    static final Image pipeStatic01 = new Image("images/01Static.png", 120, 120, false, false);
    static final Image pipeStatic10 = new Image("images/10Static.png", 120, 120, false, false);
    static final Image pipeStatic11 = new Image("images/11Static.png", 120, 120, false, false);


    @Override
    public void start(Stage stage) {

        //Create combo box
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setValue("Select Level");
        comboBox.getItems().add("Level 1");
        for (int i = 2; i <= folderSize(new File("src/levels")); i++) {
            comboBox.getItems().add("Level " + i);
        }

        //Create labels

        //Create h box
        HBox hBox = new HBox();
        hBox.getChildren().addAll(comboBox, displayLabel, movesLabel);
        hBox.setSpacing(40);

       //Create grid pane
        gridPane.setGridLinesVisible(true);

        //Create vBox
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
            //Gets the chosen value
            String value = comboBox.getValue();
            int level = 0;

            //Checks if the level is a locked level
            if (value.contains("LOCKED")) {
                displayLabel.setText("That level is Locked!");
            } else {
                try {
                    level = Integer.parseInt(value.substring(6));
                    displayLabel.setText("Level " + level);
                } catch (Exception ignored) {
                    System.out.println("Error at get Substring");
                }
            }

            //Sets the grid according to the level
            setGrid(level);
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

    public static void setGame(String[] args) {
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
        System.out.println(level);
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

    //This method moves tiles, according to the x y values
    private static void moveTile(int x1, int y1, int x2, int y2) {
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

    }

    private static boolean isTileMovable(int x1, int y1, int x2, int y2) {
        Image startImage = getImageType(x1, y1);
        Image endImage = getImageType(x2, y2);
        assert startImage != null;
        boolean b = !(startImage.equals(pipeStaticH) || startImage.equals(pipeStaticV) || startImage.equals(starterH) || startImage.equals(starterV) || startImage.equals(endH) || startImage.equals(endV) || startImage.equals(emptyFree)) || startImage.equals(pipeStatic01) || startImage.equals(pipeStatic11) || startImage.equals(pipeStatic10) || startImage.equals(pipeStatic00) && Objects.equals(endImage, emptyFree);
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

    public static Image getImageType(final int column, final int row) {
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                ImageView imageView = (ImageView) node;
                return imageView.getImage();
            }
        }
        return null;
    }

    private static boolean isWon() {

        return true;
    }

    public static void gameEnded() {
        int f;
        try {
            while (true) {
                f = ans.get(ans.size() - 1);
                if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'u') {
                    if (mp[f % 4][(f / 4) - 1].connections[0] == 'd') {
                        ans.add(f - 4);
                        unused.add(1);
                    } else if (mp[f % 4][(f / 4) - 1].connections[1] == 'd') {
                        ans.add(f - 4);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'd') {
                    if (mp[f % 4][(f / 4) + 1].connections[0] == 'u') {
                        ans.add(f + 4);
                        unused.add(1);
                    } else if (mp[f % 4][(f / 4) + 1].connections[1] == 'u') {
                        ans.add(f + 4);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'l') {
                    if (mp[(f % 4) - 1][f / 4].connections[0] == 'r') {
                        ans.add(f - 1);
                        unused.add(1);
                    } else if (mp[(f % 4) - 1][f / 4].connections[1] == 'r') {
                        ans.add(f - 1);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'r') {
                    if (mp[(f % 4) + 1][f / 4].connections[0] == 'l') {
                        ans.add(f + 1);
                        unused.add(1);
                    } else if (mp[(f % 4) + 1][f / 4].connections[1] == 'l') {
                        ans.add(f + 1);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {

        }

    }

    public static void game() {
        gameEnded();
        while (!(ans.contains(end))) {
            Scanner scin = new Scanner(System.in);
            int fi = scin.nextInt();
            int la = scin.nextInt();
            fi--;
            la--;
            if ((mp[fi % 4][fi / 4].isMoveable) && (mp[la % 4][la / 4].isFree) && (((Math.abs((fi % 4) - (la % 4)) == 1) && ((fi / 4) == (la / 4)) || ((Math.abs((fi / 4) - (la / 4)) == 1) && ((fi % 4) == (la % 4)))))) {
                Tile temp = mp[fi % 4][fi / 4];
                mp[fi % 4][fi / 4] = mp[la % 4][la / 4];
                mp[la % 4][la / 4] = temp;
                System.out.println("U R fucking awesome");
                if (ans.contains(fi)) {
                    while (ans.contains(fi)) {
                        unused.remove(ans.size() - 1);
                        ans.remove(ans.size() - 1);
                    }
                }
            } else {
                System.out.println("Invalid move");
            }
            gameEnded();
        }
        System.out.println("Next Level");
        ans.clear();
        unused.clear();
    }

    public static void gameEnded() {
        int f;
        try {
            while (true) {
                f = ans.get(ans.size() - 1);
                if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'u') {
                    if (mp[f % 4][(f / 4) - 1].connections[0] == 'd') {
                        ans.add(f - 4);
                        unused.add(1);
                    } else if (mp[f % 4][(f / 4) - 1].connections[1] == 'd') {
                        ans.add(f - 4);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'd') {
                    if (mp[f % 4][(f / 4) + 1].connections[0] == 'u') {
                        ans.add(f + 4);
                        unused.add(1);
                    } else if (mp[f % 4][(f / 4) + 1].connections[1] == 'u') {
                        ans.add(f + 4);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'l') {
                    if (mp[(f % 4) - 1][f / 4].connections[0] == 'r') {
                        ans.add(f - 1);
                        unused.add(1);
                    } else if (mp[(f % 4) - 1][f / 4].connections[1] == 'r') {
                        ans.add(f - 1);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else if (mp[f % 4][f / 4].connections[unused.get(unused.size() - 1)] == 'r') {
                    if (mp[(f % 4) + 1][f / 4].connections[0] == 'l') {
                        ans.add(f + 1);
                        unused.add(1);
                    } else if (mp[(f % 4) + 1][f / 4].connections[1] == 'l') {
                        ans.add(f + 1);
                        unused.add(0);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {

        }


    }

    public static void setGame() throws FileNotFoundException {
        // this a value will be needed for tile id
        int a;
        for (int i = 0; i < 100; i++) {
            tx = "src/CSE1242_spring2022_project_level" + (i + 1) + ".txt";
            file = new File(tx);
            sc = new Scanner(file);
            //setting the map
            while (sc.hasNextLine()) {
                a = 0;
                String[] tp = (sc.nextLine()).split(",");
                //System.out.println(tp[1]);
                if (tp.length < 3) {
                    continue;
                }
                a = Integer.parseInt(tp[0]);
                a--;
                if (tp[1].equals("Pipe")) {
                    mp[a % 4][a / 4] = new CornerPipe(tp[2], true);
                } else if (tp[1].equals("PipeStatic")) {
                    mp[a % 4][a / 4] = new CornerPipe(tp[2], false);
                } else if (tp[1].equals("Empty")) {
                    mp[a % 4][a / 4] = new Empty(tp[2].equals("Free"));
                } else if (tp[1].equals("Starter")) {
                    mp[a % 4][a / 4] = new StartPipe(tp[2]);
                    ans.add(a);
                    //starter tile's unused connection is 0
                    unused.add(0);
                } else if (tp[1].equals("End")) {
                    mp[a % 4][a / 4] = new EndPipe(tp[2]);
                    end = a;
                } else {
                    System.out.println("Invalid input for" + tp[0]);
                }

            }
            //System.out.println((mp[1][3]).isFree);
            game();
        }

    }
}
