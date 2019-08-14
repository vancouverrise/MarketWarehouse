package iks.market.marketwarehouse.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import iks.market.marketwarehouse.Database.DocumentsDatabase;
import iks.market.marketwarehouse.DocumentActivity;
import iks.market.marketwarehouse.R;


public class DocumentsListAdapter extends RecyclerView.Adapter<DocumentsListHolder> {

    private Context c;
    private ArrayList<DocumentsListModel> stuff;

    private DocumentsDatabase documentsDatabase;

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
    public void onBindViewHolder(@NonNull final DocumentsListHolder holder, final int position) {
        holder.title.setText(stuff.get(position).getTitle());
        holder.description.setText(stuff.get(position).getDescription());
        holder.vendorId.setText(stuff.get(position).getVendoirId());
        holder.vendorCode.setText(stuff.get(position).getVendorName());
        final String temp = stuff.get(position).getTitle();
        System.out.println("Создание номера: " + temp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(temp);
                Bundle bundle = new Bundle();
                bundle.putString("DocumentNumber", temp);
                c.startActivity( new Intent(c, DocumentActivity.class).putExtras(bundle));

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Удаление документа")
                        .setCancelable(true)
                        .setMessage("Вы действительно хотите удалить выбранный документ?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.out.println("Удаление документа: " + stuff.get(position).getTitle().replace("#", ""));
                                deleteHeaderFromDB(stuff.get(position).getTitle().replace("#", ""));
                                stuff.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, stuff.size());
                            }
                        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
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

    private void deleteHeaderFromDB(String id){
        documentsDatabase = DocumentsDatabase.getInstance(c);
        documentsDatabase.docHeaderDao().deleteHeader(id);
        documentsDatabase.close();
    }
}
