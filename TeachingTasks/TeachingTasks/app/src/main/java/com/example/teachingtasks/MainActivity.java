package com.example.teachingtasks;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText newUsername, newPassword;
    CheckBox showPass;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sets the initial Content View
        setContentView(R.layout.activity_main);

        //Assign variables to UI components
        newUsername = (EditText) findViewById(R.id.newUserNameEditText);
        newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        showPass = (CheckBox) findViewById(R.id.showPasswordCheckBox);
        createButton = (Button) findViewById(R.id.createUserButton);


        //Set EventHandlers
        createButton.setOnClickListener(this);
        showPass.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        //Checks the ID and calls the appropriate EventHandler
        switch (v.getId()){
            case R.id.showPasswordCheckBox: new ShowPassEventHandler().onCheck(showPass, newPassword); break;
            case R.id.createUserButton: new CreateUserEventHandler().onClick(this, newUsername, newPassword); break;
            default: return;
        }
    }
}
