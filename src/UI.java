import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

/**
 * Class for the interactive User-Interface
 * 
 * @author Gary Chiu
 * @course CS2210
 * @assignment Assignment 4
 * @date November 18, 2016
 */
public class UI {

	public static void main(String[] args) {

		// declare variables, and initialize some
		String word, command, param1 = "", param2 = "", param3 = "";
		String inputDir = null;
		int type;
		OrderedDictionary dictionary = new OrderedDictionary();

		// read input directory from file, if executed previously
		try {

			String prevDir = "directory.txt";
			BufferedReader reader = new BufferedReader(new FileReader(prevDir));
			String line = reader.readLine();
			inputDir = line;

		} catch (IOException e) {
			// no previous directory found, get directory from user input
			// allow the user to specify the input directory

			StringReader reader = new StringReader();
			inputDir = reader.read("Please enter the directory of media files: ");
			System.out.println("Reading " + inputDir + "....");

			// output directory to file for next execution
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("directory.txt"));
				writer.write(inputDir);
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		// get the input directory
		File folder = new File(inputDir);
		
		for (final File fileEntry : folder.listFiles()) {

			if (fileEntry.isFile()) {
				String tempFile = fileEntry.getName().toLowerCase();
				word = tempFile.substring(0, tempFile.length() - 4);

				// create record and store in OrderedDictionary
				type = hashIt(33, word, word.length(), 199);
				Key key = new Key(word, type);
				Record rec = new Record(key, tempFile);
				
				try {
					dictionary.insert(rec);
				} catch (DictionaryException e) {

				}

			}
		}

		// output the database
		dictionary.outputDictionary(dictionary.getRoot());

		// allow the user to input a command
		StringReader keyboard = new StringReader();
		String input = keyboard.read("\nEnter a command: ");

		while (!input.equals("end")) {

			// parse command and parameters
			String[] lineSplit = input.split(" ");
			command = lineSplit[0];
			if (lineSplit.length == 4) {
				param1 = lineSplit[1];
				param2 = lineSplit[2];
				param3 = lineSplit[3];
			} else {
				if (lineSplit.length == 3) {
					param1 = lineSplit[1];
					param2 = lineSplit[2];
				} else {
					if (lineSplit.length == 2) {
						param1 = lineSplit[1];
					}
				}
			}

			// switch statement for user commands
			switch (command) {

			case "play":
				play(dictionary, param1, inputDir, 199);
				break;

			case "remove":
				remove(dictionary, param1, param2);
				break;

			case "insert":
				insert(dictionary, param1, param2, param3);
				break;

			case "next":
				next(dictionary, param1, param2);
				break;

			case "prev":
				prev(dictionary, param1, param2);
				break;

			case "first":
				System.out.println("First: the key of the record with the smallest key is ("
						+ dictionary.smallest().getKey().getWord() + ", " + dictionary.smallest().getKey().getType()
						+ ").");
				break;

			case "last":

				System.out.println("Last: the key of the record with the largest key is ("
						+ dictionary.largest().getKey().getWord() + ", " + dictionary.largest().getKey().getType()
						+ ").");
				break;

			case "end":
				break;

			default:
				System.out.println("Sorry, command not recognized.");

			}
			// reset parameters and get next command
			param1 = param2 = param3 = "";
			input = keyboard.read("Enter a command: ");
		}
		System.out.println("Program terminated.");
	}

	private static void play(OrderedDictionary dictionary, String param1, String inputDir, int numFolder) {
		try {
			if (dictionary.findName(Integer.parseInt(param1)) != null) {
				SoundPlayer player = new SoundPlayer();
				try {
					String playDir = inputDir + "\\" + dictionary.findName(hashIt(33, param1, param1.length(), numFolder));
					player.play(playDir);
				} catch (MultimediaException e) {
					System.out.println("Search: unable to play " + param1 + ".");
				}
			} else {
				System.out.println("Search: unable to find file " + param1);
			}
		} catch (NumberFormatException e){
			System.out.println("Play: invalid track number.");
		}
	}

	private static void remove(OrderedDictionary dictionary, String param1, String param2) {
		try {
			dictionary.remove(new Key(param1, Integer.parseInt(param2)));
			System.out.println("Remove: successfully removed key from dictionary.");
		} catch (NumberFormatException e) {
			System.out.println("Remove: type is not an integer.");
		} catch (DictionaryException e) {
			System.out.println("Remove: record is not in dictionary.");
		}
	}

	private static void insert(OrderedDictionary dictionary, String param1, String param2, String param3) {
		try {
			Record rec = new Record((new Key(param1, Integer.parseInt(param2))), param3);
			dictionary.insert(rec);
			System.out.println("Insert: successfully inserted record into dictionary.");
		} catch (NumberFormatException e) {
			System.out.println("Insert: incorrect format.");
		} catch (DictionaryException e) {
			System.out.println("Insert: record already exists in dictionary.");
		}
	}

	private static void next(OrderedDictionary dictionary, String param1, String param2) {
		try {
			if (dictionary.successor(new Key(param1, Integer.parseInt(param2))) == null) {
				System.out.println(
						"Next: The current key (" + param1 + ", " + param2 + ") is the largest key in the database.");
			} else {
				String resultNext = "("
						+ dictionary.successor(new Key(param1, Integer.parseInt(param2))).getKey().getWord() + ", "
						+ dictionary.successor(new Key(param1, Integer.parseInt(param2))).getKey().getType() + ")";
				System.out.println("Next: the successor of that record is " + resultNext + ".");
			}
		} catch (NumberFormatException e) {
			System.out.println("Next: incorrect parameters.");
		} catch (NullPointerException e) {
			System.out.println("Next: invalid record specified.");
		}
	}

	private static void prev(OrderedDictionary dictionary, String param1, String param2) {
		try {
			if (dictionary.predecessor(new Key(param1, Integer.parseInt(param2))) == null) {
				System.out.println(
						"Prev: The current key (" + param1 + ", " + param2 + ") is the smallest key in the database.");
			} else {
				String resultPrev = "("
						+ dictionary.predecessor(new Key(param1, Integer.parseInt(param2))).getKey().getWord() + ", "
						+ dictionary.predecessor(new Key(param1, Integer.parseInt(param2))).getKey().getType() + ")";
				System.out.println("Prev: the predecessor of that record is " + resultPrev + ".");
			}
		} catch (NumberFormatException e) {
			System.out.println("Prev: incorrect parameters.");
		} catch (NullPointerException e) {
			System.out.println("Prev: invalid record specified.");
		}
	}

	/**
	 * Method to calculate the index, according to the config. Converts string
	 * to integer using polynomial hash algorithm
	 * 
	 * @param prime
	 *            int x
	 * @param string
	 *            text
	 * @param int
	 *            length of string length
	 * @param int
	 *            dictionary size
	 * @return the insertion index for the config
	 **/
	private static int hashIt(int x, String text, int length, int M) {
		int result = (int) (text.charAt(length - 1)) % M;
		for (int i = length - 2; i >= 0; i--) {
			char c = text.charAt(i);
			result = ((result * x + (int) c)) % M;
		}
		return result;
	}
	

}
