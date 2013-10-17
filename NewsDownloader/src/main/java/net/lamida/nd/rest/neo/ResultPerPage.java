package net.lamida.nd.rest.neo;
enum ResultPerPage {
	TEN(10), FIFTEEN(15), THIRTHY(30), FIFTY(50), ONE_HUNDRED(100);
	private int total;
	public int getTotal(){
		return total;
	}
	private ResultPerPage(int num) {
		this.total = num;
	}
}