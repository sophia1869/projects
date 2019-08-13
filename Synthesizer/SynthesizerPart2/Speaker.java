package SynthesizerPart2;

import java.util.ArrayList;

import SynthesizerPart1.Source;

public class Speaker extends TargetWidge{

	Source got;
	
	Speaker(String title, MyApp parrent) {
		super(title, parrent);
			cathode.setFill(javafx.scene.paint.Color.GREEN );
			cathode.setRadius(50);
			super.setStyle("-fx-border-color: none");
			shape.setRadius(0);
	}

//	Source speakerfilter(Source src) {
//		return src;
//	}
	
	@Override
	Source soundgenerator() {
		return got;
	}
}
