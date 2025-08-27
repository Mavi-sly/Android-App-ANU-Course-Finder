package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
public class NameExp extends Exp{
    private String  coursename;

    public String getCoursename() {
        return coursename;
    }

    public NameExp(String coursename) {
        this.coursename = coursename;
    }
    @Override
    public String show() {
        return coursename;
    }

}
