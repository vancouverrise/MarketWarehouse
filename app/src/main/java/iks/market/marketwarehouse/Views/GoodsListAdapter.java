package iks.market.marketwarehouse.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iks.market.marketwarehouse.R;

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListHolder> {

    Context c;
    ArrayList<GoodsListModel> tempArray = new ArrayList();
    String temp;

    public GoodsListAdapter(Context c, ArrayList<GoodsListModel> model){
        this.c = c;
        this.tempArray = model;
    }

    @NonNull
    @Override
    public GoodsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goodsmodel, null);
        return new GoodsListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsListHolder holder, int position) {
        holder.name.setText(tempArray.get(position).getName());
        holder.barcode.setText(tempArray.get(position).getBarcode());
        holder.code.setText(tempArray.get(position).getCode());
        holder.inpack.setText(tempArray.get(position).getInpack());

    }

    @Override
    public int getItemCount() {
        return tempArray.size();
    }
}
