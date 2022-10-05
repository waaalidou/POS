
package models;

public class SelectProduit {
    private int id;
    private String nom;
    private String code;
    private int quantite;
    private int quantite_min;
    private String category;
    private String fournisseur;

    public SelectProduit(int id, String nom, String code, int quantite, int quantite_min, String category, String fournisseur) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.quantite = quantite;
        this.quantite_min = quantite_min;
        this.category = category;
        this.fournisseur = fournisseur;
    }

    public SelectProduit() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }


}
