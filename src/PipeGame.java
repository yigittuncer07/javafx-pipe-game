import javafx.application.Application;
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

import java.util.ArrayList;

public class PipeGame extends Application {
//    private ImageView empty1 = new ImageView(new Image("src/images/Empty.png"));

    @Override
    public void start(Stage stage) throws Exception {

        Image image = new Image("images/Empty.png",120,120,false,false);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("Level 1");
        }

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setValue("Select Level");
        for (String s : list) {
            comboBox.getItems().add(s);
        }
        Label displayLabel = new Label("Welcome to PipeGame!");
        Label movesLabel = new Label("Number of moves -->");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(comboBox, displayLabel, movesLabel);

        hBox.setSpacing(50);


        GridPane gridPane = new GridPane();


        VBox pane = new VBox();
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(480);
        line.setStartY(30);
        line.setEndY(30);


        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                ImageView empty1 = new ImageView(image);
                gridPane.add(empty1, i, k);
            }
        }


        pane.getChildren().add(hBox);
        pane.getChildren().add(line);
        pane.getChildren().add(gridPane);


        Scene scene = new Scene(pane, 480, 510);

        stage.setTitle("Pipe Game v1.0");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
    /*-----------------------------------Methods-----------------------------------*/

}
