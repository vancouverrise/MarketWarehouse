package iks.market.marketwarehouse;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iks.market.marketwarehouse.Database.DocBody;
import iks.market.marketwarehouse.Database.DocumentsDatabase;
import iks.market.marketwarehouse.GoodsInDocument.GoodsInsideListAdapter;
import iks.market.marketwarehouse.GoodsInDocument.GoodsInsideModel;

public class DocumentGoodsInsideActivity extends AppCompatActivity {

    RecyclerView recyclerGoodsInside;
    GoodsInsideListAdapter goodsInsideListAdapter;
    GoodsInsideModel goodsInsideModel;
    DocumentsDatabase documentsDatabase;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_goods_inside);
        recyclerGoodsInside = findViewById(R.id.goodsInsideRecycler);
        context = getApplicationContext();
        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docBodyDao().getDocBodyList();

        recyclerGoodsInside.setLayoutManager(new LinearLayoutManager(this));
        goodsInsideListAdapter = new GoodsInsideListAdapter(this, getGoodsInside());
        recyclerGoodsInside.setAdapter(goodsInsideListAdapter);
    }

    private ArrayList<GoodsInsideModel> getGoodsInside() {
        ArrayList<GoodsInsideModel> othermodels = new ArrayList<>();
        List<DocBody> tempheader = documentsDatabase.docBodyDao().getDocBodyList();

        if (tempheader.size() > 0){
            for (int i = 0; i < tempheader.size(); i++) {
                goodsInsideModel = new GoodsInsideModel();
                goodsInsideModel.setName("Товар: " + tempheader.get(i).name);
                goodsInsideModel.setArticle("Артикул: " + tempheader.get(i).code);
                goodsInsideModel.setBarcode("Barcode: " + tempheader.get(i).barcode);
                goodsInsideModel.setInpack("Упаковка: " + tempheader.get(i).inpack);
                othermodels.add(goodsInsideModel);
            }
        }
        System.out.println("Size" + tempheader.size());
        return othermodels;


    }
}
