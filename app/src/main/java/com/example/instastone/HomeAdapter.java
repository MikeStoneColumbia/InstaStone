package com.example.instastone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;

import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>  {

    private Context context;
    private List<Post> posts;

    public HomeAdapter(Context context, List<Post> posts){

        this.context = context;
        this.posts = posts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

       Post post = posts.get(i);
       viewHolder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textPost;
        private ImageView imagePost;

        public ViewHolder(View v){

            super(v);

            textPost = v.findViewById(R.id.hometext);
            imagePost = v.findViewById(R.id.homeImg);

        }

        public void bind(Post post){

            String name = "<b>" + post.getUser().getUsername() + " : " + "</b>"  + post.getDescription();
            textPost.setText(Html.fromHtml(name));

            ParseFile image = post.getImage();



            if(image != null)
                Glide.with(context).load(image.getUrl()).into(imagePost);

        }

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


}
