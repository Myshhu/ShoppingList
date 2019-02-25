package com.example.shoppinglist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listMessages = new ArrayList<>();
    ArrayList<Integer> listIsSelected = new ArrayList<>();
    ListAdapter listAdapter;
    String currentListName;

    private String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ShoppingList_lists" + File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Created activity");
        currentListName = "";
        //Permission for external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ShoppingList_lists");
        if(!dir.exists()) {
            dir.mkdir();
        }

        listView = findViewById(R.id.listView);
        listAdapter = new ListAdapter(this, listMessages, listIsSelected);
        listView.setAdapter(listAdapter);

        Bundle extras = getIntent().getExtras();
        //Extras not null at first launch
        if(extras != null) {
            String filenameToLoad = extras.getString("filenameToLoad");
            if(filenameToLoad != null) {
                loadFromFile(filenameToLoad);
                listAdapter.notifyDataSetChanged();
                currentListName = filenameToLoad;
                setTitle(currentListName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (currentListName.equals("") && listMessages.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("List not saved");
            builder.setMessage("Do you want to save your new list?");
            builder.setPositiveButton("Yes", (dialog, which) -> createSaveDialog(false));
            builder.setNeutralButton("No", (dialog, which) -> {
                dialog.dismiss();
                super.onBackPressed();
            });
            builder.show();
        } else {
            saveToFile(currentListName);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemChangeList:
                itemChangeListDialog();
                return true;
            case R.id.itemMakeNewList:
                itemMakeNewListDialog();
                return true;
            case R.id.itemChangeListName:
                /*if(listMessages.size() != 0) { //Don't save empty list
                    createSaveDialog();
                } else {
                    Toast.makeText(this, "You can't save empty list", Toast.LENGTH_SHORT).show();
                }*/
                createSaveDialog(false);
                return true;
            /*case R.id.itemLoadFromFile:
                createLoadDialog();
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startChangeListActivity() {
        Intent intent = new Intent(this, ChooseListActivity.class);
        intent.putExtra("directoryPath", directoryPath);
        startActivity(intent);
        finish();
    }

    private void clearList() {
        listMessages.clear();
        listIsSelected.clear();
        currentListName = "";
        setTitle("");
        listAdapter.notifyDataSetChanged();
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

    private boolean loadFromFile(String filename) {
        try {
            //Read from external storage
            File f = new File(directoryPath + filename);
            FileInputStream fileInputStream = new FileInputStream(f);
            //

            //Read from app private folders
            //FileInputStream fileInputStream = openFileInput(directoryPath + "textFile.txt");
            System.out.println(getFileStreamPath(filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;

            listMessages.clear();
            listIsSelected.clear();

            while ((line = reader.readLine()) != null) {
                String[] stringArray = Arrays.copyOf(line.split(" "), line.split(" ").length - 1);
                StringBuilder arrayResult = new StringBuilder();
                for(int i = 0; i < stringArray.length; i++) {
                    if(i != stringArray.length - 1) {
                        arrayResult.append(stringArray[i]).append(" ");
                    } else {
                        arrayResult.append(stringArray[i]);
                    }
                }
                listMessages.add(arrayResult.toString());
                listIsSelected.add(Integer.parseInt(line.split(" ")[line.split(" ").length - 1]));
                sb.append(line).append("\n");
            }
            reader.close();

            Toast.makeText(this, "Loaded data", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load data from file " + filename, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean saveToFile(String filename) {
        try {
            //Write to external storage
            File f = new File(directoryPath + filename);
            System.out.println("Saving to directoryPath: " + directoryPath + filename);
            FileOutputStream fileOutputStream = new FileOutputStream(f, false);
            //

            //Write to app private folders
            //FileOutputStream fileOutputStream = openFileOutput(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/textFile.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            for (int i = 0; i < listMessages.size(); i++) {
                String item = listMessages.get(i);
                int selected = listIsSelected.get(i);
                outputStreamWriter.write(item + " " + selected + "\n");
            }

            outputStreamWriter.close();
            Toast.makeText(this, "Saved data to: " + filename, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String findFreeFilename() {
        int i = 1;
        boolean loop = true;
        while (loop) {
            File file = new File(directoryPath + "New list" + i + ".txt");
            if (file.exists()) {
                i++;
            } else {
                loop = false;
            }
        }
        return "New list" + i + ".txt";
    }

    private void createSaveDialog(boolean gotoListChange) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etInput = new EditText(this);
        etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle("Save list");
        builder.setView(etInput);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filenameWithTxt;
                if (etInput.getText().toString().equals("")) {
                    filenameWithTxt = findFreeFilename();
                } else {
                    filenameWithTxt = etInput.getText().toString() + ".txt";
                }
                File file = new File(directoryPath + filenameWithTxt);
                if (file.exists()) {
                    //Make choice
                    createOverwriteDialog(filenameWithTxt, gotoListChange);
                } else {
                    //Just save
                    saveToFile(filenameWithTxt);
                    currentListName = filenameWithTxt;
                    setTitle(currentListName);
                    if(gotoListChange){
                        startChangeListActivity();
                    }
                }
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

    private void itemChangeListDialog() {
        if (currentListName.equals("") && listMessages.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("List not saved");
            builder.setMessage("Do you want to save your new list?");
            builder.setPositiveButton("Yes", (dialog, which) -> createSaveDialog(true));
            builder.setNeutralButton("No", (dialog, which) -> {
                dialog.dismiss();
                startChangeListActivity();
            });
            builder.show();
        } else {
            saveToFile(currentListName);
            startChangeListActivity();
        }
    }

    private void itemMakeNewListDialog() {
        if (currentListName.equals("") && listMessages.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("List not saved");
            builder.setMessage("Do you want to save your new list?");
            builder.setPositiveButton("Yes", (dialog, which) -> createSaveDialog(false));
            builder.setNeutralButton("No", (dialog, which) -> {
                dialog.dismiss();
                clearList();
            });
            builder.show();
        } else {
            saveToFile(currentListName);
            clearList();
        }
    }

    private void createOverwriteDialog(String filename, boolean gotoListChange) {
        AlertDialog.Builder second_builder = new AlertDialog.Builder(this);
        second_builder.setTitle("List exists");
        second_builder.setMessage("Do you want to overwrite list?");
        second_builder.setPositiveButton("Yes", (dialog, which) -> {
            saveToFile(filename);
            currentListName = filename;
            if (gotoListChange) {
                startChangeListActivity();
            }
        });
        second_builder.setNeutralButton("No", (dialog, which) -> {
            dialog.dismiss();
            if(gotoListChange) {
                startChangeListActivity();
            }
        });
        second_builder.show();
    }

    private void createLoadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etInput = new EditText(this);
        etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle("Load list");
        builder.setView(etInput);

        builder.setPositiveButton("Load", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!etInput.getText().toString().equals("")){
                    String filenameWithTxt = etInput.getText().toString() + ".txt";
                    loadFromFile(filenameWithTxt);
                    listAdapter.notifyDataSetChanged();
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroyed Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resumed Activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Paused Activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stopped Activity");
    }
}
