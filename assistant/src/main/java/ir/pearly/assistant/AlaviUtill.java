package ir.pearly.assistant;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AlaviUtill {

    private static boolean conect_internet;
    private static int msgbox_resultValue;
    private static String inputBox_resultValue;
    private static boolean permision_resultValue;
    private static ProgressDialog dialog;
    public static boolean show_dialog = false;
    private static AsyncTask asyncTask;
    private static HttpURLConnection connection = null;

    public static int[] unsigned_byte(byte[] signed) {

        int[] unsigned = new int[signed.length];
        for (int i = 0; i < signed.length; i++) {
            unsigned[i] = signed[i] & 0xFF;
        }
        return unsigned;
    }

    public static byte[] signed_byte(int[] unsigned) {

        byte[] bytes = new byte[unsigned.length];
        for (int i = 0; i < unsigned.length; i++) {
            bytes[i] = (byte) (unsigned[i] & 0xFF);
        }
        return bytes;
    }

    public static Date datefrom_Sqlserver(String stringDate) {

        try {

            stringDate = strings.left(stringDate, stringDate.length() - 7);
            stringDate = strings.right(stringDate, stringDate.length() - 6);
            Date date = new Date((long) Double.parseDouble(stringDate));
            return date;
        } catch (Exception e) {
            Log.d("alavi_log", "fromStringToDate: " + e.getMessage());
        }
        return null;
    }

    public static String datefrom_Sqlserver_string(String stringDate) {

        try {

            stringDate = strings.left(stringDate, stringDate.length() - 7);
            stringDate = strings.right(stringDate, stringDate.length() - 6);
            Date date = new Date((long) Double.parseDouble(stringDate));

            return datetime.get_Date(date.getTime());
        } catch (Exception e) {
            Log.d("alavi_log", "fromStringToDate: " + e.getMessage());
        }
        return null;
    }

    public static boolean activity_finished(Context context) {
        boolean t = false;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                t = true;

            }
        }
        return t;
    }

    public static String dateto_Sqlserver(Date date) {

        try {

            String datestring = String.valueOf(date.getTime());
            datestring = "/Date(" + datestring + timeZone() + ")/";

            return datestring;
        } catch (Exception e) {
            Log.d("alavi_log", "fromStringToDate: " + e.getMessage());
        }
        return null;
    }

    public static String dateto_Sqlserver(Long date) {

        try {

            String datestring = getstring(date);
            datestring = "/Date(" + datestring + timeZone() + ")/";

            return datestring;
        } catch (Exception e) {
            Log.d("alavi_log", "fromStringToDate: " + e.getMessage());
        }
        return null;
    }

    public static String timeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        return localTime;
    }


    public static Bitmap drawableToBitmap(Context context, int id) {
        return BitmapFactory.decodeResource(context.getResources(),
                id);
    }

    public static boolean checkInternet(final String url) {
        conect_internet = false;
        final InetAddress[] ipAddr = new InetAddress[1];

        if (asyncTask != null) {
            asyncTask.cancel(true);
        }

        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL url1 = new URL(url);
                    connection = null;
                    connection = (HttpURLConnection) url1.openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();

                    conect_internet = true;
                    waitstop();

                } catch (UnknownHostException e) {
                    conect_internet = false;
                    waitstop();
                } catch (MalformedURLException e) {
                    conect_internet = false;
                    waitstop();
                } catch (ProtocolException e) {
                    conect_internet = false;
                    waitstop();
                } catch (IOException e) {
                    conect_internet = false;
                    waitstop();
                }
                return null;
            }
        };

        asyncTask.execute();
        waitstart();
        return conect_internet;
    }

    public static void copyToClip(Activity activity, String str){
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(str, str);
        clipboard.setPrimaryClip(clip);
    }

    public static class layout {
        public static void setLayoutAnimated(View view, int duration, int top, int left, int width, int height) {
            if (height == 0 || width == 0) return;
            float dif_w = (float) width / (float) view.getWidth();
            float dif_h = (float) height / (float) view.getHeight();
            int d_h = (height - view.getHeight()) / 2;
            int d_t = top - (view.getTop() - d_h);
            int d_w = (width - view.getWidth()) / 2;
            int d_l = left - (view.getLeft() - d_w);


            view.animate().translationY(d_t).setDuration(duration);
            view.animate().scaleY(dif_h).setDuration(duration);
            view.animate().translationX(d_l).setDuration(duration);
            view.animate().scaleX(dif_w).setDuration(duration);


        }

        public static int dpToPx(Context context, int dpValue) {
            float d = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * d);

        }

        public static void layoutParams(View view, int left, int top, int right, int bottom) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams.setMargins(left, top, right, bottom);
            view.setLayoutParams(layoutParams);

        }

        public static int activityHeight(Activity activity) {
            return activity.getWindow().getDecorView().findViewById(android.R.id.content).getHeight();
        }

        public static int activityWidth(Activity activity) {
            return activity.getWindow().getDecorView().findViewById(android.R.id.content).getWidth();
        }
    }

    public static class display {

        public enum screenType {
            mdpi,
            hdpi,
            xhdpi,
            xxhdpi,
            xxxhdpi,
            unKnown
        }

        /**
         * get Device density in float.
         *
         * @param context pass Application Context.
         * @return Device density in float.
         */
        public static float getDensity(Context context) {
            return context.getResources().getSystem().getDisplayMetrics().density;
        }

        /**
         * get Device density types.
         *
         * @param context pass Application Context.
         * @return Device density screenType.
         */
        public static screenType getDensityType(Context context) {
            float density = context.getResources().getSystem().getDisplayMetrics().density;
            if (density == 1)
                return screenType.mdpi;
            else if (density == 1.5)
                return screenType.hdpi;
            else if (density == 2)
                return screenType.xhdpi;
            else if (density == 3)
                return screenType.xxhdpi;
            else if (density == 4)
                return screenType.xxxhdpi;
            else
                return screenType.unKnown;
        }

        /**
         * Convert Dp to Px.
         *
         * @param context pass Application Context.
         * @param Dp      pass Size in Dp.
         * @return Converted Dp to Px in float.
         */
        public static float dp2Px(Context context, int Dp) {
            final float density = context.getResources().getSystem().getDisplayMetrics().density;
            return (Dp * density);
        }


        public static DisplayMetrics displayMetrics(Activity activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            return metrics;
        }

    }

    public static final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message mesg) {
            throw new RuntimeException();
        }
    };

    public static void waitstart() {
        try {
            Looper.loop();
        } catch (RuntimeException e) {
        }
    }

    public static void waitstop() {
        handler.sendMessage(handler.obtainMessage());
    }


    public static class dialogResponse {
        public static final int positive = 1;
        public static final int negative = -1;
        public static final int cancel = 0;

    }

    public static void waitForTime(long time) {
        try {
            CountDownTimer waitTimer;
            waitTimer = new CountDownTimer(time, time) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    AlaviUtill.waitstop();
                }
            }.start();
            AlaviUtill.waitstart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void animat_button(final View view) {
        final long du = 100;


        view.animate().scaleX((float) 0.9).scaleY((float) 0.9).setDuration(du).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.animate().scaleX((float) 1).scaleY((float) 1).setDuration(du).setListener(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }


    public static View loadlayout(Context context, int resourc, ViewGroup root) {
        LayoutInflater l2 = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        return l2.inflate(resourc, root);


    }

    public static String getstring(Object arg) {
        String t = "";
        if (arg == null) {
        } else {
            t = String.valueOf(arg);
        }
        return t;
    }

    public static String getNumber(Object arg) {
        String t = "0";
        if (arg == null) {
        } else {
            t = String.valueOf(arg);
        }
        if (isNumber(t))
            return t;
        else return "0";
    }

    public static String getDuble(Object arg) {
        String t = "0";
        if (arg != null)
            t = arg.toString();


        Double d = Double.parseDouble(t);
        if (d.longValue() == d.doubleValue())
            t = String.valueOf(d.longValue());

        return t;

    }

    public static boolean isNumber(String string) {
        try {
            int amount = Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Animation anim(Context context, int id_anim) {
        Animation slideUp = AnimationUtils.loadAnimation(context, id_anim);
        return slideUp;
    }

    public static void shareText(Activity activity, String my_string, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, my_string);
        activity.startActivity(Intent.createChooser(intent, title));
    }

    public static void shareData(Activity activity, File file, String title) {
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_STREAM, outputFileUri);
        activity.startActivity(Intent.createChooser(intent, title));
    }

    public static List<Integer> inputMultiList(final Context context, List item, Object title, String Positive, String Negative, boolean celable,Integer style_id) {
        final List<Integer> mSelectedItems = new ArrayList<Integer>();

        android.support.v7.app.AlertDialog.Builder builder;
        if(style_id!=null)
            builder = new android.support.v7.app.AlertDialog.Builder(context,style_id);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(context);

        builder.setTitle(title.toString());
        builder.setCancelable(celable);
        builder.setPositiveButton(Positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                waitstop();
            }
        });
        builder.setNegativeButton(Negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedItems.clear();
                waitstop();
            }
        });

        CharSequence[] items = new CharSequence[item.size()];
        for (int i = 0; i < item.size(); i++) {
            Object o = item.get(i);
            if (o instanceof CharSequence) {
                items[i] = (CharSequence) o;
            } else {
                items[i] = String.valueOf(o);
            }
        }
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mSelectedItems.clear();
                waitstop();
            }
        });
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {

                    mSelectedItems.add(which);
                } else if (mSelectedItems.contains(which)) {

                    mSelectedItems.remove(Integer
                            .valueOf(which));
                }
            }
        });
        builder.show();
        waitstart();

        Collections.sort(mSelectedItems);
        return mSelectedItems;
    }

    public static int inputList(final Context context, List item, Object title, int checkItem, boolean celable,Integer style_id) {
        msgbox_resultValue = -1;
        android.support.v7.app.AlertDialog.Builder builder;
        if(style_id!=null)
            builder = new android.support.v7.app.AlertDialog.Builder(context,style_id);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(context);

        builder.setTitle(title.toString());
        builder.setCancelable(celable);

        CharSequence[] items = new CharSequence[item.size()];
        for (int i = 0; i < item.size(); i++) {
            Object o = item.get(i);
            if (o instanceof CharSequence) {
                items[i] = (CharSequence) o;
            } else {
                items[i] = String.valueOf(o);
            }
        }
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                msgbox_resultValue = -1;
                dialog.dismiss();
                waitstop();
            }
        });
        builder.setSingleChoiceItems(items, checkItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgbox_resultValue = which;
                dialog.dismiss();
                waitstop();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgbox_resultValue = -1;
                dialog.dismiss();
                waitstop();
            }
        });

        builder.show();
        waitstart();

        return msgbox_resultValue;
    }

    public static String inputbox(final Context context, Object msg, Object title, String Positive, String Negative, int icon, boolean celable) {
        inputBox_resultValue = "";
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title.toString());
        builder.setMessage(msg.toString());

        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton(Positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputBox_resultValue = input.getText().toString();
                waitstop();
            }
        });
        builder.setNegativeButton(Negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputBox_resultValue = "";
                waitstop();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                inputBox_resultValue = "";
                waitstop();
            }
        });
        builder.setCancelable(celable);
        builder.setIcon(icon);
        builder.show();

        waitstart();
        return inputBox_resultValue;
    }

    public static int msgbox(Context c, Object msg, Object title, String Positive, String Negative, int icon, boolean celable) {
        msgbox_resultValue = 0;

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(c);

        dlgAlert.setMessage(msg.toString());
        dlgAlert.setTitle(title.toString());
        dlgAlert.setPositiveButton(Positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgbox_resultValue = dialogResponse.positive;
                waitstop();

            }
        });
        dlgAlert.setNegativeButton(Negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgbox_resultValue = dialogResponse.negative;
                waitstop();
            }
        });
        dlgAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                msgbox_resultValue = dialogResponse.cancel;
                waitstop();
            }
        });
        dlgAlert.setCancelable(celable);
        dlgAlert.setIcon(icon);
        dlgAlert.create().show();
        waitstart();
        return msgbox_resultValue;
    }

    public static int msgbox(Context c, Object msg, Object title, String Positive, String Negative, Drawable icon, boolean celable) {

        msgbox_resultValue = 0;
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(c);

        dlgAlert.setMessage(msg.toString());
        dlgAlert.setTitle(title.toString());

        dlgAlert.setPositiveButton(Positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgbox_resultValue = dialogResponse.positive;
                waitstop();

            }
        });
        dlgAlert.setNegativeButton(Negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgbox_resultValue = dialogResponse.negative;
                waitstop();
            }
        });
        dlgAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                msgbox_resultValue = dialogResponse.cancel;
                waitstop();
            }
        });

        dlgAlert.setCancelable(celable);
        dlgAlert.setIcon(icon);
        dlgAlert.create().show();
        waitstart();
        return msgbox_resultValue;
    }


    public static int msgbox(Context context, Object msg) {


        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setMessage(msg.toString());

        alert.setPositiveButton("Ok", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        msgbox_resultValue = dialogResponse.positive;
                        waitstop();
                    }
                });

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                msgbox_resultValue = dialogResponse.cancel;
                waitstop();
            }
        });


        alert.show();
        waitstart();


        return msgbox_resultValue;
    }

      public static void show_progressdialog(Context c, Object msg, boolean Cancelable) {
          show_progressdialog(c,msg,Cancelable,false,null);
    }
    public static void show_progressdialog(Context c, Object msg, boolean Cancelable, boolean showProgress) {
        show_progressdialog(c,msg,Cancelable,showProgress,null);
    }
    public static void show_progressdialog(Context c, Object msg, boolean Cancelable, boolean showProgress, DialogInterface.OnCancelListener listener) {
        dialog = new ProgressDialog(c);
        dialog.setMessage(msg.toString());
        dialog.setCancelable(Cancelable);
        show_dialog = true;
        if(showProgress){
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
        }
        if (listener!=null){
        dialog.setOnCancelListener(listener);}
        else{
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    show_dialog = false;
                }
            });
        }
        dialog.show();

    }

