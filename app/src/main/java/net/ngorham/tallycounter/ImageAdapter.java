package net.ngorham.tallycounter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Tally Counter
 * ImageAdapter.java
 * Utility
 * Purpose: Displays grid of ImageViews
 *
 * @author Neil Gorham
 * @version 1.0 06/12/2018
 *
 */

public class ImageAdapter extends BaseAdapter {
    //Private variables
    private Context context;

    //Constructor
    public ImageAdapter(Context context){
        this.context = context;
    }

    public int getCount() {
        return Utilities.colorsPrimary.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(280, 280));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        int color = Utilities.colorsPrimary[position];
        imageView.setBackgroundColor(context.getResources().getColor(color));
        return imageView;
    }

}
