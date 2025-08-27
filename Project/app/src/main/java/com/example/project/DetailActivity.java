package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;

import com.example.project.model.Career;
import com.example.project.model.Course;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Manages the course details displayed in the detail view.
 * @author u6393399 Xuan Liu
 */
public class DetailActivity extends AppCompatActivity {
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Course course = null;
        Bundle bundle=getIntent().getExtras();

        course=(Course)bundle.getSerializable("newcourse");

        Button courseCodeButton = findViewById(R.id.CourseCode);
        TextView courseCodeText = findViewById(R.id.CourseCodeText);
        TextView courseNameText = findViewById(R.id.CourseName);
        TextView courseDeliverModeText = findViewById(R.id.DeliverMode);
        TextView courseCollegeText = findViewById(R.id.CourseCollege);
        TextView courseOfferedByText = findViewById(R.id.OfferedBy);
        TextView courseUnitText = findViewById(R.id.CourseUnit);
        TextView courseSessionText = findViewById(R.id.CourseSession);
        TextView courseSubjectText = findViewById(R.id.CourseSubject);
        TextView courseCareerText = findViewById(R.id.CourseCareer);
        TextView courseConvenersText = findViewById(R.id.CourseConveners);

        courseCodeButton.setText(course.getCode().substring(0, 4)+"\n"+course.getCode().substring(4));
        courseCodeText.setText(course.getCode());
        courseNameText.setText(course.getName());
        courseDeliverModeText.setText(course.getDeliverMode().toString());
        courseCollegeText.setText(course.getCollege());
        courseOfferedByText.setText(course.getOfferedBy());
        courseUnitText.setText(String.valueOf(course.getUnit()));
        courseSessionText.setText(course.getSession().getAllSessionsAsString());
        courseSubjectText.setText(course.getSubject());
        courseCareerText.setText(course.getCareer() == Career.NONE?"":course.getCareer().toString());
        courseConvenersText.setText(course.getAllConvenersAsString());
    }
}