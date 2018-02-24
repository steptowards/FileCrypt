package crypto;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import utils.FileUtils;

public class AESFileEncryption {
	public static boolean encrypt(String file_to_encrypt, String dest_file_encrypted, String password) throws Exception {

        FileUtils fileUtils = new FileUtils();
        String encodedName  = "/" + fileUtils.getHashEncoding(file_to_encrypt);

        try {
            // file to be encrypted (input)
            FileInputStream inFile = new FileInputStream(file_to_encrypt);

            // final encrypted file (output)
            FileOutputStream outFile = new FileOutputStream(dest_file_encrypted);

            // SafetyVault is used to store salt and initialization vector
            if(!fileUtils.doesSafetyVaultExist()) {
                fileUtils.createSafetyVault();
            }

            // Generate and store salt. Random salt ensures that identical passwords do not have
            // the same derivation result when passed through PBKDF2
            byte[] salt = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            String saltPath = fileUtils.getSafetyVaultLocation() + encodedName + ".salt";
            Log.i("FILE", "Creating salt file " + saltPath);
            FileOutputStream saltOutFile = new FileOutputStream(saltPath);
            saltOutFile.write(salt);
            saltOutFile.close();

            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 32768,
                    256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();

            // Initialization Vector is used as input to first operation cycle of CBC mode
            String ivPath = fileUtils.getSafetyVaultLocation() + encodedName + ".iv";
            Log.i("FILE", "Creating iv file " + ivPath);
            FileOutputStream ivOutFile = new FileOutputStream(ivPath);
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            ivOutFile.write(iv);
            ivOutFile.close();

            // start file encryption
            byte[] input = new byte[64];
            int bytesRead;

            while ((bytesRead = inFile.read(input)) != -1) {
                // cipher.update(byte[], int, int) continues a multiple-part encryption or decryption operation.
                // The first inputLen bytes in the input buffer, starting at inputOffset inclusive, are processed,
                // and the result is stored in a new buffer.
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }
            // cipher.doFinal() finishes a multiple-part encryption or decryption operation
            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();
        }
        catch(Exception e){
		    e.printStackTrace();
		    return false;
        }
        Log.i("Operation status : ", "File Encrypted.");
        return true;
	}

}

