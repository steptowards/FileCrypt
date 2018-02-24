package utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import crypto.HashUtil;

public class FileUtils {
    //this folder is used to store iv and salt
    private String safetyVault = ".safetyVault";
    private String storageDir  =  Environment.getExternalStorageDirectory().toString();

    public static String getFileSize(String filePath){
        File file = new File(filePath);
        String fileSize = file.length() + "";
        Log.i("file_size", fileSize);
        return fileSize;
    }

    public String getSafetyVaultName(){
        return safetyVault;
    }

    public String getSafetyVaultLocation(){
        return storageDir + "/" + safetyVault;
    }

    public boolean doesSafetyVaultExist(){
        File file = new File(storageDir + "/" + safetyVault);
        Log.i("FILE","Checking existence of safety vault");
        if(file.exists())
            return true;
        else
            return false;
    }

    public boolean createSafetyVault(){
        boolean status = false;
        Log.i("FILE","creating safety vault");
        if(!doesSafetyVaultExist()){
            File file = new File(storageDir + "/" + safetyVault);
            status = file.mkdir();
            if(!status)
                Log.e("FILE","Safety Vault could not be created");
        }
        else
            Log.i("FILE", "Safety Vault already exists");
        return status;
    }

    // get byte array of a file equivalent
    public byte[] getByteArrayFromFile(String path) {
        byte[] getBytes = {};
        try {
            File file = new File(path);
            getBytes = new byte[(int) file.length()];
            InputStream is = new FileInputStream(file);
            is.read(getBytes);
            is.close();
        }
        catch (FileNotFoundException e) {
        e.printStackTrace();
        }
        catch (IOException e) {
        e.printStackTrace();
    }
    return getBytes;
    }

    // get trimmed hash encoding of file name
    public String getHashEncoding(String file) {
        String onlyFile = file.substring(file.lastIndexOf('/'));
        Log.i("FILE","Getting hash encoding of file " + onlyFile);
        String hash = HashUtil.getHash(onlyFile);
        String return_hash = hash.substring(1,11);
        Log.i("FILE","shortened hash is " + return_hash);
        return return_hash;
    }
}
