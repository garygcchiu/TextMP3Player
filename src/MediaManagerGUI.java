import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.awt.Button;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MediaManagerGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MediaManagerGUI window = new MediaManagerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// declare variables, and initialize some
		String word, extension, command, param1 = "", param2 = "", param3 = "";
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
			
			inputDir = openDirectory();

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
		try {

			File folder = new File(inputDir);
			System.out.println("full: " + folder.getAbsolutePath());

			for (final File fileEntry : folder.listFiles()) {

				if (fileEntry.isFile()) {
					String tempFile = fileEntry.getName().toLowerCase();
					System.out.println("temp = " + tempFile);

					// get and process the file extension accordingly
					extension = getExtension(tempFile);
					if (extension.equals("")) {
						type = 1;
					} else {
						if (extension.equals("gif") || extension.equals("jpg")) {
							type = 3;
						} else {
							if (extension.equals("wav") || extension.equals("mid")) {
								type = 2;
							} else {
								type = 1;
							}
						}
					}

					word = tempFile.substring(0, tempFile.length() - 4);
					System.out.println("word = " + word);
					System.out.println("type = " + type);
					System.out.println("tempFile (data) = " + tempFile);

					// create record and store in OrderedDictionary
					Key key = new Key(word, type);
					Record rec = new Record(key, tempFile);
					dictionary.insert(rec);

				}
			}

		} catch (DictionaryException e) {
			System.out.println(e.getMessage());
		}

		// allow the user to input a command
		StringReader keyboard = new StringReader();
		String input = keyboard.read("Enter a command: ");

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

			case "search":
				search(dictionary, param1, inputDir);
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

	/**
	 * Create the application.
	 */
	public MediaManagerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 817, 692);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JList list = new JList();
		frame.getContentPane().add(list, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Play");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().add(btnNewButton, BorderLayout.WEST);

	}

	public static String openDirectory() {

		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(".")); // start at current
													// directory
		chooser.setDialogTitle("Select folder...");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		} else {
			System.out.println("No Selection ");
		}

		return chooser.getSelectedFile().toString();
	}

	/**
	 * Method to return the file extension of a given string
	 * @param data: full filename
	 * @return filename's file extension
	 */
	private static String getExtension(String data) {
        if (data.lastIndexOf(".") != -1 && data.lastIndexOf(".") != 0){
        	return data.substring(data.lastIndexOf(".")+1);
        } else {
        	return "";
        }
    }
	
	private static void search(OrderedDictionary dictionary, String param1, String inputDir) {
		int counter = 0; // counter to see if word is even stored

		for (int i = 1; i <= 3; i++) {

			if (dictionary.find(new Key(param1, i)) != null) {
				if (i == 1) {
					System.out.println(dictionary.find(new Key(param1, i)).getData());
				}
				if (i == 2) { // play audio
					SoundPlayer player = new SoundPlayer();
					try {
						String playDir = inputDir + "\\" + dictionary.find(new Key(param1, i)).getData();
						player.play(playDir);
					} catch (MultimediaException e) {
						System.out.println("Search: unable to play " + param1 + ".");
					}
				}
				if (i == 3) { // output picture: images may need to be in /bin
								// instead of root
					PictureViewer viewer = new PictureViewer();
					try {
						String playDir = inputDir + "\\" + dictionary.find(new Key(param1, i)).getData();
						viewer.show(playDir);
					} catch (MultimediaException e) {
						System.out.println("Search: unable to display " + param1 + ".");
					}
				}
			} else {
				counter++;
			}

			if (counter == 3) {
				System.out.println("Search: unable to find file " + param1);
			}

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

}
