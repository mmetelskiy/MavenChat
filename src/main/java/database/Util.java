package database;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Util {
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


    public static int getRowsNumber(String tableName, Connection connection){
        try {
            Statement maxId = connection.createStatement();
            ResultSet resultSet = maxId.executeQuery("SELECT COUNT(*) FROM " + tableName);
            resultSet.next();
            return Integer.parseInt(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
