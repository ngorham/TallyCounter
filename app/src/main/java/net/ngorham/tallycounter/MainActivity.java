package net.ngorham.tallycounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import java.lang.reflect.Method;

/**
 * Tally Counter
 * MainActivity.java
 * Main
 * Purpose: Displays a list of tally counters
 *
 * @author Neil Gorham
 * @version 1.0 06/10/2018
 *
 */
public class MainActivity extends AppCompatActivity {
    //Private constants
    private static final String TAG = "MainActivity";
    private static final int DETAIL_REQUEST_CODE = 1;
    private final int INCREMENT_COLOR = 1;
    private final int DECREMENT_COLOR = 0;
    //Private variables
    private Context context;
    private RecyclerView recycler;
    private TallyCounterAdapter adapter;
    private TallyCounterDAO dao;
    private boolean hasChanged = false;
    private int tallyColor;
    private AlertDialog colorAlertDialog;

    //Public constants
    public static final String EXTRA_TALLY_ID = "tally_id";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //Set up Action bar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //Set up RecyclerView
        recycler = findViewById(R.id.recycler);
        //Set up Layout Manager
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //Set up DAO
        dao = new TallyCounterDAO(this);
        //Set up adapter
        adapter = new TallyCounterAdapter(this, dao.fetchAllTallies());
        recycler.setAdapter(adapter);
        //Set up onClick listener
        adapter.setListener(new TallyCounterAdapter.Listener(){
            @Override
            public void onClick(View view, int position){
                //intent to DetailActivity
                int tallyId = adapter.getTallies().get(position).getId();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(EXTRA_TALLY_ID, tallyId);
                startActivityForResult(intent, DETAIL_REQUEST_CODE);
            }
            @Override
            public void addTally(View view, int position){
                //on click increment count by set increment value
                Tally tally = adapter.getTallies().get(position);
                double newCount = tally.getCount() + tally.getIncrementBy();
                tally.setCount(newCount);
                tally.setLastModified(Utilities.getDateTime());
                //display new count
                //Update count in db
                if(dao.updateCount(tally, "Incremented", INCREMENT_COLOR)){
                    //Notify changes in adapter
                    adapter.notifyItemChanged(position);
                }
            }
            @Override
            public void removeTally(View view, int position){
                //on click decrement count by set increment value
                Tally tally = adapter.getTallies().get(position);
                double newCount = tally.getCount() - tally.getIncrementBy();
                tally.setCount(newCount);
                tally.setLastModified(Utilities.getDateTime());
                //display new count
                //Update count in db
                if(dao.updateCount(tally, "Decremented", DECREMENT_COLOR)){
                    //Notify changes in adapter
                    adapter.notifyItemChanged(position);
                }
            }
        });
        //Add divider item decoration
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new TallyCounterDivider(divider);
        recycler.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dao.close();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        if(hasChanged){
            //Update adapter
            adapter.setTallies(dao.fetchAllTallies());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                hasChanged = data.getExtras().getBoolean("hasChanged");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Display icons and text in overflow menu
        //code found on stackoverflow
        //https://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
            try{
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch(NoSuchMethodException e){
                Log.e("onCreateOptionsMenu", "Did not attach icons", e);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Call when user clicks an item in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){ //Handle action items
            case R.id.add_counter: //Add tally counter action
                addCounterDialog();
                return true;
            case R.id.rate_us: //Rate us action
                Utilities.rateUs(context);
                return true;
            case R.id.app_share: //Share action
                Utilities.shareApp(context);
                return true;
            case R.id.app_about: //About action
                Utilities.aboutDialog(context);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Called when invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }

    //Display Add Tally Counter AlertDialog
    private void addCounterDialog(){
        //Set up AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.add_counter);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_counter, null);
        builder.setView(view);
        final EditText nameField = view.findViewById(R.id.name_edit);
        final EditText categoryField = view.findViewById(R.id.category_edit);
        final EditText countField = view.findViewById(R.id.count_edit);
        final EditText incrementByField = view.findViewById(R.id.increment_by_edit);
        countField.setText("0.0");
        incrementByField.setText("1.0");
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tallyName = nameField.getText().toString().trim();
                String tallyCategory = categoryField.getText().toString().trim();
                double tallyCount = Double.parseDouble(countField.getText().toString().trim());
                double tallyIncrementBy = Double.parseDouble(incrementByField.getText().toString().trim());
                Tally tally = new Tally(0, tallyName, tallyCount, tallyCount,
                        tallyIncrementBy, tallyCategory, tallyColor, Utilities.getDateTime(), Utilities.getDateTime());
                //Add Tally to db
                if(dao.addTally(tally)){
                    adapter.setTallies(dao.fetchAllTallies());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Display Tally color AlertDialog
    public void colorSelectDialog(final View v){
        //Set up AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dialog_color);
        GridView gridView = (GridView)getLayoutInflater().inflate(R.layout.dialog_color_select, null);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Store position into Tally color
                tallyColor = position;
                //Set background color of imageView in add counter dialog
                v.setBackgroundColor(getResources().getColor(Utilities.colorsPrimary[position]));
                //Dismiss dialog
                colorAlertDialog.dismiss();
            }
        });
        builder.setView(gridView);
        builder.setCancelable(true);
        colorAlertDialog = builder.create();
        colorAlertDialog.show();
    }
}
