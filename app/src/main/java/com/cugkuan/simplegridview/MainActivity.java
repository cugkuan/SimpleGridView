package com.cugkuan.simplegridview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {



    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflater = getLayoutInflater();

        SimpleGridView gridView = findViewById(R.id.gridView);

        Adapter adapter = new Adapter();

        gridView.setAdapter(adapter);




    }

    class Adapter extends SimpleGridView.GridViewAdapter{

        @Override
        protected View getView(Context context, ViewGroup parent, int position) {

            View view =  inflater.inflate(R.layout.ttt,parent,false);

            return view;
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
}
