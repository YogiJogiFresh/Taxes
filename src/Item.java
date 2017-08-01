/**
 * Item class
 * books, food, and medical products are exempt from Basic Sales Tax
 * @author Alan Kai
 *
 */
public class Item {

    ExemptedList list = new ExemptedList();
    
	public boolean isImport;
	public boolean isExempt;
	public String name;
	public double price;
	public double pricePerUnit;
	private int quantity;
	
	public Item(String name, boolean isImport, double price) {
		isExempt = check_Category(name);
		this.name = name;
		this.price = price;
		this.pricePerUnit = price;
		this.isImport = isImport;
		quantity = 1;
	}

	/**
	 * Hard coded categorize function
	 * Important to identify if book, food, or medical
	 * @param itemName 
	 * @return true if category is exempt
	 */
	private boolean check_Category(String itemName){
		//TODO: Algorithm for searching through string for specific words/phrases?
		return list.get_List().contains(itemName);
	}
	/**
	 * Item classes toString method
	 * [Override]
	 */
	public String toString(){
		String pre = "";
		String addOn = "";
		if(isImport) {
			pre = "Imported ";
		}
		else {
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		if(quantity > 1) {
			addOn = " (" + quantity +" @ " + String.format("%.2f", pricePerUnit) + " )";
		}
		price = pricePerUnit * quantity;
		price = Math.round(price * 100.0) / 100.0;
		
		return pre + name +": " + String.format("%.2f", price) + addOn;
	}
	public void addQuantity() {
		quantity++;
		price *= quantity;
	}
	public int getQuantity() {
		return quantity;
	}
}
