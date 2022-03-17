import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final DBConnector DB_CONNECTOR = DBConnector.getDbConnector();

    private static final String FILE_PATH = "data/imdb-data.csv";
    private static final String FILE_NAME = FILE_PATH.substring(FILE_PATH.lastIndexOf("/") + 1, FILE_PATH.lastIndexOf(".")).replaceAll("-", "_");
    private static final File FILE = new File(FILE_PATH);

    private static Scanner fileScanner = null;
    private static String columns;

    public static void main(String[] args) throws SQLException {
        // make file scanner
        try {
            fileScanner = new Scanner(FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        makeAndExecuteDDL();

        makeAndSaveDML();
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
        DB_CONNECTOR.excuteStatment(DDLString.toString());

        // save columns, for use in the insert statements

        columns = elements[0];
        for (int i = 1; i < elements.length; i++) {
            columns += ", " + elements[i];
        }

        DB_CONNECTOR.setColumns(columns);
    }

    private static void makeAndSaveDML() {
        StringBuilder DMLString = new StringBuilder();

        while (fileScanner.hasNextLine()) {
            // get the object (the line), and spilt its elements
            String[] elements = fileScanner.nextLine().split(";");

            // make insert into statement
            DMLString.append("INSERT INTO ")
                     .append(FILE_NAME.replace("-","_"))
                     .append("(").append(columns).append(")")
                     .append("\n");

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
