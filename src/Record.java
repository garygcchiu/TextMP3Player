/**
 * Class for the Record objects to store (key, data)
 * @author Gary Chiu
 * @course CS2210
 * @assignment Assignment 4
 * @date November 18, 2016
 */
public class Record {

	// declare instance variables
	private Key key;
	private String data;
	
	// constructor for the record 
	public Record(Key key, String string) {
		this.key = key;
		this.data = string;
	}
	
	/**
	 * Getter for the data in the record
	 * @return this record's data
	 */
	public String getData() {
		return this.data;
	}
	
	/**
	 * Getter for the key of the record
	 * @return this record's key
	 */
	public Key getKey(){
		return this.key;
	}
	
}
