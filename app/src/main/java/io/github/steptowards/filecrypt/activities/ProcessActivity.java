package io.github.steptowards.filecrypt.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import io.github.steptowards.filecrypt.R;
import utils.AsyncComplex;
import utils.FileUtils;

public class ProcessActivity extends BaseActivity {

    public static String filePath = "";          // this is the file path from file selection
    public static int choice = 0;                // 0 is for secure delete, 1 for encryption, 2 for decryption, 3 for reset
    public static String passwordString = "";    // this is the password used for encryption / decryption
    public static boolean setDeleteStatus;       // used to specify if original file should be deleted
    long fileSize = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        try {
            filePath = getIntent().getExtras().getString("filePath");
            choice = getIntent().getExtras().getInt("choice");
        }
        catch(NullPointerException npe){
            Log.e("ERROR", "Getting data from intent gave NPE");
        }

        // get only file path without external storage path
        String storagePath = Environment.getExternalStorageDirectory().toString();
        String onlyFile = filePath.replace(storagePath,"");
        TextView textView = (TextView) findViewById(R.id.fileName);
        textView.setText(onlyFile);
        fileSize = Long.parseLong(FileUtils.getFileSize(filePath));
        ImageView imageView = (ImageView) findViewById(R.id.processImageView);
        TextView processTextView = (TextView) findViewById(R.id.processTextView);
        Button startButton = (Button) findViewById(R.id.button);
        if(choice == 1){
            imageView.setImageResource(R.drawable.encrypt_80);
            processTextView.setText(R.string.enterPasswordEncryption);
            startButton.setText("ENCRYPT");
        }
            else if(choice == 2) {
            imageView.setImageResource(R.drawable.decrypt_80);
            processTextView.setText(R.string.enterPasswordDecryption);
            startButton.setText("DECRYPT");
        }
    }

    public void onStartPress(View view) {
        Log.i("choice", choice + "");
        Log.i("file path", filePath);
        EditText passwordButton = (EditText) findViewById(R.id.password);
        passwordString = passwordButton.getText().toString();
        CheckBox deleteFile = (CheckBox) findViewById(R.id.deleteFileCheck);
        setDeleteStatus = deleteFile.isChecked();

        // ask user to enter a password if he / she didn't set one
        if(passwordString.length() == 0){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        else if(fileSize > 20971520 && fileSize < 524288000){ // greater than 20 MB and less than 500 MB
            showBigFileDialog("This is a big file and might take significant time");
        }
        else if(fileSize >= 524288000 && fileSize < 1073741824){ // greater than 500 MB and less than 1 GB
            showBigFileDialog("This is a very big file and might take significant time");
        }
        else if(fileSize >= 1073741824){ // greater than 1 GB
            showBigFileDialog("This is an extremely big file. " +
                    "This operation can take too much battery and CPU. " +
                    "It is better to use desktop crypto tools.");
        }
        else {
            // process crypto operation in a separate thread
            new AsyncComplex(ProcessActivity.this).execute();
        }
    }


    public void showBigFileDialog(String alertMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Continue with a big file?")
                .setMessage(alertMessage)
                .setCancelable(true)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("INFO","Proceeding crypto operation with a big file");
                        // process crypto operation in a separate thread
                        new AsyncComplex(ProcessActivity.this).execute();
                    }
                })
                .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do whatever is needed
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

