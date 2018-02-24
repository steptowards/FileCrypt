package io.github.steptowards.filecrypt.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import io.github.steptowards.filecrypt.R;
import utils.AppUtils;
import utils.AsyncComplex;
import utils.FileUtils;

public class FileShredActivity extends BaseActivity {

    public static String filePath = "";
    long fileSize = 0L;
    public static int numberOfOverwrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_shred);
        ProcessActivity.choice = 0;
        numberOfOverwrite = 1;

        ImageView imageView = (ImageView) findViewById(R.id.deleteImageView);
        TextView deleteTextView = (TextView) findViewById(R.id.deleteTextView);
        imageView.setImageResource(R.drawable.delete_80);
        deleteTextView.setText(R.string.overWriteText);

        try {
            filePath = getIntent().getExtras().getString("filePath");
            Log.i("FILE", "File path received by FileShredActivity is " + filePath);
            String storagePath = Environment.getExternalStorageDirectory().toString();
            String onlyFile = filePath.replace(storagePath,"");
            EditText textView = (EditText) findViewById(R.id.input_fileName);
            textView.setText(onlyFile);
        }
        catch(NullPointerException npe){
            Log.e("ERROR", "Getting data from intent gave NPE");
        }

        fileSize = Long.parseLong(FileUtils.getFileSize(filePath));
        TextView overwriteTextView = (TextView) findViewById(R.id.textViewDelete);
        overwriteTextView.setText(R.string.initialOverWriteText);

        // user input for number of overwrites during secure deletion
        final SeekBar  overwriteSeek = (SeekBar) findViewById(R.id.seekBarDelete);
        overwriteSeek.setMax(3);
        overwriteSeek.setProgress(1);
        overwriteSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minimum = 1;
                int overwriteCount;
                if(progress < minimum) {
                    overwriteCount = 1;
                    overwriteSeek.setProgress(minimum);
                }
                else
                    overwriteCount = progress;
                Log.i("INFO","Overwrite seek bar value is " + overwriteCount);
                setOverwriteText(overwriteCount);
                numberOfOverwrite = overwriteCount;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onDeletePress(View view) {
        if(fileSize > 20971520){ // greater than 20 MB
            showBigFileDialog("This is a big file and might take significant time");
        }
        else {
            // process delete operation in a separate thread
            new AsyncComplex(FileShredActivity.this).execute();
        }
    }

    public void showBigFileDialog(String alertMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Continue deletion with a big file?")
                .setMessage(alertMessage)
                .setCancelable(true)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("INFO","Proceeding deletion with a big file");
                        // process crypto operation in a separate thread
                        new AsyncComplex(FileShredActivity.this).execute();
                    }
                })
                .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new AppUtils().navigateToMainActivity(FileShredActivity.this);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void setOverwriteText(int overwriteCount) {
        String overwriteText = "Selected file will be overwritten " + overwriteCount + " ";
        overwriteText += (overwriteCount == 1) ? "time" : "times";
        Log.i("INFO", "Overwrite text is " + overwriteText);
        TextView overwriteTextView = (TextView) findViewById(R.id.textViewDelete);
        overwriteTextView.setText(overwriteText);
    }
}