public static ProgressDialog getProgressDialog(){
        return dialog;
}

    public static void show_progressdialogChange(Object msg, boolean Cancelable, boolean showProgress, DialogInterface.OnCancelListener listener) {
        if (dialog != null) {
            dialog.setMessage(msg.toString());
            dialog.setCancelable(Cancelable);
            if(listener!=null)
            dialog.setOnCancelListener(listener);
            if(showProgress){
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setProgress(0);
                        }

        }
    }

    public static void hide_progressdialog() {
        if (show_dialog) {
            dialog.dismiss();
            show_dialog = false;
        }

    }

    public static class strings {

        public static List<String> spilit(String str, String cama) {
            ArrayList aList = new ArrayList(Arrays.asList(str.split(cama)));
            return aList;
        }

        public static String leftR(String str, int i) {
            if (i < str.length()) {
                return str.substring(i, str.length());
            }
            return "";
        }

        public static String left(String str, long j) {
            if (j > ((long) str.length())) {
                j = (long) str.length();
            }
            return str.substring(0, (int) j);
        }

        public static String mid(String str, int i, int i2) {
            if (str.length() == 0 || i + i2 > str.length() + 1) {
                return "";
            }
            return str.substring(i - 1, (i + i2) - 1);
        }

        public static String midExtract(String str, String str2, String str3) {
            String str4 = "";
            if (str.length() == 0) {
                return "";
            }
            if (!str.contains(str2) || !str.contains(str3)) {
                return "";
            }
            str4 = str.substring(str.indexOf(str2), str.indexOf(str3));
            return str4.substring(str2.length(), str4.length()).trim();
        }

        public static String right(String str, long j) {
            if (j > ((long) str.length())) {
                j = (long) str.length();
            }
            return str.substring((int) (((long) str.length()) - j));
        }

        public static String addSpaces(int i) {
            String str = "";
            str = "";
            str = "";
            while (str.length() < ((long) i)) {
                str = str + " ";
            }
            return str;
        }

        public static String lower(String str) {
            String str2 = "";
            return str.toLowerCase();
        }


    }


    public static void callsub( Class class1, String sunname) {


        Object dog = null; // invoke empty constructor
        try {
            Method setNameMethod = null;

            try {
                setNameMethod = class1.getMethod(sunname, (Class<?>[]) null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (setNameMethod == null) {
            } else {
                setNameMethod.invoke(class1.newInstance()); // pass arg
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

    public static void callsub(final Class class1, final String sunname, long delay) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method setNameMethod = null;
                    try {

                        setNameMethod = class1.getMethod(sunname, (Class<?>[]) null);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (setNameMethod == null) {
                    } else {

                        setNameMethod.invoke(class1.newInstance()); // pass arg
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }, delay);

    }

    public static void callsub(final Class class1, final String sunname, long delay, final Object arg) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method setNameMethod = null;
                    try {
                        setNameMethod = class1.getMethod(sunname, Object.class);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (setNameMethod == null) {
                    } else {
                        setNameMethod.invoke(class1.newInstance(), arg); // pass arg
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }, delay);

    }

    public static void callsub(final Class class1, final String sunname, long delay, final Object arg1, final Object arg2) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method setNameMethod = null;
                    try {
                        setNameMethod = class1.getMethod(sunname, Object.class, Object.class);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (setNameMethod == null) {
                    } else {
                        setNameMethod.invoke(class1.newInstance(), arg1, arg2); // pass arg
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }, delay);


    }

    public static void callsub( Context c, String sunname) {


        Object dog = null; // invoke empty constructor
        try {
            Method setNameMethod = null;

            try {
                setNameMethod = c.getClass().getMethod(sunname, (Class<?>[]) null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (setNameMethod == null) {
            } else {
                setNameMethod.invoke(c); // pass arg
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    public static void callsub(final Context c, final String sunname, long delay) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method setNameMethod = null;
                    try {
                        setNameMethod = c.getClass().getMethod(sunname, (Class<?>[]) null);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (setNameMethod == null) {
                    } else {
                        setNameMethod.invoke(c); // pass arg
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }, delay);

    }

    public static void callsub(final Context c, final String sunname, long delay, final Object arg) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method setNameMethod = null;
                    try {
                        setNameMethod = c.getClass().getMethod(sunname, Object.class);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (setNameMethod == null) {
                    } else {
                        setNameMethod.invoke(c, arg); // pass arg
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }, delay);

    }

    public static void callsub(final Context c, final String sunname, long delay, final Object arg1, final Object arg2) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method setNameMethod = null;
                    try {
                        setNameMethod = c.getClass().getMethod(sunname, Object.class, Object.class);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    if (setNameMethod == null) {
                    } else {
                        setNameMethod.invoke(c, arg1, arg2); // pass arg
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }, delay);


    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1122) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permision_resultValue = true;
            }

        }

        waitstop();
    }


    public boolean checkPermision(Activity activity, String permision) {


//        write in the parent Activity
//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//            alaviUtill.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        }
//

        permision_resultValue = false;
        if (ActivityCompat.checkSelfPermission(activity, permision) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{permision},
                    1122);

        } else {
            permision_resultValue = true;

        }


        return permision_resultValue;

    }


    public static class device_info {
        public static String getMacAddr() {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        String hex = Integer.toHexString(b & 0xFF);
                        if (hex.length() == 1)
                            hex = "0".concat(hex);
                        res1.append(hex.concat(":"));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
            }
            return "";
        }

        public static String model() {
            return Build.MODEL;
        }

        public static void KEEP_SCREEN_ON(Activity activity, boolean on){
            if (on)
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            else
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        public static String versionsdk() {
            return Build.VERSION.SDK;
        }

        public static String device() {

            return Build.DEVICE;
        }

        public static String product() {

            return Build.PRODUCT;
        }

        public static String fingerprint() {

            return Build.FINGERPRINT;
        }

        public static String display() {

            return Build.DISPLAY;
        }

        public static long time() {

            return Build.TIME;
        }

        public static String brand() {

            return Build.BRAND;
        }

        @SuppressLint("MissingPermission")
        public static String imei(Activity context) {

            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return telephonyManager.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }


        @SuppressLint("MissingPermission")
        public static String imei2(Activity context) {
            String IMEI2 = "";
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                IMEI2 = telephonyManager.getDeviceId(1);
            }
            return IMEI2;
        }


        @SuppressLint("MissingPermission")
        public static String imsi(Activity context) {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            return telephonyManager.getSubscriberId();
        }
    }

    public static class datetime {

        public static long now() {
            return System.currentTimeMillis();
        }

        public static String get_Time(Long date) {
            Date date1 = new Date(date);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            return (String) df.format("hh:mm:ss", new Date());
        }

        public static String get_Date(Long date) {
            Date date1 = new Date(date);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            return (String) df.format("yyyy-MM-dd", new Date());
        }

        public static String get_Date_Time(Long date) {
            Date date1 = new Date(date);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            return (String) df.format("yyyy-MM-dd hh:mm:ss", new Date());
        }

    }

}



