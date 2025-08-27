package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;


import com.example.project.BPlusTree.BPlusFactory;
import com.example.project.BPlusTree.BPlusTree;
import com.example.project.BPlusTree.KeyCallBack;
import com.example.project.TFIDF.Document;
import com.example.project.TFIDF.TFIDFFactory;
import com.example.project.avl.AVLFactory;
import com.example.project.avl.AVLTree;
import com.example.project.avl.Tree;
import com.example.project.model.Course;
import com.example.project.model.User;
import com.example.project.utils.DataLoader;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author u7580335 Shiying Cai, u7619947 Xinlong Wu
 */
public class LoginActivity extends AppCompatActivity {
    private Button button;
    private Intent intent;
    private EditText usernameInput;
    private EditText passwordInput;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Add user to firebase
        addUserToFirebase("1234", "1234");
        addUserToFirebase("comp2100@anu.edu.au", "comp2100");
        addUserToFirebase("comp6442@anu.edu.au", "comp6442");

        // Load course Data
        initCourseData();

        button = findViewById(R.id.Login);
        intent = new Intent(this, HomeActivity.class);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);

        button.setOnClickListener(v -> {
            String usernameStr = usernameInput.getText().toString();
            String passwordStr = passwordInput.getText().toString();

            validateUser(usernameStr, passwordStr);
        });
    }

    /**
     * Store users' information to Firebase
     * @param username
     * @param password
     */
    private void addUserToFirebase(String username, String password){
        mDatabase.child("users").orderByChild("userName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User already exists
                    Log.d("Firebase", "User already exists with username: " + username);
                } else {
                    // User does not exist, add them to the database
                    String userId = mDatabase.push().getKey();
                    User user = new User(username, password);
                    mDatabase.child("users").child(userId).setValue(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    /**
     * Verify that the user is valid
     * @param username
     * @param password
     */
    private void validateUser(String username, String password) {
        mDatabase.child("users").orderByChild("userName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isUserValid = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && user.getPassword().equals(password)) {
                        isUserValid = true;
                        break;
                    }
                }

                if (isUserValid) {
                    intent.putExtra("Username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                }

                usernameInput.setText("");
                passwordInput.setText("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void initCourseData(){
        ArrayList<Course> courses = DataLoader.getCourseList(getResources().getXml(R.xml.datafile));
        TFIDFFactory.addDocuments(new ArrayList<Document>(courses));
        AVLFactory.setData(courses);
        AVLFactory.getCourseAVLTree();
        BPlusFactory.setData(courses);
        BPlusFactory.getBPlusTree(BPlusFactory.KeyType.CodeSub, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().substring(0,4).toLowerCase().hashCode();
            }
        });
        BPlusFactory.getBPlusTree(BPlusFactory.KeyType.CodeNum, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().substring(4).toLowerCase().hashCode();
            }
        });
        BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Code, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().toLowerCase().hashCode();
            }
        });
        BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Subject, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                String[] list = val.getSubject().split(" ");
                String subjectStr = "";
                for (String item :
                        list) {
                    subjectStr += item.substring(0,1).toUpperCase()+item.substring(1).toLowerCase();
                }
                return subjectStr.toLowerCase().hashCode();
            }
        });
        BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Name, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getName().toLowerCase().hashCode();
            }
        });
    }
}