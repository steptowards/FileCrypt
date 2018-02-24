package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import io.github.steptowards.filecrypt.R;

public class DialogUtil {

        public void showProcessCompleteDialog(int choice, final Context context, boolean result){

            String message = "";

            switch(choice){
                case 0 :
                    message = "Deletion";
                    break;
                case 1 :
                    message = "Encryption";
                    break;
                case 2 :
                    message = "Decryption";
                    break;
                case 3 :
                    message = "Reset";
                    break;
            }
            if(result) {
                AlertDialog.Builder pcDialog = new AlertDialog.Builder(context);
                pcDialog.setTitle(message + " complete");
                pcDialog.setIcon(R.drawable.green_tick_icon);
                pcDialog.setMessage("The " + message + " process has completed, you may now press Continue to do another " +
                        "operation or simply exit.");
                pcDialog.setCancelable(false);
                pcDialog.setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                // navigate to Main Activity
                                AppUtils.navigateToMainActivity(context);
                            }
                        });
                pcDialog.setNegativeButton("Exit app",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                // close the app
                                new AppUtils().closeApp(context);
                            }
                        });
                AlertDialog processSuccess = pcDialog.create();
                processSuccess.show();
            }
            else {
                AlertDialog.Builder pcDialog = new AlertDialog.Builder(context);
                pcDialog.setTitle(message + " error");
                pcDialog.setIcon(R.drawable.icon_error);
                pcDialog.setMessage("The " + message + " process was not completed successfully. " +
                        "Please go to FAQs section to know about reasons for failure ");
                pcDialog.setCancelable(false);
                pcDialog.setPositiveButton("Try Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                // navigate to Main Activity
                                AppUtils.navigateToMainActivity(context);
                            }
                        });
                pcDialog.setNegativeButton("Go to FAQs",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                // close the app
                                AppUtils.openOptions(context,1);
                            }
                        });
                AlertDialog processSuccess = pcDialog.create();
                processSuccess.show();
            }
        }
}
