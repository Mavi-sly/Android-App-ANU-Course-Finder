package com.example.project.model;

import java.io.Serializable;
/**
 * @author u7619947 Xinlong Wu
 */
public enum DeliverMode implements Serializable {
    InPerson {
        @Override
        public String toString() {
            return "In Person";
        }
    },
    OnLine {
        @Override
        public String toString() {
            return "Online";
        }
    },
    OnLineOrInPerson {
        @Override
        public String toString() {
            return "Online or In Person";
        }
    };

    public static DeliverMode getDeliverMode(String str) {
        switch (str){
            case "In Person":
                return InPerson;
            case "Online or In Person":
                return OnLineOrInPerson;
            case "Online":
                return OnLine;
        }
        return null;
    }
}
