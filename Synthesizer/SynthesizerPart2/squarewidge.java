package SynthesizerPart2;

import SynthesizerPart1.SineWave;
import SynthesizerPart1.Source;
import SynthesizerPart1.SquareWave;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class squarewidge extends widge {

	public double fre=440;
	SquareWave sqwave = new SquareWave(fre);
	Slider slider = new Slider (200, 2000, 440);//slider range 200 to 2000, default value 440
	Label label = new Label ("");
	
	squarewidge(String title, MyApp parrent) {
		super(title, parrent);
		super.setMaxWidth(200);
		slider.setShowTickLabels(true); 
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(10); //set min sliding unit
		label.setText(Double.toString(slider.getValue())+"Hz");
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	    sqwave.setfre((double) new_val);
                    label.setText(String.format("%.2f", new_val)+"Hz");
            }
        });		
		super.pane.add(slider, 0, 1); 
		super.pane.add(label, 0, 2); 
	}

	@Override
	Source soundgenerator() {
		return sqwave;
	}
	

}
