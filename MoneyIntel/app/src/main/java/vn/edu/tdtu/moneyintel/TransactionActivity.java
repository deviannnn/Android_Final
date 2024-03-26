package vn.edu.tdtu.moneyintel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.edu.tdtu.moneyintel.adapter.TransactionTimeAdapter;
import vn.edu.tdtu.moneyintel.adapter.TimeLineAdapter;
import vn.edu.tdtu.moneyintel.database.DatabaseHelper;
import vn.edu.tdtu.moneyintel.model.OverallTransaction;
import vn.edu.tdtu.moneyintel.model.TimeLine;
import vn.edu.tdtu.moneyintel.model.Wallet;

public class TransactionActivity extends AppCompatActivity {
    RecyclerView rvTimeLine, rvSpendingTime;
    Button btnAddTrans;
    ImageView imgvMenu;
    TextView tvReport, tvTotal, tvMoneyStart, tvMoneyEnd, tvMoneyStatistical;
    LinearLayout tab_overview;
    TimeLineAdapter timeLineAdapter;
    private static final int REQUEST_ADD_TRANSACTION = 1;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    TimeLine currentWeek = TimeLine.getCurrentWeekTimeLine();
    TimeLine currentMonth = TimeLine.getCurrentMonthTimeLine();
    TimeLine currentDay = TimeLine.getCurrentDayTimeLine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // image view
        imgvMenu = findViewById(R.id.imgvMenu);
        // text view
        tvReport = findViewById(R.id.tvReport);
        tvTotal = findViewById(R.id.tvTotal);
        tvMoneyStart = findViewById(R.id.tvMoneyStart);
        tvMoneyEnd = findViewById(R.id.tvMoneyEnd);
        tvMoneyStatistical = findViewById(R.id.tvMoneyStatistical);
        // button
        btnAddTrans = findViewById(R.id.btnAddTrans);
        // recycler view
        rvSpendingTime = findViewById(R.id.rvSpendingTime);
        rvTimeLine = findViewById(R.id.rvTimeLine);
        // tab
        tab_overview = findViewById(R.id.tab_overview);

        loadTimeLine(TimeLine.getInstance().generateWeekTimeLines());
        loadSpendingTime(currentWeek.getDateStart(), currentWeek.getDateEnd());
        loadReport(currentWeek.getDateStart(), currentWeek.getDateEnd());
        loadCurrentMoney();

        btnAddTrans.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionActivity.this, AddTrans.class);
            startActivityForResult(intent, REQUEST_ADD_TRANSACTION);
        });

        tvReport.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        tab_overview.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        imgvMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(TransactionActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.day:
                        return true;
                    case R.id.week:
                        loadTimeLine(TimeLine.getInstance().generateWeekTimeLines());
                        loadSpendingTime(currentWeek.getDateStart(), currentWeek.getDateEnd());
                        loadReport(currentWeek.getDateStart(), currentWeek.getDateEnd());
                        return true;
                    case R.id.month:
                        loadTimeLine(TimeLine.getInstance().generateMonthTimeLines());
                        loadSpendingTime(currentMonth.getDateStart(), currentMonth.getDateEnd());
                        loadReport(currentMonth.getDateStart(), currentMonth.getDateEnd());
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });
    }

    private void loadSpendingTime(String dateStart, String dateEnd) {
        rvSpendingTime.setLayoutManager(new LinearLayoutManager(this));

        List<OverallTransaction> data_spendingTime = dbHelper.getOverallTransactionsByDate(dateStart, dateEnd);

        TransactionTimeAdapter adapter_spendingTime = new TransactionTimeAdapter(data_spendingTime);
        rvSpendingTime.setAdapter(adapter_spendingTime);
    }

    private void loadCurrentMoney() {
        Wallet currentMoneyWallet = dbHelper.getCurrentMoney(currentDay.getDateEnd());
        tvTotal.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(currentMoneyWallet.getMoneyEnd()));
    }

    private void loadReport(String dateStart, String dateEnd) {
        int moneyStart = dbHelper.getMoneyInRange(dateStart, dateEnd, false).getMoneyStart();
        int moneyEnd = dbHelper.getMoneyInRange(dateStart, dateEnd, true).getMoneyEnd();
        int moneyStatistical = moneyEnd - moneyStart;
        tvMoneyStart.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(moneyStart));
        tvMoneyEnd.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(moneyEnd));
        tvMoneyStatistical.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(moneyStatistical));
    }

    private void loadTimeLine(List<TimeLine> display) {
        rvTimeLine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        timeLineAdapter = new TimeLineAdapter(display);
        timeLineAdapter.selectedPosition = display.size() - 1;
        timeLineAdapter.notifyDataSetChanged();

        rvTimeLine.setAdapter(timeLineAdapter);
        rvTimeLine.scrollToPosition(display.size() - 1);

        timeLineAdapter.setOnItemClickListener((view, position) -> {
            TimeLine selectedTimeLine = display.get(position);
            String dateStart = selectedTimeLine.getDateStart();
            String dateEnd = selectedTimeLine.getDateEnd();

            loadSpendingTime(dateStart, dateEnd);
            loadReport(dateStart, dateEnd);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                loadTimeLine(TimeLine.getInstance().generateWeekTimeLines());
                loadSpendingTime(currentWeek.getDateStart(), currentWeek.getDateEnd());
                loadReport(currentWeek.getDateStart(), currentWeek.getDateEnd());
                loadCurrentMoney();
            }
        }
    }
}