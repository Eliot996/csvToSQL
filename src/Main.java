import java.io.*;
import java.util.Scanner;

public class Main {
    // read the first line from the file
    // generate the DDL

    // read the next lines and convert them to inserts

    private static final String FILE_NAME = "imdb-data";
    private static final String FILE_PATH = "data/" + FILE_NAME + ".csv";
    private static final File FILE = new File(FILE_PATH);
    private static Scanner fileScanner = null;

    public static void main(String[] args) {
        // make file scanner
        try {
            fileScanner = new Scanner(FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        makeAndSaveDDL();

        makeAndSaveDML();
    }

    private static void makeAndSaveDDL() {
        StringBuilder DDLString = new StringBuilder();

        //Create table header
        DDLString.append("CREATE TABLE ")
                 .append(FILE_NAME.replace("-", "_"))
                 .append(" (\n")
                 .append("    ID int NOT NULL AUTO_INCREMENT");

        // define columns
        String[] elements = fileScanner.nextLine().split(";");
        for (String element : elements) {
            DDLString.append(",\n");
            DDLString.append("    ").append(element).append(" varchar(255)");
        }

        // ending
        DDLString.append("\n);");

        writeToFile("data/DDL.sql", DDLString.toString());
    }

    private static void makeAndSaveDML() {
        StringBuilder DMLString = new StringBuilder();

        while (fileScanner.hasNextLine()) {
            // get the object (the line), and spilt its elements
            String[] elements = fileScanner.nextLine().split(";");

            // make insert into statement
            DMLString.append("INSERT INTO ").append(FILE_NAME.replace("-","_")).append("\n");

            // add the values to the StringBuilder
            DMLString.append("VALUES (");
            DMLString.append("\"").append(elements[0]).append("\"");
            for (int i = 1; i < elements.length; i++) {
                DMLString.append(", ");
                DMLString.append("\"").append(elements[i]).append("\"");
            }
            // add the ending to the statement
            DMLString.append(");\n");
        }

        writeToFile("data/DML.sql", DMLString.toString());
    }

    public static void writeToFile(String filePath, String stringToSave){
        try {
            // print to file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(stringToSave);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
