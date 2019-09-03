package com.example.instastone.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instastone.BitmapScaler;
import com.example.instastone.Info;
import com.example.instastone.MainActivity;
import com.example.instastone.Post;
import com.example.instastone.R;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.WINDOW_SERVICE;
import static com.example.instastone.Info.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

public class ComposeFragment extends Fragment {

    private Button logoutButton;
    private Button submitButton;
    private Button picButton;
    private ImageView postPic;
    private EditText postDesc;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //logoutButton = view.findViewById(R.id.logoutButton);
        submitButton = view.findViewById(R.id.submitBtn);
        picButton = view.findViewById(R.id.picBtn);
        postPic = view.findViewById(R.id.postPic);
        postDesc = view.findViewById(R.id.postDesc);

        picButton.setBackgroundResource(R.drawable.camera_shadow_fill);

        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLaunchCamera();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = postDesc.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();

                if( (photoFile == null || postPic.getDrawable() == null) && postDesc.getText().toString().length() == 0){

                    Toast.makeText(getContext(), "Can't post with no image or description", Toast.LENGTH_SHORT).show();
                    return;

                }

                savePost(description,user,photoFile);

            }
        });

//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ParseUser.logOut();
//                Toast.makeText(getContext(), "Logout Complete", Toast.LENGTH_SHORT).show();
//                returnToLogin();
//
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));

//                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
//
//                Bitmap resizedBitmap = BitmapScaler.scaleToFitHeight(rawTakenImage, 300);
//
//                // Configure byte output stream
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//
//                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//
//                File resizedFile = getPhotoFileUri(photoFileName + "_resized");
//                try {
//                    resizedFile.createNewFile();
//                    FileOutputStream fos = new FileOutputStream(resizedFile);
//
//                    fos.write(bytes.toByteArray());
//                    fos.close();
//                }catch(IOException e){
//
//                    e.printStackTrace();
//
//                }

                postPic.setImageBitmap(takenImage);
                //Glide.with(this).load(resizedBitmap).apply(new RequestOptions().override(screenWidth/3,screenHeight/3)).into(postPic);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    private void onLaunchCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Info");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("Info", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(String description, ParseUser user, File photoFile) {

        Post post = new Post();
        post.setDescription(description);
        post.setUser(user);
        post.setImage(new ParseFile(photoFile));

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null){

                    Log.d("infoActivity", "Something went wrong in saving");
                    e.printStackTrace();
                    return;

                }

                Log.d("infoActivity", "Saving went well");
                postDesc.setText("");
                postPic.setImageResource(0);

            }
        });


    }

    private void returnToLogin(){

        Intent mainActivity = new Intent(getActivity(),MainActivity.class);
        startActivity(mainActivity);
        getActivity().finish();

    }

    private void queryPost(){

        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);

        postQuery.include(Post.KEY_USER);

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {

                if( e != null){

                    e.printStackTrace();
                    Log.d("infoActivity", "something went wrong");
                    return;

                }

                for(int i = 0; i < objects.size(); i++) {
                    Log.d("infoActivity", "Post: " + objects.get(i).getDescription() + "\nMikUsername: " + objects.get(i).getUser().getUsername());

                }

            }
        });

    }

    private static int getScreenWidthInDPs(Context context){

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.widthPixels/dm.density);
    }

    private static int getScreenHeightInDPs(Context context){

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.heightPixels/dm.density);

    }

    private int convertDpToPx(int dp){

        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));

    }

}
