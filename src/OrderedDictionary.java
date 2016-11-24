/**
 * Class for the OrderedDictionary
 * 
 * @author Gary Chiu
 * @course CS2210
 * @assignment Assignment 4
 * @date November 18, 2016
 */
public class OrderedDictionary implements OrderedDictionaryADT {

	// create instance variables
	private Node root;

	// constructor for the OrderedDictionary
	public OrderedDictionary() {
		root = new Node(); // leaf node
	}

	/**
	 * Method to find the record object that corresponds to a key
	 * 
	 * @return Returns the Record object with key k, or it returns null if such
	 *         a record is not in the dictionary.
	 **/
	public Record find(Key k) {
		if (root.getRecord() == null) {
			return root.getRecord();
		} else {
			Node p = root;
			// move to appropriate child if p is not a leaf and keys are
			// different
			while (p.getLeft() != null && p.getRight() != null && k.compareTo(p.getRecord().getKey()) != 0) {
				if (k.compareTo(p.getRecord().getKey()) == -1) {
					p = p.getLeft();
				} else {
					p = p.getRight();
				}
			}
			return p.getRecord();
		}
	}

	/**
	 * Method that inserts r into the ordered dictionary. It throws a
	 * DictionaryException if a record with the same key as r is already in the
	 * dictionary.
	 * 
	 * @param Record
	 *            r to be inserted
	 **/
	public void insert(Record r) throws DictionaryException {
		Node p = findNode(r);

		// check if p is internal node
		if (p.getLeft() != null && p.getRight() != null) {
			throw new DictionaryException(null); // record with same key already
													// exists
		} else {
			// store record in p
			p.setRecord(r);
			// create 2 new leaves
			Node leftChild = new Node();
			Node rightChild = new Node();
			// make children p's children
			p.setLeft(leftChild);
			p.setRight(rightChild);
			leftChild.setParent(p);
			rightChild.setParent(p);
		}
	}

	/**
	 * Method to remove the record with Key k from the dictionary. It throws a
	 * DictionaryException if the record is not in the dictionary.
	 * 
	 * @param Key
	 *            k to remove from the OrderedDictionary
	 **/
	public void remove(Key k) throws DictionaryException {
		Record r = new Record(k, "");
		Node p = findNode(r);
		Node c1;

		if (p.getRecord() == null) { // if p is a leaf, k does not exist in
										// dict.
			throw new DictionaryException(null);
		} else {
			// check if at least one child of p is a leaf
			if (p.getLeft().getRecord() == null || p.getRight().getRecord() == null) {
				if (p.getLeft().getRecord() == null) {
					c1 = p.getRight();
				} else {
					c1 = p.getLeft();
				}
				// set parent's child as p's non-leaf child
				Node p1 = p.getParent();
				if (p1 == null) {
					root = c1;
				} else {
					if (p.getRecord() == p1.getLeft().getRecord()) {
						p1.setLeft(c1);
					} else {
						p1.setRight(c1);
					}
				}
			} else { // both children has internal nodes, replace with right's
						// smallest
				Node s = smallestNode(p.getRight());
				p.setRecord(s.getRecord()); // copy record in s to p
				Node p1 = s.getParent();

				if (p1.getLeft() == s) {
					p1.setLeft(s.getRight());
				} else {
					p1.setRight(s.getRight());
				}

				s.getRight().setParent(p1);
			}

		}
	}

	/**
	 * Method to find the record with smallest key larger than k = successor
	 * 
	 * @param Key
	 *            k to find successor of
	 * @return the record with the successor of k, null if k has no successor
	 **/
	public Record successor(Key k) {
		Record r = new Record(k, "");
		// return null if root is a leaf
		if (root.getRecord() == null) {
			return null;
		} else {
			Node p = findNode(r);
			// check if right child of p is an internal node
			if (p.getRight().getRecord() != null) {
				// return smallest key larger than k
				return smallest(p.getRight().getRecord());
			} else {
				// navigate up tree to see if there is a successor above
				Node p1 = p.getParent();
				while (p1 != null && p == p1.getRight()) {
					p = p1;
					p1 = p.getParent();
				}
				// check to see if reached root or found successor
				if (p1 == null) {
					return null; // no successor
				} else {
					return p1.getRecord();
				}
			}
		}
	}

