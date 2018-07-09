package com.book.gourav.bookapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.book.gourav.bookapp.R;

public class Privacy_Policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy__policy);

        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarprivacy);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Privacy Policy");
        toolbar.setTitle("Privacy Policy");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
