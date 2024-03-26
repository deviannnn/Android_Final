package vn.edu.tdtu.moneyintel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.edu.tdtu.moneyintel.database.DatabaseHelper;
import vn.edu.tdtu.moneyintel.model.Category;
import vn.edu.tdtu.moneyintel.model.TimeLine;
import vn.edu.tdtu.moneyintel.model.Transaction;
import vn.edu.tdtu.moneyintel.model.Wallet;

public class AddTrans extends AppCompatActivity {
    TextView tvCancelTrans, tvAddTrans, tvCategory, tvDate;
    EditText etAmount, etNote;
    public static final int PICK_CATEGORY_REQUEST = 1;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    Category currentCategory;
    Transaction newTrans;
    TimeLine currentDay = TimeLine.getCurrentDayTimeLine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trans);

        tvCancelTrans = findViewById(R.id.tvCancelTrans);
        tvAddTrans = findViewById(R.id.tvAddTrans);
        tvAddTrans.setEnabled(false);
        tvCategory = findViewById(R.id.tvCategory);
        tvDate = findViewById(R.id.tvDate);
        etNote = findViewById(R.id.etNote);
        etAmount = findViewById(R.id.etAmount);

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etAmount.getText().toString().equals("") && !tvCategory.getText().toString().equals("Chọn nhóm")) {
                    tvAddTrans.setEnabled(true);
                    tvAddTrans.setTextColor(Color.WHITE);
                }
                else {
                    tvAddTrans.setEnabled(false);
                    tvAddTrans.setTextColor(Color.parseColor("#656469"));
                }
            }
        });


        tvAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt(etAmount.getText().toString());
                String note = etNote.getText().toString();
                String date = DBFormatDate(tvDate.getText().toString());

                newTrans = new Transaction(amount, note, date, currentCategory);
                dbHelper.insertTransaction(newTrans);

                Wallet currentWallet = dbHelper.getWalletByDate(date);
                if (currentWallet != null) {
                    int currentMoney = currentWallet.getMoneyEnd();
                    if (currentCategory.getType() == 0) {
                        currentWallet.setMoneyEnd(currentMoney - amount);
                    }
                    else
                    {
                        currentWallet.setMoneyEnd(currentMoney + amount);
                    }
                    dbHelper.updateWallet(currentWallet);
                }
                else
                {
                    Wallet temp = dbHelper.getCurrentMoney(currentDay.getDateEnd());
                    int moneyStart = temp.getMoneyEnd();
                    int moneyEnd = 0;
                    if (currentCategory.getType() == 0) {
                        moneyEnd = moneyStart - amount;
                    }
                    else
                    {
                        moneyEnd = moneyStart + amount;
                    }
                    dbHelper.insertWallet(new Wallet(moneyStart, moneyEnd, date));
                }
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        tvCancelTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTrans.this, CategoryPicker.class);
                startActivityForResult(intent, PICK_CATEGORY_REQUEST);
            }
        });
        tvCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etAmount.getText().toString().equals("") && !tvCategory.getText().toString().equals("Chọn nhóm")) {
                    tvAddTrans.setEnabled(true);
                    tvAddTrans.setTextColor(Color.WHITE);
                }
                else {
                    tvAddTrans.setEnabled(false);
                    tvAddTrans.setTextColor(Color.parseColor("#656469"));
                }
            }
        });


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
        tvDate.setText(dateFormat.format(calendar.getTime()));
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTrans.this, R.style.DatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
                        tvDate.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CATEGORY_REQUEST && resultCode == Activity.RESULT_OK) {
            int categoryId = data.getIntExtra("categoryId", 0);
            String categoryName = data.getStringExtra("categoryName");
            int categoryType = data.getIntExtra("categoryType", 0);
            String categoryIcon = data.getStringExtra("categoryIcon");

            currentCategory = new Category(categoryId, categoryName, categoryType, categoryIcon);

            tvCategory.setText(categoryName);
            tvCategory.setTextColor(Color.WHITE);
        }
    }

    public String DBFormatDate(String UIFormat) {
        SimpleDateFormat currentFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = currentFormat.parse(UIFormat);
        } catch (ParseException e) {
            date = Calendar.getInstance().getTime();
        }

        String DBFormat = newFormat.format(date);

        return DBFormat;
    }
}