package pl.javastart.bookshop.model;

public class BookCategory {
    private int id;
    private String name;

    public BookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookCategory() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}