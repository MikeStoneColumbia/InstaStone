package com.example.instastone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.instastone.HomeAdapter;
import com.example.instastone.Post;
import com.example.instastone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView posts;
    protected HomeAdapter adapter;
    protected List<Post> allPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        posts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new HomeAdapter(getContext(),allPosts);
        posts.setAdapter(adapter);
        posts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPost();

    }

    protected void queryPost(){

        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);

        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {

                if( e != null){

                    e.printStackTrace();
                    Log.d("HomeFragment", "something went wrong");
                    return;

                }

                allPosts.addAll(objects);
                adapter.notifyDataSetChanged();

                for(int i = 0; i < objects.size(); i++) {
                    Log.d("HomeFragment", "Post: " + objects.get(i).getDescription() + "\nUsername: " + objects.get(i).getUser().getUsername());

                }

            }
        });

    }

}
