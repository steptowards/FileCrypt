package io.github.steptowards.filecrypt.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;

import io.github.steptowards.filecrypt.R;
import utils.AsyncComplex;
import crypto.HashUtil;

public class ResetActivity extends BaseActivity {

    String resetCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        resetCode = getResetCode();
        ImageView resetImageView = (ImageView) findViewById(R.id.resetImageView);
        resetImageView.setImageResource(R.drawable.reset_64);
        TextView rtv = (TextView) findViewById(R.id.resetTextView);
        String resetText = getString(R.string.resetText, resetCode + "");
        rtv.setText(resetText);
    }

    public void onResetPress(View view) {
        boolean deleteStatus = true;
        EditText ret = (EditText) findViewById(R.id.input_reset_code);
        String resetCodeEntered = ret.getText().toString();
        if(resetCodeEntered.equals(resetCode)){
            Log.i("INFO","Reset code entered by user is correct");
            ProcessActivity.choice = 3; // for reset
            new AsyncComplex(ResetActivity.this).execute();
        }
        else {
            ret.setError("Reset code does not match");
        }
    }
    public String getResetCode(){
        Log.i("INFO", "Generating random number for reset code");
        Random rand = new Random();
        long  n = rand.nextLong() + 3; // random number
        String resetCode = HashUtil.getHash(n + "");
        resetCode = resetCode.substring(4,13);
        Log.i("INFO", "Reset code is " + resetCode);
        return resetCode;
    }
}
