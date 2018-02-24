package io.github.steptowards.filecrypt.activities;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.github.steptowards.filecrypt.R;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();
        String lineSeparator = "\n\n";
        // TODO : Move text in this file to config and read data from there
        String faq1 =   "Why are external storage / SD card files not visible?" + lineSeparator +
                        "FileCrypt uses AES encryption with salt and IV which are stored in internal storage " +
                        "and are used for decryption later. Since SD card and other " +
                        "external media can be easily ejected from devices, both the salt " +
                        "and IV can be obtained by a willing third party to break " +
                        "file encryption.For this reason, reading files from SD card / external storage is disabled " +
                        "as of now.";
        String faq2 =   "What is the encryption algorithm used?" + lineSeparator +
                        "The app uses Advanced Encryption Standard (AES) with CBC and PKCS5Padding. " +
                        "Users can enter any password starting 1 character, the password entered by user " +
                        "is changed to a longer password which is used for encrypting / decrypting.";
        String faq3 =   "What is the difference between Delete original file and Secure Delete?" + lineSeparator +
                        "Delete original file option checked during encryption / decryption will only delete " +
                        "the file once, after process is complete. " +
                        "The files deleted in this way can be recovered by a dedicated third party. " +
                        "Using the Secure Delete option will overwrite the file " +
                        "space three times, with 0,1 and random numbers before deleting the file. This is " +
                        "a more secure way to delete files but requires more time. It is advised not to use " +
                        "this option with very big files.";
        String faq4 =   "Why has the process failed?" + lineSeparator +
                        "If you are getting a dialog box with failure message, there can be many reasons why " +
                        "the process has failed :\n" +
                        "1. You are not giving the correct password for decryption.\n" +
                        "2. The salt and IV files needed for decryption have been deleted using reset " +
                            "or other means.\n" +
                        "3. The encrypted file has been renamed (renaming an encrypted file can also be used " +
                            "as means to protect successful decryption by others. You can change one or more " +
                            "character of file name to prevent successful decryption).\n" +
                        "4. The file selected for decryption might not be a valid encrypted file.";

        String about1 = "About FileCrypt" + lineSeparator +
                        "FileCrypt is made using open source libraries and code for encryption and secure " +
                        "deletion of files. It uses the NIST approved AES algorithm developed by " +
                        "Rijmen and Daemen. The implementation is covered by Java Crypto library.";
        String about2 = "Visit FileCrypt homepage" + lineSeparator +
                        "Tap here to visit FileCrypt homepage.";
        String about3 = "Version and Licence " + lineSeparator +
                        "You are using FileCrypt v1.0 (covered under MIT Licence)";

        String cntct1 = "How to contact us" + lineSeparator +
                        "Please tap here to drop a mail at \n" +
                        "stepstowards [at] protonmail [dot] com";

        List<String> faq = new ArrayList<String>();
        faq.add(faq2);
        faq.add(faq1);
        faq.add(faq3);
        faq.add(faq4);

        List<String> about = new ArrayList<String>();
        about.add(about1);
        about.add(about2);
        about.add(about3);

        List<String> contact = new ArrayList<String>();
        contact.add(cntct1);

        expandableListDetail.put("About", about);
        expandableListDetail.put("FAQs", faq);
        expandableListDetail.put("Contact Us", contact);

        Log.i("INFO", "Sending data back from expandable data pump");
        return expandableListDetail;
    }
}

