/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab;

import javafx.fxml.FXMLLoader;
import java.io.IOException;

import com.lab.utility.StringColor;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * App gestisce il caricamento della prima schermata all'utente dando vita al motore grafico.
 * <p>
 * Permette di mostrare la prima schermata IpConfigController che permette all'utente di inserire l'indirizzo del server a cui connettersi.
 * </p> 
 */
public class App extends Application {

	public static void main(String[] args) {
    launch(args);
	}

	/**
 * Il metodo start e' il metodo che viene lanciato automaticamente dal motore grafico.
 * <p>
 * Permette di definire quale schermata mostrare all'utente e di impostare alcuni configurazione della schermata.
 * </p> 
 */
	@Override
	public void start(Stage stage)
	{
		Parent root = null;
    try {
      root = FXMLLoader.load(App.class.getResource("/com/lab/fxml/ipConfig/ipConfig.fxml"));
    } catch(IOException e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "]" + " file ipConfig.fxml non trovato");
      System.exit(1);
    }

		Scene scene = new Scene(root, 640, 350);
		scene.setFill(Color.TRANSPARENT);

		Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		stage.getIcons().add(icon);
		
		stage.setTitle("TheKnife - Connect");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}
}