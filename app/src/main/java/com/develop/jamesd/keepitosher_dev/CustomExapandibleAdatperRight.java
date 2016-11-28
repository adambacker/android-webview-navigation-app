package com.develop.jamesd.keepitosher_dev;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mac-772 on 10/1/16.
 */
public class CustomExapandibleAdatperRight extends BaseExpandableListAdapter {
    private Context context;
    private List<DrawItemRight> expandableListTitle;
    private HashMap<DrawItemRight, List<DrawItemRight>> expandableListDetail;

    public CustomExapandibleAdatperRight(Context context, List<DrawItemRight> expandableListTitle, HashMap<DrawItemRight, List<DrawItemRight>> expandableListDetail) {
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
    public DrawItemRight getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public DrawItemRight getChild(int groupPosition, int childPosition) {
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
            convertView = inflater.inflate(R.layout.right_nav_item, parent, false);
        }
        DrawItemRight drawItemRight = this.expandableListTitle.get(groupPosition);

        ImageView item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
        String iconUrl = (drawItemRight.getImageUrl().equals("")) ? "error" :  drawItemRight.getImageUrl();
        if (iconUrl.equals("error")) {
            item_icon.setImageResource(drawItemRight.getImageResourceID());
        } else {
            Picasso.with(context).load(iconUrl).error(drawItemRight.getImageResourceID()).into(item_icon);
        }
        item_icon.setColorFilter(Color.parseColor(drawItemRight.getIconColor()), PorterDuff.Mode.SRC_IN);


        TextView item_title = (TextView) convertView.findViewById(R.id.item_title);
        item_title.setText(drawItemRight.getItemTitle());
        item_title.setTextColor(Color.parseColor(drawItemRight.getItemTitleColor()));

        ImageView item_mail_count_back = (ImageView) convertView.findViewById(R.id.item_mail_count_back);
        TextView item_mail_count = (TextView) convertView.findViewById(R.id.item_mail_count);
        if (!drawItemRight.getItemMailCount().equals("0")) {
            item_mail_count_back.setImageResource(R.drawable.ic_rect_circle);
            item_mail_count_back.setColorFilter(Color.parseColor("#19b5e9"), PorterDuff.Mode.SRC_IN);
            item_mail_count.setText(drawItemRight.getItemMailCount());
        } else {
            item_mail_count.setText("");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.right_nav_item_child, parent, false);
        }
        DrawItemRight drawItemRight = getChild(groupPosition, childPosition);

        TextView textView = (TextView) convertView.findViewById(R.id.item_title);
        textView.setText(drawItemRight.getItemTitle());

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