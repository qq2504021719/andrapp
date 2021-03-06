package com.bignerdranch.android.xundian.comm;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
import android.provider.Settings.Secure;
/**
 * Created by Administrator on 2017/11/25.
 */

public class Installation {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
//        if (sID == null) {
//            File installation = new File(context.getFilesDir(), INSTALLATION);
//            try {
//                if (!installation.exists())
//                    writeInstallationFile(installation);
//                sID = readInstallationFile(installation);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
