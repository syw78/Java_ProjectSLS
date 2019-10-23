package model;

public class GoodsVO {

	private int no;
	private String client;
	private String goodsName;
	private int price;
	
	public GoodsVO(String goodsName, int price) {
		super();
		this.goodsName = goodsName;
		this.price = price;
	}

	public GoodsVO(String client, String goodsName, int price) {
		super();
		this.client = client;
		this.goodsName = goodsName;
		this.price = price;
	}

	public GoodsVO(int no, String client, String goodsName, int price) {
		super();
		this.no = no;
		this.client = client;
		this.goodsName = goodsName;
		this.price = price;
	}
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "GoodsVO [goodsName=" + goodsName + ", price=" + price + "]";
	}

}
