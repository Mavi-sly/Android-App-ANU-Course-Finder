package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
public class SubExp extends Exp{
    private String  subject;

    public SubExp(String subject) {
        this.subject=subject;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String show() {
        return subject;
    }

}
