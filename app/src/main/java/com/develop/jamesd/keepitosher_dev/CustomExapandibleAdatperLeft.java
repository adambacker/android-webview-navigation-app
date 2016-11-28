package com.develop.jamesd.keepitosher_dev;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mac-772 on 10/1/16.
 */
public class CustomExapandibleAdatperLeft extends BaseExpandableListAdapter {
    private Context context;
    private List<DrawItemLeft> expandableListTitle;
    private HashMap<DrawItemLeft, List<DrawItemLeft>> expandableListDetail;

    public CustomExapandibleAdatperLeft(Context context, List<DrawItemLeft> expandableListTitle, HashMap<DrawItemLeft, List<DrawItemLeft>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public DrawItemLeft getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public DrawItemLeft getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.left_nav_item, parent, false);
        }
        DrawItemLeft drawItemLeft = this.expandableListTitle.get(groupPosition);

        RelativeLayout item_view = (RelativeLayout) convertView.findViewById(R.id.item_view);
        item_view.setBackgroundColor(Color.parseColor(drawItemLeft.getItemBackColor()));

        LinearLayout item_mark = (LinearLayout) convertView.findViewById(R.id.item_mark);
        item_mark.setBackgroundColor(Color.parseColor(drawItemLeft.getMarkColor()));

        ImageView item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
        String iconUrl = (drawItemLeft.getImageUrl().equals("")) ? "error" :  drawItemLeft.getImageUrl();
        if (iconUrl.equals("error")) {
            item_icon.setImageResource(drawItemLeft.getImageResourceID());
        } else {
            Picasso.with(context).load(iconUrl).error(drawItemLeft.getImageResourceID()).into(item_icon);
        }
        item_icon.setColorFilter(Color.parseColor(drawItemLeft.getIconColor()), PorterDuff.Mode.SRC_IN);

        TextView item_title = (TextView) convertView.findViewById(R.id.item_title);
        item_title.setText(drawItemLeft.getItemTitle());
        item_title.setTextColor(Color.parseColor(drawItemLeft.getItemTitleColor()));

        ImageView item_arrow = (ImageView) convertView.findViewById(R.id.item_arrow);
        item_arrow.setImageResource(drawItemLeft.getArrowResourceID());
        item_arrow.setColorFilter(Color.parseColor(drawItemLeft.getIconColor()), PorterDuff.Mode.SRC_IN);

        return convertView;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.left_nav_item_child, parent, false);
        }
        DrawItemLeft drawItemLeft = getChild(groupPosition, childPosition);

        LinearLayout item_view = (LinearLayout) convertView.findViewById(R.id.item_view);
        item_view.setBackgroundColor(Color.parseColor(drawItemLeft.getItemBackColor()));

        TextView textView = (TextView) convertView.findViewById(R.id.item_title);
        textView.setText(drawItemLeft.getItemTitle());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}