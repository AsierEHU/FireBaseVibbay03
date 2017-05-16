package com.example.asier.vibbay03.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by asier on 06/05/2017.
 */

public class BidAdapter extends BaseAdapter{

    private final ArrayList<LinearLayout> aly;

    public BidAdapter(ArrayList<ArticleViews> avs, Context con){
        aly = new ArrayList<>();
        for(ArticleViews av:avs){
            aly.add(av.getBidView(con));
        }
    }

    @Override
    public int getCount() {
        return aly.size();
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
        return aly.get(position);
    }
}
