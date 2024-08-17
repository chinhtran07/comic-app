package com.main.comicapp.adapters;

import android.content.Context;
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

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Map<String, String> userNames;
    private Map<String, String> titleNames;
    private OnCommentClickListener listener;
    private Context context;

    public interface OnCommentClickListener {
        void onEditClick(Comment comment);
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

    public CommentAdapter(Context context, List<Comment> comments, Map<String, String> userNames, Map<String, String> titleNames) {
        this.context = context;
        this.comments = comments;
        this.userNames = userNames;
        this.titleNames = titleNames;
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
        holder.tvCommentTitle.setText(titleNames.get(comment.getTitleId()));
        holder.tvCommentUser.setText(userNames.get(comment.getUserId()));

        Log.d("CommentAdapter", "Comment ID: " + comment.getId());

        if (comment.getIsActive() != null && comment.getIsActive()) {
            holder.btnDeleteComment.setText("Ẩn");
        } else {
            holder.btnDeleteComment.setText("Hiển thị");
        }

        holder.btnEditComment.setOnClickListener(v -> listener.onEditClick(comment));
        holder.btnDeleteComment.setOnClickListener(v -> listener.onDeleteClick(comment));
    }


    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentText, tvCommentTitle, tvCommentUser;
        Button btnEditComment, btnDeleteComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentText = itemView.findViewById(R.id.tv_comment_text);
            tvCommentTitle = itemView.findViewById(R.id.tv_comment_title);
            tvCommentUser = itemView.findViewById(R.id.tv_comment_user);
            btnEditComment = itemView.findViewById(R.id.btn_edit_comment);
            btnDeleteComment = itemView.findViewById(R.id.btn_delete_comment);
        }
    }
}
