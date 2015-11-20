package com.example.niels.todolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText newInputNote;
    private String newStringNote;
    private ListView toDoListView;
    private ArrayAdapter<String> adapterItems;
    private ArrayList<String> toDoListArray;
    private PrintStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoListView = (ListView) findViewById(R.id.listView);
        toDoListArray = new ArrayList<String>();
        readFile();
        adapterItems = new ArrayAdapter<String>(this, android.R.layout.
                simple_list_item_1, toDoListArray);

        toDoListView.setAdapter(adapterItems);

        DeleteItemsOnLongClick();

    }

    public void AddOnClick(View view) {

        newInputNote = (EditText) findViewById(R.id.editText);
        newStringNote = newInputNote.getText().toString();
        toDoListArray.add(newStringNote);
        newInputNote.setText("");
        adapterItems.notifyDataSetChanged();
        WriteToFile();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void DeleteItemsOnLongClick() {
        toDoListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        toDoListArray.remove(pos);
                        adapterItems.notifyDataSetChanged();
                        WriteToFile();
                        return true;
                    }
                });

    }


    private void WriteToFile() {

        File fileDir = getFilesDir();
        File todoList = new File(fileDir, "todoList.txt");

        try {
            FileUtils.writeLines(todoList, toDoListArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readFile() {

        File fileDir = getFilesDir();
        File todoList = new File(fileDir, "todoList.txt");

        try {
            toDoListArray = new ArrayList<>(FileUtils.readLines(todoList));
        } catch (IOException e) {
            toDoListArray = new ArrayList<String>();
        }

    }
}
