package com.example.project;

import static com.example.project.BPlusTree.BPlusFactory.getBPlusTree;
import static com.example.project.avl.AVLFactory.getCourseAVLTree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.BPlusTree.BPlusFactory;
import com.example.project.TFIDF.Document;
import com.example.project.TFIDF.TFIDFFactory;
import com.example.project.adapters.CourseAdapter;
import com.example.project.avl.Tree;
import com.example.project.model.Career;
import com.example.project.model.Course;
import com.example.project.model.FilterCallBack;
import com.example.project.parser.Token;
import com.example.project.parser.Tokenizer;
import com.example.project.parser.Parser;
import com.example.project.parser.Exp;
import com.example.project.parser.NameExp;
import com.example.project.parser.AndExp;
import com.example.project.parser.SubExp;
import com.example.project.parser.CodeExp;
import com.example.project.utils.DataLoader;
import com.example.project.utils.HistoryManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Manages the main searching activity of the application.
 * @author u6393399 Xuan Liu, u7580335 Shiying Cai, u7619947 Xinlong Wu, u7726399 Meitong Liu
 */
public class MainActivity extends AppCompatActivity {
    private TextView searchText;
    private EditText editText;
    private ImageButton button;
    private Button btnToggleFilter, btnHistory1, btnHistory2, btnHistory3, btnApply;
    private CheckBox cbUndergraduate, cbPostgraduate, cbSummer, cbAutumn, cbWinter, cbFirstSem, cbSecondSem, cbSpring;
    private ArrayList<Course> courseList;
    private ArrayList<Course> courseResult;

    CourseAdapter courseAdapter;

    private Intent intent;

    List<String> histories;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnApply = findViewById(R.id.btnApply);
        cbUndergraduate = findViewById(R.id.cbUndergraduate);
        cbPostgraduate = findViewById(R.id.cbPostgraduate);
        cbSummer = findViewById(R.id.cbSummer);
        cbAutumn = findViewById(R.id.cbAutumn);
        cbWinter = findViewById(R.id.cbWinter);
        cbFirstSem = findViewById(R.id.cbFirstSem);
        cbSecondSem = findViewById(R.id.cbSecondSem);
        cbSpring = findViewById(R.id.cbSpring);

        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(this, courseList);

        Intent fromHomeIntent = getIntent();
        String searchedCourse = fromHomeIntent.getStringExtra("Course");
        String username = fromHomeIntent.getStringExtra("Username");
        HistoryManager historyManager = new HistoryManager(this);
        historyManager.updateHistory(username, searchedCourse);
        histories = historyManager.getHistory(username);
        Collections.reverse(histories);

        // Show the first search on home page
        courseResult = doSearch(searchedCourse);
        if (!courseResult.isEmpty()) {
            courseList.addAll(courseResult);
            courseAdapter.notifyDataSetChanged();
        }
//        else {
//            Toast.makeText(MainActivity.this, "The course does not exist.", Toast.LENGTH_SHORT).show();
//        }
        // End of show of first search on home page

        // Show "Search Results for 'xxxx'"
        searchText = findViewById(R.id.tvSearchResult);
        searchText.setText("Search results for '" + searchedCourse + "'");

        editText = findViewById(R.id.MainSearch);
        button = findViewById(R.id.MainSearchBtn);
        intent = new Intent(this, DetailActivity.class);

