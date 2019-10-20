package util;

import javafx.scene.control.ButtonType;

@FunctionalInterface
public interface AlertActionHandler {
	void action(ButtonType buttonType);
}