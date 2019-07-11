package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.model.Post;
import com.parse.ParseFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    public int whichFragment;

    public PostsAdapter(Context context, List<Post> posts, int whichFragment) {
        this.context = context;
        this.posts = posts;
        this.whichFragment = whichFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.bind(post);

        //if (whichfragment == 0) -> set it for gridlayout and opposite if it's 1
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvHandle;
        ImageView ivImage;
        TextView tvDescription;
        TextView tvCreated;
        TextView tvNumLikes;
        TextView tvNumComments;
        ImageView ivProfile;
        ImageButton btnHeart;
        ImageButton btnComment;
        ImageButton btnShare;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHandle = itemView.findViewById(R.id.tvHandle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreated = itemView.findViewById(R.id.tvCreated);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
            tvNumComments = itemView.findViewById(R.id.tvNumComments);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            btnComment = itemView.findViewById(R.id.btnMessage);
            btnShare = itemView.findViewById(R.id.btnDirect);

            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            if (whichFragment == 0) {
                btnHeart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!post.isLiked()) {
                            //want it to be liked
                            post.like();
                            btnHeart.setImageResource(R.drawable.ic_heart2);
                            btnHeart.setColorFilter(Color.argb(255,255,0,0));

                        } else {
                            //unlike post
                            post.unlike();
                            btnHeart.setImageResource(R.drawable.ic_heart);
                            btnHeart.setColorFilter(Color.argb(255,0,0,0));
                        }
                        post.saveInBackground();
                        tvNumLikes.setText(Integer.toString(post.getNumLikes()));
                    }
                });

                tvNumLikes.setText(Integer.toString(post.getNumLikes()));
                if (post.isLiked()) {
                    btnHeart.setImageResource(R.drawable.ic_heart2);
                    btnHeart.setColorFilter(Color.argb(255,255,0,0));
                } else {
                    btnHeart.setImageResource(R.drawable.ic_heart);
                    btnHeart.setColorFilter(Color.argb(255,0,0,0));
                }
                tvHandle.setText(post.getUser().getUsername());
                String date = getRelativeTimeAgo(String.valueOf(post.getCreatedAt()));
                tvCreated.setText(date);
                ParseFile image = post.getImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl()).into(ivImage);
                }
                tvDescription.setText(post.getDescription());
                //else if it's the profile view
            } else if (whichFragment == 1) {
                tvHandle.setVisibility(View.GONE);
                tvCreated.setVisibility(View.GONE);
                tvDescription.setVisibility(View.GONE);
                tvNumComments.setVisibility(View.GONE);
                tvNumLikes.setVisibility(View.GONE);
                ivProfile.setVisibility(View.GONE);
                btnHeart.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
                btnShare.setVisibility(View.GONE);
                //have to set other stuff to gone!
                //to resize the smaller images
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int pxWidth = displayMetrics.widthPixels;

                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(pxWidth/3, pxWidth/3);
                ivImage.setLayoutParams(layoutParams);
                ParseFile image = post.getImage();
                if (image != null) {
                    Glide.with(context).load(image.getUrl()).into(ivImage);
                }
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, DetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), post);

                context.startActivity(intent);
            }
        }
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

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
