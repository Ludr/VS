package rmi.leapmotion;

import com.leapmotion.leap.Controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rmi.consumer.RegisterService;
import rmi.consumer.TCPConnection;

public class LeapGui extends Application {

	static private LeapMotionController leapMotion;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// TCPConnection.ipAdress = args[0];
		TCPConnection.ipAdress = "localhost";
		TCPConnection comm = TCPConnection.getInstance();

		// Create a sample listener and controller
		leapMotion = new LeapMotionController();
		Controller controller = new Controller();
		
		RegisterService registry = new RegisterService();
		registry.registerAtBroker("gui");

		// Have the sample listener receive events from the controller
		controller.addListener(leapMotion);

		// start gui
		launch(args);

		// Remove the sample listener when done
		controller.removeListener(leapMotion);
	}

	@Override
	public void start(Stage stage) throws Exception {
		GridPane gp = new GridPane();
		Label xText = new Label("x : ");
		Label xValue = new Label();
		xValue.textProperty().bind(leapMotion.horizontalString);
		gp.add(xText, 0, 0);
		gp.add(xValue, 1, 0);
		
		Label yText = new Label("y : ");
		Label yValue = new Label();
		yValue.textProperty().bind(leapMotion.verticalString);
		gp.add(yText, 0, 1);
		gp.add(yValue, 1, 1);

		Label pinch  = new Label("Pinch : ");
		Label pinchValue = new Label();
		pinchValue.textProperty().bind(leapMotion.pinchStrengthString);
		gp.add(pinch, 0, 2);
		gp.add(pinchValue, 1, 2);
		
		Label grab = new Label("Grab : ");
		Label grabValue = new Label();
		grabValue.textProperty().bind(leapMotion.grabStrengthString);
		gp.add(grab, 0, 3);
		gp.add(grabValue, 1, 3);
		

		gp.setPadding(new Insets(10));
		gp.setPrefWidth(100);

		Scene scene = new Scene(gp);
		stage.setScene(scene);
		stage.show();
	}

}
