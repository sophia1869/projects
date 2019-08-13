package SynthesizerPart2;

import java.util.ArrayList;

import SynthesizerPart1.AudioClip;
import SynthesizerPart1.Source;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public abstract class TargetWidge extends widge {
	
	Circle cathode = new Circle (10);
	
	TargetWidge(String title, MyApp parrent) {
		super(title, parrent);
		super.setMaxWidth(200);
		super.setLeft(cathode);
	}
	
	//abstract Source soundfilter (ArrayList<Source> srcs);

}
