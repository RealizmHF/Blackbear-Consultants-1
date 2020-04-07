package com.example.teachingtasks;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserEventHandler {

    public void onClick(RegisterUserActivity mainActivity, RegisterUserDBHelper mydb, EditText newUsername, EditText newPassword) {

        //Check password meets required guidelines (Caps, special char, length, etc)
        //Check database for duplicate username
        //Add new username/password combo to database

        /*
        Temp Function until above is done
         */

        System.out.println("Clicked Create User Button");
        String pass = newPassword.getText().toString();
        boolean invalidPass = false;

        //If the username doesn't exist in the database already, check the password
        if(!mydb.containsUser(newUsername.getText().toString())){

            //If the password is acceptable, add the user
            if(isAcceptablePassword(pass)){

                mydb.addUser(newUsername.getText().toString(), newPassword.getText().toString());

                Intent userSelectIntent = new Intent(mainActivity.getBaseContext(),UserSelectActivity.class);
                mainActivity.startActivity(userSelectIntent);
            }
            //Else, the password was not acceptable
            else {
                invalidPass = true;
            }
        }
        else {
            Toast.makeText(mainActivity, "Duplicate username or Invalid password", Toast.LENGTH_LONG).show();
        }
        if(invalidPass){
            Toast.makeText(mainActivity, "Password must contain one Upper Case Letter, Number, and Special" + "" +
                                              "Character (@,$,!,?,...)" + " and a length of eight.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private boolean isAcceptablePassword(String pass) {
        //True if pass length > 7 and contains at least 1 letter, number, and special char.
        //Else, false.

        if(pass.length() > 7){
            //Create regex pattern to match
            //If pass contains capital letter, number, and special char such as !,@,#,$,?... etc.
            //Then return true, else, return false

            Pattern passPattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,24}$");
            Matcher matcher = passPattern.matcher(pass);

            return matcher.matches();
        }
        return false;
    }

}
