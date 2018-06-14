package net.ngorham.tallycounter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Tally Counter
 * TallyViewHolder.java
 * Utility
 * Purpose: Provides access to a RecyclerView.ViewHolder of Tally type item
 *
 * @author Neil Gorham
 * @version 1.0 06/10/2018
 *
 */

public class TallyViewHolder extends RecyclerView.ViewHolder {
    //Private variables
    private View parent;
    private TextView nameLabel;
    private TextView countLabel;
    private FrameLayout addTallyButtonBorder;
    private FrameLayout removeTallyButtonBorder;
    private Button addTallyButton;
    private Button removeTallyButton;

    //Constructor
    public TallyViewHolder(View v){
        super(v);
        this.parent = v;
        this.nameLabel = v.findViewById(R.id.name_text);
        this.countLabel = v.findViewById(R.id.count_text);
        this.addTallyButtonBorder = v.findViewById(R.id.add_tally_button_border);
        this.removeTallyButtonBorder = v.findViewById(R.id.remove_tally_button_border);
        this.addTallyButton = v.findViewById(R.id.add_tally_button);
        this.removeTallyButton = itemView.findViewById(R.id.remove_tally_button);
    }

    public View getParent(){ return parent; }

    public TextView getNameLabel(){ return nameLabel; }

    public TextView getCountLabel(){ return countLabel; }

    public FrameLayout getAddTallyButtonBorder(){ return addTallyButtonBorder; }

    public FrameLayout getRemoveTallyButtonBorder(){ return removeTallyButtonBorder; }

    public Button getAddTallyButton(){ return addTallyButton; }

    public Button getRemoveTallyButton(){ return removeTallyButton; }
}
