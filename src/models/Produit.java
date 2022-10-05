package models;

public class Produit {
    private int id;
    private String code;
    private String nom;
    private int quantite;
    private int quantite_min;
    private int category;
    private int fournisseur;

    public Produit() {
    }

    @Override
    public String toString() {
        return "Produit{" + "id=" + id + ", code=" + code + ", nom=" + nom + ", quantite=" + quantite + ", quantite_min=" + quantite_min + ", category=" + category + ", fournisseur=" + fournisseur + '}';
    }

    public Produit(int id, String code, String nom, int quantite, int quantite_min, int category, int fournisseur) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.quantite = quantite;
        this.quantite_min = quantite_min;
        this.category = category;
        this.fournisseur = fournisseur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getQuantite_min() {
        return quantite_min;
    }

    public void setQuantite_min(int quantite_min) {
        this.quantite_min = quantite_min;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(int fournisseur) {
        this.fournisseur = fournisseur;
    }

}
