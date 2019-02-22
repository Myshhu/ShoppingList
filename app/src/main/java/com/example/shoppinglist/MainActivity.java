package com.example.shoppinglist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listMessages = new ArrayList<>();
    ArrayList<Integer> listIsSelected = new ArrayList<>();
    ListAdapter listAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        listAdapter = new ListAdapter(this, listMessages, listIsSelected);
        listView.setAdapter(listAdapter);

        initList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void initList() {
        listMessages.add("Mleko");
        listIsSelected.add(0);
        listMessages.add("Masło");
        listIsSelected.add(0);
        listMessages.add("Bułki");
        listIsSelected.add(0);
        listMessages.add("Ser");
        listIsSelected.add(0);
        listMessages.add("Jabłka");
        listIsSelected.add(0);
        listMessages.add("Cytryny");
        listIsSelected.add(0);
        listMessages.add("Woda");
        listIsSelected.add(0);

        listAdapter.notifyDataSetChanged();
    }

    public void btnAddItemClick(View view) {
        System.out.println("listMessages size: " + listMessages.size() + " listView size: " + listView.getChildCount());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add item");

        final EditText etInput = new EditText(this);
        etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(etInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = etInput.getText().toString();
                listMessages.add(item);
                listIsSelected.add(0);
                listAdapter.notifyDataSetChanged();
            }
        });

        builder.setNeutralButton("Add test items", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initList();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void btnAddAClick(View view) {
        initList();
    }
}
