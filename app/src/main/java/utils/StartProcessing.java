package utils;


import android.util.Log;

import java.io.File;

import crypto.AESFileDecryption;
import crypto.AESFileEncryption;
import crypto.HashUtil;

import static io.github.steptowards.filecrypt.activities.ProcessActivity.passwordString;

public class StartProcessing {
    public boolean startProcessing(int choice, String path, boolean checkDelete){

        //make a password to use for operation
        String passwordToUse = HashUtil.getHash(passwordString);
        passwordToUse = passwordToUse.substring(5,21); // TODO : make this configurable so that user can decide
        boolean processStatus = false;                 // check if encryption or decryption is successful or not
        String fileExtension  = "_ENC";                // this is the file extension attached to encryption files

        switch (choice) {
            case 1: // encrypt
                try {
                    processStatus = AESFileEncryption.encrypt(path, path + fileExtension, passwordToUse);
                    // delete original file after encryption
                    if(checkDelete && processStatus) {
                        File fileReference = new File(path);
                        if (fileReference.delete())
                            Log.i("deletion status : ", "File deleted successfully");
                        else
                            Log.e("deletion status : ", "File not deleted successfully");
                    }
                } catch (Exception e) {
                    Log.e("ERROR", "caught exception in file encryption");
                    e.printStackTrace();
                }
                break;

            case 2: // decrypt
                try {
                    int index = path.lastIndexOf("_");
                    String path_final = path.substring(0, index);
                    Log.i("file name : ", path_final);
                    processStatus = AESFileDecryption.decrypt(path, path_final, passwordToUse);

                    // delete original encrypted file after decryption
                    if(checkDelete && processStatus) {
                        File fileReference = new File(path);
                        if (fileReference.delete())
                            Log.i("deletion status : ", "File deleted successfully");
                        else
                            Log.e("deletion status : ", "File not deleted successfully");
                    }
                } catch (Exception e) {
                    Log.e("ERROR", "caught exception in file descryption");
                    e.printStackTrace();
                }
        }
        return processStatus;
    }
}
