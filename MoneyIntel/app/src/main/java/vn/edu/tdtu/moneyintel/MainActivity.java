package vn.edu.tdtu.moneyintel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.edu.tdtu.moneyintel.adapter.TransactionAdapter;
import vn.edu.tdtu.moneyintel.adapter.TransactionExpenseAdapter;
import vn.edu.tdtu.moneyintel.database.DatabaseHelper;
import vn.edu.tdtu.moneyintel.model.OverallTransaction;
import vn.edu.tdtu.moneyintel.model.TimeLine;
import vn.edu.tdtu.moneyintel.model.Transaction;
import vn.edu.tdtu.moneyintel.model.Wallet;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMostExpense, rvRecentTrans;
    TextView tvTrans, tvReport, tvTotalExpense, tvCurrentMoney, tvCurrentMoney2;
    Button btnAddTrans;
    ToggleButton toggleChangeTime;
    LinearLayout tab_transactions;
    GraphView graphView;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private static final int REQUEST_ADD_TRANSACTION = 1;
    TimeLine currentWeek = TimeLine.getCurrentWeekTimeLine();
    TimeLine currentMonth = TimeLine.getCurrentMonthTimeLine();
    TimeLine lasWeek = TimeLine.getLastWeekTimeLine();
    TimeLine lasMonth = TimeLine.getLastMonthTimeLine();
    TimeLine currentDay = TimeLine.getCurrentDayTimeLine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recycler view
        rvMostExpense = findViewById(R.id.rvMostExpense);
        rvRecentTrans = findViewById(R.id.rvRecentTrans);
        // text view
        tvReport = findViewById(R.id.tvReport);
        tvTrans = findViewById(R.id.tvTrans);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvCurrentMoney = findViewById(R.id.tvCurrentMoney);
        tvCurrentMoney2 = findViewById(R.id.tvCurrentMoney2);
        // button
        btnAddTrans = findViewById(R.id.btnAddTrans);
        // toggle button
        toggleChangeTime = findViewById(R.id.toggleChangeTime);
        // graph view
        graphView = findViewById(R.id.graphView);
        // tab
        tab_transactions = findViewById(R.id.tab_transactions);

        int expenseLastWeek = loadTotalExpense(lasWeek.getDateStart(), lasWeek.getDateEnd());
        int expenseCurrentWeek = loadTotalExpense(currentWeek.getDateStart(), currentWeek.getDateEnd());
        loadTop3MostExpense(currentWeek.getDateStart(), currentWeek.getDateEnd());
        loadRecentTrans();
        loadGraph(expenseLastWeek, expenseCurrentWeek, true);
        loadCurrentMoney();

        btnAddTrans.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTrans.class);
            startActivityForResult(intent, REQUEST_ADD_TRANSACTION);
        });

        tvReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        tvTrans.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(intent);
        });

        tab_transactions.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(intent);
        });

        toggleChangeTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                int expenseLastWeek1 = loadTotalExpense(lasWeek.getDateStart(), lasWeek.getDateEnd());
                int expenseCurrentWeek1 = loadTotalExpense(currentWeek.getDateStart(), currentWeek.getDateEnd());
                loadTop3MostExpense(currentWeek.getDateStart(), currentWeek.getDateEnd());
                loadGraph(expenseLastWeek1, expenseCurrentWeek1, true);
            }
            else {
                int expenseLastMonth = loadTotalExpense(lasMonth.getDateStart(), lasMonth.getDateEnd());
                int expenseCurrentMonth = loadTotalExpense(currentMonth.getDateStart(), currentMonth.getDateEnd());
                loadTop3MostExpense(currentMonth.getDateStart(), currentMonth.getDateEnd());
                loadGraph(expenseLastMonth, expenseCurrentMonth, false);
            }
        });
    }

    private void loadTop3MostExpense(String dateStart, String dateEnd) {
        rvMostExpense.setLayoutManager(new LinearLayoutManager(this));

        List<OverallTransaction> data_mostExpense = dbHelper.getOverallTransactionsByCategory(dateStart, dateEnd);

        data_mostExpense.sort((o1, o2) -> Integer.compare(o2.getTotalAmount(), o1.getTotalAmount()));

        if (data_mostExpense.size() > 3) {
            data_mostExpense = data_mostExpense.subList(0, 3);
        }

        TransactionExpenseAdapter adapter_mostExpense = new TransactionExpenseAdapter(data_mostExpense);
        rvMostExpense.setAdapter(adapter_mostExpense);
    }

    private void loadRecentTrans() {
        rvRecentTrans.setLayoutManager(new LinearLayoutManager(this));

        List<Transaction> transList = dbHelper.getRecentTransactions();

        TransactionAdapter adapter_recentTrans = new TransactionAdapter(transList);
        rvRecentTrans.setAdapter(adapter_recentTrans);
    }

    private int loadTotalExpense(String dateStart, String dateEnd) {
        int totalExpense = dbHelper.getTotalExpenseInRange(dateStart, dateEnd);
        tvTotalExpense.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(totalExpense));
        return totalExpense;
    }

    private void loadCurrentMoney() {
        Wallet currentMoneyWallet = dbHelper.getCurrentMoney(currentDay.getDateEnd());
        tvCurrentMoney.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(currentMoneyWallet.getMoneyEnd()));
        tvCurrentMoney2.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(currentMoneyWallet.getMoneyEnd()));
    }

    private void loadGraph(int last, int current, boolean mode) {
        graphView.removeAllSeries();

        GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();
        gridLabel.setGridStyle(GridLabelRenderer.GridStyle.NONE);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        if (mode) {
            staticLabelsFormatter.setHorizontalLabels(new String[] {"Tuần trước", "Tuần này", ""});
        }
        else {
            staticLabelsFormatter.setHorizontalLabels(new String[] {"Tháng trước", "Tháng này", ""});
        }

        gridLabel.setLabelFormatter(staticLabelsFormatter);
        gridLabel.setHorizontalLabelsColor(Color.WHITE);

        gridLabel.setVerticalLabelsVisible(false);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0.4, last),
                new DataPoint(1.65, current)
        });
        series.setSpacing(45);
        series.setValuesOnTopColor(Color.WHITE);
        series.setValuesOnTopSize(36);
        series.setDrawValuesOnTop(true);
        series.setColor(Color.parseColor("#f15a5b"));
        graphView.addSeries(series);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        int maxY = Math.max(last, current);
        graphView.getViewport().setMaxY(maxY*1.1);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(-1);
        graphView.getViewport().setMaxX(3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_TRANSACTION) {
            if (resultCode == Activity.RESULT_OK) {
                int expenseLastWeek = loadTotalExpense(lasWeek.getDateStart(), lasWeek.getDateEnd());
                int expenseCurrentWeek = loadTotalExpense(currentWeek.getDateStart(), currentWeek.getDateEnd());
                loadTop3MostExpense(currentWeek.getDateStart(), currentWeek.getDateEnd());
                loadGraph(expenseLastWeek, expenseCurrentWeek, true);
                loadRecentTrans();
                loadCurrentMoney();
            }
        }
    }

}