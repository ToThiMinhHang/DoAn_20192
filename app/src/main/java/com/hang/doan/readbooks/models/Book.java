package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private Long id_tac_gia;
    private String imageURL;
    private Long id_tac_pham;
    private List<String> mucSach;
    private String name;
    private List<Chapter> chapter;

    public List<Chapter> getChapter() {
        return chapter;
    }

    public void setChapter(List<Chapter> chapter) {
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getId_tac_gia() {
        return id_tac_gia;
    }

    public void setId_tac_gia(Long id_tac_gia) {
        this.id_tac_gia = id_tac_gia;
    }

    public List<String> getMucSach() {
        return mucSach;
    }

    public void setMucSach(List<String> mucSach) {
        this.mucSach = mucSach;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public Book() {
        chapter = new ArrayList<>();
        mucSach = new ArrayList<>();
    }


    public Long getId_tac_pham() {
        return id_tac_pham;
    }

    public void setId_tac_pham(Long id_tac_pham) {
        this.id_tac_pham = id_tac_pham;
    }


}
