package TanksAttack;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

    private static Statement connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tanks",
                "postgres", "Admin");

        return connection.createStatement();
    }

    public static void addUser(String login, String password, String email) throws ClassNotFoundException,
            SQLException {

        String sqlQuery = String.format("INSERT INTO Users(login,userpassword,email,points) VALUES ('%s', '%s', " +
                "'%s', 0)", login, password, email);

        Statement statement = connect();
        statement.executeUpdate(sqlQuery);
    }

    public static ResultSet returnUsernames() throws SQLException, ClassNotFoundException {
        Statement statement = connect();
        return statement.executeQuery("SELECT Login from Users");
    }

    public static boolean LogIn(String login, String password) throws SQLException, ClassNotFoundException {

        Statement statement = connect();
        ResultSet rs = statement.executeQuery(String.format("SELECT login FROM Users WHERE login = '%s'", login));
        if (rs.next() && rs.getString("Login").equals(login)) {
            rs = statement.executeQuery(String.format("SELECT UserPassword FROM Users WHERE login = '%s' AND " +
                    "UserPassword = '%s'", login, password));
            return rs.next() && rs.getString("UserPassword").equals(password);
        }
        return false;
    }

    public static int getPointsFromDB(String login) throws SQLException, ClassNotFoundException {
        Statement statement = connect();
        ResultSet rs = statement.executeQuery(String.format("SELECT Points FROM Users WHERE login = '%s'", login));
        if (rs.next())
            return Integer.parseInt(rs.getString("Points"));
        return 0;
    }

    public static void saveToDB() throws SQLException, ClassNotFoundException {
        Statement statement = connect();
        statement.executeUpdate(String.format("UPDATE Users SET points = %d WHERE login = '%s'", PlayerData.points,
                PlayerData.login));
    }

    public static ArrayList getTenBestPlayers() throws SQLException, ClassNotFoundException {
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<String> points = new ArrayList<>();
        ArrayList<ArrayList> list = new ArrayList<>();
        Statement statement = connect();
        ResultSet rs = statement.executeQuery("SELECT login,points From Users ORDER BY points DESC");
        while(rs.next()){
            nicknames.add(rs.getString("login"));
            points.add(rs.getString("points"));
        }
        list.add(nicknames);
        list.add(points);
        return list;
    }
}
