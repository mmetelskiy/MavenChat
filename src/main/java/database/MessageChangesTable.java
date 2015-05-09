package database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.*;


public class MessageChangesTable {
    public static String getEditedMessages(int messageEditId, Connection connection){
        String sql = "SELECT * from message_changes WHERE id > " + messageEditId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put("messageId", resultSet.getString("message_id"));
                tempObject.put("messageText", resultSet.getString("message_text"));
                jsonArray.add(tempObject);
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void addMessageChange(int messageId, String messageText, Connection connection) {
        String sql = "INSERT INTO message_changes (message_text, message_id, id)" + "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);
            preparedStatement.setInt(3, Util.getRowsNumber("message_changes", connection)+1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
