package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmployeeView {

    public EmployeeView(Stage employeeStage){
        employeeStage.setTitle("Welcome, employee!");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        employeeStage.setScene(scene);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        employeeStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
    }

    private void initializeSceneTitle(GridPane gridPane) {
        Text title = new Text("Welcome, dear employee!");
        title.setFont(Font.font("Tahome", FontWeight.EXTRA_BOLD, 20));
        gridPane.add(title, 0, 0, 1, 1);
    }

    private void initializeFields(GridPane gridPane){
        
    }
}
