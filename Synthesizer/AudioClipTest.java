package SynthesizerPart1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AudioClipTest {

	@Test
	void test() {
		
		AudioClip test = new AudioClip ();
		
		assertEquals(1.0, test.duration);
		assertEquals(44100, test.sampleRate);
		assertEquals(88200, test.audioArray.length);
		assertEquals(0, test.getSample(1));
		assertEquals(12, test.setSample(1,12));
		
		
	}

}
