package database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.*;


public class UsersTable {
    public static int getUserId(String username, Connection connection){
        try {
            String sql = "SELECT * FROM users WHERE username=\'" + username + "\'";
            Statement statement = connection.createStatement();
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


    public static int addNewUser(String username, Connection connection){
        int id = Util.getRowsNumber("users", connection) + 1;
        String sql = "INSERT INTO users(username, id) VALUES(?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public static String getUsers(int userId, Connection connection){
        String sql = "SELECT * from users WHERE id > " + userId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put("userId", resultSet.getString("id"));
                tempObject.put("username", resultSet.getString("username"));
                tempObject.put("userImage", "icon/doge.jpg");
                jsonArray.add(tempObject);
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getUsernameById(int userId, Connection connection){
        try {
            String sql = "SELECT * FROM users WHERE id=\'" + userId + "\'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.isBeforeFirst()){
                resultSet.next();
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void changeUsernameById(int id, String username, Connection connection) {
        String sql = "UPDATE users SET username='" + username + "' WHERE id=" + id;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
