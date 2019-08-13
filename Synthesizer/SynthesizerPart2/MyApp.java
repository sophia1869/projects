package SynthesizerPart2;

import SynthesizerPart1.FilterVolume;
import SynthesizerPart1.SineWave;
import SynthesizerPart1.Source;
import SynthesizerPart1.SquareWave;

import java.util.ArrayList;

import javax.sound.sampled.*;
import javafx.application.Application;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.shape.*;
import javafx.scene.shape.Line;

public class MyApp extends Application {

	Source src;
	ArrayList<Source> srcs;
	ArrayList<widge> list; //for drawing starting points for wires
	ArrayList<widge> listSource; //for adding sine square white_noise
	ArrayList<TargetWidge> listTarget;  //for drawing ending points for wire
	ArrayList<Volwidge> listVol;
	ArrayList<MixerWidge> listMix;
	ArrayList<MultiplyWidge> listMul;
	
	Cable cable;
	ArrayList<Cable> cables;
    widge swtemp;
    Line wire;
    
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		list = new ArrayList<>();
		listTarget = new ArrayList<>();
		listSource = new ArrayList <>();
		listVol = new ArrayList <>();
		listMix = new ArrayList <>();
		listMul = new ArrayList <>();
		srcs = new ArrayList <>();
		
		cables = new ArrayList<>();
		VBox vbox = new VBox();

		primaryStage.setTitle("SoundPanel");

		Pane maincanvas = new Pane();
		BorderPane canvas = new BorderPane();
		maincanvas.setPrefSize(900, 600);

		Button button = new Button("Play");
		Button sineButton = new Button("Sine");
		Button squareButton = new Button("Square");
		Button whitenoiseButton = new Button("White Noise");
		Button volumeButton = new Button("volumeFilter");
		Button mixerButton = new Button("mixer");
		Button multiButton = new Button("multiplier");
		
		Speaker speaker = new Speaker("",this);
		listTarget.add(speaker);
		maincanvas.getChildren().add(speaker);
		
		multiButton.setOnAction(e -> {
			
			MultiplyWidge mul = new MultiplyWidge("multiply", this);
			list.add(mul);
			listTarget.add(mul);
			listMul.add(mul);
			maincanvas.getChildren().add(mul);
			
		});
		
		mixerButton.setOnAction(e -> {
			
			MixerWidge mix = new MixerWidge("add", this);
			list.add(mix);
			listTarget.add(mix);
			listMix.add(mix);
			maincanvas.getChildren().add(mix);
			
		});
		
		volumeButton.setOnAction(e -> {
					
			Volwidge vol = new Volwidge("volume", this);
			
//			(vol.gen2).connectInput(src);
//			src = vol.gen2;
			
			list.add(vol);
			listTarget.add(vol);
			listVol.add(vol);
			maincanvas.getChildren().add(vol);

		});
		
		sineButton.setOnAction(e -> {

			sinewidge sine = new sinewidge("SineWave(frequency)",this);
			//srcs.add(sine.wave);
			list.add(sine);
			listSource.add(sine);
			maincanvas.getChildren().add(sine);

		});

		squareButton.setOnAction(e -> {

			squarewidge square = new squarewidge("SquareWave(frequency)",this);
			//srcs.add(square.sqwave);
			list.add(square);
			listSource.add(square);
			maincanvas.getChildren().add(square);

		});
		
		whitenoiseButton.setOnAction(e -> {

			whitenoisewidge wn = new whitenoisewidge("White Noise",this);
			//srcs.add(wn.whiten);
			list.add(wn);
			listSource.add(wn);
			maincanvas.getChildren().add(wn);

		});
		
		maincanvas.setOnMousePressed(event -> {

			for (widge w : list) {
				
				Point2D mousescene = new Point2D(event.getSceneX(), event.getSceneY());
				Circle c = w.shape;
				Point2D ccenter = new Point2D(c.getCenterX(), c.getCenterY());

				if (c.contains(c.sceneToLocal(mousescene))) {
					swtemp = w;
					wire = new Line (c.localToScene(ccenter).getX(), c.localToScene(ccenter).getY(), event.getSceneX(),event.getSceneY());
					maincanvas.getChildren().add(wire);
				}
			}	

		});

		maincanvas.setOnMouseDragged(event2 -> {
			if (wire != null) {
				wire.setEndX(event2.getSceneX());
				wire.setEndY(event2.getSceneY());
			}
		});

		maincanvas.setOnMouseReleased(e -> {
			
			for (TargetWidge tw:listTarget) {
				
				Point2D mousescene = new Point2D(e.getSceneX(), e.getSceneY());
				Circle c = tw.cathode;
				Point2D ccenter = new Point2D(c.getCenterX(), c.getCenterY());
				
				maincanvas.getChildren().remove(wire);
				
				if (c.contains(c.sceneToLocal(mousescene))) {
					cable = new Cable(swtemp,tw);
				    cables.add(cable);
					maincanvas.getChildren().add(cable);
				}		
			}	
			    cable = null;
				swtemp = null;
				wire = null;	
		});

		button.setOnAction(actionEvent -> {
			try {
				for (widge sw:listSource) {
					if (isConnect(sw)) {
						srcs.add(sw.soundgenerator());
					}
				}
					
				for (Volwidge twv:listVol ) {
					if (isConnect(twv)) {
							twv.volfilter(srcs);
					}
				}
				
				for (MixerWidge twm:listMix ) {
					if (isConnect(twm)) {
							src = twm.mixfilter(srcs);
					}
				}
				for (MultiplyWidge twmul:listMul ) {
					if (isConnect(twmul)) {
							src = twmul.multifilter(srcs);
					}
				} 
				
				if (isConnect(speaker)) {
					if (srcs.size() == 1) {src = srcs.get(0);}
					SineWave.playsound(src.giveAudio());
				}
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		});

		vbox.getChildren().addAll(multiButton,mixerButton,volumeButton,sineButton, squareButton,whitenoiseButton,button);
		canvas.setCenter(maincanvas);
		canvas.setRight(vbox);
		Scene scene = new Scene(canvas, 1000, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	void updateCableMain(){
		for (Cable cable:cables) {
			cable.updateCable();
		}
	}
	
	boolean isConnect(widge wid) {
		boolean touch = false;
		for (Cable cable : cables) {
			if (cable.tw == wid || cable.w == wid) {
				touch = true;
			}
		}
		return touch;
	}
	
}
