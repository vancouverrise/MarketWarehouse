package iks.market.marketwarehouse;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iks.market.marketwarehouse.Database.DocGoods;
import iks.market.marketwarehouse.Database.DocumentsDatabase;
import iks.market.marketwarehouse.Views.GoodsListAdapter;
import iks.market.marketwarehouse.Views.GoodsListModel;

public class  GoodsWatchActivity extends AppCompatActivity {

    RecyclerView recyclerGoods;
    GoodsListAdapter goodsListAdapter;
    GoodsListModel goodsListModel;
    DocumentsDatabase documentsDatabase;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_watch);

        recyclerGoods = findViewById(R.id.goodsRecycler);
        context = getApplicationContext();
        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docGoodsDao().getDocumentsGoodList();

        recyclerGoods.setLayoutManager(new LinearLayoutManager(this));
        goodsListAdapter = new GoodsListAdapter(this, getGoods());
        recyclerGoods.setAdapter(goodsListAdapter);
        
    }

    private ArrayList<GoodsListModel> getGoods() {
        ArrayList<GoodsListModel> othermodels = new ArrayList<>();
        List<DocGoods> tempheader = documentsDatabase.docGoodsDao().getDocumentsGoodList();

        if (tempheader.size() > 0){
            for (int i = 0; i < tempheader.size(); i++) {
                goodsListModel = new GoodsListModel();
                goodsListModel.setCode("Название:1 " + tempheader.get(i).code);
                goodsListModel.setBarcode("Артикул: " +tempheader.get(i).barcode);
                goodsListModel.setName("Код: " + tempheader.get(i).name);
                goodsListModel.setInpack("Кол-во упк: " + tempheader.get(i).inpack);
                othermodels.add(goodsListModel);
            }
        }
        System.out.println("Size" + tempheader.size());
        return othermodels;
    }
}
