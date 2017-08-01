import java.util.*;
/**
 * Exempted List class
 * Hard coded predefined list as given from example test data
 * @author Alan Kai
 *
 */
public class ExemptedList {
	private ArrayList<String> exempted_list;
	public ExemptedList() {
		exempted_list = new ArrayList<String>();
		exempted_list.add("chocolate bar");
		exempted_list.add("book");
		exempted_list.add("box of chocolates");
		exempted_list.add("packet of headache pills");
	}
	public ArrayList<String> get_List() {
		return exempted_list;
	}
}
