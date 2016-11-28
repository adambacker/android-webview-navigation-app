package com.develop.jamesd.keepitosher_dev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ImageButton showSearchBtn;
    EditText etKeyword;
    String keyword = "";
    ExpandableRelativeLayout searchExpandLayout;
    ExpandableRelativeLayout settingsExpandLayout;
    WebView webView;
    NavigationView navigationView_left, navigationView_right;
    View view = null;

    boolean flag1=false,flag2=false;
    boolean flag3=false,flag4=false;
    String loginResponse = "";
    String session_userid = "";
    String session = "";
    String details_userid = "";
    String access;
    String mail_count="0";

    String[] itemMarkColorArray;
    String[] itemBackColorArray_clicked;
    String[] itemTitleArray;
    String[] itemIconUrlArray;
    String[] itemLinkUrlArray;
    String[] itemStatusArray;
    HashMap<String, Integer> itemIconResourceArray;
    HashMap<String, String> itemBackColorArray;

    String[] childItemRootTitleArray;
    String[] childItemTitleArray;
    String[] childItemLinkUrlArray;
    String[] childItemStatusArray;

    String[] right_itemTitleArray;
    String[] right_itemIconUrlArray;
    HashMap<String, Integer> right_itemIconResourceArray;

    String[] right_itemUrlArray;

    ExpandableListView listView_left;
    ExpandableListView listView_right;

    List<DrawItemLeft> itemDrawerList_left = new ArrayList<>();
    List<DrawItemRight> itemDrawerList_right = new ArrayList<>();

    HashMap<DrawItemLeft, List<DrawItemLeft>> listHashMap_left = new HashMap<>();
    HashMap<DrawItemRight, List<DrawItemRight>> listHashMap_right = new HashMap<>();

    List<DrawItemLeft> inititemDrawerList;
    List<DrawItemRight> right_inititemDrawerList;
    HashMap<DrawItemLeft, List<DrawItemLeft>> initHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent myIntent = getIntent();
        if (myIntent != null) {
            loginResponse = myIntent.getStringExtra("response");
        }

        listView_left = (ExpandableListView) findViewById(R.id.left_drawer);
        listView_right = (ExpandableListView) findViewById(R.id.right_drawer);

        getLoginInfo();
        getMailCount();
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        searchExpandLayout = (ExpandableRelativeLayout) findViewById(R.id.searchExpandableLayout);
        settingsExpandLayout = (ExpandableRelativeLayout) findViewById(R.id.settinsExpandableLayout);

        showSearchBtn = (ImageButton) findViewById(R.id.showSearchBtn);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        etKeyword = (EditText) findViewById(R.id.etSearchKey);

        etKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Toast.makeText(getApplicationContext(), ""+keyCode, Toast.LENGTH_LONG).show();
                //when press Enter key
                if (keyCode == 66) {
                    onSearch(v);
                }
                return false;
            }
        });

    }

    public void getLoginInfo() {
        try {
            JSONObject jsonObject = (new JSONObject(loginResponse)).getJSONObject("response");
            JSONObject sessiondetail_json = jsonObject.getJSONObject("session_detail");
            session_userid = sessiondetail_json.getString("user_id");
            session = sessiondetail_json.getString("session");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (session_userid.equals("")) {
            Toast.makeText(getApplicationContext(), "userid empty.", Toast.LENGTH_LONG).show();
            return;
        }
        if (session.equals("")) {
            Toast.makeText(getApplicationContext(), "session empty.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void getMailCount() {
        String url = Constants.backendURL;

        JSONObject json = new JSONObject();
        JSONObject request_json = new JSONObject();
        JSONObject userinfo_json = new JSONObject();
        JSONObject requestinfo_json = new JSONObject();
        try {
            request_json.put("type", "mailcount");
            request_json.put("devicetype","Android");
            userinfo_json.put("user_id", session_userid);
            userinfo_json.put("session", session);
            requestinfo_json.put("mail_type", "inbox");
            json.put("request", request_json);
            json.put("userinfo", userinfo_json);
            json.put("requestinfo", requestinfo_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject response_json = response.getJSONObject("response");
                    String code = response_json.getString("code");
                    String message = response_json.getString("message");
//                    Toast.makeText(getApplicationContext(), response_json.toString(), Toast.LENGTH_LONG).show();
                    if (!code.equals("200")) {
                        return;
                    }
                    mail_count = response_json.getString("mails");
//                    Toast.makeText(getApplicationContext(), mail_count, Toast.LENGTH_LONG).show();
                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        BackendApplication.getsInstance().getRequestQueue().add(jsonObjectRequest);
    }


    ArrayList<String> mnNameArrayList = new ArrayList<>();
    ArrayList<String> mnIconArrayList = new ArrayList<>();
    ArrayList<String> mnUrlArrayList = new ArrayList<>();
    ArrayList<String> mnBorderStyleArrayList = new ArrayList<>();
    ArrayList<String> mnSelectStyleArrayList = new ArrayList<>();
    ArrayList<String> mnStatusArrayList = new ArrayList<>();

    ArrayList<String> submnRootNameArrayList = new ArrayList<>();
    ArrayList<String> submnNameArrayList = new ArrayList<>();
    ArrayList<String> submnIconArrayList = new ArrayList<>();
    ArrayList<String> submnUrlArrayList = new ArrayList<>();
    ArrayList<String> submnBorderStyleArrayList = new ArrayList<>();
    ArrayList<String> submnSelectStyleArrayList = new ArrayList<>();
    ArrayList<String> submnStatusArrayList = new ArrayList<>();

    ArrayList<String> right_mnNameArrayList = new ArrayList<>();
    ArrayList<String> right_mnIconArrayList = new ArrayList<>();
    ArrayList<String> right_mnUrlArrayList = new ArrayList<>();

    public void getData() {

        // Get json data from backend website.
        try {
            JSONObject jsonObject = (new JSONObject(loginResponse)).getJSONObject("response");
            JSONArray menus_jsonArray = jsonObject.getJSONArray("menus");

            for (int j = 0;j<menus_jsonArray.length();j++) {
                JSONObject mn_json = menus_jsonArray.getJSONObject(j);
                mnNameArrayList.add(mn_json.getString("name"));
                mnIconArrayList.add(mn_json.getString("icon"));
                mnUrlArrayList.add(mn_json.getString("url"));
                mnBorderStyleArrayList.add(mn_json.getString("border_style"));
                mnSelectStyleArrayList.add(mn_json.getString("select_style"));
                mnStatusArrayList.add(mn_json.getString("status"));
                String url = mn_json.getString("url");
                if (url.equals("")) {
                    JSONArray submn_jsonArray = mn_json.getJSONArray("sub_menus");
                    for (int jj=0;jj<submn_jsonArray.length();jj++) {
                        submnRootNameArrayList.add(mn_json.getString("name"));
                        submnNameArrayList.add(submn_jsonArray.getJSONObject(jj).getString("name"));
                        submnIconArrayList.add(submn_jsonArray.getJSONObject(jj).getString("icon"));
                        submnUrlArrayList.add(submn_jsonArray.getJSONObject(jj).getString("url"));
                        submnBorderStyleArrayList.add(submn_jsonArray.getJSONObject(jj).getString("border_style"));
                        submnSelectStyleArrayList.add(submn_jsonArray.getJSONObject(jj).getString("select_style"));
                        submnStatusArrayList.add(submn_jsonArray.getJSONObject(jj).getString("status"));
                    }
                }
            }

            //add Country item
            mnNameArrayList.add("Country");
            mnIconArrayList.add("");
            mnUrlArrayList.add("");
            mnBorderStyleArrayList.add("#435c72");
            mnSelectStyleArrayList.add("#4e6883");
            mnStatusArrayList.add("1");
            JSONArray country_submnArray = jsonObject.getJSONArray("country");
            for (int jj=0;jj<country_submnArray.length();jj++) {
                submnRootNameArrayList.add("Country");
                submnNameArrayList.add(country_submnArray.getJSONObject(jj).getString("country"));
                submnIconArrayList.add("");
                submnUrlArrayList.add("country");
                submnBorderStyleArrayList.add("");
                submnSelectStyleArrayList.add("");
                submnStatusArrayList.add("1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //add Settings item
        mnNameArrayList.add("Settings");
        mnIconArrayList.add("");
        mnUrlArrayList.add("settings");
        mnBorderStyleArrayList.add("#125c01");
        mnSelectStyleArrayList.add("#198600");
        mnStatusArrayList.add("1");

        itemBackColorArray = new HashMap<String, String>();
        itemBackColorArray.put("0", "#d63d44");
        itemBackColorArray.put("1", "#222222");
        itemMarkColorArray = mnBorderStyleArrayList.toArray(new String[mnBorderStyleArrayList.size()]);
        itemBackColorArray_clicked = mnSelectStyleArrayList.toArray(new String[mnSelectStyleArrayList.size()]);
        itemIconUrlArray = mnIconArrayList.toArray(new String[mnIconArrayList.size()]);
        itemIconResourceArray = new HashMap<String, Integer>();
        itemIconResourceArray.put("Home", R.drawable.ic_menu_home);
        itemIconResourceArray.put("Directory", R.drawable.ic_menu_directory);
        itemIconResourceArray.put("Food & Drink", R.drawable.ic_menu_food_drink);
        itemIconResourceArray.put("Jobs", R.drawable.ic_menu_job_search);
        itemIconResourceArray.put("Offers", R.drawable.ic_menu_offers);
        itemIconResourceArray.put("Community", R.drawable.ic_message_icon);
        itemIconResourceArray.put("Other Services", R.drawable.ic_menu_other_services);
        itemIconResourceArray.put("Country", R.drawable.ic_menu_country);
        itemIconResourceArray.put("Settings", R.drawable.ic_menu_settings);
        itemTitleArray = mnNameArrayList.toArray(new String[mnNameArrayList.size()]);
        itemStatusArray = mnStatusArrayList.toArray(new String[mnStatusArrayList.size()]);
        itemLinkUrlArray = mnUrlArrayList.toArray(new String[mnUrlArrayList.size()]);

        childItemRootTitleArray = submnRootNameArrayList.toArray(new String[submnRootNameArrayList.size()]);
        childItemTitleArray = submnNameArrayList.toArray(new String[submnNameArrayList.size()]);
        childItemLinkUrlArray = submnUrlArrayList.toArray(new String[submnUrlArrayList.size()]);
        childItemStatusArray = submnStatusArrayList.toArray(new String[submnStatusArrayList.size()]);

        // left menu add item /////////////////////////
        itemDrawerList_left = new ArrayList<>();
        for (int jj =0;jj<itemTitleArray.length;jj++) {

            if (!itemLinkUrlArray[jj].equals("")) {
                DrawItemLeft drawItemLeft = new DrawItemLeft(
                        itemBackColorArray.get(itemStatusArray[jj]),
                        itemMarkColorArray[jj],
                        itemIconUrlArray[jj],
                        itemIconResourceArray.get(itemTitleArray[jj]).intValue(),
                        itemTitleArray[jj],
                        itemLinkUrlArray[jj]
                );
                listHashMap_left.put(drawItemLeft, new ArrayList<DrawItemLeft>());
                itemDrawerList_left.add(drawItemLeft);
            } else {
                DrawItemLeft drawItemLeft = new DrawItemLeft(
                        itemBackColorArray.get(itemStatusArray[jj]),
                        itemMarkColorArray[jj],
                        itemIconUrlArray[jj],
                        itemIconResourceArray.get(itemTitleArray[jj]).intValue(),
                        itemTitleArray[jj],
                        itemLinkUrlArray[jj],
                        R.drawable.ic_arrow_icon
                );
                List<DrawItemLeft> childList = new ArrayList<>();
                for (int jjj = 0;jjj<childItemTitleArray.length;jjj++) {
                    if (childItemRootTitleArray[jjj].equals(itemTitleArray[jj])) {
                        childList.add(new DrawItemLeft(
                                itemBackColorArray.get(childItemStatusArray[jjj]),
                                null,
                                null,
                                0,
                                childItemTitleArray[jjj],
                                null));
                    }
                }
                listHashMap_left.put(drawItemLeft, childList);
                itemDrawerList_left.add(drawItemLeft);
            }
        }

        // right menu add item -------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------------------
        try {
            JSONObject jsonObject = (new JSONObject(loginResponse)).getJSONObject("response");
            JSONArray right_menus_jsonArray = jsonObject.getJSONArray("right_menus");
            for (int j=0;j<right_menus_jsonArray.length();j++) {
                JSONObject mn_json = right_menus_jsonArray.getJSONObject(j);
                right_mnNameArrayList.add(mn_json.getString("name"));
                right_mnIconArrayList.add(mn_json.getString("icon"));
                right_mnUrlArrayList.add(mn_json.getString("url"));
                if (mn_json.getString("name").equals("My Mail")) {
                    mail_count = String.format("%d",mn_json.getInt("unread_mail_count"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        right_itemIconResourceArray = new HashMap<String, Integer>();
        right_itemIconResourceArray.put("ADMIN PANEL", R.drawable.ic_user_icon);
        right_itemIconResourceArray.put("MODERATOR PANEL", R.drawable.ic_user_icon);
        right_itemIconResourceArray.put("My Mail", R.drawable.ic_mail_icon);
        right_itemIconResourceArray.put("Following", R.drawable.ic_star_icon);
        right_itemIconResourceArray.put("My Profile", R.drawable.ic_user_icon);
        right_itemIconResourceArray.put("Add Business", R.drawable.ic_business_icon);

        right_itemTitleArray = right_mnNameArrayList.toArray(new String[right_mnNameArrayList.size()]);
        right_itemIconUrlArray = right_mnIconArrayList.toArray(new String[right_mnIconArrayList.size()]);
        right_itemUrlArray = right_mnUrlArrayList.toArray(new String[right_mnUrlArrayList.size()]);

        itemDrawerList_right = new ArrayList<>();
        for (int j=0;j<right_itemTitleArray.length;j++) {
            if (right_itemTitleArray[j].equals("My Mail")) {
                DrawItemRight drawItemRight = new DrawItemRight(
                        right_itemIconUrlArray[j],
                        right_itemIconResourceArray.get(right_itemTitleArray[j]),
                        right_itemTitleArray[j],
                        right_itemUrlArray[j],
                        mail_count
                );
                listHashMap_right.put(drawItemRight, new ArrayList<DrawItemRight>());
                itemDrawerList_right.add(drawItemRight);
            } else {
                DrawItemRight drawItemRight = new DrawItemRight(
                        right_itemIconUrlArray[j],
                        right_itemIconResourceArray.get(right_itemTitleArray[j]),
                        right_itemTitleArray[j],
                        right_itemUrlArray[j],
                        "0"
                );
                listHashMap_right.put(drawItemRight, new ArrayList<DrawItemRight>());
                itemDrawerList_right.add(drawItemRight);
            }
        }


        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------

        inititemDrawerList = itemDrawerList_left;
        right_inititemDrawerList = itemDrawerList_right;

        final CustomExapandibleAdatperLeft adatper_left = new CustomExapandibleAdatperLeft( this, itemDrawerList_left, listHashMap_left);
        final CustomExapandibleAdatperRight adatper_right = new CustomExapandibleAdatperRight( this, itemDrawerList_right, listHashMap_right);

        listView_left.setGroupIndicator(null);
        listView_right.setGroupIndicator(null);

        listView_left.setAdapter(adatper_left);
        listView_right.setAdapter(adatper_right);

        listView_left.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                /// highlight----------------------------
                itemDrawerList_left = inititemDrawerList;
                for (int jj = 0; jj < itemDrawerList_left.size(); jj++) {
                    itemDrawerList_left.get(jj).setIconColor("#666666");
                    itemDrawerList_left.get(jj).setItemTitleColor("#999999");
                    itemDrawerList_left.get(jj).setItemBackColor("#222222");
                    if (itemDrawerList_left.get(jj).getItemLinkUrl().equals(""))
                        itemDrawerList_left.get(jj).setArrowResourceID(R.drawable.ic_arrow_icon);
                }
                itemDrawerList_left.get(groupPosition).setIconColor("#ffffff");
                itemDrawerList_left.get(groupPosition).setItemBackColor(itemBackColorArray_clicked[groupPosition]);
                itemDrawerList_left.get(groupPosition).setItemTitleColor("#ffffff");
                if (itemDrawerList_left.get(groupPosition).getItemLinkUrl().equals(""))
                    itemDrawerList_left.get(groupPosition).setArrowResourceID(R.drawable.ic_arrow_icon);
                adatper_left.notifyDataSetChanged();
                //-----------------------------------------------

                if (itemDrawerList_left.get(groupPosition).getItemLinkUrl().equals("settings")) {
                    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    settingsExpandLayout.toggle();
                    flag4 = !flag4;
                } else if (!itemDrawerList_left.get(groupPosition).getItemLinkUrl().equals("")) {
                    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if (flag4) {
                        settingsExpandLayout.toggle();
                        flag4 = !flag4;
                    }
                    if (flag3) {
                        showSearchBtn.setImageResource(R.drawable.ic_show_search);
                        flag3 = !flag3;
                        searchExpandLayout.toggle();
                    }
                    webView.loadUrl(itemDrawerList_left.get(groupPosition).getItemLinkUrl());
                }
                return false;
            }
        });

        listView_left.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int jj=0;jj<itemDrawerList_left.size();jj++) {
                    if(jj==groupPosition) continue;
                    listView_left.collapseGroup(jj);
                }
            }
        });

        listView_left.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int count = 0;
                for (int jjj = 0;jjj<childItemTitleArray.length;jjj++) {
                    if (itemDrawerList_left.get(groupPosition).getItemTitle().equals(childItemRootTitleArray[jjj])) {
                        if (count == childPosition) {
//                            Toast.makeText(getApplicationContext(), childItemLinkUrlArray[jjj], Toast.LENGTH_LONG).show();
                            if (childItemLinkUrlArray[jjj].equals("country")) {
                                Toast.makeText(getApplicationContext(), childItemTitleArray[jjj], Toast.LENGTH_LONG).show();
                            } else {
                                webView.loadUrl(childItemLinkUrlArray[jjj]);
                            }
                            listView_left.collapseGroup(groupPosition);
                            if (flag4){
                                settingsExpandLayout.toggle();
                                flag4 = !flag4;
                            }
                            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawerLayout.closeDrawer(GravityCompat.START);
                            if (flag3) {
                                showSearchBtn.setImageResource(R.drawable.ic_show_search);
                                flag3 = !flag3;
                                searchExpandLayout.toggle();
                            }
                        }
                        count++;
                    }
                }
                return false;
            }
        });

        listView_right.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                itemDrawerList_right = right_inititemDrawerList;
                for (int jj = 0; jj < itemDrawerList_right.size(); jj++) {
                    itemDrawerList_right.get(jj).setIconColor("#666666");
                    itemDrawerList_right.get(jj).setItemTitleColor("#999999");
                }
                itemDrawerList_right.get(groupPosition).setIconColor("#ffffff");
                itemDrawerList_right.get(groupPosition).setItemTitleColor("#ffffff");
                adatper_left.notifyDataSetChanged();

                drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.END);

                if (flag4) {
                    settingsExpandLayout.toggle();
                    flag4 = !flag4;
                }
                if (flag3) {
                    showSearchBtn.setImageResource(R.drawable.ic_show_search);
                    flag3 = !flag3;
                    searchExpandLayout.toggle();
                }
                webView.loadUrl(itemDrawerList_right.get(groupPosition).getItemLinkUrl());
                return false;
            }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(!url.startsWith("www.")&& !url.startsWith("https://")){
                url = "www."+url;
            }
            if(!url.startsWith("https://")){
                url = "https://"+url;
            }
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
            view.loadUrl(url);

            return true;
        }
    }

    public void showMainMenu(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);

    }
    public void showSearch(View view){
        if (!flag3) {
            showSearchBtn.setImageResource(R.drawable.ic_search_close);
            flag3 = !flag3;
        } else {
            showSearchBtn.setImageResource(R.drawable.ic_show_search);
            flag3 = !flag3;
        }
        searchExpandLayout.toggle();
    }

    public void onSearch(View view) {
        if (flag3) {
            showSearchBtn.setImageResource(R.drawable.ic_show_search);
            flag3 = !flag3;
            searchExpandLayout.toggle();
        }
        keyword = etKeyword.getText().toString();
        if (keyword.equals("") || keyword.length()<3) {
            Toast.makeText(getApplicationContext(), "Please Enter Minimum 3 Characters to Search", Toast.LENGTH_LONG).show();
            return;
        } else {
            webView.loadUrl("http://temp.keepitkosher.co.uk/search/results/directory?keyword=" + keyword);
            searchExpandLayout.toggle();
        }
    }

    public void showLogin(View view) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void onLogout(View view) {
        //////backend
        String url = Constants.backendURL;

        JSONObject json = new JSONObject();
        JSONObject request_json = new JSONObject();
        JSONObject userinfo_json = new JSONObject();
        try {
            request_json.put("type", "userLogout");
            request_json.put("devicetype","Android");
            userinfo_json.put("user_id", session_userid);
            userinfo_json.put("session", session);
            json.put("request", request_json);
            json.put("userinfo", userinfo_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject response_json = response.getJSONObject("response");
                    String code = response_json.getString("code");
                    String message = response_json.getString("message");
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    if (!code.equals("200")) {
                        return;
                    }
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        BackendApplication.getsInstance().getRequestQueue().add(jsonObjectRequest);

//        String url = Constants.backendURL + Constants.logoutURL;
//
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Toast.makeText(getApplicationContext(), "res " + response, Toast.LENGTH_LONG).show();
//                    String code = jsonObject.getString("code");
//                    if (!code.equals("200")) {
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
//                        String message = jsonObject1.getString("message");
//
//                        return;
//                    }
//                    Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    NetworkResponse response = error.networkResponse;
//                    Toast.makeText(getApplicationContext(), "error : " + response.statusCode, Toast.LENGTH_LONG).show();
//                }
//            }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put(Constants.userid, userid);
//                params.put(Constants.session, session);
//                params.put(Constants.devicetoken, androidid);
//                //  params.put(KEY_EMAIL, email);
//                return params;
//                //  return super.getParams();
//            }
//        };
//        BackendApplication.getsInstance().getRequestQueue().add(request);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        return true;
    }

}
