package com.example.project.TFIDF;

import java.util.ArrayList;


/**
 * @author u7619947 Xinlong Wu
 */
public class Document {
    String[] terms;

    public Document(){}
    public Document(String str){
        setTerms(str);
    }

    public int size(){
        return terms.length;
    }

    public void setTerms(String str){
        this.terms = str.split(" ");
    }

    public String[] getTerms(){
        return terms;
    }
}
