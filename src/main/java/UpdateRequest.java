import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateRequest {
    Connection connection = null;

    public static void proceedUpdateRequest(HttpServletRequest request, HttpServletResponse response){
        int messageId = Integer.parseInt(request.getParameter("messageToken"));
        int messageEditId = Integer.parseInt(request.getParameter("messageEditToken"));
        int messageDeletedId = Integer.parseInt(request.getParameter("messageDeleteToken"));
        int userId = Integer.parseInt(request.getParameter("userToken"));
        int userChangeId = Integer.parseInt(request.getParameter("userChangeToken"));

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("messageToken", Util.getRowsNumber("messages"));
        jsonObject.put("messages", getMessages(messageId));

        jsonObject.put("messageEditToken", Util.getRowsNumber("message_changes"));
        jsonObject.put("editedMessages", getEditedMessages(messageEditId));

        jsonObject.put("messageDeleteToken", Util.getRowsNumber("message_deletions"));
        jsonObject.put("deletedMessagesIds", getDeletedMessages(messageDeletedId));

        jsonObject.put("userToken", Util.getRowsNumber("users"));
        jsonObject.put("users", getUsers(userId));

        jsonObject.put("userChangeToken", Util.getRowsNumber("username_changes"));
        jsonObject.put("changedUsers", getChangedUsers(userChangeId));

        Util.sendResponse(response, jsonObject.toJSONString());
    }


    public static String getMessages(int messageId){
        String sql = "SELECT * from messages WHERE message_id > " + messageId;
        try {
            Statement statement = Servlet.connection.createStatement();
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


    public static String getEditedMessages(int messageEditId){
        String sql = "SELECT * from message_changes WHERE id > " + messageEditId;
        try {
            Statement statement = Servlet.connection.createStatement();
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


    public static String getDeletedMessages(int messageDeletedId){
        String sql = "SELECT * from message_deletions WHERE id > " + messageDeletedId;
        try {
            Statement statement = Servlet.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray jsonArray = new JSONArray();
            while(resultSet.next()) {
                jsonArray.add(resultSet.getString("message_id"));
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getUsers(int userId){
        String sql = "SELECT * from users WHERE id > " + userId;
        try {
            Statement statement = Servlet.connection.createStatement();
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


    public static String getChangedUsers(int userChangeId){
        String sql = "SELECT * from username_changes WHERE id > " + userChangeId;
        try {
            Statement statement = Servlet.connection.createStatement();
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
}
