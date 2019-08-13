package SynthesizerPart2;

import java.util.ArrayList;

import SynthesizerPart1.Interference;
import SynthesizerPart1.Source;
import javafx.scene.control.Label;

public class MixerWidge extends TargetWidge{
	
	Interference gen_ = new Interference(0);
	
	Label label = new Label ("");
	
	MixerWidge(String title, MyApp parrent) {
		super(title, parrent);
		label.setText("mixer");
		
		super.pane.add(label, 0, 2); 
	}

	Source mixfilter(ArrayList<Source> srcs) {
//		ArrayList <Source> srcs = new ArrayList <>();
//		srcs.add(src);
		gen_.addInput(srcs);
		return gen_;
	}

	@Override
	Source soundgenerator() {
		return gen_;
	}

}
