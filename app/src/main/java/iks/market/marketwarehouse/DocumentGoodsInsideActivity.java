package iks.market.marketwarehouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    String bundleString, bundleStringName;
    Button export_btn;

    ArrayList<GoodsInsideModel> othermodels;
    List<DocBody> tempheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_goods_inside);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleString = bundle.getString("difference");
            bundleStringName = bundle.getString("docnumber");
        }


        System.out.println("Условие прихода" + bundleString);
        System.out.println("Номер документа: " + bundleStringName);
        othermodels = new ArrayList<>();
        recyclerGoodsInside = findViewById(R.id.goodsInsideRecycler);
        export_btn = findViewById(R.id.export_button_new);
        context = getApplicationContext();
        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docBodyDao().getDocBodyList();
        tempheader = documentsDatabase.docBodyDao().getDocBodyList();

        recyclerGoodsInside.setLayoutManager(new LinearLayoutManager(this));
        goodsInsideListAdapter = new GoodsInsideListAdapter(this, getGoodsInside());
        recyclerGoodsInside.setAdapter(goodsInsideListAdapter);

        export_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExportDifference().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

    }

    private ArrayList<GoodsInsideModel> getGoodsInside() {

        switch (bundleString) {
            case "true":
                othermodels.clear();
                System.out.println("Просмотр разницы");
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
                break;

            case "false":
                othermodels.clear();
                System.out.println("Просмотр кодов документа");
                othermodels = new ArrayList<>();
                tempheader = documentsDatabase.docBodyDao().getDocBodyListDifference(bundleStringName);

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

    public class ExportDifference extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(DocumentGoodsInsideActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(final String... args) {

            File file = new File(Environment.getExternalStorageDirectory(), "docdifference.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String[] column = {"docnumber", "code", "barcode", "inpack", "qty", "qtypredict"};
                csvWrite.writeNext(column);

                List<DocBody> docbody = documentsDatabase.docBodyDao().getDocBodyListDifference(bundleStringName);
                for (int i = 0; i < docbody.size(); i++) {
                    String[] mySecondStringArray = {
                            docbody.get(i).docnumber,
                            docbody.get(i).code,
                            docbody.get(i).barcode,
                            docbody.get(i).inpack,
                            String.valueOf(docbody.get(i).qty),
                            String.valueOf(docbody.get(i).qtypredict)
                    };
                    csvWrite.writeNext(mySecondStringArray);
                }
                csvWrite.close();
                return true;
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(context, "Экспортирование завершенно", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Ошибка экспорта", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


