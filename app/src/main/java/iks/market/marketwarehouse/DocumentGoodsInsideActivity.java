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
    String bundleString;

    ArrayList<GoodsInsideModel> othermodels;
    List<DocBody> tempheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_goods_inside);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleString = bundle.getString("difference");
        }
        othermodels = new ArrayList<>();
        recyclerGoodsInside = findViewById(R.id.goodsInsideRecycler);
        context = getApplicationContext();
        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docBodyDao().getDocBodyList();
        tempheader = documentsDatabase.docBodyDao().getDocBodyList();

        recyclerGoodsInside.setLayoutManager(new LinearLayoutManager(this));
        goodsInsideListAdapter = new GoodsInsideListAdapter(this, getGoodsInside());
        recyclerGoodsInside.setAdapter(goodsInsideListAdapter);


    }

    private ArrayList<GoodsInsideModel> getGoodsInside() {

        switch (bundleString) {
            case "true":
                System.out.println("Let's go difference");
                othermodels = new ArrayList<>();
                tempheader = documentsDatabase.docBodyDao().getDocBodyList();

                if (tempheader.size() > 0) {
                    for (int i = 0; i < tempheader.size(); i++) {
                        goodsInsideModel = new GoodsInsideModel();
                        goodsInsideModel.setName("Товар: " + tempheader.get(i).name);
                        goodsInsideModel.setArticle("Артикул: " + tempheader.get(i).code);
                        goodsInsideModel.setBarcode("Barcode: " + tempheader.get(i).barcode);
                        goodsInsideModel.setInpack("Упаковка: " + tempheader.get(i).inpack);
                        goodsInsideModel.setQty("Введено: " + tempheader.get(i).qty);
                        goodsInsideModel.setQtyPredict("Учёт: " + tempheader.get(i).qtypredict);
                        othermodels.add(goodsInsideModel);
                    }
                }
                System.out.println("Size" + tempheader.size());

            case "false":

                System.out.println("Nothing to show");
                othermodels = new ArrayList<>();
                tempheader = documentsDatabase.docBodyDao().getDocBodyListDifference();

                if (tempheader.size() > 0) {
                    for (int i = 0; i < tempheader.size(); i++) {
                        goodsInsideModel = new GoodsInsideModel();
                        goodsInsideModel.setName("Товар: " + tempheader.get(i).name);
                        goodsInsideModel.setArticle("Артикул: " + tempheader.get(i).code);
                        goodsInsideModel.setBarcode("Barcode: " + tempheader.get(i).barcode);
                        goodsInsideModel.setInpack("Упаковка: " + tempheader.get(i).inpack);
                        goodsInsideModel.setQty("Введено: " + tempheader.get(i).qty);
                        goodsInsideModel.setQtyPredict("Учёт: " + tempheader.get(i).qtypredict);
                        othermodels.add(goodsInsideModel);
                    }
                }
                System.out.println("Size" + tempheader.size());
        }
        return othermodels;
    }
}


