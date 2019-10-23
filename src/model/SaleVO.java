package model;

public class SaleVO {
	
	private int no;
	private String client;
	private String date;
	private String goods;
	private int price;
	private int count;
	private int total;
	private String coments;

	public SaleVO() {

	}
	
	public SaleVO(int no, String client, String date, String goods, int price, int count, int total, String coments) {
		super();
		this.no = no;
		this.client = client;
		this.date = date;
		this.goods = goods;
		this.price = price;
		this.count = count;
		this.total = total;
		this.coments = coments;
	}



	public SaleVO(int no, String client, String date, String goods, int price, int count, String coments) {
		super();
		this.no = no;
		this.client = client;
		this.date = date;
		this.goods = goods;
		this.price = price;
		this.count = count;
		this.coments = coments;
	}

	public SaleVO(String goods, int count, int total) {
		super();
		this.goods = goods;
		this.count = count;
		this.total = total;
	}

	public SaleVO(String date, String goods, int price, int count, int total, String coments) {
		super();
		this.date = date;
		this.goods = goods;
		this.price = price;
		this.count = count;
		this.total = total;
		this.coments = coments;
	}

	public SaleVO(int no, String date, String goods, int price, int count, int total, String coments) {
		super();
		this.no = no;
		this.date = date;
		this.goods = goods;
		this.price = price;
		this.count = count;
		this.total = total;
		this.coments = coments;
	}
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
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

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getComents() {
		return coments;
	}

	public void setComents(String coments) {
		this.coments = coments;
	}

	@Override
	public String toString() {
		return "SaleVO [date=" + date + ", goods=" + goods + ", price=" + price + ", count=" + count + ", total="
				+ total + ", coments=" + coments + "]";
	}

}