	/**
	 * Method to return the predecessor of k: record with largest key smaller
	 * than k
	 * 
	 * @param Key
	 *            k to find predecessor of
	 * @return the record with the predecessor of k, null if k has no
	 *         predecessor
	 **/
	public Record predecessor(Key k) {
		Record r = new Record(k, "");

		if (root.getRecord() == null) {
			return null;
		} else {
			Node p = findNode(r);
			// return largest key smaller than k if left child is internal node
			if (p.getLeft().getRecord() != null) {
				return largest(p.getLeft().getRecord());
			} else {
				// navigate up tree to see if there is a predecessor above
				Node p1 = p.getParent();
				while (p1 != null && p == p1.getLeft()) {
					p = p1;
					p1 = p.getParent();
				}
				if (p1 == null) {
					return null; // no predecessor
				} else {
					return p1.getRecord();
				}
			}
		}
	}

	/**
	 * Method to return the smallest key in the ordered dict.
	 * 
	 * @return the record with smallest key in the ordered dictionary
	 * @return null if the dictionary is empty
	 **/
	public Record smallest() {
		if (root.getRecord() == null) {
			return null;
		} else {
			Node p = root;
			while (p.getLeft() != null && p.getRight() != null) {
				p = p.getLeft();
			}
			return p.getParent().getRecord();
		}
	}

	/**
	 * Method to get the largest key in the dict.
	 * 
	 * @return Record with largest key in the ordered dictionary
	 * @return null if dictionary is empty
	 **/
	public Record largest() {
		if (root.getRecord() == null) {
			return null;
		} else {
			Node p = root;
			while (p.getLeft() != null && p.getRight() != null) {
				p = p.getRight();
			}
			return p.getParent().getRecord();
		}
	}

	/**
	 * Helper method to find the node containing the record r
	 * 
	 * @param Record
	 *            r to find
	 * @return Node that must contain the record r.
	 */
	private Node findNode(Record r) {
		// check if root is a leaf
		if (root.getRecord() == null) {
			return root;
		} else {
			Node p = root;
			while (p.getLeft() != null && p.getRight() != null && r.getKey().compareTo(p.getRecord().getKey()) != 0) {
				if (r.getKey().compareTo(p.getRecord().getKey()) == -1) {
					p = p.getLeft();
				} else {
					p = p.getRight();
				}
			}
			return p;
		}
	}

	/**
	 * Helper method for the predecessor(k) method.
	 * 
	 * @param Record
	 *            r to find the largest record within
	 * @return Record with the smallest record, rooted at r
	 */
	private Record largest(Record r) {
		Node newRoot = findNode(r);
		if (newRoot.getRecord() == null) {
			return null;
		} else {
			Node p = newRoot;
			while (p.getLeft() != null && p.getRight() != null) {
				p = p.getRight();
			}
			return p.getParent().getRecord();
		}
	}

	/**
	 * Helper method for the successor(k) method.
	 * 
	 * @param Record
	 *            r to find smallest record within
	 * @return Record storing smallest record, rooted at r
	 */
	private Record smallest(Record r) {
		Node newRoot = findNode(r);
		if (newRoot.getRecord() == null) {
			return null;
		} else {
			Node p = newRoot;
			while (p.getLeft() != null && p.getRight() != null) {
				p = p.getLeft();
			}
			return p.getParent().getRecord();
		}
	}

	/**
	 * Helper method used in the remove(k) method. Finds smallest node rooted at
	 * root
	 * 
	 * @param Node
	 *            root of a subtree/tree.
	 * @return Node storing the smallest key, rooted at the parameter root node
	 **/
	private Node smallestNode(Node root) {
		if (root.getRecord() == null) {
			return null;
		} else {
			Node p = root;
			while (p.getLeft() != null && p.getRight() != null) {
				p = p.getLeft();
			}
			return p.getParent();
		}
	}

	public Node getRoot() {
		return root;
	}

	public void outputDictionary(Node root) {
		if (root != null) {
			try {
				outputDictionary(root.getLeft());
				System.out.printf("%-5d %s \n", root.getRecord().getKey().getType(),
						root.getRecord().getKey().getWord());
				outputDictionary(root.getRight());
			} catch (NullPointerException e) {

			}
		}
	}

	public String findName(int type) {
		// check if root is a leaf
		
		System.out.println("type looking for is " + type);
		
		if (root.getRecord() == null) {
			return null;
		} else {
			Node p = root;
			System.out.println("p.getRecord.type RN is " + p.getRecord().getKey().getType());
			while (p.getLeft() != null && p.getRight() != null && p.getRecord().getKey().getType() != type) {
				
				System.out.println("p = " + p.getRecord().getKey().getWord() + "'s type is " + p.getRecord().getKey().getType());
				
				if (p.getRecord().getKey().getType() > type) {
					p = p.getLeft();
				} else {
					p = p.getRight();
				}
			}
			if (p.getRecord() == null) {
				return null; // no predecessor
			} else {
				return p.getRecord().getKey().getWord();
			}
		}
	}

}
