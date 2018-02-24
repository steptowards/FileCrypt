package crypto;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import utils.FileUtils;


public class AESFileDecryption {
	public static boolean decrypt(String encryptedFile, String plainTextFile, String password) throws Exception {

		FileUtils fileUtils = new FileUtils();
		String encodedName  = "/" + fileUtils.getHashEncoding(plainTextFile);

		try {
			// read salt from SafetyVault
			String saltPath = fileUtils.getSafetyVaultLocation() + encodedName + ".salt";
			Log.i("FILE","salt exists : " + new File(saltPath).exists()+ "");
			FileInputStream saltFis = new FileInputStream(saltPath);
			byte[] salt = new byte[8];
			saltFis.read(salt);
			saltFis.close();

			// read initialization vector from SafetyVault
			String ivPath = fileUtils.getSafetyVaultLocation() + encodedName + ".iv";
			Log.i("FILE","iv exists : " + new File(ivPath).exists()+ "");
			FileInputStream ivFis = new FileInputStream(ivPath);
			byte[] iv = new byte[16];
			ivFis.read(iv);
			ivFis.close();

			SecretKeyFactory factory = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 32768,
					256);
			SecretKey tmp = factory.generateSecret(keySpec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			FileInputStream fis = new FileInputStream(encryptedFile);
			FileOutputStream fos = new FileOutputStream(plainTextFile);

            // start file decryption
			byte[] in = new byte[64];
			int read;
			while ((read = fis.read(in)) != -1) {
                // cipher.update(byte[], int, int) continues a multiple-part encryption or decryption operation.
                // The first inputLen bytes in the input buffer, starting at inputOffset inclusive, are processed,
                // and the result is stored in a new buffer.
				byte[] output = cipher.update(in, 0, read);
				if (output != null)
					fos.write(output);
			}
            // cipher.doFinal() finishes a multiple-part encryption or decryption operation
			byte[] output = cipher.doFinal();
			if (output != null)
				fos.write(output);
			fis.close();
			fos.flush();
			fos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		Log.i("Operation status : ", "File Decrypted.");
		return true;
	}
}