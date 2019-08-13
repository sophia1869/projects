package SynthesizerPart1;

import java.util.ArrayList;

public interface mixer extends Source {
	 void addInput(ArrayList<Source> input);
}

