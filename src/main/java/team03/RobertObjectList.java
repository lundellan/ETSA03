package team03;

import java.util.ArrayList;

/**
 * List of Roberts used in RobertTalk
 * @author Parents of R.O.B.E.R.T (Team03)
 *
 */
public class RobertObjectList {
	
	private ArrayList<RobertObject> roberts;
	
	/**
	 * Initializes ArrayList of RobertObjects
	 */
	public RobertObjectList() {
		roberts = new ArrayList<RobertObject>();
	}
	
	/**
	 * Adds RobertObject to list of Roberts
	 * @param robert Robert to be added
	 */
	public void add(RobertObject robert) {
		roberts.add(robert);
	}
	
	/**
	 * Removes robert with the name which is passed as a parameter
	 * @param name Robert to be removed
	 */
	public void remove(String name) {
		for (int i = 0; i < roberts.size(); i++) {
			if (roberts.get(i).getName().equals(name)) {
				roberts.remove(i);
			}
		}
	}
	
	/**
	 * Returns size of the current list
	 * @return int size of current list
	 */
	public int size() {
		return roberts.size();
	}
	
	/**
	 * Checks if list contains a object with the same name as parameter
	 * @param name Name to be checked
	 * @return True if it contains name passed in, false if not.
	 */
	public boolean contains(String name) {
		for (int i = 0; i < roberts.size(); i++) {
			if (roberts.get(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets rank of name
	 * @param name
	 * @return
	 */
	public int getRank(String name) {
		for (int i = 0; i < roberts.size(); i++) {
			if (roberts.get(i).getName().equals(name)) {
				return i+1;
			}
		}
		return 3;
	}
	
	/**
	 * Sorts list based on rank.
	 */
	public void sortList() {
		/*ArrayList<RobertObject> sorted = new ArrayList<RobertObject>();	
		for (int i = 0; i < roberts.size(); i++) {
		    int ID = roberts.get(i).getID();
			int pos = 0;
			while (pos < sorted.size() && sorted.get(pos).getID() > ID) {
		        pos++;
			}
		    sorted.add(pos, roberts.get(i));
		}
		for (int i = sorted.size()-1; i >= 0; i--) {
			roberts.remove(i);
		}
		for (int i = 0; i < sorted.size(); i++) {
			roberts.add(sorted.get(i));
		}*/
		
		ArrayList<RobertObject> sorted = new ArrayList<RobertObject>();	
		for (int i = 0; i < roberts.size(); i++) {
		    String word = roberts.get(i).getName();
			int pos = 0;
			while (pos < sorted.size() && sorted.get(pos).getName().compareTo(word) < 0) {
		        pos++;
			}
		    sorted.add(pos, roberts.get(i));
		}
		for (int i = sorted.size()-1; i >= 0; i--) {
			roberts.remove(i);
		}
		for (int i = 0; i < sorted.size(); i++) {
			roberts.add(sorted.get(i));
		}
	}

}
