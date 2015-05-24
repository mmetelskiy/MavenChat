package servlet;

import connection.DatabaseConnection;
import database.MessageDeletionsTable;
import database.MessagesTable;
import database.UsernameChangesTable;
import database.UsersTable;
import longPolling.AsyncContextListener;
import longPolling.ContextsContainer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import requests.BaseRequest;
import requests.UpdateRequest;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"}, asyncSupported = true)
public class Servlet extends HttpServlet {
    protected static Connection connection = null;
    protected static ContextsContainer contextContainer = null;

    public Servlet() {
        connection = DatabaseConnection.setupDBConnection();
        contextContainer = new ContextsContainer();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessageDeletionsTable.deleteMessage(request, connection);
        contextContainer.executeAllContexts(connection);
        contextContainer.clearContainer();
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
                    contextContainer.executeAllContexts(connection);
                    contextContainer.clearContainer();
                }
                if(type.compareTo("CHANGE_USER_IMAGE")==0) {
                    int id = ((Long)((JSONObject)jsonObject.get("struct")).get("id")).intValue();
                    String url = (String)((JSONObject)jsonObject.get("struct")).get("url");
                    UsernameChangesTable.changeUserImage(id, url, connection);
                    UsersTable.setUserImageUrlById(id, url, connection);
                    contextContainer.executeAllContexts(connection);
                    contextContainer.clearContainer();
                }
                if(type.compareTo("CHANGE_USERNAME")==0) {
                    int userId = ((Long)((JSONObject)jsonObject.get("user")).get("userId")).intValue();
                    String username = (String)((JSONObject)jsonObject.get("user")).get("username");
                    UsernameChangesTable.changeUsername(userId, username, connection);
                    UsersTable.changeUsernameById(userId, username, connection);
                    contextContainer.executeAllContexts(connection);
                    contextContainer.clearContainer();
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
        contextContainer.executeAllContexts(connection);
        contextContainer.clearContainer();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if(type.compareTo("BASE_REQUEST") == 0) {
            BaseRequest.proceedBaseRequest(request, response, connection);
        }
        else if(type.compareTo("GET_UPDATE")==0) {
            if(UpdateRequest.hasNullTokens(request)) {
                UpdateRequest.proceedUpdateRequest(request, response, connection);
            }
            else {
                AsyncContext ac = request.startAsync(request, response);
                ac.addListener(new AsyncContextListener());
                ac.setTimeout(3600000);
                contextContainer.addContext(ac);
            }
        }
        else {
            System.out.println("Unsupported type.");
        }
    }
}


