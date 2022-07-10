package com.linea.uno.applineav1.adapter;

public class SliderItem {

    private String title;
    private int image;

    private SliderItem(){}

    public SliderItem(int image, String title) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
