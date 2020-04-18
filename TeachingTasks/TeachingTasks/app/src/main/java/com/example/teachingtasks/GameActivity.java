package com.example.teachingtasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class GameActivity extends AppCompatActivity {

    TextView username;
    TextView question;
    TextView questionObject;
    HashMap<String, Button> taskObject = new HashMap<>();
    Button taskNavButton, statisticsNavButton, settingsNavButton;
    ConstraintLayout taskObjectLayout;
    NumberTask numberTask;
    ConstraintSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        numberTask = new NumberTask(this, UUID.randomUUID(), "Click the Number", new HashMap<String, Button>());

        taskObjectLayout = (ConstraintLayout) findViewById(R.id.taskObjectLayout);

        set = new ConstraintSet();
        set.clone(taskObjectLayout);

        username = (TextView) findViewById(R.id.username);
        username.setText(getIntent().getStringExtra("EXTRA_USER"));

        question = (TextView) findViewById(R.id.taskQuestion);
        question.setText(getIntent().getStringExtra("EXTRA_QUESTION"));

        questionObject = (TextView) findViewById(R.id.taskQuestionObject);
        questionObject.setText(getIntent().getStringExtra("EXTRA_TASK_OBJECT"));

        final String qObject = questionObject.getText().toString();

        taskObject = numberTask.getTaskObjects();

        //This if check is just for now, but should be made redundant as GameController will not return a Task that doesn't exist
        if (taskObject.size() > 0){
            //If a task has an Object, constrain it and
            int tempID = taskObject.get(qObject).getId();
            set.connect(tempID, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,0);
            set.connect(tempID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
            set.connect(tempID, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            set.connect(tempID, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
            set.constrainHeight(tempID, 180);
            set.constrainWidth(tempID,250);

            taskObjectLayout.addView(taskObject.get(qObject));
            set.applyTo(taskObjectLayout);

            taskObject.get(qObject).setTypeface(Typeface.create("casual", Typeface.BOLD));
            taskObject.get(qObject).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    new TaskObjectEventHandler().onClick(GameActivity.this, v, username.getText().toString(), taskObject.get(qObject).getText().toString());
                    return false;
                }
            });
        }

        if(questionObject.getText().toString().equals("Two")){
            tryToChangeConstraints();
        }

        /*
        TEMP EVENT HANDLER
         */

        taskNavButton = (Button) findViewById(R.id.taskNavButton);
        statisticsNavButton = (Button) findViewById(R.id.statisticsNavButton);
        settingsNavButton = (Button) findViewById(R.id.settingsNavButton);
        /*
        TEMP EVENT HANDLERS
         */

        taskNavButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent loginIntent = new Intent(GameActivity.this, LoginActivity.class);
                loginIntent.putExtra("EXTRA_USER", username.getText());
                GameActivity.this.startActivity(loginIntent);
                return false;
            }
        });

        statisticsNavButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent statsIntent = new Intent(GameActivity.this, StatisticsActivity.class);
                GameActivity.this.startActivity(statsIntent);
                return false;
            }
        });

        settingsNavButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent settingsIntent = new Intent(GameActivity.this, SettingsActivity.class);
                GameActivity.this.startActivity(settingsIntent);
                return false;
            }
        });
    }

    private void tryToChangeConstraints() {
        //Change constraints on taskObjects
        //Add them to the layout


        //Going to need to check for mastery level to determine how many to include on screen
        //MAX = 4
        //MIN = 1
        int maxMastery = 4;

        Iterator keySet = taskObject.keySet().iterator();
        ArrayList<Button> tempObjects = new ArrayList<>();

        while (keySet.hasNext()){

            String temp = (String) keySet.next();
            if(!temp.equals(questionObject.getText().toString()))
                tempObjects.add(taskObject.get(temp));
        }

        Collections.shuffle(tempObjects);

        //Loop through all objects
        for(int k = 1; k < maxMastery; k++){

            int id = tempObjects.get(k).getId();
            int prevID = tempObjects.get(k-1).getId();
            int topConstraint = ConstraintSet.PARENT_ID;
            int leftConstID = prevID;
            int rightConstID = id;
            int leftConstraint = ConstraintSet.RIGHT;
            int rightConstraint = ConstraintSet.LEFT;

            //2x2 grid, if this is the 2nd object it's RIGHT -> PATENT_RIGHT
            if (k == 1){
              set.connect(id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            }

            //2x2 grid, ensure the bottom 2 objects constrain bottomRow_TOP -> topRow_BOTTOM
            if (k >= 2){
                topConstraint = tempObjects.get(k-2).getId();
            }

            //If this is the 3rd object, reset so:
            // LEFT -> PARENT_LEFT
            if (k % 2 == 0){
                leftConstID = ConstraintSet.PARENT_ID;
                rightConstID = ConstraintSet.PARENT_ID;
                leftConstraint = ConstraintSet.LEFT;
                rightConstraint = ConstraintSet.RIGHT;

                //Set previous object's topRow_BOTTOM -> bottomRow_TOP
                set.connect(topConstraint, ConstraintSet.BOTTOM, id, ConstraintSet.BOTTOM, 200);
                set.connect(prevID, ConstraintSet.BOTTOM, id, ConstraintSet.BOTTOM, 200);
            }

            //Set the constraints
            set.connect(id, ConstraintSet.LEFT, leftConstID, leftConstraint, 0);
            set.connect(id, ConstraintSet.TOP, topConstraint, ConstraintSet.TOP, 0);
            set.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            set.connect(prevID, ConstraintSet.RIGHT, rightConstID, rightConstraint,0);

            set.constrainHeight(id, 180);
            set.constrainWidth(id, 250);

            tempObjects.get(k).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    new TaskObjectEventHandler().onClick(GameActivity.this, v, username.getText().toString(), taskObject.get(questionObject).getText().toString());
                    return false;
                }
            });

            //Set font, add to Layout, and apply constraints
            tempObjects.get(k).setTypeface(Typeface.create("casual", Typeface.BOLD));
            taskObjectLayout.addView(tempObjects.get(k));
            set.applyTo(taskObjectLayout);
        }

        //Ensure last object added constraints RIGHT -> PARENT_RIGHT
        set.connect(tempObjects.get(maxMastery-1).getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.applyTo(taskObjectLayout);
    }
}