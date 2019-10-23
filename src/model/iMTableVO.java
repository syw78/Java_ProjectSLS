package model;

public class iMTableVO {

	private int no;
	private String date;
	private String name;
	private int pay;
	private int ea;
	private String remakers;
	
	public iMTableVO(int no, String date, String name, int pay, int ea, String remakers) {
		super();
		this.no = no;
		this.date = date;
		this.name = name;
		this.pay = pay;
		this.ea = ea;
		this.remakers = remakers;
	}
	
	

	public iMTableVO(String name, int ea) {
		super();
		this.name = name;
		this.ea = ea;
	}



	public iMTableVO(String date, String name, int pay, int ea) {
		super();
		this.date = date;
		this.name = name;
		this.pay = pay;
		this.ea = ea;
	}



	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPay() {
		return pay;
	}

	public void setPay(int pay) {
		this.pay = pay;
	}

	public int getEa() {
		return ea;
	}

	public void setEa(int ea) {
		this.ea = ea;
	}

	public String getRemakers() {
		return remakers;
	}

	public void setRemakers(String remakers) {
		this.remakers = remakers;
	}
	
	
	
}
