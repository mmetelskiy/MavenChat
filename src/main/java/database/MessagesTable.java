package database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;


public class MessagesTable {
    public static String getMessages(int messageId, Connection connection){
        String sql = "SELECT * from messages WHERE message_id > " + messageId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put("userId", resultSet.getString("user_id"));
                tempObject.put("messageId", resultSet.getString("message_id"));
                tempObject.put("messageText", resultSet.getString("message_text"));
                tempObject.put("messageTime", resultSet.getString("message_time"));
                tempObject.put("isDeleted", resultSet.getString("is_deleted"));
                jsonArray.add(tempObject);
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void changeMessageText(int messageId, String messageText, Connection connection) {
        String sql = "UPDATE messages SET message_text='" + messageText + "' WHERE message_id=" + messageId;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addMessage(HttpServletRequest request, Connection connection) {
        int userId = 0;
        String messageText = null;
        try(BufferedReader br = request.getReader()){
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject)parser.parse(br.readLine());
                userId = ((Long)jsonObject.get("userId")).intValue();
                messageText = (String)jsonObject.get("messageText");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO messages (message_text, username, message_time, message_id, is_deleted, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setString(2, UsersTable.getUsernameById(userId, connection));
            preparedStatement.setString(3, ((Long)System.currentTimeMillis()).toString());
            preparedStatement.setInt(4, Util.getRowsNumber("messages", connection) + 1);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void changeMessage(HttpServletRequest request, Connection connection, BufferedReader br) throws IOException {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject)parser.parse(br.readLine());
            int messageId = Integer.parseInt((String)((JSONObject)jsonObject.get("message")).get("messageId"));
            String messageText = (String)((JSONObject)jsonObject.get("message")).get("messageText");
            changeMessageText(messageId, messageText, connection);
            //weak moment
            MessageChangesTable.addMessageChange(messageId, messageText, connection);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
