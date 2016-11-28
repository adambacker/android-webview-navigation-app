package com.develop.jamesd.keepitosher_dev;

import android.graphics.Color;

/**
 * Created by mac-772 on 10/1/16.
 */
public class DrawItemLeft
{
    private String itemBackColor;
    private String itemMarkColor;
    private int imageResourceID;
    private String imageUrl;
    private String iconColor;
    private String itemTitle;
    private String itemTitleColor;
    private int arrowResourceID;
    private String itemLinkUrl;



//    public DrawItemLeft(String itemMarkColor, int imageResourceID, String itemTitle, int arrowResourceID) {
//        this.itemMarkColor = itemMarkColor;
//        this.imageResourceID = imageResourceID;
//        this.itemTitle = itemTitle;
//        this.arrowResourceID = arrowResourceID;
//        this.itemTitleColor = "#999999";
//        this.itemBackColor = "#222222";
//        this.iconColor = "#666666";
//        this.itemLinkUrl = "";
//    }
//
//    public DrawItemLeft(String itemMarkColor, int imageResourceID, String itemTitle) {
//        this.itemMarkColor = itemMarkColor;
//        this.imageResourceID = imageResourceID;
//        this.itemTitle = itemTitle;
//        this.arrowResourceID = 0;
//        this.itemTitleColor = "#999999";
//        this.itemBackColor = "#222222";
//        this.iconColor = "#666666";
//        this.itemLinkUrl = "";
//    }
//
//    public DrawItemLeft(String itemMarkColor, String imageUrl, String itemTitle) {
//        this.itemMarkColor = itemMarkColor;
//        this.itemTitle = itemTitle;
//        this.imageUrl = imageUrl;
//        this.arrowResourceID = 0;
//        this.itemTitleColor = "#999999";
//        this.itemBackColor = "#222222";
//        this.iconColor = "#666666";
//        this.itemLinkUrl = "";
//    }
//
//    public DrawItemLeft(String itemMarkColor, String imageUrl, String itemTitle, int arrowResourceID) {
//        this.itemMarkColor = itemMarkColor;
//        this.itemTitle = itemTitle;
//        this.imageUrl = imageUrl;
//        this.arrowResourceID = arrowResourceID;
//        this.itemTitleColor = "#999999";
//        this.itemBackColor = "#222222";
//        this.iconColor = "#666666";
//        this.itemLinkUrl = "";
//    }

    public DrawItemLeft(String itemBackColor, String itemMarkColor, String imageUrl, int imageResourceID, String itemTitle , String itemLinkUrl) {
        this.itemMarkColor = itemMarkColor;
        this.itemTitle = itemTitle;
        this.imageUrl = imageUrl;
        this.imageResourceID = imageResourceID;
        this.arrowResourceID = 0;
        this.itemTitleColor = "#999999";
        this.itemBackColor = itemBackColor;
        this.iconColor = "#666666";
        this.itemLinkUrl = itemLinkUrl;
    }

    public DrawItemLeft(String itemBackColor, String itemMarkColor, String imageUrl, int imageResourceID, String itemTitle, String itemLinkUrl, int arrowResourceID) {
        this.itemMarkColor = itemMarkColor;
        this.itemTitle = itemTitle;
        this.imageUrl = imageUrl;
        this.imageResourceID = imageResourceID;
        this.arrowResourceID = arrowResourceID;
        this.itemTitleColor = "#999999";
        this.itemBackColor = itemBackColor;
        this.iconColor = "#666666";
        this.itemLinkUrl = itemLinkUrl;
    }

    // item MarkColor
    public String getMarkColor() { return itemMarkColor;}
    public void setMarkColor(String colorStr) { this.itemMarkColor = colorStr; }
    // item icon
    public int getImageResourceID() {
        return imageResourceID;
    }
    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getIconColor() { return iconColor; }
    public void setIconColor(String iconColor) { this.iconColor = iconColor; }

    public String getItemTitle() {
        return itemTitle;
    }
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemTitleColor() { return itemTitleColor; }
    public void setItemTitleColor(String colorStr) { this.itemTitleColor = colorStr; }

    public int getArrowResourceID() {
        return arrowResourceID;
    }
    public void setArrowResourceID(int arrowResourceID) { this.arrowResourceID = arrowResourceID; }

    public String getItemBackColor() { return itemBackColor; }
    public void setItemBackColor(String colorStr) { this.itemBackColor = colorStr; }

    public String getItemLinkUrl() { return itemLinkUrl; }
    public void setItemLinkUrl(String itemLinkUrl) { this.itemLinkUrl = itemLinkUrl; }

}
