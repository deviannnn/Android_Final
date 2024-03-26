package vn.edu.tdtu.moneyintel.model;

public class Category {
    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_INCOME = 1;
    private int id;
    private String name;
    private int type;
    private String icon;

    public Category(int id, String name, int type, String icon) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public Category(String name, int type, String icon) {
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
