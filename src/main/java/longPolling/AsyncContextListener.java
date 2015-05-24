package longPolling;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;


public class AsyncContextListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        System.out.println("Async complete");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        System.out.println("Timed out...");
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        System.out.println("Error...");
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        System.out.println("Starting async...");
    }
}
