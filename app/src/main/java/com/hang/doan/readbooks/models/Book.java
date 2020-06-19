package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private String id_tac_gia;
    private String imageURL;
    private String id_tac_pham;
    private List<String> mucSach;
    private String name;
    private List<Chapter> chapter;
    private String authorName;

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


    public String getId_tac_gia() {
        return id_tac_gia;
    }

    public void setId_tac_gia(String id_tac_gia) {
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Book() {
        chapter = new ArrayList<>();
        mucSach = new ArrayList<>();
    }


    public String getId_tac_pham() {
        return id_tac_pham;
    }

    public void setId_tac_pham(String id_tac_pham) {
        this.id_tac_pham = id_tac_pham;
    }

    public boolean search(String keyword) {
        return name.contains(keyword) || authorName.contains(keyword);
    }

}
