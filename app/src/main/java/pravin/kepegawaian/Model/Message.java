package pravin.kepegawaian.Model;

import java.util.List;

public class Message {

    String code,message;

    List<User> user;
    List<Reason> reason;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<User> getUser() {
        return user;
    }


    public List<Reason> getReason() {
        return reason;
    }
}

