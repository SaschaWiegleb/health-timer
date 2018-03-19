package datameer.health.frontend;

import datameer.health.backend.CountDown;
import datameer.health.backend.events.DelayedEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyAlert extends Alert {
	public static final String CLOSE = "Start";
	// public static final String SNOOZE = "Snooze";

	public static ButtonType buttonTypeClose = new ButtonType(CLOSE, ButtonData.CANCEL_CLOSE);
	// public static ButtonType buttonTypeSnooze = new ButtonType(SNOOZE,
	// ButtonData.NEXT_FORWARD);

	public MyAlert(DelayedEvent event) {
		super(AlertType.INFORMATION);

		final CountDown countdown = new CountDown((int) event.duration() / 1000);
		// final CountDownLabel countdownLabel = new CountDownLabel(countdown);

		setTitle("Notification for you!");
		setHeaderText(event.message());

		Image image = new Image(event.image());
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(512);
		imageView.setPreserveRatio(true);
		setGraphic(imageView);

		getButtonTypes().setAll(buttonTypeClose);// , buttonTypeSnooze);

		// FlowPane fp = new FlowPane();
		// fp.getChildren().addAll(countdownLabel);

		// getDialogPane().contentProperty().set(fp);

		final Button btOk = (Button) getDialogPane().lookupButton(buttonTypeClose);

		btOk.textProperty().bind(countdown.asString());

		btOk.addEventFilter(ActionEvent.ACTION, e -> {
			if (!countdown.isRunning())
				countdown.start();

			if (!countdown.timeLeftProperty().isEqualTo(0).get()) {
				System.out.println("consume");
				e.consume();
			}
		});

	}
}