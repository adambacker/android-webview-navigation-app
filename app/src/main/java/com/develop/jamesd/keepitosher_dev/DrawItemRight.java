package com.develop.jamesd.keepitosher_dev;

/**
 * Created by mac-772 on 10/1/16.
 */
public class DrawItemRight
{
    private int imageResourceID;
    private String itemIconUrl;
    private String itemTitle;
    private String itemMailCount;
    private String iconColor;
    private String itemTitleColor;
    private String itemLinkUrl;



    public DrawItemRight(String itemIconUrl, int imageResourceID, String itemTitle, String itemLinkUrl, String itemMailCount) {
        this.itemIconUrl = itemIconUrl;
        this.imageResourceID = imageResourceID;
        this.itemTitle = itemTitle;
        this.itemMailCount = itemMailCount;
        this.itemTitleColor ="#999999";
        this.itemLinkUrl = itemLinkUrl;
        this.iconColor = "#666666";
    }

    public String getImageUrl() {
        return itemIconUrl;
    }
    public void setImageUrl(String itemIconUrl) {
        this.itemIconUrl = itemIconUrl;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }
    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public String getItemTitle() {
        return itemTitle;
    }
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemTitleColor() { return itemTitleColor; }
    public void setItemTitleColor(String colorStr) { this.itemTitleColor = colorStr; }

    public String getItemMailCount() { return itemMailCount; }
    public void setItemMailCount(String itemMailCount) {
        this.itemMailCount = itemMailCount;
    }

    public String getIconColor() { return iconColor; }
    public void setIconColor(String iconColor) { this.iconColor = iconColor; }

    public String getItemLinkUrl() { return itemLinkUrl; }
    public void setItemLinkUrl(String itemLinkUrl) { this.itemLinkUrl = itemLinkUrl; }

}
