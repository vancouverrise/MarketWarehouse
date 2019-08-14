package iks.market.marketwarehouse;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import iks.market.marketwarehouse.Database.DocHeader;
import iks.market.marketwarehouse.Database.DocPartners;
import iks.market.marketwarehouse.Database.DocumentsDatabase;
import iks.market.marketwarehouse.Views.DocumentsListAdapter;
import iks.market.marketwarehouse.Views.DocumentsListModel;

public class MainActivity extends AppCompatActivity {

    Button buttonAdd;
    RecyclerView recyclerView;
    DocumentsListAdapter documentsListAdapter;
    Context context;
    DocumentsDatabase documentsDatabase;
    DocumentsListModel documentsListModel;
    DocPartners docPartners;
    List<DocPartners> docPartnersArray;
    ArrayAdapter<String> adapterName;
    ArrayAdapter<String> adapterCode;
    EditText editText, editText2;
    AutoCompleteTextView   editText3, editText4;
    String licence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapterName = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        adapterCode = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);

        recyclerView = findViewById(R.id.recycler);
        buttonAdd = findViewById(R.id.buttonAdd);
        context = this.getApplicationContext();

        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docHeaderDao().getDocumentsHeaderList();
        documentsDatabase.docBodyDao().getDocBodyList();

        docPartnersArray = documentsDatabase.partnersDao().getPartnerList();
        if (docPartnersArray.size() != 0){
            for (int i = 0; i < docPartnersArray.size(); i++) {
                adapterName.add(docPartnersArray.get(i).name);
                adapterCode.add(docPartnersArray.get(i).code);
            }
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter();
    }

    private ArrayList<DocumentsListModel> getAnotherStuff() {
        ArrayList<DocumentsListModel> othermodels = new ArrayList<>();
        List<DocHeader> tempheader = documentsDatabase.docHeaderDao().getDocumentsHeaderList();

        if (tempheader.size() > 0){
            for (int i = 0; i < tempheader.size(); i++) {
                documentsListModel = new DocumentsListModel();
                documentsListModel.setTitle("#" + tempheader.get(i).documentNumber);
                documentsListModel.setVendoirId("Код: " +tempheader.get(i).code);
                documentsListModel.setDescription("Описание: " + tempheader.get(i).description);
                documentsListModel.setVendorName("Лицо: " + tempheader.get(i).name);
                othermodels.add(documentsListModel);
            }
        }
        System.out.println("Size" + tempheader.size());
        return othermodels;
    }

    public void ShowDialog(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.addnewdocument, null);

        editText = dialogView.findViewById(R.id.edt_comment);
        editText2 = dialogView.findViewById(R.id.descriptionEditText);
        editText3 = dialogView.findViewById(R.id.codeSupplierEditText);
        editText4 = dialogView.findViewById(R.id.nameSupplierTextView);
        Button button1 = dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importPartners();
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")){
                    FancyToast.makeText(context, "Введите номер документа", Toast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    DocHeader docHeader = new DocHeader(editText.getText().toString(), editText3.getText().toString(), editText4.getText().toString(), editText2.getText().toString());
                    documentsDatabase.docHeaderDao().insertDocuments(docHeader);
                    dialogBuilder.dismiss();
                    docPartnersArray = documentsDatabase.partnersDao().getPartnerList();
                    adapter();
                }
            }
        });

        editText3.setThreshold(1);
        editText3.setAdapter(adapterCode);
        editText4.setThreshold(1);
        editText4.setAdapter(adapterName);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public void importPartners() {
        File file = new File(Environment.getExternalStorageDirectory(), "partners.txt");
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
                docPartners = new DocPartners(
                        String.valueOf(row.getField(0)),
                        String.valueOf(row.getField(1)));
                documentsDatabase.partnersDao().insertDocuments(docPartners);
            }
        }
        Toast.makeText(MainActivity.this, "Импортирование завершенно", Toast.LENGTH_SHORT).show();
        
    }

    public void adapter(){
        documentsDatabase.docHeaderDao().getDocumentsHeaderList();
        documentsListAdapter = new DocumentsListAdapter(this, getAnotherStuff());
        recyclerView.setAdapter(documentsListAdapter);
    }

   /* public int encode (String serial){
        int temp = Integer.valueOf(serial.substring(5, 9));
        return (temp +131) * 2781;
    }*/

    public String receivedLicence(int a){
        return null;
    }
}