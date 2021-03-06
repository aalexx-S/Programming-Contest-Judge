package Judge.EventHandler;

import Shared.EventHandler.EventHandler;
import Judge.JudgeCore;
import Judge.JudgeSubmissionTask;

import Shared.InfoManager.SubmissionManager;
import Shared.SubmissionInfo;
import org.json.JSONException;
import org.json.JSONObject;

public class SubmissionHandler extends EventHandler {
    public SubmissionHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("submit")) {
                SubmissionManager submissionManager = new SubmissionManager();
                int submissionID = Integer.valueOf(msg.getString("submission_id"));
                String problemID = msg.getString("problem_id");
                String language = msg.getString("language");
                String sourceCode = msg.getString("source_code");
                int submissioniTimeStamp = Integer.valueOf(msg.getString("time_stamp"));
                int testDataTimeStamp = Integer.valueOf(msg.getString("testdata_time_stamp"));

                SubmissionInfo submissionInfo = new SubmissionInfo(submissionID, problemID, language,
                                                        sourceCode, submissioniTimeStamp);

                submissionManager.addEntry(submissionInfo);

                JudgeCore.getInstance().getJudgeTaskProcessor().judge(new JudgeSubmissionTask(submissionID,
                        problemID, language, sourceCode, submissioniTimeStamp, testDataTimeStamp));
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
