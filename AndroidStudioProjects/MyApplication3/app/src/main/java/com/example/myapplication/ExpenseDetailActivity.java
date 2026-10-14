package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvAmount = findViewById(R.id.tvDetailAmount);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvNote = findViewById(R.id.tvDetailNote);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnBack = findViewById(R.id.btnBackDetail);

        Expense currentExpense = (Expense) getIntent().getSerializableExtra("EXPENSE_ITEM");

        if (currentExpense != null) {
            tvName.setText(currentExpense.getName());
            tvAmount.setText(String.format("%,d đ", currentExpense.getAmount()));
            tvCategory.setText(currentExpense.getCategory());
            tvDate.setText(currentExpense.getDate());
            tvNote.setText(currentExpense.getNote());
        }

        btnDelete.setOnClickListener(v -> {
            if (currentExpense != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("DELETE_EXPENSE_NAME", currentExpense.getName());
                setResult(RESULT_OK, resultIntent);
                finish(); // Quay lại danh sách
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}