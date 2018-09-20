package ir.pearly.assistant;

//  for Crop need
//  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//
//@Override
//protected void onActivityResult(int requestCode, int resultCode, Intent data)
//        {
//
//        if (requestCode==AlaviGallary.requestCode_PIC_CROP || requestCode==AlaviGallary.requestCode_Select_Camera || requestCode==AlaviGallary.requestCode_Select_Gallery ){
//
// alaviGallary.onActivityResult( requestCode,  resultCode,  data);
//
//        }

//for android 7.0
//        add res/xml/provider_paths.xml
//
//        add in Manifest inside tag
//
//        <provider
//        android:name="android.support.v4.content.FileProvider"
//        android:authorities="${applicationId}.provider"
//        android:exported="false"
//        android:grantUriPermissions="true">
//        <meta-data
//        android:name="android.support.FILE_PROVIDER_PATHS"
//        android:resource="@xml/provider_paths"/>
//        </provider>

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

public class AlaviGallary {
    Context inContext;
    private Activity activity;
    private String filename;
    private String dir;
    public String subname;
    public Bitmap image;
    public boolean success;
    private Uri fileUri;
    private File cameraFile;

    public static final int requestCode_PIC_CROP = 1915;
    public static final int requestCode_Select_Gallery = 1916;
    public static final int requestCode_Select_Camera = 1917;
    public int crop_aspectX = 0;//crop_aspectX = 3
    public int crop_aspectY = 0;//crop_aspectY = 4

    public int crop_outputX = 0;//crop_outputX = 500
    public int crop_outputY = 0;//crop_outputY =500
    private static boolean crop_yes = false;

    public AlaviGallary(Context inContext) {
        this.inContext = inContext;
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void cropPic() {
        try {
            AlaviFile alaviFile = new AlaviFile(inContext);
            filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
            dir = alaviFile.getExternalCacheDir();
            AlaviFile.bitmap.saveBitmapToFile(image, dir, filename, Bitmap.CompressFormat.JPEG);
            Uri mFinalImageUri;
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            if(Build.VERSION.SDK_INT > M){
                mFinalImageUri=  FileProvider.getUriForFile(inContext, BuildConfig.APPLICATION_ID +".provider"
                       ,
                        new File(dir, filename));

                cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFinalImageUri);

            }else {
                mFinalImageUri= Uri.fromFile(new File(dir, filename));
            }






            //indicate image type and Uri
            cropIntent.setDataAndType(mFinalImageUri, "image/*");

            List list = activity.getPackageManager().queryIntentActivities(cropIntent, 0);
            int size = list.size();
            if (size == 0) {
                Toast.makeText(activity, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
                AlaviFile.FileDelete(dir, filename);
                AlaviUtill.callsub(activity, subname, 0, this);
                return;}


            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            if (crop_aspectX != 0 && crop_aspectY != 0) {
                cropIntent.putExtra("aspectX", crop_aspectX);
                cropIntent.putExtra("aspectY", crop_aspectY);
            }


            if (crop_outputX != 0 && crop_outputY != 0) {
                cropIntent.putExtra("outputX", crop_outputX);
                cropIntent.putExtra("outputY", crop_outputY);
            }


            cropIntent.putExtra("return-data", true);

            Intent i   = new Intent(cropIntent);
            ResolveInfo res = (ResolveInfo) list.get(0);
            i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            activity.startActivityForResult(cropIntent, requestCode_PIC_CROP);


        } catch (Exception e) {
            Log.d("alavi_log", "cropPic: " + e.getMessage());
        }

    }

    public void selectFromGallery(Activity activity, boolean crop, String subname) {

        crop_yes = crop;
        this.activity = activity;
        this.subname = subname;
        success = false;
        image = null;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Choose Picture"), requestCode_Select_Gallery);
    }


    public void selectFromGCamera(Activity activity, boolean crop, String subname) {
        crop_yes = crop;
        this.activity = activity;
        this.subname = subname;
        image = null;
        success = false;
        AlaviFile alaviFile = new AlaviFile(activity);
        cameraFile = new File( alaviFile.getExternalCacheDir(),String.valueOf(System.currentTimeMillis()) + ".jpg");


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT > M){
            fileUri=  FileProvider.getUriForFile(inContext, BuildConfig.APPLICATION_ID +".provider"
                    ,
                    cameraFile);

            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        }else {
            fileUri = Uri.fromFile(cameraFile);
        }




        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(cameraIntent, requestCode_Select_Camera);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {

            // Let's read picked image path using content resolver


            if (requestCode == requestCode_Select_Camera) {

//                image = data.getExtras().getParcelable("data");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                fileUri = Uri.fromFile(cameraFile);
                image = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);
                if (cameraFile != null) cameraFile.delete();
                if (crop_yes) {
                    cropPic();
                    return;
                }
            }
            if (requestCode == requestCode_Select_Gallery) {

                Uri imageUri = data.getData();
                InputStream inputstream;
                try {
                    inputstream = inContext.getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(inputstream);
                } catch (FileNotFoundException e) {
                    Log.d("alavi_log", "onActivityResult: " + e.getMessage());
                }

                if (crop_yes) {
                    cropPic();
                    return;
                }
            }
            if (requestCode == requestCode_PIC_CROP) {

                if(data.getExtras()!=null){
                    image = data.getExtras().getParcelable("data");}else{
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    fileUri = Uri.fromFile(new File(dir, filename));
                    image = BitmapFactory.decodeFile(fileUri.getPath(),
                            options);

                }

                AlaviFile.FileDelete(dir, filename);
            }

            success = true;
            AlaviUtill.callsub(activity, subname, 0, this);


        } else {
            success = false;
            AlaviUtill.callsub(activity, subname, 0, this);

        }

    }


}




