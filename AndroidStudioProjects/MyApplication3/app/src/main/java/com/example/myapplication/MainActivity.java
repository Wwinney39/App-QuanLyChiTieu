package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;
    private TextView tvTotalAmount;

    // Lắng nghe kết quả trả về từ AddExpenseActivity
    private final ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Expense newExpense = (Expense) result.getData().getSerializableExtra("NEW_EXPENSE");
                    if (newExpense != null) {
                        expenseList.add(newExpense);
                        adapter.notifyDataSetChanged();
                        updateTotalAmount();
                    }
                }
            }
    );

    // Lắng nghe kết quả trả về từ ExpenseDetailActivity (xử lý xóa)[cite: 1]
    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String deleteName = result.getData().getStringExtra("DELETE_EXPENSE_NAME");
                    if (deleteName != null) {
                        for (int i = 0; i < expenseList.size(); i++) {
                            if (expenseList.get(i).getName().equals(deleteName)) {
                                expenseList.remove(i);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        updateTotalAmount();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button btnAddExpense = findViewById(R.id.btnAddExpense);

        expenseList = new ArrayList<>();
        // Khởi tạo dữ liệu mẫu theo yêu cầu[cite: 1]
        expenseList.add(new Expense("Ăn sáng", 30000, "Ăn uống", "12/10/2026", ""));
        expenseList.add(new Expense("Đổ xăng", 50000, "Đi lại", "12/10/2026", ""));
        expenseList.add(new Expense("Mua sách", 120000, "Học tập", "13/10/2026", "Sách Lập trình Android"));

        adapter = new ExpenseAdapter(expenseList, expense -> {
            Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
            intent.putExtra("EXPENSE_ITEM", expense);
            detailLauncher.launch(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateTotalAmount();

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            addLauncher.launch(intent);
        });
    }

    private void updateTotalAmount() {
        long total = 0;
        for (Expense e : expenseList) {
            total += e.getAmount();
        }
        tvTotalAmount.setText(String.format("%,d đ", total));
    }
}