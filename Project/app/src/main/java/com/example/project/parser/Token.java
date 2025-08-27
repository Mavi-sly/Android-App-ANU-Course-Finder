package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
import java.util.Objects;

/**
 * name contains: abc, &,(,),,,:,123,;,-
 * Token class to save extracted token from tokenizer.
 * Each token has its surface form saved in {@code token}
 * and type saved in {@code type} which is one of the predefined type in Type enum.
 * The following are the different types of tokens:
 * INT:     integer
 * SEP:     |
 * EQUAL:   =
 * CODE:    String "code"
 * CODESUB: Four letters  eg."comp"
 * CODENUM: four numbers,might add a letter(A/B). eg. 6442 1007B
 * NAME:    "name"
 * SUBJECT: "subject"
 * VALUE:   eg. "Software Design Methodologies" "Computer Science"
 */
public class Token {
    public enum Type {SEP,CODE, NAME, SUBJECT, CODESUBNUM, SUBVALUE, EQUAL, CODESUB, CODENUM, VALUE, NAMEVALUE}
    //eg.              |  code= name= subject=  COMP6442    "computer science""software construction"

    /**
     * The following exception should be thrown if a tokenizer attempts to tokenize something that is not of one
     * of the types of tokens.
     */
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        if (type==Type.CODESUBNUM){
            return "CODESUBNUM(" + token + ")";
        } else if(type==Type.CODE){
            return "CODE(" + token + ")";
        } else if(type==Type.NAME){
            return "NAME(" + token + ")";
        } else if(type==Type.SUBJECT){
            return "SUBJECT(" + token + ")";
        } else if(type==Type.SUBVALUE){
            return "SUBVALUE(" + token + ")";
        }else if(type==Type.NAMEVALUE){
            return "SUBVALUE(" + token + ")";
        } else    return type + "";
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true; // Same hashcode.
        if (!(other instanceof Token)) return false; // Null or not the same type.
        return this.type == ((Token) other).getType() && this.token.equals(((Token) other).getToken()); // Values are the same.
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, type);
    }
}

