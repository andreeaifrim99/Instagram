package com.example.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.model.Post;

public class DetailActivity extends AppCompatActivity {
    private TextView tvHandle;
    private TextView tvName;
    private ImageView image;
    private TextView tvCaption;
    private TextView tvCreatedAt;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvHandle = findViewById(R.id.tvHandleName);
        tvName = findViewById(R.id.tvUsername);
        image = findViewById(R.id.ivImage2);
        tvCaption = findViewById(R.id.tvCaption);
        tvCreatedAt = findViewById(R.id.tvTime);

        post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());

        tvCaption.setText(post.getDescription());
        tvName.setText(post.getUser().getUsername());
        //tvHandle.setText("@" + tweet.user.screenName);
        tvCreatedAt.setText(post.getCreatedAt().toString());
        Glide.with(this).load(post.getImage().getUrl()).into(image);
    }

}
