package SynthesizerPart2;

import java.util.ArrayList;

import SynthesizerPart1.Multiply;
import SynthesizerPart1.Source;
import javafx.scene.control.Label;

public class MultiplyWidge extends TargetWidge{
	
	Multiply gen_2 = new Multiply(0);
	
	Label label = new Label ("");
	
	MultiplyWidge(String title, MyApp parrent) {
		super(title, parrent);
		
		label.setText("Multiplier");
		super.pane.add(label, 0, 2); 
	}

	Source multifilter(ArrayList<Source> srcs) {
//		ArrayList <Source> srcs = new ArrayList <>();
//		srcs.add(src);
		gen_2.addInput(srcs);
		return gen_2;
	}

	@Override
	Source soundgenerator() {
		return gen_2;
	}

}
