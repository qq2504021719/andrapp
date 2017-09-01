package com.bignerdranch.android.xundian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButtonView1;
    private Button mButtonView2;
    private Button mButtonView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonView1 = (Button)findViewById(R.id.button_view_1);
        mButtonView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = ViewActivity.newIntent(MainActivity.this,1);

                startActivity(i);
            }
        });

        mButtonView2 = (Button)findViewById(R.id.button_view_2);
        mButtonView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = ViewActivity.newIntent(MainActivity.this,2);

                startActivity(i);
            }
        });

        mButtonView3 = (Button)findViewById(R.id.button_view_3);
        mButtonView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = ViewActivity.newIntent(MainActivity.this,3);

                startActivity(i);
            }
        });
    }
}
