package com.example.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project.model.Course;

import java.util.ArrayList;

/**
 * @author u6393399 Xuan Liu
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    private final Context context;
    private final ArrayList<Course> courses;

    public CourseAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
        this.context = context;
        this.courses = courses;
    }

    // Overridden method for obtaining and displaying views
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        Course course = courses.get(position);
        if (course != null){
            if (course.getName()!= null) {
                textView.setText(course.getCode()+"  "+course.getName());
            } else {
                textView.setText(course.getCode());
            }
        }
        return convertView;
    }
}