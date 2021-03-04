package team03;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for RobertTalk.
 * @author Parents of R.O.B.E.R.T (Team03)
 *
 */
public class RobertObject  {
	
	private String name;
	private int rank;
	
	/**
	 * Used in RobertTalk, in order to localize and identify correct Roberts.
	 * @param name
	 */
	public RobertObject(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the rank to parameter n
	 * @param n which rank to set
	 */
	public void setRank(int n) {
		rank = n;
	}
	
	/**
	 * Getter of current rank
	 * @return int rank
	 */
	public int getRank() {
		return rank;
	}
	
	/**
	 * Getter of name
	 * @return String name
	 */
	public String getName() {
		return name;
	}

}
