package com.main.comicapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvBirthDate;
    private Button btnPickDate, btnRegister, btnSelectImage;
    private Spinner spinnerGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvBirthDate = findViewById(R.id.tv_birth_date);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnSelectImage = findViewById(R.id.btn_select_image);
        spinnerGender = findViewById(R.id.spinner_gender);
        btnRegister = findViewById(R.id.btn_register);

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic để chọn ảnh từ thiết bị
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic đăng ký
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvBirthDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
