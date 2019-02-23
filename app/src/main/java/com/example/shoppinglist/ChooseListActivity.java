package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ChooseListActivity extends AppCompatActivity {

    ListView lvChooseList;
    ArrayAdapter<String> lvAdapter;
    ArrayList<String> files;
    String directoryPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        if (getIntent().getExtras() != null) {
            directoryPath = getIntent().getExtras().getString("directoryPath");
        }

        files = new ArrayList<>();
        getListFiles(new File(directoryPath), files);

        lvChooseList = findViewById(R.id.lvChooseList);
        lvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
        lvChooseList.setAdapter(lvAdapter);

        lvChooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("filenameToLoad", files.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void getListFiles(File parentDir, ArrayList<String> inFiles) {
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                System.out.println("Found file");
                inFiles.add(file.getName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroyed chooseListActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resumed chooseListActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Paused chooseListActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stopped chooseListActivity");
    }
}
