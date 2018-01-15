package com.jaykallen.sanctuary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Created by Jay Kallen on 1/16/2017

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_APPT_ID = "com.jaykallen.sanctuary.id";
    static final int REQUEST_CODE = 1;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private Toolbar mToolbar;
    private DBAdapter mDBAdapter;
    Button mButtonAdd;
    String flag = "***************";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupToolbar();
        openDB();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    public void openDB(){
        mDBAdapter =  new DBAdapter(this);
        mDBAdapter.open();
    }

    private void setupToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Sanctuary Schedule");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK) {
            Bundle bundle = data.getExtras();
            Appointment appointment = (Appointment)bundle.getSerializable("appt");
            long id =  mDBAdapter.insertRow(appointment.getTime(), appointment.getTitle(), appointment.getImage());
            Log.d("Sanctuary", "Inserted row to database, id = " + id + flag);
            updateUI();
        }
    }

    private void updateUI(){
        Log.d("Sanctuary", "Update UI run" + flag);
        List<Appointment> appointments = new ArrayList<>();
        Cursor cursor = mDBAdapter.getAllRows();
        String output = "";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                String id = cursor.getString(DBAdapter.COL_ROWID);
                String time = cursor.getString(DBAdapter.COL_TIME);
                String title = cursor.getString(DBAdapter.COL_TITLE);
                String image = cursor.getString(DBAdapter.COL_IMAGE);
                String complete = cursor.getString(DBAdapter.COL_COMPLETE);
                Appointment appointment = new Appointment(id,time,title,"N/A",image, (complete.equals("T")));
                appointments.add(appointment);
            } while(cursor.moveToNext());
        }
        cursor.close();
        mRecyclerAdapter = new RecyclerAdapter(appointments);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {
        private List<Appointment> mAppointments;
        public RecyclerAdapter(List<Appointment> appointments) {
            mAppointments = appointments;
        }

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_appointment, parent, false);
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerHolder recyclerholder, int position) {
            Appointment appointment = mAppointments.get(position);
            recyclerholder.bindRecyclerData(appointment);
        }

        @Override
        public int getItemCount(){
            return mAppointments.size();
        }
    }

    private class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mIdTextView;
        private TextView mTimeTextView;
        private TextView mTitleTextView;
        private CheckBox mCompleteCheckBox;
        private Appointment mAppointment;

        public RecyclerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView=(ImageView)itemView.findViewById(R.id.appointment_item_image);
            // mIdTextView=(TextView)itemView.findViewById(R.id.appointment_item_id);
            mTimeTextView=(TextView)itemView.findViewById(R.id.appointment_item_time);
            mTitleTextView=(TextView)itemView.findViewById(R.id.appointment_item_title);
            mCompleteCheckBox=(CheckBox)itemView.findViewById(R.id.appointment_completed);
        }

        public void bindRecyclerData(Appointment appointment) {
            mAppointment = appointment;
            int imageId = getResources().getIdentifier(mAppointment.getImage(), "drawable", getPackageName());
            mImageView.setImageResource(imageId);
            // mIdTextView.setText(mAppointment.getId());
            mTimeTextView.setText(mAppointment.getTime());
            mTitleTextView.setText(mAppointment.getTitle());
            mCompleteCheckBox.setChecked(mAppointment.isCompleted());
            mCompleteCheckBox.setTag(mAppointment);
            mCompleteCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String complete = "F";
                    CheckBox cb = (CheckBox) view;
                    Appointment appt = (Appointment) cb.getTag();
                    appt.setCompleted(cb.isChecked());
                    if (cb.isChecked()) {
                        complete = "T";
                    } else {
                        complete = "F";
                    }
                    mDBAdapter.updateData(mAppointment.getId(), mAppointment, complete);
                }
            });
        }

        @Override
        public void onClick(View v) {
            String id = mAppointment.getId();
            Intent intent = new Intent(MainActivity.this, ApptActivity.class);
            intent.putExtra(EXTRA_APPT_ID, id);
            startActivity(intent);
            Log.d("ScheduleListFragment", "Intent starting with " + mAppointment.getId());
            updateUI();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_appt:
                Intent intent = new Intent(MainActivity.this, AddApptActivity.class);
                final Appointment appt = new Appointment("1","1","1","1","paudarco",false);
                intent.putExtra("appt", appt);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.menu_clear_db:
                mDBAdapter.deleteAll();
                updateUI();
                break;
            case R.id.menu_add_candida:
                mDBAdapter.initDB();
                updateUI();
                break;
            default: break;
        }
        return true;
    }

}
