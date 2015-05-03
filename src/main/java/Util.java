import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Util {
    public static int getUserId(String username){
        try {
            String sql = "SELECT * FROM users WHERE username=\'" + username + "\'";
            Statement statement = Servlet.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                return Integer.parseInt(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static int addNewUser(String username){
        int id = getRowsNumber("users") + 1;
        String sql = "INSERT INTO users(username, id) VALUES(?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = Servlet.connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public static void sendResponse(HttpServletResponse response, String jsonString) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(jsonString);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Sending response error.");
            e.printStackTrace();
        }
    }


    public static int getRowsNumber(String tableName){
        try {
            Statement maxId = Servlet.connection.createStatement();
            ResultSet resultSet = maxId.executeQuery("SELECT COUNT(*) FROM " + tableName);
            resultSet.next();
            return Integer.parseInt(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
