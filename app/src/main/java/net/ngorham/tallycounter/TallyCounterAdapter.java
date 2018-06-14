package net.ngorham.tallycounter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Tally Counter
 * TallyCounterAdapter.java
 * Utility
 * Purpose: Displays list of tally counters in a RecyclerView
 *
 * @author Neil Gorham
 * @version 1.0 06/10/2018
 *
 */

public class TallyCounterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private constants
    private final String TAG = "TallyCounterAdapter";
    private final int TALLY_TYPE = 0;
    private final int EVENT_TYPE = 1;
    //Private variables
    private Context context;
    private Listener listener;
    private ArrayList<Tally> tallies;
    private ArrayList<Event> events;

    public interface Listener {
        void onClick(View view, int position);
        void addTally(View view, int position);
        void removeTally(View view, int position);
    }

    //Tally Constructor
    public TallyCounterAdapter(Context context, ArrayList<Tally> tallies){
        this.context = context;
        this.tallies = tallies;
    }

    //Event Constructor
    public TallyCounterAdapter(ArrayList<Event> events, Context context){
        this.context = context;
        this.events = events;
    }

    //Set onClick Listener
    public void setListener(Listener listener){ this.listener = listener; }

    //Configure Tally type item
    private void configureTally(TallyViewHolder holder, final int position){
        Tally tally = tallies.get(position);
        if(tally != null){
            int color = Utilities.colorsPrimary[tally.getColor()];
            int colorDark = Utilities.colorsPrimaryDark[tally.getColor()];
            int colorLight = Utilities.colorsPrimaryLight[tally.getColor()];
            //Set name
            holder.getNameLabel().setText(tally.getName());
            //Set count
            holder.getCountLabel().setText(String.valueOf(tally.getCount()));
            holder.getParent().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null){ listener.onClick(v, position); }
                }
            });
            holder.getParent().setBackgroundColor(context.getResources().getColor(color));
            holder.getAddTallyButton().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null){ listener.addTally(v, position); }
                }
            });
            holder.getAddTallyButton().setBackgroundColor(context.getResources().getColor(colorLight));
            holder.getAddTallyButtonBorder().setBackgroundColor(context.getResources().getColor(colorDark));
            holder.getRemoveTallyButton().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null){ listener.removeTally(v, position); }
                }
            });
            holder.getRemoveTallyButton().setBackgroundColor(context.getResources().getColor(colorLight));
            holder.getRemoveTallyButtonBorder().setBackgroundColor(context.getResources().getColor(colorDark));
        }
    }

    //Configure Event type item
    private void configureEvent(EventViewHolder holder, final int position){
        Event event = events.get(position);
        if(event != null){
            int color = Utilities.colorsPrimary[event.getColor()];
            //Set description
            holder.getDescriptionLabel().setText(event.getDescription());
            holder.getDescriptionLabel().setTextColor(context.getResources().getColor(color));
            //Set count
            holder.getCountLabel().setText(String.valueOf(event.getCount()));
            //Set createdOn
            holder.getCreatedOnLabel().setText(event.getCreatedOn());
        }
    }

    //Return the item view type
    @Override
    public int getItemViewType(int position){
        if(tallies != null) {
            return TALLY_TYPE;
        } else if(events != null){
            return EVENT_TYPE;
        } else {
            return -1;
        }
    }

    //Create viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == TALLY_TYPE){
            view = inflater.inflate(R.layout.tally_view, parent, false);
            return new TallyViewHolder(view);
        } else if(viewType == EVENT_TYPE){
            view = inflater.inflate(R.layout.view_event, parent, false);
            return new EventViewHolder(view);
        }
        return null;
    }

    //Set data inside viewHolder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        switch(holder.getItemViewType()){
            case TALLY_TYPE:
                TallyViewHolder tvh = (TallyViewHolder)holder;
                configureTally(tvh, position);
                break;
            case EVENT_TYPE:
                EventViewHolder evh = (EventViewHolder)holder;
                configureEvent(evh, position);
        }
    }

    //Return number of items in the data set
    @Override
    public int getItemCount(){
        if(tallies != null) {
            return tallies.size();
        } else if(events != null){
            return events.size();
        } else {
            return 0;
        }
    }

    public void setTallies(ArrayList<Tally> tallies){ this.tallies = tallies; }

    public void setEvents(ArrayList<Event> events){ this.events = events; }

    //Return reference to ArrayList<Tally>
    public ArrayList<Tally> getTallies(){ return tallies; }

    //Return reference to ArrayList<Event>
    public ArrayList<Event> getEvents(){ return events; }
}
