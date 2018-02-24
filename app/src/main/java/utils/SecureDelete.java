package utils;

import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

import io.github.steptowards.filecrypt.activities.FileShredActivity;

/* buffer.force() method :
Forces any changes made to this buffer's content to be written to the storage device containing the mapped file.
If the file mapped into this buffer resides on a local storage device then when this method returns it is
guaranteed that all changes made to the buffer since it was created, or since this method was last invoked,
will have been written to that device.If the file does not reside on a local device then no such guarantee is made.
*/
public class SecureDelete {

    public boolean shredFile(String filePath) {
        Log.i("FILE", "Filepath send to shredFile method is " + filePath);
        boolean deleteStatus = false;
        int overwriteCount = FileShredActivity.numberOfOverwrite;
        Log.i("INFO","Overwrite Count in shredFile method : " + overwriteCount);
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Log.i("FILE", "File to be shreded exists");
                SecureRandom random = new SecureRandom();
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel channel = raf.getChannel();
                MappedByteBuffer buffer
                        = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
                Log.i("INFO","Starting overwrite");

                switch (overwriteCount) {
                    case 3 :
                        Log.i("INFO","Starting overwrite with 0");
                        while (buffer.hasRemaining()) {
                        buffer.put((byte) 0);
                        }
                        buffer.force();
                        buffer.rewind();

                    case 2 :
                        Log.i("INFO","Starting overwrite with 1");
                        while (buffer.hasRemaining()) {
                        buffer.put((byte) 0xFF);
                        }
                        buffer.force();
                        buffer.rewind();

                    case 1 :
                        Log.i("INFO","Starting overwrite with random numbers");
                        byte[] data = new byte[1];
                        while (buffer.hasRemaining()) {
                        random.nextBytes(data);
                        buffer.put(data[0]);
                        }
                        buffer.force();
                        buffer.rewind();
                }

                Log.i("FILE","Proceeding with deletion");
                deleteStatus = file.delete();
            }
        } catch (IOException e) {
            Log.e("ERROR", "IOException caught while trying to shred file");
            e.printStackTrace();
            deleteStatus = false;
        }
        Log.i("INFO", "return delete status " + deleteStatus);
        return deleteStatus;
    }
}