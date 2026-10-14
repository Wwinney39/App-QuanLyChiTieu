package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        EditText etName = findViewById(R.id.etExpenseName);
        EditText etAmount = findViewById(R.id.etExpenseAmount);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        TextView tvSelectDate = findViewById(R.id.tvSelectDate);
        EditText etNote = findViewById(R.id.etExpenseNote);
        Button btnSave = findViewById(R.id.btnSave);

        // Khởi tạo danh mục cho Spinner
        String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // Khởi tạo ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = day + "/" + (month + 1) + "/" + year;
        tvSelectDate.setText(selectedDate);

        // Hiển thị hộp thoại chọn ngày
        tvSelectDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        tvSelectDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Xử lý nút lưu
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Tên khoản chi không được để trống");
                return;
            }
            if (amountStr.isEmpty() || Long.parseLong(amountStr) <= 0) {
                etAmount.setError("Số tiền phải lớn hơn 0");
                return;
            }

            long amount = Long.parseLong(amountStr);
            String category = spinnerCategory.getSelectedItem().toString();
            String note = etNote.getText().toString().trim();

            Expense newExpense = new Expense(name, amount, category, selectedDate, note);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("NEW_EXPENSE", newExpense);
            setResult(RESULT_OK, returnIntent);
            finish();
        });
    }
}