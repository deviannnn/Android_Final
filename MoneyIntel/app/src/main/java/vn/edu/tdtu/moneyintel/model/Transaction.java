package vn.edu.tdtu.moneyintel.model;

public class Transaction {
    private int id;
    private int amount;
    private String note;
    private String date;
    private Category category;

    public Transaction(int id, int amount, String note, String date, Category category) {
        this.id = id;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.category = category;
    }

    public Transaction(int amount, String note, String date, Category category) {
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
