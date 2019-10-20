package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertManager {
	public enum AlertInfo {
		ERROR_LOAD_MEMBER(AlertType.ERROR, "에러", "회원정보 불러오기에 실패했습니다."),
		ERROR_LOAD_SCENE(AlertType.ERROR, "에러", "화면 로딩에 실패했습니다."),
		ERROR_UNKNOWN(AlertType.ERROR, "에러", "알수없는 에러가 발생했습니다."),
		ERROR_ACCESS_DB(AlertType.ERROR, "에러", "데이터베이스에 접속 실패했습니다."),
		ERROR_TASK_DB(AlertType.ERROR, "에러", "데이터베이스 작업을 실패했습니다."), 
		ERROR_TASK(AlertType.ERROR, "에러", "요청작업을 실패했습니다."),

		FAIL_LOGIN(AlertType.WARNING, "로그인 실패", "아이디 또는 비밀번호를 확인하세요."),
		FAIL_SIGNUP_INCOMPLETE_FORM(AlertType.WARNING, "가입 실패", "모든 항목을 작성해 주세요."),
		FAIL_SIGNUP_DUPLICATE_ID(AlertType.WARNING, "가입 실패", "이미 사용중인 아이디 입니다."),

		SUCCESS_TASK(AlertType.INFORMATION, "작업 완료", "요청작업을 완료했습니다."),
		SUCCESS_SIGNUP(AlertType.INFORMATION, "가입 완료", "회원 가입을 완료했습니다.");

		private AlertType type;
		private String header;
		private String message;

		AlertInfo(AlertType type, String header, String message) {
			this.type = type;
			this.header = header;
			this.message = message;
		}
	}

	static private AlertManager shared = new AlertManager();

	private AlertManager() {
	}

	static public AlertManager getInstance() {
		return shared;
	}

	public void show(AlertInfo info, AlertActionHandler actionHandler) {
		show(info.type, info.header, info.message, actionHandler);
	}

	public void show(AlertType type, String header, String message, AlertActionHandler actionHandler) {
		Alert alert = new Alert(type);

		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.setResizable(false);
		alert.showAndWait();

		if (actionHandler != null) {
			actionHandler.action(alert.getResult());
		}

	}
}
