package io.github.steptowards.filecrypt.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.github.steptowards.filecrypt.R;
import utils.AppUtils;

public class BaseActivity extends AppCompatActivity {


    // display Menu option on top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    //menu button actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                AppUtils.share(this);
                break;
            case R.id.about :
                AppUtils.openOptions(this,0);
                break;
            case R.id.faqs :
                AppUtils.openOptions(this,1);
                break;
            case R.id.contact :
                AppUtils.openOptions(this,2);
                break;
            case R.id.resetSV :
                AppUtils.openReset(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
