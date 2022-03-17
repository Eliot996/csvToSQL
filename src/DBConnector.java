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

    public boolean insertData(String[] info) throws SQLException {
        String sqlString = "INSERT INTO " + fileName +  "(" + columns + ")" +
                "VALUES (";

        sqlString += "?";

        for (int i = 1; i < info.length; i++) {
            sqlString += ",?";
        }

        sqlString += ")";


        PreparedStatement stmt = con.prepareStatement(sqlString);

        for (int i = 0; i < info.length; i++) {
            stmt.setString(i + 1, info[i]);
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
