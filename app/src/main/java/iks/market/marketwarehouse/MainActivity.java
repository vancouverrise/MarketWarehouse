package iks.market.marketwarehouse;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import iks.market.marketwarehouse.Database.DocHeader;
import iks.market.marketwarehouse.Database.DocumentsDatabase;
import iks.market.marketwarehouse.Views.DocumentsListAdapter;
import iks.market.marketwarehouse.Views.DocumentsListModel;

public class MainActivity extends AppCompatActivity {

    Button buttonAdd, buttonSearch;
    RecyclerView recyclerView;
    DocumentsListAdapter documentsListAdapter;
    Context context;
    DocumentsDatabase documentsDatabase;
    DocumentsListModel documentsListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSearch = findViewById(R.id.buttonSearch);
        context = this.getApplicationContext();

        documentsDatabase = DocumentsDatabase.getInstance(this);
        documentsDatabase.docHeaderDao().getDocumentsHeaderList();
        documentsDatabase.docBodyDao().getDocBodyList();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int n = random.nextInt(500000);
                n += 1;
                int b = random.nextInt(15000);
                b += 1;
                DocHeader docHeader = new DocHeader("102", String.valueOf(b), "Рандом компания", "Подсчет склада");
                documentsDatabase.docHeaderDao().insertDocuments(docHeader);

            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }});


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        documentsListAdapter = new DocumentsListAdapter(this, getAnotherStuff());
        recyclerView.setAdapter(documentsListAdapter);
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

        final EditText editText = dialogView.findViewById(R.id.edt_comment);
        Button button1 = dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }
}