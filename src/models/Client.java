package models;

public class Client {
    private int id;
    private String nom;
    private String num_tele;
    private String Code;
    private String Adress;
    private String Abonement;

    public Client(int id, String nom, String num_tele, String code, String adress, String abonement) {
        this.id = id;
        this.nom = nom;
        this.num_tele = num_tele;
        Code = code;
        Adress = adress;
        Abonement = abonement;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", num_tele='" + num_tele + '\'' +
                ", Code='" + Code + '\'' +
                ", Adress='" + Adress + '\'' +
                ", Abonement='" + Abonement + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNum_tele() {
        return num_tele;
    }

    public void setNum_tele(String num_tele) {
        this.num_tele = num_tele;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getAbonement() {
        return Abonement;
    }

    public void setAbonement(String abonement) {
        Abonement = abonement;
    }

    public Client() {
    }


}
