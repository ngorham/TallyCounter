package net.ngorham.tallycounter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Tally Counter
 * EventViewHolder.java
 * Utility
 * Purpose: Provides access to a RecyclerView.ViewHolder of Event type item
 *
 * @author Neil Gorham
 * @version 1.0 06/11/2018
 *
 */

public class EventViewHolder extends RecyclerView.ViewHolder {
    //Private variables
    private View parent;
    private TextView descriptionLabel;
    private TextView countLabel;
    private TextView createdOnLabel;

    //Constructor
    public EventViewHolder(View v){
        super(v);
        this.parent = v;
        this.descriptionLabel = v.findViewById(R.id.description_text);
        this.countLabel = v.findViewById(R.id.count_text);
        this.createdOnLabel = v.findViewById(R.id.created_on_text);
    }

    public View getParent(){ return parent; }

    public TextView getDescriptionLabel(){ return descriptionLabel; }

    public TextView getCountLabel(){ return countLabel; }

    public TextView getCreatedOnLabel(){ return createdOnLabel; }
}
