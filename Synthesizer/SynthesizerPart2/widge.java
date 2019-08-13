package SynthesizerPart2;

import SynthesizerPart1.Source;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public abstract class widge extends BorderPane {
	
	GridPane pane = new GridPane();
	Circle shape = new Circle(10);
	double dragStartX, dragStartY;
	double translateStartX, translateStartY;
	
	widge(String title, MyApp parrent) {
		super();
		super.setRight(shape);
	    super.setCenter(pane);
		super.setStyle("-fx-border-color: black");
		Label labeltitle = new Label(title);
		pane.add(labeltitle,0,0);
		shape.setFill(javafx.scene.paint.Color.RED);
		super.setMaxSize(100, 50);
		
		this.pane.setOnMousePressed(e -> {
			
		Point2D  eventplace = new Point2D(e.getSceneX(),e.getSceneY());
		//if (!shape.contains(shape.sceneToLocal(eventplace))) {
			dragStartX = e.getSceneX();
			dragStartY = e.getSceneY();
			translateStartX = this.getTranslateX();
			translateStartY = this.getTranslateY();
		//}
			
		});

		this.pane.setOnMouseDragged(e -> {
			//Point2D  eventplace = new Point2D(e.getSceneX(),e.getSceneY());
			//if (!shape.contains(shape.sceneToLocal(eventplace))) {
			this.setTranslateX(translateStartX + e.getSceneX() - dragStartX);
			this.setTranslateY(translateStartY + e.getSceneY() - dragStartY);
			parrent.updateCableMain();
			//}
		});

	}
	
	abstract Source soundgenerator();
	
}
