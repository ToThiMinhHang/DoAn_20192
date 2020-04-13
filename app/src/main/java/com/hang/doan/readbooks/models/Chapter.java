package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chapter {

    Date editTime;
    String price;
    String chapterName;
    String data;

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getTime() {
        return editTime;
    }

    public void setTime(Date time) {
        this.editTime = time;
    }

}
