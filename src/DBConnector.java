import java.sql.*;

public class DBConnector {

    private static DBConnector dbConnector = new DBConnector();

    private Connection con;

    private String columns;
    private String fileName;

    private DBConnector() {
        try {
            DBConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String query) throws SQLException {
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        return stmt.executeQuery(query);
    }

    public boolean createTable(String fileName,String[] columns) {
        this.fileName = fileName;

        this.columns = columns[0];
        for (int i = 1; i < columns.length; i++) {
            this.columns += ", " + columns[i];
        }

        StringBuilder DDLString = new StringBuilder();

        //Create table header
        DDLString.append("CREATE TABLE ")
                .append(fileName)
                .append(" (\n")
                .append("    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY");

        // define columns

        for (String element : columns) {
            DDLString.append(",\n");
            DDLString.append("    ").append(element).append(" varchar(255)");
        }

        // ending
        DDLString.append("\n);");

        try {
            Statement stmt = con.createStatement();
            stmt.execute(DDLString.toString());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean insertData(String[] info) {
        String sqlString = "INSERT INTO " + fileName +  "(" + columns + ")" +
                "VALUES (?";

        for (int i = 1; i < info.length; i++) {
            sqlString += ",?";
        }

        sqlString += ")";


        PreparedStatement stmt;
        try{
            stmt = con.prepareStatement(sqlString);

            for (int i = 0; i < info.length; i++) {
                stmt.setString(i + 1, info[i]);
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert data into statement");
            return false;
        }

        try {
            stmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean executeStatement(String sql) {
        try {
            Statement stmt = con.createStatement();

            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void DBConnect() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hold_all", "root", "ThisIsAPassword");
        System.out.println("Connected to DB");
    }

    public void DBDisconnect() throws SQLException {
        con.close();
        System.out.println("Disconnected from DB");
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public static DBConnector getDbConnector() {
        return dbConnector;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
