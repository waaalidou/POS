package models;

public class Category {
    private int id;
    private String name;
    private  String code;

    public Category(String name,String code) {
        this.name = name;
        this.code = code;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {
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

    @Override
    public String toString() {
        return "CategoryController{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
