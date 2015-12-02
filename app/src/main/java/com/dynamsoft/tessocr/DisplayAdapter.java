package com.dynamsoft.tessocr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lyndis on 12/1/15.
 */
public class DisplayAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> data;


    public DisplayAdapter(Context c, ArrayList<String> inData) {
        this.mContext = c;
        this.data=inData;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int pos, View child,ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutinflater;
        if (child==null) {
            layoutinflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutinflater.inflate(R.layout.listcell, null);
            mHolder = new Holder();
            mHolder.txt_data = (TextView) child.findViewById(R.id.txt_data);

            child.setTag(mHolder);
        }else {
            mHolder = (Holder) child.getTag();
        }
        mHolder.txt_data.setText(data.get(pos));

        return child;
        }
    public class Holder {

        TextView txt_data;
    }
}

