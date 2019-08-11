package iks.market.marketwarehouse.Views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import iks.market.marketwarehouse.R;

public class GoodsListHolder extends RecyclerView.ViewHolder {

    TextView name, code, barcode, inpack;

    public GoodsListHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.goodsName);
        this.code = itemView.findViewById(R.id.goodsCode);
        this.barcode = itemView.findViewById(R.id.goodsArticle);
        this.inpack = itemView.findViewById(R.id.goodsInpack);
    }
}
