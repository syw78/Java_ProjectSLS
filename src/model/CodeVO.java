package model;

public class CodeVO {
	
	private String codeId;

	public CodeVO(String codeId) {
		super();
		this.codeId = codeId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	@Override
	public String toString() {
		return "CodeVO [codeId=" + codeId + "]";
	}
	
}
