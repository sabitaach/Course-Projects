package sabitaWork;
//exploring out all the files that are present in a particular directory. Important process because filenames provide us the subject for triples
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.core.helpers.FileUtils;

public class ListFilesUtil {

	/**
	 * List all the files and folders from a directory
	 * 
	 * @param directoryName
	 *            to be listed
	 */
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
		File file_to_write = new File(
				"C:\\Users\\prakash\\Documents\\contentselection\\ContentSelection\\names_of_folders.txt");
		for (File file : fList) {
			if (file.isDirectory()) {
				String the_name_of_file = file.getName();
				System.out.println(the_name_of_file);
				try {
					FileWriter fw = new FileWriter(file_to_write, true);
					fw.write(the_name_of_file + "\n");
					fw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// try to get all the contents that are in that folder
			String new1 = file.getPath();
			// System.out.println("neew one:"+new1);
			File folder = new File(new1);
			File[] listOfFiles = folder.listFiles();
			// System.out.println("the length:"+listOfFiles.length);

			for (int i = 0; i < listOfFiles.length; i++) {
				File file1 = listOfFiles[i];
				if (file1.isFile() && file1.getName().endsWith(".tsv")) {
					String content = file1.getName();
					System.out.println("sabitas" + content);
					BufferedReader reader = new BufferedReader(new FileReader(
							file1.getPath()));
					String str = "";
					while ((str = reader.readLine()) != null) {
						if (str.startsWith("predicate")
								|| str.trim().length() == 0
								|| str.trim().length() <= 2)
							continue;
						String[] values = str.split("\\t", -1);
						System.out.println("the fields are:" + values[0]
								+ "the second value:" + values[1]);
						String onlythemainpredicate = values[0].substring(
								values[0].lastIndexOf("/") + 1,
								values[0].length());
						String editedsecond = values[1];
						if (values[1].contains("\""))
							editedsecond = values[1].replace("\"", "");

						if (editedsecond.contains("Freebase")) {
							String the_one_to_replace = editedsecond.substring(
									editedsecond.indexOf("- Freebase"),
									editedsecond.length());

							editedsecond = editedsecond.replace(
									the_one_to_replace, "");
						}
						String the_final_predicate = file.getName().replace(
								".tsv", "")
								+ "\t"
								+ onlythemainpredicate
								+ "\t"
								+ editedsecond;
						writepredicate(the_final_predicate, file1.getPath());

						System.out.println("after editing:"
								+ the_final_predicate);
					}

				}
			}

		}

	}
//extracting the file names and appending it with predicate and object to generate triples
	public static void writepredicate(String text, String path) {
		path = path.replace(".tsv", "");

		String name_of_file = path + "edited";
		name_of_file = name_of_file + ".tsv";
		try {
			FileWriter fw = new FileWriter(name_of_file, true);
			fw.write(text + "\n");
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		String all_the_triples = "C:\\Users\\prakash\\Documents\\contentselection\\ContentSelection\\all_triples.txt";
		try {
			FileWriter fw1 = new FileWriter(all_the_triples, true);
			fw1.write(text + "\n");
			fw1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getTheFiles(String directoryName) {

		File folder = new File(directoryName);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".tsv")) {
				System.out.println("file:" + file);

			}
		}
	}

	/**
	 * List all files from a directory and its subdirectories
	 * 
	 * @param directoryName
	 *            to be listed
	 */
	public void listFilesAndFilesSubDirectories(String directoryName) {

		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				System.out.println(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}

	public static void main(String[] args) throws IOException {

		final String directoryWindows = "C:/Users/prakash/Desktop/cruz/related_papers/from content selection challenge/pairs/pairs";

		listFolders(directoryWindows);
	}
}