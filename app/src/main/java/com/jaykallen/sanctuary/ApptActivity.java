package com.jaykallen.sanctuary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jay Kallen on 1/16/2017.
 */

public class ApptActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mIdTextView;
    private TextView mTimeTextView;
    private TextView mTitleTextView;
    private ImageView mImageView;
    private Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appt);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String mId = (String) getIntent().getStringExtra(MainActivity.EXTRA_APPT_ID);
        DBAdapter mDBAdapter =  new DBAdapter(ApptActivity.this);
        mDBAdapter.open();
        long id = Long.parseLong(mId);
        Cursor cursor = mDBAdapter.getRow(id);

        String time = cursor.getString(DBAdapter.COL_TIME);
        String title = cursor.getString(DBAdapter.COL_TITLE);
        String image = cursor.getString(DBAdapter.COL_IMAGE);

        mTimeTextView=(TextView)findViewById(R.id.display_time);
        mTitleTextView=(TextView)findViewById(R.id.display_title);
        mIdTextView=(TextView)findViewById(R.id.display_id);
        mImageView=(ImageView)findViewById(R.id.display_image);
        mTimeTextView.setText(time);
        mTitleTextView.setText(title);
        mIdTextView.setText(mId);
        int imageId = getResources().getIdentifier(image, "drawable", getPackageName());
        mImageView.setImageResource(imageId);

        mDeleteButton = (Button) findViewById(R.id.delete_appt_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter mDBAdapter =  new DBAdapter(ApptActivity.this);
                mDBAdapter.open();
                long id = Long.parseLong(mId);
                mDBAdapter.deleteRow(id);
                finish();
            }
        });
    }
}
