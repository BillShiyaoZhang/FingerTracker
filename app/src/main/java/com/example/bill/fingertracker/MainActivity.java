package com.example.bill.fingertracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static boolean ON = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MultitouchView multitouchView = (MultitouchView) findViewById(R.id.view_touch);
        final Button button_person = (Button) findViewById(R.id.button_person);
        button_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {setButtonDialog(button_person);
            }
        });
        final Button button_task = (Button) findViewById(R.id.button_task);
        button_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonDialog(button_task);
            }
        });
        final Button button_status = (Button) findViewById(R.id.button_status);
        button_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ON = !ON;
                multitouchView.setStatus(ON);
                if (ON) {
                    button_person.setVisibility(View.INVISIBLE);
                    button_task.setVisibility(View.INVISIBLE);
                    button_status.setText("Stop");
                } else {
                    button_person.setVisibility(View.VISIBLE);
                    button_task.setVisibility(View.VISIBLE);
                    button_status.setText("Start");
                }
            }
        });

        multitouchView.setButton_person(button_person);
        multitouchView.setButton_task(button_task);

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
                        if (button.getId() == R.id.button_task){
                            String task = "Please type following sentences:";
                            switch (button.getText().toString()){
                                case "1":
                                    task += ":\nThe quick brown fox jumps over the lazy dog.";
                                    break;
                                case "2":
                                    task += "\nSphinx of black quartz, judge my vow.";
                                    break;
                                case "3":
                                    task += "\nJackdaws love my big sphinx of quartz.";
                                    break;
                                case "4":
                                    task += "\nPack my box with five dozen liquor jugs.";
                                    break;
                                case "5":
                                    task += "\nThe quick onyx goblin jumps over the lazy dwarf.";
                                    break;
                                case "6":
                                    task += "\nCwm fjord bank glyphs vext quiz.";
                                    break;

                                default:
                                    task += "\nWrong!";
                            }
                            ((TextView) findViewById(R.id.text_task)).setText(task);
                        }
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
