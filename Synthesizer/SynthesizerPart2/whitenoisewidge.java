package SynthesizerPart2;

import SynthesizerPart1.Source;
import SynthesizerPart1.WhiteNoise;

public class whitenoisewidge extends widge {

	WhiteNoise whiten = new WhiteNoise();
	
	whitenoisewidge(String title, MyApp parrent) {
		super(title, parrent);
	}

	@Override
	Source soundgenerator() {
		return whiten;
	}

}
