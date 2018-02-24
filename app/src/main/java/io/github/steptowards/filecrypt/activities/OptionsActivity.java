package io.github.steptowards.filecrypt.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.steptowards.filecrypt.R;
import utils.AppUtils;
import utils.EmailUtil;

public class OptionsActivity extends BaseActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String optionNumberString = getIntent().getExtras().getString("optionNumber");
        Log.i("INFO", "Option number string received from Intent extras " + optionNumberString);
        int option = Integer.parseInt(optionNumberString);
        Log.i("INFO", "Group to open : " + option);
        setContentView(R.layout.activity_options);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        if(expandableListView == null){
            Log.e("ERROR", "List view is null");
        }
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        if(option != -1) { // don't expand any group if option is -1
            expandableListView.expandGroup(option);
        }
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Log.i("INFO",expandableListTitle.get(groupPosition) + " list expanded");
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.i("INFO",expandableListTitle.get(groupPosition) + " list collapsed");
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Log.i("INFO",expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition) + " clicked");
                // tapping on contact us should open mail dialog
                if(groupPosition == 0 && childPosition == 1)
                    AppUtils.openLinkInBrowser(OptionsActivity.this,"https://steptowards.github.io/FileCrypt/");
                else if(groupPosition == 2)
                    EmailUtil.getInstance().sendEmail(OptionsActivity.this);

                return false;
            }
        });
    }
}
