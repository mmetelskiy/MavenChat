package longPolling;

import requests.UpdateRequest;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ContextsContainer {
    List<AsyncContext> contexts = Collections.synchronizedList(new ArrayList<AsyncContext>());

    public void addContext(AsyncContext context){
        contexts.add(context);
        System.out.println("context added");
        System.out.println("Size= " + contexts.size());
    }

    public void clearContainer(){
        contexts.clear();
    }

    public int getSize(){
        return contexts.size();
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
        System.out.println("Contexts executed \n");
    }

}