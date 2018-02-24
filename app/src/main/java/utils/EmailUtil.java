package utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class EmailUtil {

    private static EmailUtil single_instance = null;

    // singleton
    public static EmailUtil getInstance() {
        if (single_instance == null)
            single_instance = new EmailUtil();

        return single_instance;
    }

    public void sendEmail(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"steptowards@protonmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Contact from app");
        i.putExtra(Intent.EXTRA_TEXT   , "Hi, \n\n" + "You may choose to send or delete the debug info \n\n"+ getSystemInformation() + "\n\n");
        try {
            context.startActivity(Intent.createChooser(i, "Send mail via"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    // include system information in the email body, user may delete this information in the email compose window
    public String getSystemInformation() {
        String debugInfo = "------------------------------------------------------------" + "\n";
        debugInfo += String.format("%-20s %s %n", " OS Version",System.getProperty("os.version"));
        debugInfo += String.format("%-20s %s %n", " Build Version", Build.VERSION.INCREMENTAL);
        debugInfo += String.format("%-20s %s %n", " API Level", Build.VERSION.SDK_INT);
        debugInfo += String.format("%-20s %s %n", " Device", Build.DEVICE);
        debugInfo += String.format("%-20s %s %n", " Brand", Build.BRAND);
        debugInfo += String.format("%-20s %s %n", " Model", Build.MODEL);
        debugInfo += String.format("%-20s %s %n", " Product", Build.PRODUCT);
        debugInfo += "------------------------------------------------------------" + "\n\n";
        return debugInfo;
    }
}
