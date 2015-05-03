import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;


public class Servlet extends HttpServlet {
    protected static Connection connection = null;

    public Servlet() {
        connection = DatabaseConnection.setupDBConnection();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        deleteMessage(request, connection);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BufferedReader br = request.getReader();
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject)parser.parse(br.readLine());
                int messageId = Integer.parseInt((String)((JSONObject)jsonObject.get("message")).get("messageId"));
                String messageText = (String)((JSONObject)jsonObject.get("message")).get("messageText");
                changeMessage(messageId, messageText);
                addMessageChange(messageId, messageText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addMessage(request);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");

        if(type.compareTo("BASE_REQUEST")==0) {
            BaseRequest.proceedBaseRequest(request, response, connection);
        }
        else if(type.compareTo("GET_UPDATE")==0) {
            UpdateRequest.proceedUpdateRequest(request, response);
        }
        else {
            System.out.println("Unsupported type.");
        }

        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }


    public void addMessage(HttpServletRequest request) {
        int userId = 0;
        String messageText = null;
        try {
            BufferedReader br = request.getReader();
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject)parser.parse(br.readLine());
                userId = ((Long)jsonObject.get("userId")).intValue();
                messageText = (String)jsonObject.get("messageText");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO messages (message_text, username, message_time, message_id, is_deleted, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setString(2, getUsernameById(userId));
            preparedStatement.setString(3, ((Long) System.currentTimeMillis()).toString());
            preparedStatement.setInt(4, Util.getRowsNumber("messages")+1);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getUsernameById(int userId){
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


    public void deleteMessage(HttpServletRequest request, Connection connection) {
        try {
            BufferedReader br = request.getReader();
            JSONParser parser = new JSONParser();
            try {
                JSONArray jsonArray = (JSONArray)parser.parse(br.readLine());
                for (Object s: jsonArray){
                    int newId = Util.getRowsNumber("message_deletions") + 1;
                    String sql = "INSERT INTO message_deletions (id, message_id) VALUES (?, ?)";
                    String markAsDeleted = "UPDATE messages SET is_deleted='" + 1 + "' WHERE message_id=" + Integer.parseInt((String)s);
                    PreparedStatement preparedStatement = null;
                    Statement delete = null;
                    try {
                        delete = connection.createStatement();
                        delete.executeQuery(markAsDeleted);
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, newId);
                        preparedStatement.setInt(2, Integer.parseInt((String)s));
                        //System.out.println(Integer.parseInt((String) s));
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeMessage(int messageId, String messageText) {
        String sql = "UPDATE messages SET message_text='" + messageText + "' WHERE message_id=" + messageId;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addMessageChange(int messageId, String messageText) {
        String sql = "INSERT INTO message_changes (message_text, message_id, id)" + "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);
            preparedStatement.setInt(3, Util.getRowsNumber("message_changes")+1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*public void insertMessage(HttpServletRequest request, Connection connection, int newId) {
        String message = request.getParameter("message");
        String creator = request.getParameter("creator");
        String time = request.getParameter("time");

        String sql = "INSERT INTO messages (message_text, username, message_time, message_id) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message);
            preparedStatement.setString(2, creator);
            preparedStatement.setString(3, time);
            preparedStatement.setInt(4, newId);
            preparedStatement.executeUpdate();
            System.out.println("Insert done.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void sendMessageId(HttpServletResponse response, Connection connection, int newId){
        sendResponse(response, "{\"id\": " + newId + "}");
    }

*/

/*




    public void giveAllMessages(Connection connection, HttpServletResponse response) {
        String jsonString = DBToJsonString(connection);
        sendResponse(response, jsonString);
    }


    public String DBToJsonString(Connection connection) {
        try {
            Statement selectAll = connection.createStatement();
            ResultSet resultSet = selectAll.executeQuery("SELECT * from messages");
            JSONArray ja = new JSONArray();

            JSONObject jobj = new JSONObject();
            jobj.put("lastChangeId", getLastChangeId(connection));
            ja.add(jobj);
            while(resultSet.next()) {
                JSONObject jo = new JSONObject();
                jo.put("message", resultSet.getString("message"));
                jo.put("creator", resultSet.getString("creator"));
                jo.put("time", resultSet.getString("time"));
                jo.put("id", resultSet.getString("id"));
                ja.add(jo);
            }
            return ja.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getMessageId(Connection connection) {
        try {
            Statement maxId = connection.createStatement();
            ResultSet resultSet = maxId.executeQuery("SELECT MAX(message_id) FROM messages");
            resultSet.next();
            int newId = Integer.parseInt(resultSet.getString(1)) + 1;
            return newId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getLastChangeId(Connection connection) {
        try {
            Statement maxId = connection.createStatement();
            ResultSet resultSet = maxId.executeQuery("SELECT MAX(id) FROM message_changes");
            resultSet.next();
            int newId = Integer.parseInt(resultSet.getString(1));
            return newId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void changeMessage(HttpServletRequest request, Connection connection) {
        int idToChange = Integer.parseInt(request.getParameter("id"));
        String newMessage = request.getParameter("newMessage");
        String newTime = request.getParameter("newTime");
        String creator = request.getParameter("creator");
        String sql = "UPDATE messages SET message_text='" + newMessage + "',message_time='" + newTime + "' WHERE message_id=" + idToChange;
        System.out.println(sql);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String lastChangesInJson(HttpServletRequest request, Connection connection){
        int lastChangeId = Integer.parseInt(request.getParameter("lastChangeId"));
        int newlastChangeId = getLastChangeId(connection);
        try {
            Statement selectAll = connection.createStatement();
            ResultSet resultSet = selectAll.executeQuery("SELECT * from message_changes WHERE id>"+lastChangeId);
            JSONArray ja = new JSONArray();

            JSONObject jobj = new JSONObject();
            jobj.put("lastChangeId", newlastChangeId);
            ja.add(jobj);
            while(resultSet.next()) {
                JSONObject jo = new JSONObject();
                jo.put("type", resultSet.getString("type"));
                jo.put("message", resultSet.getString("message"));
                jo.put("creator", resultSet.getString("creator"));
                jo.put("time", resultSet.getString("time"));
                jo.put("id", resultSet.getString("messageId"));
                ja.add(jo);
            }
            return ja.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public  void sendLastChanges(HttpServletRequest request, HttpServletResponse response, Connection connection) {
        String jsonObject = lastChangesInJson(request, connection);
        sendResponse(response, jsonObject);
    }





    public void addInsertChange(HttpServletRequest request, Connection connection, int newId) {
        int id = getLastChangeId(connection)+1;
        String message = request.getParameter("message");
        String creator = request.getParameter("creator");
        String time = request.getParameter("time");
        String sql = "INSERT INTO message_changes (type, message_text, creator, time, message_id, id)" + "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "INSERT_MESSAGE");
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, creator);
            preparedStatement.setString(4, time);
            preparedStatement.setInt(5, newId);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
            System.out.println("Insert into changes done.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
