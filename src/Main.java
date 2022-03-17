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

    private static void makeAndExecuteDDL() throws SQLException {
        StringBuilder DDLString = new StringBuilder();

        //Create table header
        DDLString.append("CREATE TABLE ")
                 .append(FILE_NAME.replace("-", "_"))
                 .append(" (\n")
                 .append("    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY");

        // define columns
        String[] elements = fileScanner.nextLine().split(";");
        for (String element : elements) {
            DDLString.append(",\n");
            DDLString.append("    ").append(element).append(" varchar(255)");
        }

        // ending
        DDLString.append("\n);");

        //writeToFile("data/DDL.sql", DDLString.toString());
        DB_CONNECTOR.executeStatement(DDLString.toString());

        // save columns, for use in the insert statements

        columns = elements[0];
        for (int i = 1; i < elements.length; i++) {
            columns += ", " + elements[i];
        }

        DB_CONNECTOR.setColumns(columns);
        DB_CONNECTOR.setFileName(FILE_NAME);
    }

    private static void makeAndExecuteDML() throws SQLException {
        boolean added;

        while (fileScanner.hasNextLine()) {
            // get the object (the line), and spilt its elements
            String[] elements = fileScanner.nextLine().split(";");

            // give the elements to the DBConnector to add the data
            added = DB_CONNECTOR.insertData(elements);

            if (added) {
                System.out.println("Data added: " + Arrays.toString(elements));
            } else {
                System.out.println("Failed to add data: " + Arrays.toString(elements));
            }
        }
    }
}
