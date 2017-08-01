import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.*;
import java.math.*;
/**
 * Sales Tax
 * Basic sales tax = 10% on all goods, except books, food, and medical products
 * Import sales tax = additional 5%, no exemption
 * -----------------------
 * Example Input:
 * 1 book at 12.49
 * 1 book at 12.49
 * 1 music CD at 14.99
 * 1 chocolate bar at 0.85
 * 
 * Example output:
 * Book: 24.98 (2 @ 12.49)
 * Music CD 16.49
 * Chocolate bar: 0.85
 * Sales Tax: 1.50
 * Total: 42.32
 * ------------------------
 * Defined Item set {book, music CD, chocolate bar, box of chocolates, bottle of perfume, packet of headache pills}
 * ------------------------
 * @author Alan Kai
 *
 */
public class Main {
    private static ArrayList<Item> itemList = new ArrayList<Item>();
    private static Hashtable<Double, String> dupCheck = new Hashtable<Double, String>();
	private final static double BASIC_TAX = 0.10;
    private final static double IMPORT_TAX = 0.05;
    
    private static double salesTax = 0;
    private static double total = 0;
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.print("Usage: input.txt");
		}
		else {
			String fileName = args[0];
			try {
				processFile(fileName);
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found");
				e.printStackTrace();
			}
		}
	}
	/**
	 * Open input file to parse
	 * 'quantity(1) import? itemName price'
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public static void processFile(String fileName) throws FileNotFoundException {
		Scanner inFile = new Scanner(new FileReader(fileName));
		while(inFile.hasNextLine()) {
			boolean isImport = false;
			String line = inFile.nextLine().toString();
			String delims = "[ ]";
			String[] tokens = line.split(delims);
			isImport = line.contains("imported");
			
			Pattern remove = Pattern.compile("imported ");
			Matcher remover = remove.matcher(line);
			while(remover.find()) {
				line = line.replaceAll(remover.group(0), "");
			}
			Pattern pattern = Pattern.compile("\\d\\ (.*?)\\d");
			
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				String itemName = matcher.group(1).substring(0, matcher.group(1).length()-4);
				double price = Double.parseDouble(tokens[tokens.length-1]);
				Item item = new Item(itemName, isImport, price);
				//Used price as a identifier instead of name
				if(dupCheck.put(item.price, item.name) != null) {
					for(int i = 0; i < itemList.size(); i++) {
						if(item.price == itemList.get(i).price) {
							itemList.get(i).addQuantity();
						}
					}
					
				}
				else {
					itemList.add(item);
				}
				
			}
			
		}
		checkOut();
		inFile.close();
	}
	/**
	 * Calculations for receipt and output to file
	 */
	public static void checkOut() {
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			System.out.println("Error outputing to text file");
			e.printStackTrace();
		}
		for(int i = 0; i < itemList.size(); i++) {
			double tax = 0;
			if(itemList.get(i).isExempt) {
				if(itemList.get(i).isImport) {
					tax += (IMPORT_TAX * itemList.get(i).pricePerUnit);
					tax = round(tax);
					itemList.get(i).pricePerUnit +=tax;
				}
			}
			else {
				if(itemList.get(i).isImport) {
					tax += ((IMPORT_TAX+BASIC_TAX) * itemList.get(i).pricePerUnit);
					tax = round(tax);
					itemList.get(i).pricePerUnit += tax;
				}
				else {
					tax += (BASIC_TAX * itemList.get(i).pricePerUnit);
					tax = round(tax);
					itemList.get(i).pricePerUnit += tax;
				}
			}
			//System.out.println("Sub " + itemList.get(i).pricePerUnit);
			System.out.println(itemList.get(i).toString());
			salesTax += tax;
			total += itemList.get(i).price; 
			
		}
		//total += salesTax;
		System.out.printf("Sales Taxes: %.2f\n",salesTax);
		System.out.printf("Total: %.2f\n", total);
		
		
	}
	/**
	 * Roundup to nearest .05
	 * @param tax
	 * @return rounded result
	 */
	public static double round(double tax) {
		 BigDecimal amount = new BigDecimal(tax);
		 amount =  new BigDecimal(Math.ceil(amount.doubleValue() * 20) / 20);
		 amount.setScale(2, RoundingMode.HALF_UP);
		 tax = amount.doubleValue();
		return tax;
	}
}
