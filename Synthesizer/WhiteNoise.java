package SynthesizerPart1;

import java.util.Random;

public class WhiteNoise implements Source{

	double f;
	int max;
	
	public WhiteNoise (){
		max = Short.MAX_VALUE;
	}
	@Override
	public AudioClip giveAudio() {
		AudioClip whiteClip = new AudioClip();		
		for(int i = 0; i < whiteClip.getsampleRate(); i++) {
			Random rand = new Random();
			whiteClip.setSample(i, rand.nextInt((max - -1* max) + 1) + -1*max);
		}		
		return whiteClip;	
	}

	@Override
	public double getfre() {
		return f;
	}

	@Override
	public void setfre(double new_fre) {
		f = new_fre;
	}

}
