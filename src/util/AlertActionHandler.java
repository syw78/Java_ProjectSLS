package util;

import javafx.scene.control.ButtonType;

/*********************
 * 
 * 기능 : 버튼의 타입을 얻어오는 인터페이스
 *
 * 심재현
 */

@FunctionalInterface
public interface AlertActionHandler {
	
	void action(ButtonType buttonType);
	
}