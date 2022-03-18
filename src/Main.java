import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final DBConnector DB_CONNECTOR = DBConnector.getDbConnector();

    private static final String FILE_PATH = "data/imdb-data.csv";
    private static final String FILE_NAME = FILE_PATH.substring(FILE_PATH.lastIndexOf("/") + 1, FILE_PATH.lastIndexOf(".")).replaceAll("-", "_");
    private static final File FILE = new File(FILE_PATH);

    private static Scanner fileScanner = null;
    private static String columns;

    public static void main(String[] args) throws SQLException {
        // clean the for the table
        DB_CONNECTOR.executeStatement("DROP TABLE " + FILE_NAME);

        // make file scanner
        try {
            fileScanner = new Scanner(FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        makeAndExecuteDDL();

        makeAndExecuteDML();

        DB_CONNECTOR.DBDisconnect();
    }

    private static void makeAndExecuteDDL() {
        boolean created;

        String[] elements = fileScanner.nextLine().split(";");

        created = DB_CONNECTOR.createTable(FILE_NAME, elements);

        if (created) {
            System.out.println("Created table: " + FILE_NAME);
        } else {
            System.out.println("Failed to create the table");
            System.exit(1);
        }
    }

    private static void makeAndExecuteDML() {
        boolean added;

        while (fileScanner.hasNextLine()) {
            // get the object (the line), and spilt its elements
            String[] elements = fileScanner.nextLine().split(";");

            // give the elements to the DBConnector to add the data
            added = DB_CONNECTOR.insertData(elements);

            // print to the user, the success of the insert
            if (added) {
                System.out.println("Data added: " + Arrays.toString(elements));
            } else {
                System.out.println("Failed to add data: " + Arrays.toString(elements));
            }
        }
    }
}
