package SynthesizerPart1;

import java.util.ArrayList;

public class AudioClip {

	double duration;
	double sampleRate;
	byte[] audioArray;
	
	AudioClip(){
		duration = 1.0;
		sampleRate = 44100;
		audioArray = new byte[88200];
	}
	public AudioClip(AudioClip origin){
		this.duration = origin.duration;
		this.sampleRate = origin.sampleRate;
		this.audioArray = new byte[88200];
		for (int i = 0; i<audioArray.length; i++) {
			audioArray[i] = origin.audioArray[i];
		}
	}
	
	int getSample(int index) {
		int a = 0 | audioArray[2*index+1]<<8 & 0x0000FF00 | audioArray[2*index] & 0x000000FF;
		return a;
	}
	
	int setSample(int index, int newvalue) {
		audioArray[2*index+1] = (byte) (newvalue<<16>>24);
		audioArray[2*index] = (byte) (newvalue<<24>>24);
		return getSample(index);
	}
	
	byte[] getData(){
		return audioArray;
	}
	
	double getsampleRate() {
		return sampleRate;
	}


}
