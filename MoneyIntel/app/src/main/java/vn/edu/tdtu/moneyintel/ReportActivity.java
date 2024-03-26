package vn.edu.tdtu.moneyintel;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.edu.tdtu.moneyintel.adapter.TimeLineAdapter;
import vn.edu.tdtu.moneyintel.database.DatabaseHelper;
import vn.edu.tdtu.moneyintel.model.TimeLine;

public class ReportActivity extends AppCompatActivity {
    RecyclerView rvTimeLine;
    ImageView imgvBack, imgvMenu;
    TextView tvMoneyStart, tvMoneyEnd, tvMoneyStatistical;
    TimeLineAdapter timeLineAdapter;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    TimeLine currentWeek = TimeLine.getCurrentWeekTimeLine();
    TimeLine currentMonth = TimeLine.getCurrentMonthTimeLine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // text view
        tvMoneyStart = findViewById(R.id.tvMoneyStart);
        tvMoneyEnd = findViewById(R.id.tvMoneyEnd);
        tvMoneyStatistical = findViewById(R.id.tvMoneyStatistical);
        // image view
        imgvBack = findViewById(R.id.imgvBack);
        imgvMenu = findViewById(R.id.imgvMenu);
        // recycler view
        rvTimeLine = findViewById(R.id.rvTimeLine);

        loadTimeLine(TimeLine.getInstance().generateWeekTimeLines());
        loadReport(currentWeek.getDateStart(), currentWeek.getDateEnd());
        loadGraphByDate();
        loadExpensePieGraph();
        loadIncomePieGraph();

        imgvBack.setOnClickListener(v -> finish());


        imgvMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(ReportActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.day:
                        return true;
                    case R.id.week:
                        loadTimeLine(TimeLine.getInstance().generateWeekTimeLines());
                        loadReport(currentWeek.getDateStart(), currentWeek.getDateEnd());
                        return true;
                    case R.id.month:
                        loadTimeLine(TimeLine.getInstance().generateMonthTimeLines());
                        loadReport(currentMonth.getDateStart(), currentMonth.getDateEnd());
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });
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

            loadReport(dateStart, dateEnd);
        });
    }
    
    private void loadGraphByDate() {
        GraphView graphView = findViewById(R.id.graphView);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, -1000000),
                new DataPoint(1, 100000),
                new DataPoint(2, 540000),
                new DataPoint(3, -79000),
                new DataPoint(4, -80000),
                new DataPoint(5, -60000),
                new DataPoint(6, -110000),
                new DataPoint(6, 450000),
                new DataPoint(7, -265000)
        });
        series.setSpacing(20);
        series.setValueDependentColor(data -> {
            int x,y,z;
            if(data.getY() < 0) {
                x=255;
                y=0;
                z=0;

            }
            else {
                x=0;
                y=0;
                z=255;
            }

            return Color.rgb(x, y,z );
        });
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphView.getGridLabelRenderer().setGridColor(Color.parseColor("#9a9a9a"));
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#9a9a9a"));
        graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#9a9a9a"));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(9);
        //graphView.getGridLabelRenderer().setNumVerticalLabels(20);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX && (value == 0 || value == 8)) {
                    return "";
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(-1200000);
        graphView.getViewport().setMaxY(600000);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(8);

        graphView.addSeries(series);
    }

    private void loadIncomePieGraph() {
        PieChart pieChart = findViewById(R.id.pcIncome);

        // Tạo dữ liệu cho biểu đồ tròn
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(70f, "Lương"));
        entries.add(new PieEntry(30f, "Đầu tư"));

        // Tạo dataset từ dữ liệu vừa tạo
        PieDataSet dataSet = new PieDataSet(entries, "Color Distribution");

        // Cấu hình màu sắc cho các phần tử trong dataset
        dataSet.setColors(Color.parseColor("#f918b6"), Color.parseColor("#7124ff"));
        dataSet.setDrawValues(false);

        // Tạo biểu đồ từ dataset vừa tạo
        PieData data = new PieData(dataSet);

        // Thiết lập các thuộc tính cho biểu đồ tròn
        pieChart.setData(data);
        pieChart.setDrawCenterText(false);
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(30f);
        pieChart.setHoleColor(Color.parseColor("#1c1c1e"));
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setEntryLabelColor(Color.BLUE);
        pieChart.setEntryLabelTextSize(12f);

        // Thay đổi tỷ lệ của biểu đồ
        pieChart.setScaleX(0.8f);
        pieChart.setScaleY(0.8f);
    }

    private void loadExpensePieGraph() {
        PieChart pieChart = findViewById(R.id.pcExpense);

        // Tạo dữ liệu cho biểu đồ tròn
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20f, "Vui chơi"));
        entries.add(new PieEntry(30f, "Ăn uống"));
        entries.add(new PieEntry(50f, "Hoá đơn nước"));

        // Tạo dataset từ dữ liệu vừa tạo
        PieDataSet dataSet = new PieDataSet(entries, "Color Distribution");

        // Cấu hình màu sắc cho các phần tử trong dataset
        dataSet.setColors(Color.parseColor("#00ffc8"), Color.parseColor("#fbff00"), Color.parseColor("#ff5500"));
        dataSet.setDrawValues(false);

        // Tạo biểu đồ từ dataset vừa tạo
        PieData data = new PieData(dataSet);

        // Thiết lập các thuộc tính cho biểu đồ tròn
        pieChart.setData(data);
        pieChart.setDrawCenterText(false);
        pieChart.setCenterTextSize(24f);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(30f);
        pieChart.setHoleColor(Color.parseColor("#1c1c1e"));
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setEntryLabelColor(Color.BLUE);
        pieChart.setEntryLabelTextSize(12f);

        // Thay đổi tỷ lệ của biểu đồ
        pieChart.setScaleX(0.8f);
        pieChart.setScaleY(0.8f);
    }
}