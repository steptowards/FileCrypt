package crypto;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    final static String hashAlgo = "MD5";

    public static String getHash(String password) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(hashAlgo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md.update(password.getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format method
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        Log.i("INFO","Digest(in hex format) : " + hexString.toString());
        return hexString.toString();
    }
}

