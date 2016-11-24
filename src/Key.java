/**
 * Class for the Key objects to store (word, type)
 * @author Gary Chiu
 * @course CS2210
 * @assignment Assignment 4
 * @date November 18, 2016
 */
public class Key {

	// declare instance variables
	private String word;
	private int type;
	
	// constructor
	public Key(String word, int type) {
		this.word = word;
		this.type = type;
	}

	/**
	 * Getter for the key's word
	 * @return key's word
	 */
	public String getWord() {
		return this.word;
	}

	/**
	 * Getter for the key's type
	 * @return key's type
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * CompareTo method: serves to compare 2 keys by lexicographical order
	 * @param Key k to compare with this key
	 * @return 0 if keys are the same, -1 if this key is < k, 1 otherwise
	 */
	public int compareTo(Key k){
		
		if (this.word.equals(k.getWord()) && this.type == k.getType()){
			return 0;
		} else {
			if (this.word.compareTo(k.getWord()) < 0 || (this.word.equals(k.getWord()) && this.type < k.getType())){
				return -1;
			} else {
				return 1;
			}
		}
	}
	
}
