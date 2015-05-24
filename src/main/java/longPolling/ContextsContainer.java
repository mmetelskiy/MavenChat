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
    private static List<AsyncContext> contexts = Collections.synchronizedList(new ArrayList<AsyncContext>());
    private static List<AsyncContext> tempContexts = Collections.synchronizedList(new ArrayList<AsyncContext>());
    private static boolean contextsExecutingInProcess = false;

    public void addContext(AsyncContext context){
        if(contextsExecutingInProcess)
            tempContexts.add(context);
        else
            contexts.add(context);
        System.out.println("context added");
        System.out.println("C Size= " + contexts.size());
        System.out.println("TC Size= " + tempContexts.size() + "\n");
    }

    public void clearContainer(){
        contexts.clear();
        System.out.println("Cleared. Size= " + contexts.size() + "\n");
        for (AsyncContext ac: tempContexts)
            contexts.add(ac);
        tempContexts.clear();
    }

    public int getSize(){
        return contexts.size();
    }

    public void executeAllContexts(Connection connection){
        contextsExecutingInProcess = true;
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
        contextsExecutingInProcess = false;
    }

}