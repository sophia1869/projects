package SynthesizerPart1;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import SynthesizerPart2.MyApp;

public class SineWave implements Source {
	
	AudioClip start = new AudioClip ();
	double fre;
	
	@Override
	public AudioClip giveAudio() {
		
		for (int i=0; i<start.sampleRate; i++) {
		start.setSample(i, (int) (Short.MAX_VALUE*Math.sin((2*Math.PI*fre*i)/start.sampleRate)));
	}
		return start;
		//deep copy clone() not recommended
	}
	
	public SineWave(double f){
		fre=f;
	}
	
	public static void playsound(AudioClip a) throws LineUnavailableException { 
		
		Clip c = AudioSystem.getClip();
		
		AudioFormat format16 = new AudioFormat(c.getFormat().getSampleRate(), 16, 1, true, false);
		
		c.open(format16, a.getData(), 0, a.getData().length); 
		
		System.out.println("about to play");

		c.start();
		
		c.loop(2); //plays it 2 more times if desired

		while(c.getFramePosition() < a.getsampleRate() || c.isActive() || c.isRunning()){} //makes sure the program doesn't quit before the sound plays

		System.out.println("done");

		c.close();
	}
	
	
	public static void main(String[] args) throws LineUnavailableException {
	
		Source gen = new SineWave(440);
	    AudioClip a = gen.giveAudio();
		
	    Source gensq = new SquareWave (222);
	    AudioClip b = gensq.giveAudio();
	    
	    Source genwh = new WhiteNoise();
	    AudioClip c = genwh.giveAudio();
	    
		FilterVolume gen2 = new FilterVolume(0.2);
		gen2.connectInput(gen);
		AudioClip d = gen2.giveAudio();
		
		
		ArrayList<Source> test = new ArrayList <> ();
		Source gen3 = new SineWave(500);
		test.add(gen);
		test.add(gen3);
		//Interference gen_ = new Interference (0);
		Multiply gen_ = new Multiply (0);
		gen_.addInput(test);
		AudioClip e = gen_.giveAudio();
	
		
		playsound(e);
	
	}

	@Override
	public double getfre() {
		return fre;
	}

	@Override
	public void setfre(double new_fre) {
		fre = new_fre;
	}
	
}
