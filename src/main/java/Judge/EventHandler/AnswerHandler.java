package Judge.EventHandler;

import Judge.InfoManager.QAManager;
import Judge.InfoManager.AnswerInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswerHandler extends EventHandler {
    public AnswerHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("answer")) {
                QAManager qaManager = new QAManager();
                int questionID = Integer.valueOf(msg.getString("question_id"));
                String content = msg.getString("answer");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                qaManager.addAnswer(questionID, new AnswerInfo(content, timeStamp));
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
