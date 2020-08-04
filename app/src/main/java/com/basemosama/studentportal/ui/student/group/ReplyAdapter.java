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
import com.basemosama.studentportal.databinding.ItemReplyBinding;
import com.basemosama.studentportal.model.student.Reply;
import com.basemosama.studentportal.utility.Constants;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private Context context;
    private List<Reply> items;

    public ReplyAdapter(Context context, List<Reply> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemReplyBinding itemBinding = ItemReplyBinding.inflate(inflater, parent, false);
        return new ReplyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateAdapter(List<Reply> newReplies) {
        items.clear();
        items.addAll(newReplies);
        notifyDataSetChanged();
    }

    public void addReplyToAdapter(Reply reply) {
        items.add(reply);
        notifyItemInserted(getItemCount() - 1);

    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        private ItemReplyBinding replyBinding;
        private TextView likeTv;

        public ReplyViewHolder(@NonNull ItemReplyBinding itemReplyBinding) {
            super(itemReplyBinding.getRoot());
            replyBinding = itemReplyBinding;
            likeTv = replyBinding.replyLikeTv;
        }

        private void bind(int position) {
            final Reply reply = items.get(position);
            replyBinding.setReply(reply);
            likeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleLikeButtonClick(reply);
                }
            });
        }

        private void handleLikeButtonClick(Reply reply) {
            String tag = String.valueOf(likeTv.getTag());
            if (!TextUtils.equals(tag, Constants.LIKED_TAG)) {
                likeTv.setTextColor(context.getResources().getColor(R.color.colorBlue));
                likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                likeTv.setTag(Constants.LIKED_TAG);
                reply.setNoOfLikes(reply.getNoOfLikes() + 1);

            } else {
                likeTv.setTextColor(context.getResources().getColor(R.color.colorBlack));
                likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_border, 0, 0, 0);
                likeTv.setTag("");
                reply.setNoOfLikes(reply.getNoOfLikes() - 1);
            }
            replyBinding.setReply(reply);
        }
    }
}