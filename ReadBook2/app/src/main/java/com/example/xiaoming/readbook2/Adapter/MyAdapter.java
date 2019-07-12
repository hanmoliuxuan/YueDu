package com.example.xiaoming.readbook2.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaoming.readbook2.R;
import com.example.xiaoming.readbook2.Ui.Read;
import com.example.xiaoming.readbook2.Var.All;
import com.example.xiaoming.readbook2.Var.ReadTextSiez;
import com.example.xiaoming.readbook2.Var.Second;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<String> Datas;
    private Context Context;

    public MyAdapter(Context context, List<String> datas) {
        this.Context = context;
        this.Datas = datas;
    }

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Second.checkclick) {
                    Read.setting.setVisibility(View.VISIBLE);
                    Second.checkclick = true;
                } else if (Second.checkclick) {
                    Read.setting.setVisibility(View.INVISIBLE);
                    Read.mululist.setVisibility(View.INVISIBLE);
                    Read.mulu_title.setVisibility(View.INVISIBLE);
                    Second.checkclick = false;
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Second.nowpage = holder.getAdapterPosition();//recycleview加载数据是三个一组，每次获取的是最后的一个当前显示的是中间的。
        holder.textView.setText(Datas.get(position));
        if (Second.check3) {//check=ture
            holder.textView.setTextColor(Color.BLACK);
        } else {//check=false
            holder.textView.setTextColor(Color.WHITE);
        }
    }
    @Override
    public int getItemCount() {
        return Datas.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    public  TextView textView;

    public MyViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.justify);
        textView.setTextSize(ReadTextSiez.TextSiez[All.SIZE]);
    }
}
