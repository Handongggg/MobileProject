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
import java.util.StringTokenizer;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<ListViewItem> listViewItemList =new ArrayList<ListViewItem>();

    public ListViewAdapter(){}

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
            convertView = inflater.inflate(R.layout.taxi_rec, parent, false);
        }
        //
        ImageView imageIcon=convertView.findViewById(R.id.image_taxi);
        TextView titleText=convertView.findViewById(R.id.txt_title);
        TextView sourceText=convertView.findViewById(R.id.txt_source);
        TextView destText=convertView.findViewById(R.id.txt_dest);
        TextView timeText=convertView.findViewById(R.id.txt_time);
        TextView personnelText=convertView.findViewById(R.id.txt_personnel);

        ListViewItem listViewItem = listViewItemList.get(position);

        titleText.setText(listViewItem.titleStr);
        sourceText.setText(listViewItem.sourceStr);
        destText.setText(listViewItem.destStr);
        timeText.setText(listViewItem.timeStr);
        personnelText.setText(listViewItem.personnelStr);

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
            ListViewItem item=new ListViewItem();
            String temp=array.get(i);
            Log.d("Temp", "Temp= " + temp);
            StringTokenizer str= new StringTokenizer(temp,"/");
            item.titleStr=str.nextToken();
            item.sourceStr=str.nextToken();
            item.destStr=str.nextToken();
            item.timeStr=str.nextToken();
            item.personnelStr=str.nextToken();
            listViewItemList.add(item);
        }
    }
}
