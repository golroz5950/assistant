package ir.pearly.assistant;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Administrator on 1/14/2018.
 */

public class AlaviFile {
    private Context context;

    public AlaviFile(Context context) {
        this.context = context;
    }


    public static class bitmap {
        // convert from bitmap to String
        public static String convertBitmapToString(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] image = stream.toByteArray();
            return Base64.encodeToString(image, Base64.NO_WRAP);
        }

        // convert from String to bitmap
        public static Bitmap convertStringToBitmap(String imageString) {
            byte[] image = Base64.decode(imageString, Base64.NO_WRAP);
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        // convert from bitmap to byte array
        public static byte[] convertBitmapTobyte(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] image = stream.toByteArray();

            return image;
        }

        // convert from byte array to bitmap
        public static Bitmap convertbyteToBitmap(byte[] image) {

            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        public static Bitmap convertImageFormat(Bitmap bmp, int quality, Bitmap.CompressFormat format, boolean isGray) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (isGray) {
                    bmp = toGrayscale(bmp);
                }
                bmp.compress(format, quality, stream);
                byte[] image = stream.toByteArray();
                return BitmapFactory.decodeByteArray(image, 0, image.length);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        public static Bitmap toGrayscale(Bitmap srcImage) {

            Bitmap bmpGrayscale = Bitmap.createBitmap(srcImage.getWidth(), srcImage.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bmpGrayscale);
            Paint paint = new Paint();

            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(srcImage, 0, 0, paint);

            return bmpGrayscale;
        }

        public static boolean saveBitmapToFile(Bitmap Bmp, String Dir, String Filename, Bitmap.CompressFormat format) {
            FileOutputStream out = null;
            boolean res = true;
            try {
                out = new FileOutputStream(Dir + "/" + Filename);
            } catch (FileNotFoundException e) {
                res = false;
                e.printStackTrace();
            }
            Bmp.compress(format, 100, out);
            return res;
        }

        public static boolean saveWebviewBitmapToFile(WebView webview1, String Dir, String Filename, Bitmap.CompressFormat format) {

            boolean res = true;

            try {
                Bitmap mBitmap;
                mBitmap = Bitmap.createBitmap(webview1.getWidth(), webview1.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);
                webview1.draw(canvas);
                saveBitmapToFile(mBitmap, Dir, Filename, Bitmap.CompressFormat.JPEG);
            } catch (Exception e) {
                res = false;
            }

            return res;

        }

        public static Bitmap LoadBitmap(Context context, String filePath, String imageName, int Width, int Height) throws Exception {

            InputStream istr;
            Bitmap bitmap = null;

            if (filePath.startsWith("AssetsDir")) {
                filePath = filePath.replace("AssetsDir", "");
                AssetManager assetManager = context.getAssets();
                istr = assetManager.open(filePath + imageName);
            } else {
                istr = new FileInputStream(filePath + "/" + imageName);

            }
            bitmap = BitmapFactory.decodeStream(istr);
            bitmap = Bitmap.createScaledBitmap(bitmap, Width, Height, true);
            return bitmap;


        }

        public static Bitmap LoadBitmap(Context context, String filePath, String imageName) throws Exception {

            InputStream istr;
            Bitmap bitmap = null;

            if (filePath.startsWith("AssetsDir")) {
                filePath = filePath.replace("AssetsDir", "");
                AssetManager assetManager = context.getAssets();
                istr = assetManager.open(filePath + imageName);
            } else {
                istr = new FileInputStream(filePath + "/" + imageName);

            }
            bitmap = BitmapFactory.decodeStream(istr);
            return bitmap;


        }

        public static Bitmap resizeBitmap(Bitmap bpm, int Width, int Height) {
            return Bitmap.createScaledBitmap(bpm, Width, Height, true);
        }

        public static Bitmap rotateBitmap(Bitmap source, float angle) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }
    }


    public static boolean FileDelete(String Dir, String FileName) {

        return new File(Dir, FileName).delete();

    }

    public static boolean FileRename(String Dir, String FileName, String NewName) {
        File file = new File(Dir, NewName);
        return new File(Dir, FileName).renameTo(file);

    }

    public static boolean FileExists(String Dir, String FileName) {
        File file = new File(Dir + FileName);
        if (file.exists())
            return true;
        else
            return false;

    }

    public static void File_MakeDir(String Parent, String Dir) {
        new File(Parent, Dir).mkdirs();

    }

    public static boolean File_ExistsDir(String Parent, String Dir) {
        return new File(Parent, Dir).exists();

    }

    public static long FileSize(String Dir, String FileName) {
        return new File(Dir, FileName).length();
    }

    public static long FileLastModified(String Dir, String FileName) {
        return new File(Dir, FileName).lastModified();
    }

    public static String convertDate(long dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }


    public static boolean IsDirectory(String Dir, String FileName) {
        return new File(Dir, FileName).isDirectory();
    }

    public static boolean deleteDirectory(String Dir) {
        File file = new File(Dir);

        if (file.exists()) {
            String deleteCmd = "rm -r " + Dir;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public static void writeToFile(String dstPath, String dstName, String data) throws IOException {

        OutputStream out = new FileOutputStream(dstPath + "/" + dstName);
        out.write(data.getBytes(), 0, data.length());
        out.close();

    }

    public static String readFromFile(String dstPath, String dstName) throws IOException {
        File file = new File(dstPath + "/" + dstName);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");
        return str;
    }


    public String getDirInternal() {
        return context.getFilesDir().toString();
    }


    public String getExternalCacheDir() {
        File cd = context.getExternalCacheDir();

        if (cd == null) {
            return getDirInternal();
        }
        return cd.toString();
    }

    public String getDirInternalCache() {
        File cd = context.getCacheDir();

        if (cd == null) {
            return getDirInternal();
        }
        return cd.toString();
    }

    public String getDirAssets() {
        return "AssetsDir";
    }


    public String getDirRootExternal() {
        return Environment.getExternalStorageDirectory().toString();

    }


    public void FileCopy(String srcPath, String srcName, String dstPath, String dstName) throws IOException {
        InputStream in = null;
        if (srcPath.startsWith("AssetsDir")) {
            srcPath = srcPath.replace("AssetsDir", "");
            AssetManager assetManager = context.getAssets();
            in = assetManager.open(srcPath + srcName);
        } else {
            in = new FileInputStream(srcPath + "/" + srcName);

        }

        OutputStream out = new FileOutputStream(dstPath + "/" + dstName);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();

    }


}


