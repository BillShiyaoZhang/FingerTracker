package com.example.bill.fingertracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button person = (Button) findViewById(R.id.button_person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonDialog(person);
            }
        });
        final Button task = (Button) findViewById(R.id.button_task);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonDialog(task);
            }
        });
        final Button status = (Button) findViewById(R.id.button_status);
//        status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

//        writeToFile("test", "Hello world! This is a test");
    }

    private void setButtonDialog(final Button button) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.pop_up_edit_text, null);
        customizeDialog.setTitle("Specify " + button.getText() + ":");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edit_text = (EditText) dialogView.findViewById(R.id.edit_text);
                        button.setText(edit_text.getText().toString());
                    }
                });
        customizeDialog.show();
    }

    private void writeToFile(String filename, String fileContents) {
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
