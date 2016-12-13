package com.albertbelardino.bitcoindashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by alber on 11/30/2016.
 */
public class PriceChartAdapter extends ArrayAdapter{
    public String[] timePeriods;
    public String[] actualTimePeriods = {"1d", "5d", "7d", "15d"};
    public int cur;

    public PriceChartAdapter(Context context, int resource, Object[] timePeriods) {
        super(context, resource, timePeriods);
        timePeriods = (String[]) timePeriods;
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }

    public int getCur(){
        return cur;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v =  super.getDropDownView(position, convertView, parent);

        cur = position;

        return v;
    }
}
