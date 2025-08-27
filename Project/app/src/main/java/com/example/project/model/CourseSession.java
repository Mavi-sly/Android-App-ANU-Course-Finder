package com.example.project.model;

import java.io.Serializable;
import java.util.Arrays;
/**
 * @author u7619947 Xinlong Wu
 */
public class CourseSession implements Serializable {
    /**
     * session[0]: Spring Session
     * session[1]: Summer Session
     * session[2]: Autumn Session
     * session[3]: Winter Session
     * session[4]: First Semester
     * session[5]: Second Semester
     */
    boolean[] session = new boolean[6];

    public CourseSession() {
        Arrays.fill(session, false);
    }

    public void setSession(String str) {
        switch (str){
            case "Spring Session":
                session[0] = true;
                break;
            case "Summer Session":
                session[1] = true;
                break;
            case "Autumn Session":
                session[2] = true;
                break;
            case "Winter Session":
                session[3] = true;
                break;
            case "First Semester":
                session[4] = true;
                break;
            case "Second Semester":
                session[5] = true;
                break;
        }
    }

    public boolean isSprintSession(){
        return session[0];
    }

    public boolean isSummerSession(){
        return session[1];
    }

    public boolean isAutumnSession(){
        return session[2];
    }

    public boolean isWinterSession(){
        return session[3];
    }

    public boolean isFirstSemester(){
        return session[4];
    }

    public boolean isSecondSemester(){
        return session[5];
    }

    public static CourseSession getCourseSession(String str) {
        String[] sessions = str.split("/");
        CourseSession coueseSession = new CourseSession();
        for (String session: sessions) {
            coueseSession.setSession(session);
        }
        return coueseSession;
    }

    /**
     * @author u6393399 Xuan Liu
     * @return a formatted string representation of sessions.
     */
    public String getAllSessionsAsString() {
        StringBuilder sessionsStringBuilder = new StringBuilder();

        if (session[0]) {
            sessionsStringBuilder.append("Spring Session, ");
        }
        if (session[1]) {
            sessionsStringBuilder.append("Summer Session, ");
        }
        if (session[2]) {
            sessionsStringBuilder.append("Autumn Session, ");
        }
        if (session[3]) {
            sessionsStringBuilder.append("Winter Session, ");
        }
        if (session[4]) {
            sessionsStringBuilder.append("First Semester, ");
        }
        if (session[5]) {
            sessionsStringBuilder.append("Second Semester, ");
        }

        // Remove the trailing space
        if (sessionsStringBuilder.length() > 0) {
            sessionsStringBuilder.setLength(sessionsStringBuilder.length() - 2);
        }

        return sessionsStringBuilder.toString();
    }
}
