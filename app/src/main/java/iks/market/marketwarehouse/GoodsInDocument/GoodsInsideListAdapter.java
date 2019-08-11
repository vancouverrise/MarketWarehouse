package iks.market.marketwarehouse.GoodsInDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iks.market.marketwarehouse.R;

public class GoodsInsideListAdapter extends RecyclerView.Adapter<GoodsInsideListHolder> {

    private Context c;
    private ArrayList<GoodsInsideModel> stuff;
    private String temp;

    public GoodsInsideListAdapter(Context c, ArrayList<GoodsInsideModel> stuff) {
        this.c = c;
        this.stuff = stuff;
    }

    @NonNull
    @Override
    public GoodsInsideListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goodsinsidemodel, null);
        return new GoodsInsideListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsInsideListHolder holder, int position) {
        holder.name.setText(stuff.get(position).getName());
        holder.article.setText(stuff.get(position).getArticle());
        holder.barcode.setText(stuff.get(position).getBarcode());
        holder.inpack.setText(stuff.get(position).getInpack());
    }

    @Override
    public int getItemCount() {
        return stuff.size();
    }
}
