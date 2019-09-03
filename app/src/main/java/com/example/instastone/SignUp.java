package com.example.instastone;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SignUp extends AppCompatActivity {

    private Button signUpButton;
    private EditText signName;
    private EditText signUsername;
    private EditText signNumber;
    private EditText signEmail;
    private EditText signPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton = findViewById(R.id.signUpButton);
        signName = findViewById(R.id.signName);
        signUsername = findViewById(R.id.signUsername);
        signNumber = findViewById(R.id.signNumber);
        signEmail = findViewById(R.id.signEmail);
        signPassword = findViewById(R.id.signPassword);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = signUsername.getText().toString();
                String name = signName.getText().toString();
                String number = signNumber.getText().toString();
                String email = signEmail.getText().toString();
                String password = signPassword.getText().toString();
                createAccount(name,username,email,number,password);

            }
        });

    }

    private void createAccount(String name, String username, String email, String number, String password) {

        final boolean validAccount;

        Intent info = new Intent(this,Info.class);

        validAccount = validSignUp(name,username,email,number,password);

        ParseUser newUser = new ParseUser();

        if(validAccount) {

            newUser.setUsername(name);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.put("phoneNumber", number);
            newUser.setPassword(password);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        Log.d("SignUp", "Sign up is successful");
                        goToInfo();
                    } else {
                        Log.d("SignUp", e.toString());
                        Toast.makeText(SignUp.this,e.toString().substring(e.toString().indexOf(':')+1,e.toString().length()) , Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

    private boolean validSignUp(String name, String username, String email, String number, String password) {

        if(name.length() == 0){

            Toast.makeText(this, "Please input your name.", Toast.LENGTH_SHORT).show();
            return false;

        }

        if(username.length() == 0){

            Toast.makeText(this, "Please input a username", Toast.LENGTH_SHORT).show();
            return false;

        }

        if(email.length() == 0){

            Toast.makeText(this, "Please input a valid email.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(number.length() == 0){

            Toast.makeText(this, "Please input your phone number.", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

    private void goToInfo() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }



}
