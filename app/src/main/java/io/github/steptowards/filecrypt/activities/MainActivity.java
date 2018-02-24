package io.github.steptowards.filecrypt.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import io.github.steptowards.filecrypt.R;
import utils.AppUtils;

public class MainActivity extends BaseActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    public int choice = 0;   // 0 is initial value, 1 for encryption and 2 for decryption.
    public String path = ""; // file path which is picked for encryption / decryption

    // options for Grid layout on Main Activity
    GridView androidGridView;
    String[] gridViewString = {
            "Encrypt", "Decrypt", "Delete", "Help"
    } ;
    int[] gridViewImageId = {
            R.drawable.encrypt_80, R.drawable.decrypt_80,
            R.drawable.delete_80, R.drawable.options_80
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // close the app if exitFlag is passed true from another activity
        if( getIntent().getBooleanExtra("exitFlag", false)){
            finish();
            return;
        }

        setContentView(R.layout.gridview_image_text);
        TextView gridTextView = (TextView) findViewById(R.id.gridView_textView);
        gridTextView.setText(R.string.mainDescription);
        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(MainActivity.this, gridViewString, gridViewImageId);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Log.i("INFO", "int " + i + " id" + id);
                switch(i) {
                    case 0 :
                        encryptButton(); break;
                    case 1 :
                        decryptButton(); break;
                    case 2 :
                        deleteButton(); break;
                    case 3 :
                        optionsButton(); break;
                    default :
                        Log.e("ERROR", "Click index not suitable for any mapping to any method");
                }
            }
        });
    }

    // method called when encrypt button is pressed on main activity
    public void encryptButton() {
        choice = 1;
        checkPermissionsAndOpenFilePicker();
    }

    // method called when decrypt button is pressed on main activity
    public void decryptButton() {
        choice = 2;
        checkPermissionsAndOpenFilePicker();
    }

    // method called when delete button is pressed on main activity
    public void deleteButton() {
        choice = 0;
        checkPermissionsAndOpenFilePicker();
    }

    // method called when options button is pressed on main activity
    public void optionsButton() {
        AppUtils.openOptions(MainActivity.this,-1);
    }

    // File explorer code is taken from https://github.com/nbsp-team/MaterialFilePicker

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(this, "Allow external storage permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    private void openFilePicker() {
        String action = "";
        if(choice == 1)
            action = "encrypt";
        else if(choice == 2)
            action = "decrypt";
        else if(choice == 0)
            action = "delete";

        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withTitle("Choose file to " + action)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            if (path != null) {
                Log.i("FILE", "File path from Material File picker " + path);
            }

            String storageDir = Environment.getExternalStorageDirectory().toString();
            Log.i("FILE", "External Storage Directory is " + storageDir);

            // navigate to Main Activity if file is not selected
            // (user probably pressed back button in file explorer)
            // move to next screen if file is selected
            String storagePath = Environment.getExternalStorageDirectory().toString();
            String onlyFile = path.replace(storagePath, "");
            if (onlyFile.length() != 0 && choice != 0)
                callProcessIntent();
            else if (onlyFile.length() != 0 && choice == 0)
                callProcessShred();
        }
    }

    public void callProcessIntent() {
        Intent intent = new Intent(MainActivity.this,ProcessActivity.class);
        intent.putExtra("filePath",path.toString());
        intent.putExtra("choice",choice);
        startActivity(intent);
    }

    public void callProcessShred() {
        Intent intent = new Intent(MainActivity.this,FileShredActivity.class);
        intent.putExtra("filePath",path.toString());
        startActivity(intent);
    }

    // exit app on back press in Main Activity
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
