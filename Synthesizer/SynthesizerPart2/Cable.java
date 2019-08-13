package SynthesizerPart2;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Cable extends Line{
	
	widge w;
	TargetWidge tw;
	Circle shape;
	Circle cathode;

	public Cable(widge w, TargetWidge tw) {
		
		super();
		this.w = w;
		this.tw = tw;		
		updateCable();
 
	}
	
	public void updateCable() {
		shape = w.shape;
		this.setStartX(shape.localToScene(shape.getCenterX(),shape.getCenterY()).getX());
		this.setStartY(shape.localToScene(shape.getCenterX(),shape.getCenterY()).getY());
		
		cathode = tw.cathode;
		this.setEndX(cathode.localToScene(cathode.getCenterX(),cathode.getCenterY()).getX());
		this.setEndY(cathode.localToScene(cathode.getCenterX(),cathode.getCenterY()).getY());
	}

}
