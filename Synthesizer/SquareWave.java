package SynthesizerPart1;

public class SquareWave implements Source{

	double f;
	
	public SquareWave(double fre){
		f = fre;
	}
	
	@Override
	public AudioClip giveAudio() {
		AudioClip squareClip = new AudioClip();		
		for(int i = 0; i < squareClip.getsampleRate(); i++) {
			int val = 0;
			if((f * i/squareClip.getsampleRate()) % 1 >0.5) {
				val = Short.MAX_VALUE;
			}else {
				val = -1 * Short.MAX_VALUE;
			}
			
			squareClip.setSample(i, val);
		}		
		return squareClip;	
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
