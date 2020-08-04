package com.basemosama.studentportal.utility;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.model.student.Reply;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Binding Adapters used Across The app For DataBinding
 **/
public class BindingAdapters {
    //helper Method to convert no of likes > 1000 to k,M,etc.
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    //Load Image From the internet with glide
    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        if (TextUtils.isEmpty(url)) {
            url = "imageError";
        }
        Glide.with(view.getContext())
                .load(url)
                .centerCrop()
                .error(error)
                .placeholder(error)
                .thumbnail(.25f)
                .into(view);
    }

    //Load circle Image From the internet with glide
    @BindingAdapter({"circleImageUrl", "error"})
    public static void loadCircleImage(ImageView view, String url, Drawable error) {
        if (TextUtils.isEmpty(url)) {
            url = "imageError";
        }
        Glide.with(view.getContext())
                .load(url)
                .circleCrop()
                .placeholder(error)
                .error(error)
                .into(view);
    }

    //Load circle Image From the internet with glide for Posts
    @BindingAdapter({"circleImageUrl", "error"})
    public static void loadCircleImageForPostWriter(ImageView view, Post post, Drawable error) {
        String url = "";
        if (post.getStudent() != null) {
            url = post.getStudent().getImageUrl();
        } else if (post.getProfessor() != null) {
            url = post.getProfessor().getImageUrl();
        }
        if (TextUtils.isEmpty(url)) {
            url = "imageError";
        }
        Glide.with(view.getContext())
                .load(url)
                .circleCrop()
                .placeholder(error)
                .error(error)
                .into(view);
    }

    //Load circle Image From the internet with glide for comments
    @BindingAdapter({"circleImageUrl", "error"})
    public static void loadCircleImageForCommentWriter(ImageView view, Comment comment, Drawable error) {
        String url = "";
        if (comment.getStudent() != null) {
            url = comment.getStudent().getImageUrl();
        } else if (comment.getProfessor() != null) {
            url = comment.getProfessor().getImageUrl();
        }

        if (TextUtils.isEmpty(url)) {
            url = "imageError";
        }
        Glide.with(view.getContext())
                .load(url)
                .circleCrop()
                .placeholder(error)
                .error(error)
                .into(view);
    }

    //Load circle Image From the internet with glide for replies.
    @BindingAdapter({"circleImageUrl", "error"})
    public static void loadCircleImageForReplyWriter(ImageView view, Reply reply, Drawable error) {
        String url = "imageError";
        if (reply != null) {
            if (reply.getStudent() != null) {
                url = reply.getStudent().getImageUrl();
            } else if (reply.getProfessor() != null) {
                url = reply.getProfessor().getImageUrl();
            }
        }
        if (TextUtils.isEmpty(url)) {
            url = "imageError";
        }

        Glide.with(view.getContext())
                .load(url)
                .circleCrop()
                .placeholder(error)
                .error(error)
                .into(view);
    }

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImageForPhotoView(PhotoView view, String url, Drawable error) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(view.getContext())
                    .load(url)
                    .centerCrop()
                    .error(error)
                    .placeholder(error)
                    .into(view);
        }
    }

    @BindingAdapter(value = {"text", "placeholder"}, requireAll = false)
    public static void setText(TextView view, String text, String placeHolder) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            if (!TextUtils.isEmpty(placeHolder)) {
                view.setText(placeHolder);
            } else {
                view.setText("");
            }
        }
    }

    @BindingAdapter("nameText")
    public static void setTextForCommentWriter(TextView view, Comment comment) {
        String user = "";
        if (comment != null) {
            if (comment.getStudent() != null) {
                user = comment.getStudent().getName();
            } else if (comment.getProfessor() != null) {
                user = comment.getProfessor().getName();
            }
        }
        if (!TextUtils.isEmpty(user)) {
            view.setText(user);
        } else {
            view.setText("");
        }
    }

    @BindingAdapter("nameText")
    public static void setTextForReplyWriter(TextView view, Reply reply) {
        String user = "";
        if (reply != null) {
            if (reply.getStudent() != null) {
                user = reply.getStudent().getName();
            } else if (reply.getProfessor() != null) {
                user = reply.getProfessor().getName();
            }
        }
        if (!TextUtils.isEmpty(user)) {
            view.setText(user);
        } else {
            view.setText("");
        }
    }

    // set recyclerview layout manger linear
    @BindingAdapter("isLayoutMangerLinear")
    public static void setLinearLayoutMangerToRecyclerView(RecyclerView recyclerView, boolean isItTrue) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @BindingAdapter("likeText")
    public static void setLikesText(TextView view, long value) {
        Resources resources = view.getResources();
        String likeText = resources.getString(R.string.like_text);
        String likesText = resources.getString(R.string.likes_text);
        String text = likeText;
        if (value == 1) {
            text = format(value) + " " + likeText;
        } else if (value > 1) {
            text = format(value) + " " + likesText;
        }
        view.setText(text);
    }

    @BindingAdapter("postLikeText")
    public static void setPostLikesText(TextView view, long value) {
        Resources resources = view.getResources();
        String likeText = resources.getString(R.string.like_text);
        String likesText = resources.getString(R.string.likes_text);
        String text = likeText;
        if (value == 1) {
            text = format(value) + " " + likeText;
        } else if (value > 1) {
            text = format(value) + " " + likesText;
        }
        if (value > 0) {
            view.setText(text);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    //set post, comment, reply writer name
    @BindingAdapter("nameText")
    public static void setTextForPostWriter(TextView view, Post post) {
        String user = "";
        if (post != null) {
            if (post.getStudent() != null) {
                user = post.getStudent().getName();
            } else if (post.getProfessor() != null) {
                user = post.getProfessor().getName();
            }
        }
        if (!TextUtils.isEmpty(user)) {
            view.setText(user);
        } else {
            view.setText("");
        }
    }

    //set No Of Likes after formatting
    @BindingAdapter({"postText", "prefix"})
    public static void setTextForPostLikes(TextView view, long value, String prefix) {
        String text = format(value) + " " + prefix;
        if (value > 0) {
            view.setText(text);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("commentText")
    public static void setCommentText(TextView view, long value) {
        Resources resources = view.getResources();
        String commentText = resources.getString(R.string.comment_text);
        String commentsText = resources.getString(R.string.comments_text);
        String text = commentText;
        if (value == 1) {
            text = format(value) + " " + commentText;
        } else if (value > 1) {
            text = format(value) + " " + commentsText;
        }
        if (value > 0) {
            view.setText(text);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("viewMoreText")
    public static void setViewMoreTextForComment(TextView view, long noOfReplies) {
        Resources res = view.getResources();
        String text = "";
        if (noOfReplies > 0) {
            text = String.format(res.getString(R.string.view_more_replies_text), noOfReplies);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        view.setText(text);
    }

    @BindingAdapter("text")
    public static void setTextFromDouble(TextView view, double value) {
        String text = String.valueOf(value);
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }

    @BindingAdapter("text")
    public static void setTextFromDate(TextView view, Date date) {
        String text = AppUtility.getFormattedDate(date);
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }

    @BindingAdapter({"text", "prefix"})
    public static void setFormattedTextWithPrefix(TextView view, String text, String prefix) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(String.format(prefix, text));
        } else {
            view.setText("");
        }
    }

    @BindingAdapter("isVisible")
    public static void setViewVisibilityBasedOnText(View view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("isVisible")
    public static void setViewVisibilityBasedOnLongValue(View view, long value) {
        if (value > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    //Setting Visibility to hide view if data is loaded
    @BindingAdapter("isVisible")
    public static void setViewVisibilityAfterLoading(View view, MutableLiveData<Boolean> isLoadedMutableLiveData) {
        if (isLoadedMutableLiveData != null && isLoadedMutableLiveData.getValue()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("date")
    public static void setTextFromDateForGroup(TextView view, Date date) {
        String ago = AppUtility.getFormattedPastDate(date);
        if (!TextUtils.isEmpty(ago)) {
            view.setText(ago);
        } else {
            view.setText("");
        }
    }

    //set Text From Integer
    @BindingAdapter("text")
    public static void setTextFromInteger(TextView view, int value) {
        String text = String.valueOf(value);
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


}


