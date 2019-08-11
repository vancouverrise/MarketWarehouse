package iks.market.marketwarehouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import iks.market.marketwarehouse.Database.DocBody;
import iks.market.marketwarehouse.Database.DocGoods;
import iks.market.marketwarehouse.Database.DocumentsDatabase;

public class DocumentActivity extends AppCompatActivity {

    Button viewDocBarcodes, viewDocBarcodesDiff, masks, viewDBBarcodes, reset, end;
    TextInputEditText barcode, article;
    String tempy;
    Context c;
    DocGoods docGoods;
    DocBody docBody;
    DocumentsDatabase documentsDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docGoodsDao().getDocumentsGoodList();

        c = getApplicationContext();
        final Intent databaseGoodsActivity = new Intent(c, GoodsWatchActivity.class);
        final Intent documentGoodsActivity = new Intent(c, DocumentGoodsInsideActivity.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tempy = bundle.getString("DocumentNumber");
        }

        barcode = findViewById(R.id.textInputBarcode);
        article = findViewById(R.id.textInputArticle);

        viewDocBarcodes = findViewById(R.id._viewButton);
        viewDocBarcodesDiff = findViewById(R.id._differenceButton);
        masks = findViewById(R.id._masksButton);
        viewDBBarcodes = findViewById(R.id._barcodesButton);
        reset = findViewById(R.id._resetButton);
        end = findViewById(R.id._endButton);

        viewDocBarcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(documentGoodsActivity);
            }
        });


        viewDBBarcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(databaseGoodsActivity);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode.setText("");
                article.setText("");
            }
        });





    }

    public void loadCSV(){
        File file = new File(Environment.getExternalStorageDirectory(),"Goods.txt");
        CsvReader csvReader = new CsvReader();
        csvReader.setFieldSeparator(';');


        CsvContainer csv = null;
        try {
            csv = csvReader.read(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (csv != null) {
            for (CsvRow row : csv.getRows()) {
                System.out.println(row);
                docGoods = new DocGoods(String.valueOf(row.getField(0)),
                        String.valueOf(row.getField(1)),
                        String.valueOf(row.getField(2)),
                        String.valueOf(row.getField(3)));

                documentsDatabase.docGoodsDao().insertGoods(docGoods);

            }
        }
    }

    public void loadDocWithGoods(){
        File file = new File(Environment.getExternalStorageDirectory(),"docs.txt");
        CsvReader csvReader = new CsvReader();
        csvReader.setFieldSeparator(';');


        CsvContainer csv = null;
        try {
            csv = csvReader.read(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (csv != null) {
            for (CsvRow row : csv.getRows()) {
                System.out.println(row);
                docBody = new DocBody(String.valueOf(row.getField(0)),
                        String.valueOf(row.getField(1)),
                        String.valueOf(row.getField(2)),
                        String.valueOf(row.getField(3)),
                        String.valueOf(row.getField(4)),
                        String.valueOf(row.getField(5)),
                        String.valueOf(row.getField(6)));

                documentsDatabase.docBodyDao().insertDocuments(docBody);

            }
        }
    }
}
