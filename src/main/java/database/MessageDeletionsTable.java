package database;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;


public class MessageDeletionsTable {
    public static String getDeletedMessages(int messageDeletedId, Connection connection){
        String sql = "SELECT * from message_deletions WHERE id > " + messageDeletedId;
        try {
            Statement statement = connection.createStatement();
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


    public static void deleteMessage(HttpServletRequest request, Connection connection) {
        try(BufferedReader br = request.getReader()) {
            JSONParser parser = new JSONParser();
            try {
                JSONArray jsonArray = (JSONArray)parser.parse(br.readLine());
                for (Object s: jsonArray){
                    int newId = Util.getRowsNumber("message_deletions", connection) + 1;
                    String sql = "INSERT INTO message_deletions (id, message_id) VALUES (?, ?)";
                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, newId);
                        preparedStatement.setInt(2, Integer.parseInt((String)s));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String markAsDeleted = "UPDATE messages SET is_deleted='" + 1 + "' WHERE message_id=" + Integer.parseInt((String)s);
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        statement.executeUpdate(markAsDeleted);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
