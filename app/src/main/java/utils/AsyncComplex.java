package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import io.github.steptowards.filecrypt.activities.FileShredActivity;
import io.github.steptowards.filecrypt.activities.ProcessActivity;


public class AsyncComplex extends AsyncTask <String, String, Boolean> {
    private Context mContext;

    ProgressDialog mProgress;
    private TaskCompleted mCallback;

    public AsyncComplex(Context context) {
        String processingMessage = " file. Please do not shut down or minimize";
        if (ProcessActivity.choice == 0)
            processingMessage = "Deleting" + processingMessage;
        else if (ProcessActivity.choice == 1)
            processingMessage = "Encrypting" + processingMessage;
        else if (ProcessActivity.choice == 2)
            processingMessage = "Decrypting" + processingMessage;
        else if (ProcessActivity.choice == 3)
            processingMessage = "Resetting SV " + processingMessage;

        this.mContext = context;
//        this.mCallback = (TaskCompleted) context;
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage(processingMessage);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

    }

    @Override
    public void onPreExecute() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mProgress.setMessage(values[0]);
    }

    @Override
    protected Boolean doInBackground(String... values) {
        boolean status = false;
        if(ProcessActivity.choice == 1 || ProcessActivity.choice == 2){
            status = new StartProcessing().startProcessing(ProcessActivity.choice, ProcessActivity.filePath, ProcessActivity.setDeleteStatus);
        }
        else if(ProcessActivity.choice == 0)
            status = new SecureDelete().shredFile(FileShredActivity.filePath);
        else if(ProcessActivity.choice == 3)
            status = Reset.resetSV();
        return status;
    }

    @Override
    protected void onPostExecute(Boolean results) {
        mProgress.setMessage("Completed");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.dismiss();
        Log.i("DEBUG", "Status in onPostExecute : " + String.valueOf(results));
        // returning data back to caller
        // mCallback.onTaskComplete(results);
        // show a dialog box once process is complete
        new DialogUtil().showProcessCompleteDialog(ProcessActivity.choice, mContext, results);
    }
}