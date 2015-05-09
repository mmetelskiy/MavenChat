package requests;

import database.*;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class UpdateRequest {
    public static void proceedUpdateRequest(HttpServletRequest request, HttpServletResponse response,
                                            Connection connection){
        int messageId = Integer.parseInt(request.getParameter("messageToken"));
        int messageEditId = Integer.parseInt(request.getParameter("messageEditToken"));
        int messageDeletedId = Integer.parseInt(request.getParameter("messageDeleteToken"));
        int userId = Integer.parseInt(request.getParameter("userToken"));
        int userChangeId = Integer.parseInt(request.getParameter("userChangeToken"));

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("messageToken", Util.getRowsNumber("messages", connection));
        jsonObject.put("messages", MessagesTable.getMessages(messageId, connection));

        jsonObject.put("messageEditToken", Util.getRowsNumber("message_changes", connection));
        jsonObject.put("editedMessages", MessageChangesTable.getEditedMessages(messageEditId, connection));

        jsonObject.put("messageDeleteToken", Util.getRowsNumber("message_deletions", connection));
        jsonObject.put("deletedMessagesIds", MessageDeletionsTable.getDeletedMessages(messageDeletedId, connection));

        jsonObject.put("userToken", Util.getRowsNumber("users", connection));
        jsonObject.put("users", UsersTable.getUsers(userId, connection));

        jsonObject.put("userChangeToken", Util.getRowsNumber("username_changes", connection));
        jsonObject.put("changedUsers", UsernameChangesTable.getChangedUsers(userChangeId, connection));

        Util.sendResponse(response, jsonObject.toJSONString());
    }
}
