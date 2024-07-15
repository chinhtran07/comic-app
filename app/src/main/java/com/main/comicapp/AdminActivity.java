package com.main.comicapp;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private ListView pageListView;
    private Button addPageButton;
    private ArrayList<String> pages;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        pageListView = findViewById(R.id.pageListView);
        addPageButton = findViewById(R.id.addPageButton);

        // Giả lập danh sách các trang
        pages = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            pages.add("Trang " + i);
        }

        // Sử dụng custom adapter
        adapter = new ArrayAdapter<>(this, R.layout.activity_admin_list_page, R.id.pageTitleTextView, pages);
        pageListView.setAdapter(adapter);

        addPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện thêm trang
                pages.add("Trang " + (pages.size() + 1));
                adapter.notifyDataSetChanged();
                Toast.makeText(AdminActivity.this, "Thêm trang mới", Toast.LENGTH_SHORT).show();
            }
        });

        pageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý sự kiện click vào một trang trong danh sách
                Toast.makeText(AdminActivity.this, "Click vào " + pages.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý các nút trong list_item_page.xml
        pageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Button editButton = view.findViewById(R.id.editButton);
                Button deleteButton = view.findViewById(R.id.deleteButton);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý sự kiện sửa trang
                        Toast.makeText(AdminActivity.this, "Sửa " + pages.get(position), Toast.LENGTH_SHORT).show();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý sự kiện xóa trang
                        pages.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(AdminActivity.this, "Xóa trang", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
