package com.lab.controller;

import java.util.ArrayList;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class SignedinHomeController {
  
  @FXML private VBox listOfRestaurants_VB;

  @FXML
  public void initialize()
  {
		ToolbarController.showBackButton(false);
		PageController.showTitle(false);

		String[] ristorante1 = {"Ristorante 1", "via Trieste 12, Milano", "5", "10"};
		ArrayList<String[]> ristoranti = new ArrayList<>();
		for(int i=0; i<15; i++) ristoranti.add(ristorante1);

		for(String[] r : ristoranti){
			HBox line_HB = new HBox();
			line_HB.setSpacing(15);
			line_HB.setAlignment(Pos.CENTER_LEFT);

			line_HB.setStyle("-fx-border-color: black; -fx-border-width: 0 0 1 0; -fx-padding: 20;");

			VBox text_VB = new VBox();
			text_VB.setSpacing(5);

			Label name_L = new Label(r[0] + "\t-\t" + r[1]);
			name_L.getStyleClass().add("theknifeLabel");
			Label stats_L = new Label("⭐ " + r[2] + " Stelle \t 💬 " + r[3] + " Recensioni");

			text_VB.getChildren().addAll(name_L, stats_L);

			Pane spacer = new Pane();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			Button details_B = new Button("Dettagli");
			details_B.getStyleClass().add("detailButton");
			details_B.setPrefSize(150, 40);
			details_B.setOnAction(e -> System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Detail button pressed"));

			final boolean[] isBookmarked = {false};
			Button bookmark_B = new Button();
			bookmark_B.getStyleClass().add("bookmarkButton");
			bookmark_B.setPrefSize(40, 40);
      bookmark_B.setMinSize(40, 40);
			bookmark_B.setOnAction(e -> {
				isBookmarked[0] = !isBookmarked[0]; 
          
        if (isBookmarked[0]) {
          bookmark_B.setStyle("-fx-background-image: url('/com/lab/img/bookmarkedIcon.png');");
          System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Bookmarked [" + r[0] + "]");
        } else {
          bookmark_B.setStyle("-fx-background-image: url('/com/lab/img/bookmarkIcon.png');");
          System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Unbookmarked [" + r[0] + "]");
        }
			});

			line_HB.getChildren().addAll(text_VB, spacer, details_B, bookmark_B);

			listOfRestaurants_VB.getChildren().addAll(line_HB);
		}
  }

	@FXML
	public void searchClicked(ActionEvent event)
	{
		System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Search");
	}

	@FXML
	public void bookmarkClicked(ActionEvent event)
	{
		System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Bookmark");
	}

	@FXML
	public void reviewClicked(ActionEvent event)
	{
		System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Review");
	}
}
