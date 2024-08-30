package com.main.comicapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.main.comicapp.R;
import com.main.comicapp.models.Comment;
import com.main.comicapp.viewmodels.CommentViewModel;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Map<String, String> userNames;
    private Map<String, String> titleNames;
    private OnCommentClickListener listener;
    private CommentViewModel commentViewModel;
    private Context context;

    public interface OnCommentClickListener {
        void onDeleteClick(Comment comment);
    }

    public void setListener(OnCommentClickListener listener) {
        this.listener = listener;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void setUserNames(Map<String, String> userNames) {
        this.userNames = userNames;
        notifyDataSetChanged();
    }

    public void setTitleNames(Map<String, String> titleNames) {
        this.titleNames = titleNames;
        notifyDataSetChanged();
    }

    public CommentAdapter(Context context, List<Comment> comments, Map<String, String> userNames, Map<String, String> titleNames, CommentViewModel commentViewModel) {
        this.context = context;
        this.comments = comments;
        this.userNames = userNames;
        this.titleNames = titleNames;
        this.commentViewModel = commentViewModel;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvCommentText.setText(comment.getText());

        // Nhấn mạnh "Tên truyện: "
        String titlePrefix = "Tên truyện: ";
        String title = titleNames.get(comment.getTitleId());
        SpannableString spannableTitle = new SpannableString(titlePrefix + title);
        spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, titlePrefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvCommentTitle.setText(spannableTitle);

        // Nhấn mạnh "Tên người dùng: "
        String userPrefix = "Tên người dùng: ";
        String user = userNames.get(comment.getUserId());
        SpannableString spannableUser = new SpannableString(userPrefix + user);
        spannableUser.setSpan(new StyleSpan(Typeface.BOLD), 0, userPrefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvCommentUser.setText(spannableUser);

        Log.d("CommentAdapter", "Comment ID: " + comment.getId());

        // Set text for the button based on the isActive status
        if (comment.getIsActive() != null && comment.getIsActive()) {
            holder.btnToggleStatus.setText("Ẩn");
        } else {
            holder.btnToggleStatus.setText("Hiển thị");
        }

        holder.btnToggleStatus.setOnClickListener(v -> {
            boolean newStatus = comment.getIsActive() == null ? false : !comment.getIsActive();
            commentViewModel.updateStatusComment(comment.getId(), newStatus);
        });
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentText, tvCommentTitle, tvCommentUser;
        Button btnToggleStatus;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentText = itemView.findViewById(R.id.tv_comment_text);
            tvCommentTitle = itemView.findViewById(R.id.tv_comment_title);
            tvCommentUser = itemView.findViewById(R.id.tv_comment_user);
            btnToggleStatus = itemView.findViewById(R.id.btn_toggle_status);
        }
    }
}
