package Controller;

import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ynchsung on 12/27/15.
 */
public class Scheduler extends Thread {
    private ScheduleStrategy strategy;
    private BlockingQueue<JSONObject> submissionQueue;

    public Scheduler() {
        this.strategy = null;
        this.submissionQueue = new LinkedBlockingQueue<JSONObject>();
    }

    public void setStrategy(ScheduleStrategy strategy) {
        this.strategy = strategy;
    }

    public void add(JSONObject submit) throws InterruptedException {
        this.submissionQueue.put(submit);
    }

    public void run() {
        if (this.strategy == null)
            return;
        this.submissionQueue.clear();
        try {
            while (true) {
                JSONObject submit = this.submissionQueue.take();
                Judge judge = this.strategy.schedule(submit);
                judge.send(submit);
            }
        }
        catch (InterruptedException e) {
        }
    }
}
