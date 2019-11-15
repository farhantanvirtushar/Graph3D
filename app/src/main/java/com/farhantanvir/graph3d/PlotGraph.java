package com.farhantanvir.graph3d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PlotGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_graph);

        TextView textView = (TextView)findViewById(R.id.temp);
        textView.setText(MainActivity.data.equation);
    }
}
