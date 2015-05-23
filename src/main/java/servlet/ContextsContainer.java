package servlet;

import requests.UpdateRequest;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;


public class ContextsContainer {
    ArrayList<AsyncContext> contexts = new ArrayList<>();

    public void addContext(AsyncContext context){
        System.out.println("context added");
        System.out.println("Size= " + contexts.size());
        contexts.add(context);
    }

    public void clearContainer(){
        contexts.clear();
    }

    public void executeAllContexts(Connection connection){
        System.out.println("Executing contexts");
        System.out.println("Size= " + contexts.size());
        for (AsyncContext context: contexts){
            UpdateRequest.proceedUpdateRequest((HttpServletRequest)context.getRequest(),
                    (HttpServletResponse)context.getResponse(),
                    connection);
            System.out.println("Update proceeded");
            context.complete();
            System.out.println("Context completed");
        }
        System.out.println("Contexts executed");
    }

}