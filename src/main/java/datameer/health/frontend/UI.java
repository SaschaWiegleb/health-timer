package datameer.health.frontend;

import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.DelayedBus;
import com.google.common.eventbus.EventBus;

import datameer.health.backend.events.DelayedEvent;
import datameer.health.backend.listener.EventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UI extends Application {
	private static Stage primaryStage;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Platform.setImplicitExit(false);

		UI.primaryStage = primaryStage;
		primaryStage.setTitle("Hello World!");

		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.setAlwaysOnTop(true);

		EventBus eventBus = DelayedBus.getInstance();
		EventListener listener = new EventListener();
		eventBus.register(listener);

		TimeUnit unit = TimeUnit.SECONDS;// MINUTES;

		DelayedEvent workingposition = DelayedEvent.chain(true,
				new DelayedEvent.Builder().message("stand up").delay(40, unit).image(ImageHelper.getImage("standup"))
						.build(),
				new DelayedEvent.Builder().message("sit down").delay(20, unit).image(ImageHelper.getImage("sitdown"))
						.build());

		DelayedEvent breaks = DelayedEvent.chain(true,
				new DelayedEvent.Builder().message("take a break").delay(25, unit).duration(5, TimeUnit.MINUTES)
						.image(ImageHelper.getImage("exercise")).playSound().build(),
				new DelayedEvent.Builder().message("take a break").delay(25, unit).duration(5, TimeUnit.MINUTES)
						.image(ImageHelper.getImage("exercise")).playSound().build(),
				new DelayedEvent.Builder().message("take a break").delay(25, unit).duration(5, TimeUnit.MINUTES)
						.image(ImageHelper.getImage("exercise")).playSound().build(),
				new DelayedEvent.Builder().message("take a break").delay(25, unit).duration(15, TimeUnit.MINUTES)
						.image(ImageHelper.getImage("exercise")).playSound().build()
		);

		DelayedEvent eyes = DelayedEvent.chain(true,
				new DelayedEvent.Builder().message("close your eyes for 20 sec and take a deep breath").delay(20, unit)
						.duration(20, TimeUnit.SECONDS).playSound()
						.image(ImageHelper.getImage("breath")).build(),
				new DelayedEvent.Builder().message("look into distance for 20 sec").delay(20, unit)
						.duration(20, TimeUnit.SECONDS).playSound()
						.image(ImageHelper.getImage("eyes")).build());


		eventBus.post(new DelayedEvent.Builder().message("drink 200ml").delay(60, unit)
				.image(ImageHelper.getImage("drink")).repeat().build());
		eventBus.post(workingposition);
		eventBus.post(eyes);
		eventBus.post(breaks);
	}

	public static Stage getPrimary() {
		return primaryStage;
	}
}
