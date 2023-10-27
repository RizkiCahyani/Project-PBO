/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbouts;

/**
 *
 * @author LUI
 */
public class Buku {
 
    private String ISBN;
    private String JudulBuku;
    private String TahunTerbit;
    private String Penerbit;

    /**
     * @return the ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN the ISBN to set
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return the JudulBuku
     */
    public String getJudulBuku() {
        return JudulBuku;
    }

    /**
     * @param JudulBuku the JudulBuku to set
     */
    public void setJudulBuku(String JudulBuku) {
        this.JudulBuku = JudulBuku;
    }

    /**
     * @return the TahunTerbit
     */
    public String getTahunTerbit() {
        return TahunTerbit;
    }

    /**
     * @param TahunTerbit the TahunTerbit to set
     */
    public void setTahunTerbit(String TahunTerbit) {
        this.TahunTerbit = TahunTerbit;
    }

    /**
     * @return the Penerbit
     */
    public String getPenerbit() {
        return Penerbit;
    }

    /**
     * @param Penerbit the Penerbit to set
     */
    public void setPenerbit(String Penerbit) {
        this.Penerbit = Penerbit;
    }

}