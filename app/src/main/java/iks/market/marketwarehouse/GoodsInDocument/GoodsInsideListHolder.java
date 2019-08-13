package iks.market.marketwarehouse.GoodsInDocument;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import iks.market.marketwarehouse.R;

public class GoodsInsideListHolder extends RecyclerView.ViewHolder {

    TextView name, barcode, article, inpack, qty, qtypredict;


    public GoodsInsideListHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.goodsNameList);
        this.barcode = itemView.findViewById(R.id.goodsBarcodeList);
        this.article = itemView.findViewById(R.id.goodsArticleList);
        this.inpack = itemView.findViewById(R.id.goodsInpackList);
        this.qty = itemView.findViewById(R.id.goodsQtyScannedList);
        this.qtypredict = itemView.findViewById(R.id.goodsQtyPredictList);

    }
}
