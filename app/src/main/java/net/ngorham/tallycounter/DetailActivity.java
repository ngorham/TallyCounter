package net.ngorham.tallycounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Tally Counter
 * DetailActivity.java
 * Detail
 * Purpose: Displays a list of events made on the given tally
 *
 * @author Neil Gorham
 * @version 1.0 06/11/2018
 *
 */

public class DetailActivity extends AppCompatActivity {
    //Private constants
    private static final String TAG = "DetailActivity";
    private final int RESET_COLOR = 6;
    //Private variables
    private Context context;
    private Tally tally;
    private boolean hasChanged = false;
    private RecyclerView recycler;
    private TallyCounterAdapter adapter;
    private TallyCounterDAO dao;
    private int tallyColor;
    private AlertDialog colorAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;
        //Set up Action bar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Set up DAO
        dao = new TallyCounterDAO(this);
        //Store data from intent
         int tallyId = (int)getIntent().getExtras().get(MainActivity.EXTRA_TALLY_ID);
        //Set up Tally
        tally = dao.fetchTally(tallyId);
        if(tally != null){ tallyColor = tally.getColor(); }
        //Set up Action bar and status bar colors
        setActionBarColor();
        //Set up RecyclerView
        recycler = findViewById(R.id.recycler);
        //Set up Layout Manager
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //Set up adapter
        adapter = new TallyCounterAdapter(dao.fetchAllEvents(tallyId), this);
        recycler.setAdapter(adapter);
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
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("hasChanged", hasChanged);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            case R.id.edit_counter: //Edit tally counter action
                editCounterDialog();
                return true;
            case R.id.reset_counter: //Reset tally counter action
                resetCountDialog();
                return true;
            case R.id.delete_counter: //Delete tally counter action
                deleteCounterDialog();
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

    //Utility that sets up Action bar and status bar colors
    private void setActionBarColor(){
        if(tally != null){
            int color = Utilities.colorsPrimary[tally.getColor()];
            getSupportActionBar().setTitle(tally.getName());
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(color)));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                int colorDark = Utilities.colorsPrimaryDark[tally.getColor()];
                getWindow().setStatusBarColor(getResources().getColor(colorDark));
            }
        }
    }

    //Utility to reset counter to starting count
    private void resetCountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle(R.string.reset_counter);
        builder.setIcon(R.drawable.ic_warning_black_18dp);
        builder.setMessage(R.string.dialog_message_reset_counter);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tally.setCount(tally.getStartCount());
                tally.setLastModified(Utilities.getDateTime());
                dao.updateCount(tally, "Reset", RESET_COLOR);
                adapter.setEvents(dao.fetchAllEvents(tally.getId()));
                adapter.notifyDataSetChanged();
                hasChanged = true;
                Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Display Edit Tally Counter AlertDialog
    private void editCounterDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle(R.string.edit_counter);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_counter, null);
        builder.setView(view);
        final EditText nameField = view.findViewById(R.id.name_edit);
        final EditText categoryField = view.findViewById(R.id.category_edit);
        final EditText startCountField = view.findViewById(R.id.start_count_edit);
        final EditText currentCountField = view.findViewById(R.id.current_count_edit);
        final EditText incrementByField = view.findViewById(R.id.increment_by_edit);
        ImageView colorImageView = view.findViewById(R.id.color_img);
        //Set up fields
        if(tally != null){
            nameField.setText(tally.getName());
            categoryField.setText(tally.getCategory());
            startCountField.setText(String.valueOf(tally.getStartCount()));
            currentCountField.setText(String.valueOf(tally.getCount()));
            incrementByField.setText(String.valueOf(tally.getIncrementBy()));
            int color = Utilities.colorsPrimary[tally.getColor()];
            colorImageView.setBackgroundColor(getResources().getColor(color));
        }
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tallyName = nameField.getText().toString().trim();
                String tallyCategory = categoryField.getText().toString().trim();
                double tallyCurrentCount = Double.parseDouble(currentCountField.getText().toString().trim());
                double tallyStartCount = Double.parseDouble(startCountField.getText().toString().trim());
                double tallyIncrementBy =  Double.parseDouble(incrementByField.getText().toString().trim());
                //Edit Tally with fields
                tally.setName(tallyName);
                tally.setCount(tallyCurrentCount);
                tally.setStartCount(tallyStartCount);
                tally.setIncrementBy(tallyIncrementBy);
                tally.setCategory(tallyCategory);
                tally.setColor(tallyColor);
                tally.setLastModified(Utilities.getDateTime());
                //Update Tally in db
                if(dao.updateTally(tally)){
                    //Update event adapter
                    adapter.setEvents(dao.fetchAllEvents(tally.getId()));
                    adapter.notifyDataSetChanged();
                    hasChanged = true;
                    setActionBarColor();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle(R.string.dialog_color);
        GridView gridView = (GridView)getLayoutInflater().inflate(R.layout.dialog_color_select, null);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Store position into Tally color
                tallyColor = position;
                //Set background color of imageView in edit counter dialog
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

    //Display Delete Tally Counter AlertDialog
    private void deleteCounterDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle(R.string.delete_counter);
        builder.setIcon(R.drawable.ic_warning_black_18dp);
        builder.setMessage(R.string.dialog_message_delete_counter);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Delete tally counter
                //Delete events from db
                if(dao.deleteAllEvents(tally.getId())){
                    //Delete tally from db
                    if(dao.deleteTally(tally.getId())){
                        Toast.makeText(getApplicationContext(), "Deleted",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel,null);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
