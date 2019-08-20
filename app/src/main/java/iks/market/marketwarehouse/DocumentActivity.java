package iks.market.marketwarehouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    String _TempDocumentString;
    Context c;
    DocGoods docGoods;
    DocBody docBody;
    DocumentsDatabase documentsDatabase;
    TextView goodname, quantity, inpackUchet;
    NumberPicker numberPicker;
    CheckBox checkBox;

    List<DocBody> query_fetch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);


        c = getApplicationContext();
        numberPicker = findViewById(R.id.numberPicker);
        goodname = findViewById(R.id._goods_textview);
        quantity = findViewById(R.id._quantity_textview);
        inpackUchet = findViewById(R.id._predict_quantity_textview);
        barcode = findViewById(R.id.barcode_layout);
        article = findViewById(R.id.article_layout);
        add = findViewById(R.id._addGoodsButton);
        viewDocBarcodes = findViewById(R.id._viewButton);
        viewDocBarcodesDiff = findViewById(R.id._differenceButton);
        masks = findViewById(R.id._masksButton);
        viewDBBarcodes = findViewById(R.id._barcodesButton);
        reset = findViewById(R.id._resetButton);
        end = findViewById(R.id._endButton);
        checkBox = findViewById(R.id.checkBox);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(1);

        checkBox.setChecked(false);

        System.out.println(numberPicker.getValue());

        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docGoodsDao().getDocumentsGoodList();

        final Intent databaseGoodsActivity = new Intent(c, GoodsWatchActivity.class);
        final Intent documentGoodsActivity = new Intent(c, DocumentGoodsInsideActivity.class);
        final Intent documentDifferrence = new Intent(c, DocumentGoodsInsideActivity.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _TempDocumentString = bundle.getString("DocumentNumber");
        }

        if (_TempDocumentString != null) {
            _TempDocumentString = _TempDocumentString.replace("#", "");
        }

        System.out.println("Номер документа: "+ _TempDocumentString);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    barcode.setText("");
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (query_fetch != null && query_fetch.size() != 0) {

                        System.out.println("Размер массива кнопки" + query_fetch.size());
                        documentsDatabase.docBodyDao().updateQuantity(query_fetch.get(0).barcode, numberPicker.getValue(), _TempDocumentString);
                        FancyToast.makeText(c, "Добавленно", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        query_fetch = documentsDatabase.docBodyDao().getGoodsByBarcode(barcode.getText().toString(), _TempDocumentString);
                        goodname.setText(String.format("Товар: %s", query_fetch.get(0).name));
                        quantity.setText(String.format("Введено: %s", query_fetch.get(0).qty));
                        inpackUchet.setText(String.format("Упак | Учёт: %s / %s", query_fetch.get(0).inpack, query_fetch.get(0).qtypredict));
                    } else {
                        FancyToast.makeText(c, "Товара нет в подсчете", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        viewDocBarcodesDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("difference", "false");
                bundle.putString("docnumber", _TempDocumentString);
                documentGoodsActivity.putExtras(bundle);
                startActivity(documentGoodsActivity);

            }
        });

        viewDocBarcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("difference", "true");
                bundle.putString("docnumber", _TempDocumentString);
                documentDifferrence.putExtras(bundle);
                startActivity(documentDifferrence);
            }
        });

        viewDocBarcodes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DocumentActivity.this);
                builder.setTitle("Импорт")
                        .setMessage("Импорт товаров в документ из файла docs.txt")
                        .setCancelable(true)
                        .setNegativeButton("Импорт", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(c, "Импортирую", Toast.LENGTH_SHORT).show();
                                loadDocWithGoods();
                                Toast.makeText(c, "Ок", Toast.LENGTH_SHORT).show();
                            }
                    }
                );
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewDBBarcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(databaseGoodsActivity);
            }
        });

        viewDBBarcodes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DocumentActivity.this);
                builder.setTitle("Импорт")
                        .setMessage("Импорт товаров в базу из файла goodsimport.txt")
                        .setCancelable(true)
                        .setNegativeButton("Импорт", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                loadBarcodes();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode.setText("");
                article.setText("");
                goodname.setText("Товар: ");
                quantity.setText("Введено: ");
                inpackUchet.setText("Упак | Учёт: ");
            }
        });


        barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    System.out.println(keyEvent.toString());
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
                    getQuery();
                }
            }
        });

        article.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    System.out.println(keyEvent.toString());
                    Toast.makeText(c, "Works", Toast.LENGTH_SHORT).show();
                    article.setText("");
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void loadDocWithGoods() {
        File file = new File(Environment.getExternalStorageDirectory(), "docs.txt");
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
                        Integer.parseInt(row.getField(6)));
                documentsDatabase.docBodyDao().insertDocuments(docBody);

            }
        }
    }

    public void getQuery() {
        /**
         * Выборка из базы
         */
        query_fetch = documentsDatabase.docBodyDao().getGoodsByBarcode(barcode.getText().toString(), _TempDocumentString);
        /**
         * Если фетч инициализирован и есть данные то...
         */
        if (query_fetch != null && query_fetch.size() != 0) {
            // TODO: Если включён чекбокс
            if (checkBox.isChecked()) {
                goodname.setText(String.format("Товар: %s", query_fetch.get(0).name));
                quantity.setText(String.format("Введено: %s", query_fetch.get(0).qty));
                inpackUchet.setText(String.format("Упак | Учёт: %s / %s", query_fetch.get(0).inpack, query_fetch.get(0).qtypredict));

                documentsDatabase.docBodyDao().updateQuantity(query_fetch.get(0).barcode, numberPicker.getValue(), _TempDocumentString);
                FancyToast.makeText(c, "Добавленно", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                query_fetch.clear();
                query_fetch = documentsDatabase.docBodyDao().getGoodsByBarcode(barcode.getText().toString(), _TempDocumentString);
                goodname.setText(String.format("Товар: %s", query_fetch.get(0).name));
                quantity.setText(String.format("Введено: %s", query_fetch.get(0).qty));
                inpackUchet.setText(String.format("Упак | Учёт: %s / %s", query_fetch.get(0).inpack, query_fetch.get(0).qtypredict));
                barcode.setText("");
            }
            // TODO: Если нет
            if (!checkBox.isChecked()) {
                goodname.setText(String.format("Товар: %s", query_fetch.get(0).name));
                quantity.setText(String.format("Введено: %s", query_fetch.get(0).qty));
                inpackUchet.setText(String.format("Упак | Учёт: %s / %s", query_fetch.get(0).inpack, query_fetch.get(0).qtypredict));
                System.out.println("Размер массива " + query_fetch.size());
            }
        }
        /**
         * Нет товара
         */
        else {
            FancyToast.makeText(c, "Нет товара", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }

    }

    public void loadBarcodes() {
        File file = new File(Environment.getExternalStorageDirectory(), "goodsimport.txt");
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
                docGoods = new DocGoods(
                        String.valueOf(row.getField(0)),
                        String.valueOf(row.getField(1)),
                        String.valueOf(row.getField(2)),
                        String.valueOf(row.getField(3)));
                documentsDatabase.docGoodsDao().insertGoods(docGoods);
            }
        }
        Toast.makeText(c, "Импортирование завершенно", Toast.LENGTH_SHORT).show();
    }
}