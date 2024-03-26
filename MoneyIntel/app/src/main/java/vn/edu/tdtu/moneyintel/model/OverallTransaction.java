package vn.edu.tdtu.moneyintel.model;

import java.util.List;

public class OverallTransaction {
    private int totalAmount;
    private String date;
    private Category category;
    private List<Transaction> transactions;

    public OverallTransaction(int totalAmount, Category category, List<Transaction> transactions) {
        this.totalAmount = totalAmount;
        this.category = category;
        this.transactions = transactions;
    }

    public OverallTransaction(int totalAmount, String date, List<Transaction> transactions) {
        this.totalAmount = totalAmount;
        this.date = date;
        this.transactions = transactions;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
