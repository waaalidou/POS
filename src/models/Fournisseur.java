package models;

public class Fournisseur {
    private int id;
    private String name;
    private String num_tele;
    private String code;
    private String adresse;

    public Fournisseur() {
    }

    public Fournisseur(int id, String name, String num_tele, String code, String adresse) {
        this.id = id;
        this.name = name;
        this.num_tele = num_tele;
        this.code = code;
        this.adresse = adresse;
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

    public String getNum_tele() {
        return num_tele;
    }

    public void setNum_tele(String num_tele) {
        this.num_tele = num_tele;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
