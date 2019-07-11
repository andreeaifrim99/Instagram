package com.example.instagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

        //tvHandle = findViewById(R.id.tvHandleName);
        tvName = findViewById(R.id.tvUsername);
        image = findViewById(R.id.ivImage2);
        tvCaption = findViewById(R.id.tvCaption);
        tvCreatedAt = findViewById(R.id.tvTime);

        post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());

        String date = getRelativeTimeAgo(String.valueOf(post.getCreatedAt()));

        tvCaption.setText(post.getDescription());
        tvName.setText(post.getUser().getUsername());
        //tvHandle.setText("@" + tweet.user.screenName);
        tvCreatedAt.setText(date);
        Glide.with(this).load(post.getImage().getUrl()).into(image);
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
