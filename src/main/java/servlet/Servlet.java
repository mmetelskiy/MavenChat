package servlet;

import connection.DatabaseConnection;
import database.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import requests.BaseRequest;
import requests.UpdateRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;


public class Servlet extends HttpServlet {
    protected static Connection connection = null;

    public Servlet() {
        connection = DatabaseConnection.setupDBConnection();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessageDeletionsTable.deleteMessage(request, connection);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String type = null;
        try(BufferedReader br = request.getReader()){
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject)parser.parse(br.readLine());
                type = (String)jsonObject.get("type");
                if(type.compareTo("CHANGE_MESSAGE")==0) {
                    int messageId = Integer.parseInt((String) ((JSONObject) jsonObject.get("message")).get("messageId"));
                    String messageText = (String)((JSONObject)jsonObject.get("message")).get("messageText");
                    MessagesTable.changeMessage(connection, messageId, messageText);
                }
                if(type.compareTo("CHANGE_USER_IMAGE")==0) {
                    int id = ((Long)((JSONObject)jsonObject.get("struct")).get("id")).intValue();
                    String url = (String)((JSONObject)jsonObject.get("struct")).get("url");
                    UsernameChangesTable.changeUserImage(id, url, connection);
                    UsersTable.setUserImageUrlById(id, url, connection);
                }
                if(type.compareTo("CHANGE_USERNAME")==0) {
                    int userId = ((Long)((JSONObject)jsonObject.get("user")).get("userId")).intValue();
                    String username = (String)((JSONObject)jsonObject.get("user")).get("username");
                    UsernameChangesTable.changeUsername(userId, username, connection);
                    UsersTable.changeUsernameById(userId, username, connection);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessagesTable.addMessage(request, connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if(type.compareTo("BASE_REQUEST")==0) {
            BaseRequest.proceedBaseRequest(request, response, connection);
        }
        else if(type.compareTo("GET_UPDATE")==0) {
            UpdateRequest.proceedUpdateRequest(request, response, connection);
        }
        else {
            System.out.println("Unsupported type.");
        }
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }
}
