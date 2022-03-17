import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DBConnector {

    private static DBConnector dbConnector = new DBConnector();

    private Connection con;
    private String columns;

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

    /*public boolean insertUser(String user_name, String user_password, String gender, String email, String phone) throws SQLException {
        String sqlString = "INSERT INTO users(user_name, user_password, gender, email, phone)" +
                           "VALUES (?,?,?,?,?)";

        PreparedStatement stmt = con.prepareStatement(sqlString);
        stmt.setString(1, user_name);
        stmt.setString(2, user_password);
        stmt.setString(3, gender);
        stmt.setString(4, email);
        stmt.setString(5, phone);

        return stmt.execute();
    }*/

    public boolean excuteStatment(String sql) throws SQLException {
        Statement stmt = con.createStatement();
        return stmt.execute(sql);
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
}
