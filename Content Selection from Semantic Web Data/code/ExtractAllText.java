package sabitaWork;
//used to extract all the sentences by going through each folder and pulling out their contents
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExtractAllText {

	public static void listFilesAndFolders(String directoryName) {

		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			System.out.println(file.getName());
		}
	}

	/**
	 * List all the files under a directory
	 * 
	 * @param directoryName
	 *            to be listed
	 */
	public static void listFiles(String directoryName) {
		System.out.println("directory%%%%%%%%%%%%%%%%%%%" + directoryName);

		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				System.out.println("here");
				System.out.println(file.getName());
			}
		}
	}

	/**
	 * List all the folder under a directory
	 * 
	 * @param directoryName
	 *            to be listed
	 * @throws IOException
	 */
	public static void listFolders(String directoryName) throws IOException {

		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			// try to get all the contents that are in that folder

			String new1 = file.getPath();
			String the_name_of_file = file.getName();
			the_name_of_file = the_name_of_file.replace("_", " ");
			// System.out.println("neew one:"+new1);
			File folder = new File(new1);
			File[] listOfFiles = folder.listFiles();
			// System.out.println("the length:"+listOfFiles.length);

			for (int i = 0; i < listOfFiles.length; i++) {
				File file1 = listOfFiles[i];
				if (file1.isFile() && file1.getName().endsWith(".txt")) {
					String content = file1.getName();
					System.out.println("sabitas" + content);
					BufferedReader reader = new BufferedReader(new FileReader(
							file1.getPath()));
					String str = "";
					while ((str = reader.readLine()) != null) {
						String pronoun = "He";
						String pronoun1 = "She";
						String pronoun2 = "His";
						String pronoun3 = "Her";
						String[] contents = str.split(" ");
						String final_sentence = "";
						for (String inside : contents) {
							int check = 0;
							String thepart = "";
							if (inside.contains("(")
									&& inside.contains(")")
									&& ((inside.contains(pronoun))
											|| (inside.contains(pronoun1))
											|| (inside.contains(pronoun2)) || (inside
												.contains(pronoun3)))) {
								check = 1;
								thepart = inside.substring(inside.indexOf("("),
										inside.indexOf(")") + 1);
								inside = inside.replace(thepart, "");
							}
							if (inside.equalsIgnoreCase(pronoun))
								inside = the_name_of_file;
							if (inside.equalsIgnoreCase(pronoun1))
								inside = the_name_of_file;
							if (inside.equalsIgnoreCase(pronoun2))
								inside = the_name_of_file;
							if (inside.equalsIgnoreCase(pronoun3))
								inside = the_name_of_file;
							if (check == 1)
								final_sentence = thepart + " " + final_sentence
										+ inside + " ";
							else
								final_sentence = final_sentence + inside + " ";

						}

						writepredicate(final_sentence, file1.getPath());

						System.out.println("after editing:" + final_sentence);
					}

				}
			}

		}

	}

	public static void writepredicate(String text, String path) {
		path = path.replace(".txt", "");

		String name_of_file = path + "edited";
		name_of_file = name_of_file + ".txt";
		try {
			FileWriter fw = new FileWriter(name_of_file, true);
			fw.write(text + "\n");
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		String all_the_text = "C:\\Users\\prakash\\Documents\\contentselection\\ContentSelection\\all_text.txt";
		try {
			FileWriter fw1 = new FileWriter(all_the_text, true);
			fw1.write(text + "\n");
			fw1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		final String directoryWindows = "C:/Users/prakash/Desktop/cruz/related_papers/from content selection challenge/pairs/pairs";

		listFolders(directoryWindows);
	}

}
