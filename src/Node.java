/**
 * Class for the Nodes in the OrderedDictionary
 * @author Gary Chiu
 * @course CS2210
 * @assignment Assignment 4
 * @date November 18, 2016
 */
public class Node {

	// declare instance variables
	private Record record;
	private Node left, right, parent;
	
	// constructor for an internal node
	public Node(Record record){
		this.record = record;
		this.left = new Node();
		this.right = new Node();
		this.parent = null;
	}
	
	// constructor for a leaf
	public Node(){
		this.record = null;
		this.left = null;
		this.right = null;
		this.parent = null;
	}
	
	/**
	 * Getter for the node's left child
	 * @return this node's left child
	 */
	public Node getLeft(){
		return this.left;
	}
	
	/**
	 * Getter for the node's right child
	 * @return this node's right child
	 */
	public Node getRight(){
		return this.right;
	}
	
	/**
	 * Setter for the node's left child
	 */
	public void setLeft(Node left){
		this.left = left;
	}
	
	/**
	 * Setter for the node's right child
	 */
	public void setRight(Node right){
		this.right = right;
	}
	
	/**
	 * Getter for the node's record object
	 * @return this node's record
	 */
	public Record getRecord(){
		return this.record;
	}
	
	/**
	 * Setter for the node's record
	 */
	public void setRecord(Record record){
		this.record = record;
	}
	
	/**
	 * Getter for the node's parent node
	 * @return this node's parent node
	 */
	public Node getParent(){
		return this.parent;
	}
	
	/**
	 * Setter for this node's parent
	 * @param parent node of this node
	 */
	public void setParent(Node parent){
		this.parent = parent;
	}
	
}
