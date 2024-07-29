package com.main.comicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.R;
import com.main.comicapp.adapters.ChapterAdapter;
import com.main.comicapp.adapters.CommentsAdapter;
import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.models.Comment;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.ChapterViewModel;
import com.main.comicapp.viewmodels.CommentViewModel;
import com.main.comicapp.viewmodels.GenreViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TitleDetailActivity extends BaseActivity {

    private static final String TAG = "TitleDetailActivity";
    private ImageView imageView;
    private Button btnWriteComment;
    private TextView txtTitleName;
    private TextView txtGenres;
    private TextView txtViews;
    private TextView txtCreatedDate;
    private TextView txtPublishStatus;
    private RecyclerView rvChapters;
    private RecyclerView rvComments;
    private ChapterAdapter chapterAdapter;
    private CommentsAdapter commentsAdapter;
    private ChapterViewModel chapterViewModel;
    private GenreViewModel genreViewModel;
    private CommentViewModel commentViewModel;
    private UserViewModel userViewModel;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_detail);

        // Initialize views
        txtTitleName = findViewById(R.id.title_detail_title_name);
        btnWriteComment = findViewById(R.id.title_detail_write_comment);
        txtGenres = findViewById(R.id.title_detail_genres);
        txtViews = findViewById(R.id.title_detail_views);
        txtCreatedDate = findViewById(R.id.title_detail_created_date);
        txtPublishStatus = findViewById(R.id.title_detail_publishing_status);
        rvChapters = findViewById(R.id.title_detail_chapter_list);
        rvComments = findViewById(R.id.title_detail_comments_list);

        // Initialize RecyclerView
        initRv();
    }

    private void initRv() {
        rvChapters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chapterAdapter = new ChapterAdapter(null);
        commentsAdapter = new CommentsAdapter(null, null);
        rvChapters.setAdapter(chapterAdapter);
        rvComments.setAdapter(commentsAdapter);
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
        genreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        Title title = (Title) intent.getSerializableExtra("title");
        if (title != null) {
            loadTitleData(title);
            // Loading title's chapters section
            chapterAdapter.setListener(new ChapterAdapter.OnChapterClickListener() {
                @Override
                public void onChapterClick(Chapter chapter) {
                    intentToReading(chapter, title);
                }

                @Override
                public void onUpdateClick(Chapter chapter) {
                    Intent updateIntent = new Intent(TitleDetailActivity.this, UpdateChapterActivity.class);
                    updateIntent.putExtra("chapter_id", chapter.getId());
                    startActivity(updateIntent);
                }

                @Override
                public void onDeleteClick(Chapter chapter) {
                    chapterViewModel.deleteChapter(chapter.getId());
                    Toast.makeText(TitleDetailActivity.this, "Chapter deleted", Toast.LENGTH_SHORT).show();
                }
            });
            chapterViewModel.getChapters(title.getId()).observeForever(new Observer<List<Chapter>>() {
                @Override
                public void onChanged(List<Chapter> chapters) {
                    if (chapters != null) {
                        chapterAdapter.setChapters(chapters.stream().sorted(Comparator.comparing(Chapter::getUploadedDate)).collect(Collectors.toList()));
                    }
                }
            });
            // Loading title's comments section
            commentsAdapter.setListener(new CommentsAdapter.OnCommentClickListener() {
                @Override
                public void onCommentClick(Comment comment) {
                    // TODO: Open reply section
                }
            });
            commentViewModel.getCommentsByTitle(title.getId()).observeForever(new Observer<List<Comment>>() {
                @Override
                public void onChanged(List<Comment> comments) {
                    if (comments != null) {
                        // TODO: Sort comments by uploaded date (Add another field in Comment model)
                        commentsAdapter.setComments(comments);
                        List<String> userIds = comments.stream().map(Comment::getUserId).collect(Collectors.toList());
                        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                        for (String userId : userIds) {
                            tasks.add(FirebaseFirestore.getInstance().collection("users").document(userId).get());
                        }
                        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> objects) {
                                for (Object object : objects) {
                                    DocumentSnapshot documentSnapshot = (DocumentSnapshot) object;
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> data = documentSnapshot.getData();
                                        User user = User.toObject(data, documentSnapshot.getId());
                                        userList.add(user);
                                    } else {
                                        Log.e(TAG, "onSuccess: Document does not exist.");
                                    }
                                }
                                commentsAdapter.setUsers(userList);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e.getCause());
                            }
                        });
                    }
                }
            });
        }


        btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToWriteComment(title);
            }
        });


    }

    // Get title data from other activities
    private void loadTitleData(Title title) {
        txtTitleName.setText(title.getTitle());
        LiveData<List<Genre>> genres = genreViewModel.getGenres(title.getGenreIds());
        genres.observe(this, new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genres) {
                txtGenres.setText(genres.stream().map(Genre::getName).collect(Collectors.joining(", ")));
            }
        });
        txtViews.setText(String.valueOf(title.getViews()));
        txtCreatedDate.setText(title.getUploadedDate().toString());
        txtPublishStatus.setText(PubStatus.valueOf(title.getPubStatus()).toString());
    }

    private void intentToReading(Chapter chapter, Title title) {
        Intent intent = new Intent(getApplicationContext(), ReadingActivity.class);
        intent.putExtra("titleId", title.getId());
        intent.putExtra("titleFormat", title.getTitleFormat());
        intent.putExtra("chapter", chapter);
        startActivity(intent);
    }


    private void intentToWriteComment(Title title) {
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void loadCommentData(Title title) {
        LiveData<List<Comment>> comments = commentViewModel.getCommentsByTitle(title.getId());
        comments.observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {

            }
        });
    }
}

