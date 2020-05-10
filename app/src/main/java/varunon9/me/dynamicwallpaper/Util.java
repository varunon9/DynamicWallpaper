package varunon9.me.dynamicwallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.Random;

public class Util {
    private String TAG = "Util";
    private String WALLPAPER_DIRECTORY = "dynamic-wallpaper";

    /**
     * @return integer in range [min, max]
     */
    public static int getRandomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static boolean hasPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static long getFileSizeInKb(File file) {
        return file.length() / 1024;
    }

    public void setRandomWallpaper(Context context) {
        // check for permission
        if (!hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "App does not have WRITE_EXTERNAL_STORAGE permission");
            return;
        }
        String sdCardPath = Environment.getExternalStorageDirectory().toString();
        String wallpaperDirectoryPath = sdCardPath + File.separator + WALLPAPER_DIRECTORY;
        File wallpaperDirectory = new File(wallpaperDirectoryPath);
        if (wallpaperDirectory.exists()) {
            try {
                File[] files = wallpaperDirectory.listFiles();
                if (files != null && files.length > 0) {
                    Log.d(TAG, "Size: " + files.length);
                    int randomFilePathIndex = getRandomInt(0, files.length - 1);
                    File randomFile = files[randomFilePathIndex];
                    long fileSizeInKb = getFileSizeInKb(randomFile);
                    long maxFileSizeInKb = 1024;
                    Log.d(TAG, "Wallpaper file size: " + fileSizeInKb);
                    if (fileSizeInKb <= maxFileSizeInKb) {
                        String randomFilePath = randomFile.getAbsolutePath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap image = BitmapFactory.decodeFile(randomFilePath, options);
                        WallpaperManager manager = WallpaperManager.getInstance(context);
                        manager.setBitmap(image);
                    } else {
                        Log.d(TAG, "File size exceeds limit: " + maxFileSizeInKb);
                    }
                } else {
                    Log.d(TAG, "Wallpaper directory is empty: " + wallpaperDirectoryPath);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

        } else {
            boolean createDirectoryResult = wallpaperDirectory.mkdirs();
            Log.d(TAG, "Wallpaper directory creation result: " + createDirectoryResult);
        }

    }
}
