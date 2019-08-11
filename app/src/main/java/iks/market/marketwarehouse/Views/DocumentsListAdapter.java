package iks.market.marketwarehouse.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iks.market.marketwarehouse.DocumentActivity;
import iks.market.marketwarehouse.R;


public class DocumentsListAdapter extends RecyclerView.Adapter<DocumentsListHolder> {

    private Context c;
    private ArrayList<DocumentsListModel> stuff;
    private String temp;

    public DocumentsListAdapter(Context c, ArrayList<DocumentsListModel> stuff) {
        this.c = c;
        this.stuff = stuff;
    }


    @NonNull
    @Override
    public DocumentsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, null);
        return new DocumentsListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsListHolder holder, final int position) {
        holder.title.setText(stuff.get(position).getTitle());
        holder.description.setText(stuff.get(position).getDescription());
        holder.vendorId.setText(stuff.get(position).getVendoirId());
        holder.vendorCode.setText(stuff.get(position).getVendorName());
        temp = stuff.get(position).getTitle();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(temp);
                Bundle bundle = new Bundle();
                bundle.putString("DocumentNumber", temp);
                Intent intent = new Intent(c, DocumentActivity.class);
                intent.putExtras(bundle);
                c.startActivity(intent);

            }
        });
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return stuff.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
