package com.example.opdshe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ListAdapter extends BaseAdapter {

    public ArrayList<String> listViewItemList =new ArrayList<String>();

    public ListAdapter(){}

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypage_list, parent, false);
        }
        //

        TextView ListName=convertView.findViewById(R.id.txt_list_name);

        String listViewItem = listViewItemList.get(position);

        ListName.setText(listViewItem);


        return convertView;

    }
    public void clear(){
        listViewItemList.clear();
        Log.d("Num of listviewitemlist", "number = " + listViewItemList.size());


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }



    public void  addAll(ArrayList<String> array){
        for(int i=0;i<array.size();i++){
            String item=new String();
            item=array.get(i);
            listViewItemList.add(item);
        }
    }
}
