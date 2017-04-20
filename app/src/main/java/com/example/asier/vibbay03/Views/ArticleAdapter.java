package com.example.asier.vibbay03.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by asier on 20/04/2017.
 */

public class ArticleAdapter extends BaseAdapter{

    private final ArrayList<ArticleViews> avs;
    private final Context con;

    public ArticleAdapter(ArrayList<ArticleViews> avs, Context con){
        this.avs = avs;
        this.con = con;
    }


    @Override
    public int getCount() {
        return avs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return avs.get(position).getTinyView(con);
    }
}
