package vn.edu.tdtu.moneyintel.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class TimeLine {
    private String label;
    private String dateStart;
    private String dateEnd;

    public TimeLine() {
    }

    public TimeLine(String label, String dateStart, String dateEnd) {
        this.label = label;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public static TimeLine getInstance() {
        return new TimeLine();
    }

    public static TimeLine getCurrentDayTimeLine() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = now.format(formatter);
        return new TimeLine("HÔM NAY", date, date);
    }

    public static TimeLine getCurrentWeekTimeLine() {
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = now.with(DayOfWeek.SUNDAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStart = weekStart.format(formatter);
        String dateEnd = weekEnd.format(formatter);
        return new TimeLine("TUẦN NÀY", dateStart, dateEnd);
    }

    public static TimeLine getCurrentMonthTimeLine() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = now.with(TemporalAdjusters.lastDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStart = monthStart.format(formatter);
        String dateEnd = monthEnd.format(formatter);
        return new TimeLine("THÁNG NÀY", dateStart, dateEnd);
    }

    public static TimeLine getLastWeekTimeLine() {
        LocalDate now = LocalDate.now().minusWeeks(1);
        LocalDate weekStart = now.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = now.with(DayOfWeek.SUNDAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStart = weekStart.format(formatter);
        String dateEnd = weekEnd.format(formatter);
        return new TimeLine("TUẦN TRƯỚC", dateStart, dateEnd);
    }

    public static TimeLine getLastMonthTimeLine() {
        LocalDate now = LocalDate.now().minusMonths(1);
        LocalDate monthStart = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = now.with(TemporalAdjusters.lastDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStart = monthStart.format(formatter);
        String dateEnd = monthEnd.format(formatter);
        return new TimeLine("THÁNG TRƯỚC", dateStart, dateEnd);
    }

    public List<TimeLine> generateWeekTimeLines() {
        List<TimeLine> timeLines = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (int i = 8; i >= 0; i--) {
            LocalDate weekStart = currentDate.minusWeeks(i).with(DayOfWeek.MONDAY);
            LocalDate weekEnd = weekStart.plusDays(6);

            TimeLine timeLine;
            DateTimeFormatter UIFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter DBFormatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (i == 1) {
                timeLine = new TimeLine("Tuần trước", weekStart.format(DBFormatDate), weekEnd.format(DBFormatDate));
            } else if (i == 0) {
                timeLine = new TimeLine("Tuần này", weekStart.format(DBFormatDate), weekEnd.format(DBFormatDate));
            } else {
                String label = weekStart.format(UIFormatDate) + " - " + weekEnd.format(UIFormatDate);
                timeLine = new TimeLine(label, weekStart.format(DBFormatDate), weekEnd.format(DBFormatDate));
            }
            timeLines.add(timeLine);
        }
        return timeLines;
    }

    public List<TimeLine> generateMonthTimeLines() {
        List<TimeLine> timeLines = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (int i = 8; i >= 0; i--) {
            LocalDate monthStart = currentDate.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

            TimeLine timeLine;
            DateTimeFormatter UIFormatDate = DateTimeFormatter.ofPattern("MM/yyyy");
            DateTimeFormatter DBFormatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (i == 1) {
                timeLine = new TimeLine("Tháng trước", monthStart.format(DBFormatDate), monthEnd.format(DBFormatDate));
            } else if (i == 0) {
                timeLine = new TimeLine("Tháng này", monthStart.format(DBFormatDate), monthEnd.format(DBFormatDate));
            } else {
                String label = monthStart.format(UIFormatDate);
                timeLine = new TimeLine(label, monthStart.format(DBFormatDate), monthEnd.format(DBFormatDate));
            }
            timeLines.add(timeLine);
        }
        return timeLines;
    }

}
