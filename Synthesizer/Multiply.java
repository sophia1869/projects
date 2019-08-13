package SynthesizerPart1;

import java.util.ArrayList;

public class Multiply implements mixer {
	
	double f;
	//SineWave outputsine = new SineWave(f);
	//AudioClip output = new SineWave(f).giveAudio();
	AudioClip output;
	ArrayList<AudioClip> temp = new ArrayList <> ();
	
	@Override
	public AudioClip giveAudio() {
		return output;
	}

	@Override
	public void addInput(ArrayList<Source> input) {
		output = new AudioClip(input.get(0).giveAudio());
		for (int j=0; j< input.size(); j++) {
	    //cannot create a temp of size input and set(i,value). have to pushback. 
	    //maybe it is initialized as empty container
		temp.add(input.get(j).giveAudio()); 
		for (int i=0; i < temp.get(j).getsampleRate(); i++) { 
			output.setSample(i, (int)(output.getSample(i) * temp.get(j).getSample(i)));
		}	
		}
	}
	
	public Multiply (double ff){
		f = ff;
		//output = new SineWave(f).giveAudio();
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

