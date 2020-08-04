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
import com.basemosama.studentportal.databinding.ItemCommentBinding;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.utility.Constants;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> items;
    private CommentClickListener commentClickListener;

    public CommentAdapter(Context context, List<Comment> items, CommentClickListener commentClickListener) {
        this.context = context;
        this.items = items;
        this.commentClickListener = commentClickListener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCommentBinding itemBinding = ItemCommentBinding.inflate(inflater, parent, false);
        return new CommentViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateAdapter(List<Comment> newComments) {
        items.clear();
        items.addAll(newComments);
        notifyDataSetChanged();
    }

    public void addCommentToAdapter(Comment comment) {
        items.add(comment);
        notifyItemInserted(getItemCount() - 1);

    }

    public interface CommentClickListener {
        void onCommentClickListener(Comment comment);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemCommentBinding commentBinding;
        private TextView likeTv;

        public CommentViewHolder(@NonNull ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            commentBinding = itemCommentBinding;
            likeTv = commentBinding.commentLikeTv;
            commentBinding.commentReplyTv.setOnClickListener(this);
            commentBinding.viewMoreRepliesTv.setOnClickListener(this);
            commentBinding.commentReplyLayout.setOnClickListener(this);
        }

        private void bind(int position) {
            final Comment comment = items.get(position);
            commentBinding.setComment(comment);
            likeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleLikeButtonClick(comment);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Comment comment = items.get(getAdapterPosition());
            commentClickListener.onCommentClickListener(comment);
        }

        private void handleLikeButtonClick(Comment comment) {
            String tag = String.valueOf(likeTv.getTag());
            if (!TextUtils.equals(tag, Constants.LIKED_TAG)) {
                likeTv.setTextColor(context.getResources().getColor(R.color.colorBlue));
                likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                likeTv.setTag(Constants.LIKED_TAG);
                comment.setNoOfLikes(comment.getNoOfLikes() + 1);
            } else {
                likeTv.setTextColor(context.getResources().getColor(R.color.colorBlack));
                likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_border, 0, 0, 0);
                likeTv.setTag("");
                comment.setNoOfLikes(comment.getNoOfLikes() - 1);
            }
            commentBinding.setComment(comment);
        }
    }
}