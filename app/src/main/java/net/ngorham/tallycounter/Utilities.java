package net.ngorham.tallycounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Tally Counter
 * Utilities.java
 * Utility
 * Purpose: Collection of utilities
 *
 * @author Neil Gorham
 * @version 1.0 06/11/2018
 *
 */

public class Utilities {
    //Primary colors
    public static final int[] colorsPrimary = {
            R.color.colorRedPrimary, R.color.colorGreenPrimary, R.color.colorBluePrimary,
            R.color.colorYellowPrimary, R.color.colorOrangePrimary, R.color.colorPurplePrimary,
            R.color.colorBlackPrimary, R.color.colorGrayPrimary, R.color.colorWhitePrimary
    };
    //Primary dark colors
    public static final int[] colorsPrimaryDark = {
            R.color.colorRedPrimaryDark, R.color.colorGreenPrimaryDark, R.color.colorBluePrimaryDark,
            R.color.colorYellowPrimaryDark, R.color.colorOrangePrimaryDark, R.color.colorPurplePrimaryDark,
            R.color.colorBlackPrimaryDark, R.color.colorGrayPrimaryDark, R.color.colorWhitePrimaryDark
    };
    //Primary light colors
    public static final int[] colorsPrimaryLight = {
            R.color.colorRedPrimaryLight, R.color.colorGreenPrimaryLight, R.color.colorBluePrimaryLight,
            R.color.colorYellowPrimaryLight, R.color.colorOrangePrimaryLight, R.color.colorPurplePrimaryLight,
            R.color.colorBlackPrimaryLight, R.color.colorGrayPrimaryLight, R.color.colorWhitePrimaryLight
    };
    //Utility that creates a string of the current date and time
    public static String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    //Display About AlertDialog
    public static void aboutDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_about);
        builder.setIcon(R.drawable.ic_information_black_18dp);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_about, null);
        builder.setView(view);
        TextView signature = view.findViewById(R.id.signature_text);
        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String website = context.getResources().getString(R.string.website);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                context.startActivity(webIntent);
            }
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //Share intent
    public static void shareApp(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Download this app!");
        context.startActivity(intent);
    }
    //Rate us
    public static void rateUs(Context context){
        String site = context.getResources().getString(R.string.play_store);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
        context.startActivity(intent);
    }
}
