import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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

        makeDDL();

        makeDML();
    }

    private static void makeDDL() {
        StringBuilder DDLString = new StringBuilder();

        //Create table header
        DDLString.append("CREATE TABLE ")
                 .append(FILE_NAME.replace("-", "_"))
                 .append(" (\n")
                 .append("    ID int NOT NULL AUTO_INCREMENT");

        // define columns
        String[] elements = fileScanner.nextLine().split(";");
        for (int i = 0; i < elements.length; i++) {
            DDLString.append(",\n");
            DDLString.append("    ").append(elements[i]).append(" varchar(255)");
        }

        // ending
        DDLString.append("\n);");

        writeToFile("data/DDL.sql", DDLString.toString());
    }

    private static void makeDML() {
        /*
        INSERT INTO table_name
        VALUES (value1, value2, value3, ...);
         */
        StringBuilder DMLString = new StringBuilder();



        while (fileScanner.hasNextLine()) {
            String[] elements = fileScanner.nextLine().split(";");
            // make insert
            DMLString.append("INSERT INTO ").append(FILE_NAME.replace("-","_")).append("\n");

            // values
            DMLString.append("VALUES (");
            DMLString.append("\"").append(elements[0]).append("\"");
            for (int i = 1; i < elements.length; i++) {
                DMLString.append(", ");
                DMLString.append("\"").append(elements[i]).append("\"");
            }
            DMLString.append(");\n");
        }

        writeToFile("data/DML.sql", DMLString.toString());
    }

    public static void writeToFile(String filePath, String stringToSave){
        try {
            // print to file
            PrintStream printStream = new PrintStream(filePath);
            printStream.print(stringToSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
