package SynthesizerPart1;

public class FilterVolume implements filter{

	public double scale;
	double f;
	SineWave outputsine = new SineWave(f);
	AudioClip output = new SineWave(f).giveAudio();
	
	@Override
	public AudioClip giveAudio() {//this giveAudio is for volume
		return output;
	}

	@Override
	public void connectInput(Source input) { 
		output = new AudioClip(input.giveAudio());// this giveAudio is the one in Source interface, it is for sinewave
		for (int i=0; i < output.getsampleRate(); i++) { 
			output.setSample(i, (int)(scale*output.getSample(i)));
		}
		
	}
	
	public FilterVolume(double s){
		scale = s;
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
