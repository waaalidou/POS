package models;

public class Abonement {
    private int id;
    private String name;
    private  String code;

    public Abonement(String name,String code) {
        this.name = name;
        this.code = code;
    }

    public Abonement(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Abonement() {
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
