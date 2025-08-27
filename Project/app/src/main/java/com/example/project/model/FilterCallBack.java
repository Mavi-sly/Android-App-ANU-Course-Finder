package com.example.project.model;

import com.example.project.model.Course;

import java.util.ArrayList;
/**
 * @author u7619947 Xinlong Wu
 */
public interface FilterCallBack {
    public boolean isMatch(Course course);
}
