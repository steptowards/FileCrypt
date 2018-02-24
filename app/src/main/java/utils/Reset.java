package utils;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*  Reset will overwrite the files present in safety vault once before deleting them.
    The directory remains there but is emptied after this operation.
    */
public class Reset {

    static boolean deleteStatus = true;

    public static boolean resetSV() {
        deleteStatus = true;
        String sv = new FileUtils().getSafetyVaultLocation();
        Log.i("INFO","Safety Vault location is " + sv);
        File file = new File(sv);
        // if Safety Vault does not exist, return status false
        if(!file.exists()){
            deleteStatus = false;
            Log.i("INFO","Safety Vault does not exist");
            return deleteStatus;
        }
        //delete recursively
        recursivelyDelete(file);
        Log.i("INFO","Returning deleteStatus for SV : " + deleteStatus);
        return deleteStatus;
    }

    public static void recursivelyDelete(File file) {
        // base condition
        if (!file.exists())
            return;
        //go inside and call recursively to delete all files of a directory
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursivelyDelete(f);
            }
        }
        if(!file.isDirectory()){
            overwrite(file);
            file.delete();
        }
        Log.i("INFO","Deleted file from SV : "+file.getAbsolutePath());
    }

    // Overwrite the Secure Vault files once before deleting
    public static void overwrite(File file){
        Log.i("INFO","Starting overwrite of file " + file.getAbsolutePath());
        try{
            if (file.exists()) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel channel = raf.getChannel();
                MappedByteBuffer buffer
                        = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
                // overwrite with zeros
                while (buffer.hasRemaining()) {
                    buffer.put((byte) 0);
                }
                buffer.force();
                buffer.rewind();
                raf.close();
            }
        } catch (IOException e) {
            Log.e("ERROR", "Exception while trying to delete " + file.getAbsolutePath());
            e.printStackTrace();
            deleteStatus = false;
        }
    }
}
