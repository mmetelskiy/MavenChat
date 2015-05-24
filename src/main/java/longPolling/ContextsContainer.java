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
    private static List<AsyncContext> contexts = Collections.synchronizedList(new ArrayList<>());
    private static List<AsyncContext> tempContexts = Collections.synchronizedList(new ArrayList<>());
    private static boolean contextsExecutingInProcess = false;


    public void addContext(AsyncContext context){
        if(contextsExecutingInProcess)
            tempContexts.add(context);
        else
            contexts.add(context);
    }


    public void clearContainer(){
        contexts.clear();
        for (AsyncContext ac: tempContexts)
            contexts.add(ac);
        tempContexts.clear();
    }


    public void executeAllContexts(Connection connection){
        contextsExecutingInProcess = true;
        for (AsyncContext context: contexts){
            UpdateRequest.proceedUpdateRequest((HttpServletRequest)context.getRequest(),
                    (HttpServletResponse)context.getResponse(),
                    connection);
            context.complete();
        }
        contextsExecutingInProcess = false;
    }
}