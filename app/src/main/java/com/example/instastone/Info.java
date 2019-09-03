package com.example.instastone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instastone.fragments.ComposeFragment;
import com.example.instastone.fragments.HomeFragment;
import com.example.instastone.fragments.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Info extends AppCompatActivity {

    private Button logoutButton;
    private Button submitButton;
    private Button picButton;
    private ImageView postPic;
    private EditText postDesc;
    private File photoFile;
    private BottomNavigationView nav;

    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        nav = findViewById(R.id.nav);


        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;

                switch (menuItem.getItemId()) {

                    case R.id.action_compose:
                        //TODO: swap fragments
                        fragment = new ComposeFragment();
                        Toast.makeText(Info.this, "Compose Clicked!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_home:
                        //TODO: swap fragments
                        fragment = new HomeFragment();
                        Toast.makeText(Info.this, "Home Clicked!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        //TODO: swap fragments
                        fragment = new ProfileFragment();
                        Toast.makeText(Info.this, "Profile Clicked!", Toast.LENGTH_SHORT).show();
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.frameContainer,fragment).commit();

                return true;

            }
        });

        nav.setSelectedItemId(R.id.action_home);

    }



}
