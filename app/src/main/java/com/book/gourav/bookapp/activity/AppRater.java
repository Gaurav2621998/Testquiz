package com.book.gourav.bookapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.book.gourav.bookapp.R;

public class AppRater {
    private static String APP_TITLE ="BookDesk";// App Name
    private final static String APP_PNAME = "com.book.gourav.bookapp.activity";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 0;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 0;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }


        SharedPreferences.Editor editor = prefs.edit();


        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();

    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

//        RelativeLayout ll = new RelativeLayout(mContext);
//       // ll.setOrientation(LinearLayout.VERTICAL);
//
//        TextView tv = new TextView(mContext);
//        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
//        tv.setWidth(240);
//        tv.setPadding(4, 0, 4, 10);
//        ll.addView(tv);
//
//        Button b1 = new Button(mContext);
//        b1.setText("Rate " + APP_TITLE);
//        b1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("" + APP_PNAME)));
//                dialog.dismiss();
//            }
//        });
//        ll.addView(b1);
//        // rate code
//        // { https://play.google.com/store/apps/details?id= }
//
//        TextView b2 = new TextView(mContext);
//        b2.setText("Remind me later");
//        b2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//
//            }
//        });
//        ll.addView(b2);
//
//        Button b3 = new Button(mContext);
//        b3.setText("No, thanks");
//        b3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (editor != null) {
//                    editor.putBoolean("dontshowagain", true);
//                    editor.commit();
//                }
//                dialog.dismiss();
//            }
//        });
//        ll.addView(b3);
//
//        dialog.setContentView(ll);
//        dialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Rate the app");
        builder.setMessage("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        builder.setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PNAME)));
                editor.putBoolean("dontshownagain",true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setNeutralButton("NoThanks! ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();

            }
        });
        builder.show();


    }
}

