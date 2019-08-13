package SynthesizerPart2;

import java.util.ArrayList;

import SynthesizerPart1.FilterVolume;
import SynthesizerPart1.Source;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

public class Volwidge extends TargetWidge{
	
	double scalev = 1;
	FilterVolume gen2 = new FilterVolume(scalev);
	Slider slider = new Slider (0.1, 10, 3);
	Label label = new Label ("");
	//Circle cathode = new Circle (10);
	
	Volwidge(String title, MyApp parrent) {
		super(title, parrent);
		super.setMaxWidth(200);
		slider.setShowTickLabels(true); 
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(10); //set min sliding unit
		label.setText(Double.toString(slider.getValue())+"volumeScale");
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	    gen2.scale = (double) new_val;
                    label.setText(String.format("%.2f", new_val)+"volumeScale");
            }
        });		
		super.pane.add(slider, 0, 1); 
		super.pane.add(label, 0, 2); 
		//super.setLeft(cathode); 
	}


	void volfilter(ArrayList<Source> srcs) {
		for (Source src:srcs) {
			(gen2).connectInput(src);
		}
	}

	@Override
	Source soundgenerator() {
		return gen2;
	}

}
