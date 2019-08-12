package iks.market.marketwarehouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import iks.market.marketwarehouse.Database.DocBody;
import iks.market.marketwarehouse.Database.DocGoods;
import iks.market.marketwarehouse.Database.DocumentsDatabase;

public class DocumentActivity extends AppCompatActivity {

    Button viewDocBarcodes, viewDocBarcodesDiff, masks, viewDBBarcodes, reset, end, add;
    EditText barcode, article;
    String tempy;
    Context c;
    DocGoods docGoods;
    DocBody docBody, docBodyInsert;
    DocumentsDatabase documentsDatabase;
    TextView goodname, quantity, inpackUchet;
    NumberPicker numberPicker;

    List<DocBody> query_fetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        //Context

        c                   = getApplicationContext();
        numberPicker        = findViewById(R.id.numberPicker);
        goodname            = findViewById(R.id._goods_textview);
        quantity            = findViewById(R.id._quantity_textview);
        inpackUchet         = findViewById(R.id._predict_quantity_textview);
        barcode             = findViewById(R.id.barcode_layout);
        article             = findViewById(R.id.article_layout);
        add                 = findViewById(R.id._addGoodsButton);
        viewDocBarcodes     = findViewById(R.id._viewButton);
        viewDocBarcodesDiff = findViewById(R.id._differenceButton);
        masks               = findViewById(R.id._masksButton);
        viewDBBarcodes      = findViewById(R.id._barcodesButton);
        reset               = findViewById(R.id._resetButton);
        end                 = findViewById(R.id._endButton);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(1);
        System.out.println(numberPicker.getValue());

        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docGoodsDao().getDocumentsGoodList();

        final Intent databaseGoodsActivity = new Intent(c, GoodsWatchActivity.class);
        final Intent documentGoodsActivity = new Intent(c, DocumentGoodsInsideActivity.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tempy = bundle.getString("DocumentNumber");
        }

        if (tempy != null) {
            tempy = tempy.replace("#", "");
        }



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (query_fetch != null && query_fetch.size() != 0){
                    /**
                     * Фетч  из базы
                     */
                    documentsDatabase.docBodyDao().updateQuantity(query_fetch.get(0).barcode, numberPicker.getValue());
                    FancyToast.makeText(c, "Добавленно", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    /**
                     * Очистка кеша
                     */
                    query_fetch.clear();
                }
                else{
                    FancyToast.makeText(c, "Товара нет в подсчете", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }

            }
        });

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


       barcode.setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View view, int i, KeyEvent keyEvent) {
               if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== KeyEvent.ACTION_UP){
                   System.out.println(keyEvent.toString());
                   Toast.makeText(c, "Works" + barcode.getText().toString()  , Toast.LENGTH_SHORT).show();


               }
               return false;
           }
       });

       barcode.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               if (barcode.getText().length() <= 6) {

               } else {
                   query_fetch = documentsDatabase.docBodyDao().getGoodsByBarcode(barcode.getText().toString(), tempy);
                   System.out.println(tempy + " " + query_fetch.size());
                   if (query_fetch.size() != 0) {
                       Toast.makeText(c, query_fetch.get(0).barcode, Toast.LENGTH_SHORT).show();
                       goodname.setText(String.format("Товар: %s", query_fetch.get(0).name));
                       quantity.setText(String.format("Введено: %s", query_fetch.get(0).qty));
                       inpackUchet.setText(String.format("Упак | Учёт: %s | %s", query_fetch.get(0).inpack, query_fetch.get(0).qtypredict));
                       barcode.setText("");
                   } else {
                       Toast.makeText(c, "Товар не найден", Toast.LENGTH_SHORT).show();
                   }
               }
           }
       });

       article.setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View view, int i, KeyEvent keyEvent) {
               if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== KeyEvent.ACTION_UP){
                   System.out.println(keyEvent.toString());


                   Toast.makeText(c, "Works"  , Toast.LENGTH_SHORT).show();
                   article.setText("");


               }
               return false;
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
                        Integer.parseInt(row.getField(5)),
                        String.valueOf(row.getField(6)));

                documentsDatabase.docBodyDao().insertDocuments(docBody);

            }
        }
    }
}
