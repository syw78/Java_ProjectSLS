package model;

public class DBVO {
	private String txtId;
	private String txtFiled;
	private String txtFiled2;
	private String txtName;
	private int frontN;
	private int backN;
	private String comboFM;
	private String phoneNum;
	private String enterpriseName;
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DBVO(String txtId, String txtFiled, String txtFiled2, String txtName, int frontN, int backN, String comboFM,
			String phoneNum, String enterpriseName, String fileName) {
		super();
		this.txtId = txtId;
		this.txtFiled = txtFiled;
		this.txtFiled2 = txtFiled2;
		this.txtName = txtName;
		this.frontN = frontN;
		this.backN = backN;
		this.comboFM = comboFM;
		this.phoneNum = phoneNum;
		this.enterpriseName = enterpriseName;
		this.fileName = fileName;
	}

	public DBVO(String txtId, String txtFiled, String txtFiled2, String txtName, int frontN,int backN, String comboFM, 
			String phoneNum,String enterpriseName) {
		// int midN, int lastN,
		super();
		this.txtId = txtId;
		this.txtFiled = txtFiled;
		this.txtFiled2 = txtFiled2;
		this.txtName = txtName;
		this.frontN = frontN;
		this.backN = backN;
		this.comboFM = comboFM;
		this.phoneNum = phoneNum;
		// this.midN = midN;
		// this.lastN = lastN;
		this.enterpriseName = enterpriseName;
	}

	public DBVO(String txtId, String txtName, String phoneNum, String enterpriseName) {
		super();
		this.txtId = txtId;
		this.txtName = txtName;
		this.phoneNum = phoneNum;
		this.enterpriseName = enterpriseName;
	}
	public DBVO(String txtId, String txtName, String phoneNum, String enterpriseName, String fileName) {
		super();
		this.txtId = txtId;
		this.txtName = txtName;
		this.phoneNum = phoneNum;
		this.enterpriseName = enterpriseName;
		this.fileName = fileName;
	}

	public String getTxtId() {
		return txtId;
	}

	public void setTxtId(String txtId) {
		this.txtId = txtId;
	}

	public String getTxtFiled() {
		return txtFiled;
	}

	public void setTxtFiled(String txtFiled) {
		this.txtFiled = txtFiled;
	}

	public String getTxtFiled2() {
		return txtFiled2;
	}

	public void setTxtFiled2(String txtFiled2) {
		this.txtFiled2 = txtFiled2;
	}

	public String getTxtName() {
		return txtName;
	}

	public void setTxtName(String txtName) {
		this.txtName = txtName;
	}

	public int getFrontN() {
		return frontN;
	}

	public void setFrontN(int frontN) {
		this.frontN = frontN;
	}

	public int getBackN() {
		return backN;
	}

	public void setBackN(int backN) {
		this.backN = backN;
	}

	public String getComboFM() {
		return comboFM;
	}

	public void setComboFM(String comboFM) {
		this.comboFM = comboFM;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}