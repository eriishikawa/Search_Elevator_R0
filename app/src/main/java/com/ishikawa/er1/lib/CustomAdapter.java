package com.ishikawa.er1.lib;import android.content.Context;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.BaseAdapter;import android.widget.ImageView;import android.widget.TextView;import com.ishikawa.er1.object.CustomData;import com.ishikawa.er1.search_elevator_r0.R;import java.util.ArrayList;/** * Created by takenogaku on 15/06/11. */public class CustomAdapter extends BaseAdapter {    private LayoutInflater layoutInflater_;    private ArrayList<CustomData> my_List;    public CustomAdapter(Context context) {        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    }    public void setMyList(ArrayList<CustomData> my_List) {        this.my_List = my_List;    }    @Override    public int getCount() {        return my_List.size();    }    @Override    public Object getItem(int position) {        return this.my_List.get(position);    }    @Override    public long getItemId(int position) {        return 0;    }    @Override    public View getView(int position, View convertView, ViewGroup parent) {        // 特定の行(position)のデータを得る        CustomData item = (CustomData)getItem(position);        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る        if (null == convertView) {            convertView = layoutInflater_.inflate(R.layout.custom_listview, null);        }        TextView name;        name = (TextView)convertView.findViewById(R.id.name);        name.setText(item.getNameData());        TextView line;        line = (TextView)convertView.findViewById(R.id.line);        line.setText(item.getLineData());        return convertView;    }}