        ListView listView = findViewById(R.id.CourseList);
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Course clickedCourse = courseList.get(position);
            Bundle bundle=new Bundle();
            bundle.putSerializable("newcourse",clickedCourse);
            intent.putExtras(bundle);
            //intent.putExtra("Course", clickedCourse);
            startActivity(intent);
        });

        button.setOnClickListener(v -> {
            String course = editText.getText().toString();
            historyManager.updateHistory(username, course);
            histories = historyManager.getHistory(username);
            Log.d("MainHistoryManager", "Histories: " + histories.toString());
            // *******Add tokenizer parsing******
            searchText.setText("Search results for '" + course + "'");
            courseResult = doSearch(course);
            if (courseResult != null) {
                refreshCourseList(courseResult);
            }
//            else {
//                Toast.makeText(MainActivity.this, "The course does not exist.", Toast.LENGTH_SHORT).show();
//            }
            editText.setText("");

            cbAutumn.setChecked(false);
            cbPostgraduate.setChecked(false);
            cbFirstSem.setChecked(false);
            cbSpring.setChecked(false);
            cbSummer.setChecked(false);
            cbSecondSem.setChecked(false);
            cbUndergraduate.setChecked(false);
            cbWinter.setChecked(false);
        });

        // Show Recently Searched
        btnHistory1 = findViewById(R.id.mainHistoryBtn1);
        btnHistory2 = findViewById(R.id.mainHistoryBtn2);
        btnHistory3 = findViewById(R.id.mainHistoryBtn3);

        List<String> displayedHistories = new ArrayList<>();
        setupHistoryButton(btnHistory1, 0, displayedHistories);
        setupHistoryButton(btnHistory2, 1, displayedHistories);
        setupHistoryButton(btnHistory3, 2, displayedHistories);

        final LinearLayout filterBox = findViewById(R.id.FilterBox);
        btnToggleFilter = findViewById(R.id.btnToggleFilter);

        // Show "REFINE xxx RESULTS:"
        int numResult = courseList.size();
        btnToggleFilter.setText("REFINE " + numResult + " RESULTS:");

        /**
         * Filter box controller
         */
        btnToggleFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use TransitionManager to animate visibility change
                TransitionManager.beginDelayedTransition((ViewGroup) filterBox.getParent());
                // Toggle visibility of the FilterBox
                if (filterBox.getVisibility() == View.VISIBLE) {
                    filterBox.setVisibility(View.GONE);
                } else {
                    filterBox.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         * Implementation of filtering feature
         */
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<Course> careerResult = new HashSet<>();
                if (cbUndergraduate.isChecked() || cbPostgraduate.isChecked()){
                    if (cbUndergraduate.isChecked())
                        careerResult.addAll(doFilterBy(courseResult, course -> course.getCareer() == Career.UGRD));
                    if (cbPostgraduate.isChecked())
                        careerResult.addAll(doFilterBy(courseResult, course -> course.getCareer() == Career.PGRD));
                }
                else
                    careerResult.addAll(courseResult);

                if (cbSummer.isChecked() || cbAutumn.isChecked() ||
                        cbWinter.isChecked() || cbFirstSem.isChecked() ||
                        cbSecondSem.isChecked() || cbSpring.isChecked()){
                    HashSet<Course> res = new HashSet<>();
                    if (cbSummer.isChecked())
                        res.addAll(doFilterBy(careerResult, course -> course.getSession().isSummerSession()));
                    if (cbAutumn.isChecked())
                        res.addAll(doFilterBy(careerResult, course -> course.getSession().isAutumnSession()));
                    if (cbWinter.isChecked())
                        res.addAll(doFilterBy(careerResult, course -> course.getSession().isWinterSession()));
                    if (cbFirstSem.isChecked())
                        res.addAll(doFilterBy(careerResult, course -> course.getSession().isFirstSemester()));
                    if (cbSecondSem.isChecked())
                        res.addAll(doFilterBy(careerResult, course -> course.getSession().isSecondSemester()));
                    if (cbSpring.isChecked())
                        res.addAll(doFilterBy(careerResult, course -> course.getSession().isSprintSession()));
                    refreshCourseList(res);
                }
                else
                    refreshCourseList(careerResult);

                if (filterBox.getVisibility() == View.VISIBLE) {
                    filterBox.setVisibility(View.GONE);
                } else {
                    filterBox.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Sets up a button to display and select a unique history item.
     * @param button
     * @param historyIndex
     * @param displayedHistories
     */
    private void setupHistoryButton(Button button, int historyIndex, List<String> displayedHistories) {
        while (historyIndex < histories.size()) {
            String history = histories.get(historyIndex).trim();
            if (!history.isEmpty() && !displayedHistories.contains(history)) {
                button.setText(history);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> editText.setText(history));
                displayedHistories.add(history);
                break;
            } else {
                historyIndex++;
            }
        }
        if (historyIndex >= histories.size()) {
            button.setVisibility(View.GONE);
        }
    }

    private void refreshCourseList(Collection<Course> newList){
        courseList.clear();
        courseList.addAll(newList);
        courseAdapter.notifyDataSetChanged();
        int numResult = courseList.size();
        btnToggleFilter.setText("REFINE " + numResult + " RESULTS:");
    }


    private ArrayList<Course> doFilterBy(Collection<Course> courseList, FilterCallBack callBack){
        ArrayList<Course> res = new ArrayList<>();
        for (Course course :
                courseList) {
            if (course == null)
                continue;
            if (callBack.isMatch(course))
                res.add(course);
        }
        return res;
    }

    private ArrayList<Course> doSearch(String query){
        ArrayList<Course> res = new ArrayList<>();
        try {
            Tokenizer queryToken = new Tokenizer(query);
            Parser parser = new Parser(queryToken);
            Exp[] result = parser.parseExp();
            if (result[0] != null) {
                if (((CodeExp) result[0]).getCodesub() != null && ((CodeExp) result[0]).getCodenum() != null) {
                    Tree<Course> course = getCourseAVLTree().find(new Course(((CodeExp) result[0]).getCode().toUpperCase()));
                    if (course != null)
                        res.add(course.value);
                } else if (((CodeExp) result[0]).getCodesub() != null) {
                    res.addAll(getBPlusTree(BPlusFactory.KeyType.CodeSub).findAll(((CodeExp) result[0]).getCodesub().toLowerCase().hashCode()));
                } else if (((CodeExp) result[0]).getCodenum() != null) {
                    res.addAll(getBPlusTree(BPlusFactory.KeyType.CodeNum).findAll(((CodeExp) result[0]).getCodenum().toLowerCase().hashCode()));
                }
            }//end of search by result[0], which is course code
            if (result[1] != null) {
                ArrayList<Course> subRes = new ArrayList<>();
                subRes.addAll(getBPlusTree(BPlusFactory.KeyType.Subject).findAll(((SubExp) result[1]).getSubject().toLowerCase().hashCode()));
                if (res.size() > 0 || result[0] != null){
                    ArrayList<Course> newRes = new ArrayList<>();
                    for (Course course: res){
                        if (subRes.contains(course)){
                            newRes.add(course);
                        }
                    }
                    res=newRes;
                }
                else
                    res = subRes;
            }// end of search by result[1], which is subject
            if (result[2] != null && !((NameExp) result[2]).getCoursename().isEmpty()) {
                Course course = getBPlusTree(BPlusFactory.KeyType.Name).find(((NameExp) result[2]).getCoursename().toLowerCase().hashCode());
                if (course != null){
                    if(res.contains(course)){
                        res.remove(course);
                    }
                    ArrayList<Course> buffer= new ArrayList<>();
                    buffer.add(course);
                    buffer.addAll(res);
                    res=buffer;
                }
                else{
                    ArrayList<Course> nameRes = new ArrayList<>();
                    PriorityQueue<Map.Entry<Double, Course>> tfidfList = new PriorityQueue<>(new Comparator<Map.Entry<Double, Course>>() {
                        @Override
                        public int compare(Map.Entry<Double, Course> o1, Map.Entry<Double, Course> o2) {
                            if (o1.getKey() > o2.getKey())
                                return -1;
                            else if (o1.getKey() < o2.getKey())
                                return 1;
                            else
                                return 0;
                        }
                    });
                    for(Course doc: DataLoader.getCourseList()){
                        double tfidf = TFIDFFactory.getTFIDF(doc, ((NameExp) result[2]).getCoursename());
                        if (tfidf == 0)
                            continue;
                        tfidfList.add(new AbstractMap.SimpleEntry<>(tfidf, doc));
                    }
                    double t = 0;
                    for (Map.Entry<Double, Course> item :
                            tfidfList) {
                        if (res.size() > 0){
                            if (res.contains(item.getValue())){
                                nameRes.add(item.getValue());
                            }
                        }
                        else
                            nameRes.add(item.getValue());
                    }
                    res = nameRes;
                }
            }//end of search by result[2], which is course name
            if (result[0] == null && result[1] == null && result[2] == null)    //all of the result[] is empty
                Toast.makeText(MainActivity.this, "Invalid query '" + query + "'.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return res;
    }
}
