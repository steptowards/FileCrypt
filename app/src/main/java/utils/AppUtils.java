package utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.github.steptowards.filecrypt.activities.MainActivity;
import io.github.steptowards.filecrypt.activities.OptionsActivity;
import io.github.steptowards.filecrypt.activities.ResetActivity;


public class AppUtils {

    // close the app by navigating to main activity and passing exitFlag
    public void closeApp(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("exitFlag", true);
        context.startActivity(intent);
    }

    public static void navigateToMainActivity(Context presentContext) {
        Intent intent = new Intent(presentContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        presentContext.startActivity(intent);
    }



    /*Nothing is yours. It is to use. It is to share. If you will not share it, you cannot use it.”
            ― Ursula K. Le Guin, The Dispossessed*/
    public static void share(Context context) {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT,"Share : ");
        i.putExtra(Intent.EXTRA_TEXT, "FileCrypt is a simple to use Android app for file encryption. " +
                "To know more, please visit :\nhttp://bit.ly/2EPDmaK");
        context.startActivity(Intent.createChooser(i,"Share via"));
    }

    public static void openOptions(Context context, int option) {
        Intent intent = new Intent(context, OptionsActivity.class);
        intent.putExtra("optionNumber", option + "");
        context.startActivity(intent);
    }

    public static void openReset(Context context) {
        Intent intent = new Intent(context, ResetActivity.class);
        context.startActivity(intent);
    }

    public static void openLinkInBrowser(Context context, String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);
    }

}
