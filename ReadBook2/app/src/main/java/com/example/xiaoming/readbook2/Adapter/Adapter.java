package com.example.xiaoming.readbook2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaoming.readbook2.R;
import com.example.xiaoming.readbook2.Var.First;

import org.w3c.dom.Text;

import java.util.List;


public class Adapter extends BaseAdapter {
    private Context Context;
    private List<String> datalist;

    public Adapter(Context context, List<String> list) {
        this.Context = context;
        this.datalist = list;
    }

    @Override
    public int getCount() {
        return First.NAMES.size();
    }

    @Override
    public Object getItem(int position) {
        return First.NAMES.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(Context,
                    R.layout.book_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = new ViewHolder(convertView);
        holder.textView.setText(datalist.get(position));
        holder.imageView.setImageResource(R.drawable.book2);
        return convertView;
    }
}

class ViewHolder {
    ImageView imageView;
    TextView textView;

    public ViewHolder(View view) {
        imageView = view.findViewById(R.id.pic);
        textView = view.findViewById(R.id.text);
        view.setTag(this);
    }

}