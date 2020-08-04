package com.basemosama.studentportal.ui.student.group;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.ItemPostBinding;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.utility.Constants;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.GroupViewHolder> {
    private Context context;
    private List<Post> items;
    private PostClickListener postClickListener;

    public PostAdapter(Context context, List<Post> items, PostClickListener postClickListener) {
        this.context = context;
        this.items = items;
        this.postClickListener = postClickListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemPostBinding itemBinding = ItemPostBinding.inflate(inflater, parent, false);
        return new GroupViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateAdapter(List<Post> newPosts) {
        items.clear();
        items.addAll(newPosts);
        notifyDataSetChanged();
    }

    public void addPostToAdapter(Post post) {
        items.add(0, post);
        notifyItemInserted(0);
    }

    public interface PostClickListener {
        void onPostClickListener(Post post, View view);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemPostBinding postBinding;
        private TextView postLikeTv;

        public GroupViewHolder(@NonNull ItemPostBinding itemPostBinding) {
            super(itemPostBinding.getRoot());
            postBinding = itemPostBinding;
            postLikeTv = postBinding.postLikeTv;
            itemPostBinding.postNoOfCommentsTv.setOnClickListener(this);
            itemPostBinding.postCommentTv.setOnClickListener(this);
        }

        private void bind(int position) {
            final Post post = items.get(position);
            postBinding.setPost(post);
            postLikeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleLikeButtonClick(post);
                }
            });

        }

        private void handleLikeButtonClick(Post post) {
            String tag = String.valueOf(postLikeTv.getTag());
            if (!TextUtils.equals(tag, Constants.LIKED_TAG)) {
                postLikeTv.setTextColor(context.getResources().getColor(R.color.colorBlue));
                postLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                postLikeTv.setTag(Constants.LIKED_TAG);
                post.setNoOfLikes(post.getNoOfLikes() + 1);
            } else {
                postLikeTv.setTextColor(context.getResources().getColor(R.color.colorBlack));
                postLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_border, 0, 0, 0);
                postLikeTv.setTag("");
                post.setNoOfLikes(post.getNoOfLikes() - 1);
            }
            postBinding.setPost(post);
        }

        @Override
        public void onClick(View view) {
            Post post = items.get(getAdapterPosition());
            postClickListener.onPostClickListener(post, view);
        }
    }
}