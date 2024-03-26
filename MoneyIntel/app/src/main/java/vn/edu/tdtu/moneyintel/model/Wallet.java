package vn.edu.tdtu.moneyintel.model;

public class Wallet {
    private int id;
    private int moneyStart;
    private int moneyEnd;
    private String date;

    public Wallet(int moneyStart, int moneyEnd, String date) {
        this.moneyStart = moneyStart;
        this.moneyEnd = moneyEnd;
        this.date = date;
    }

    public Wallet(int id, int moneyStart, int moneyEnd, String date) {
        this.id = id;
        this.moneyStart = moneyStart;
        this.moneyEnd = moneyEnd;
        this.date = date;
    }

    public Wallet(int moneyStart, int moneyEnd) {
        this.moneyStart = moneyStart;
        this.moneyEnd = moneyEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoneyStart() {
        return moneyStart;
    }

    public void setMoneyStart(int moneyStart) {
        this.moneyStart = moneyStart;
    }

    public int getMoneyEnd() {
        return moneyEnd;
    }

    public void setMoneyEnd(int moneyEnd) {
        this.moneyEnd = moneyEnd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
