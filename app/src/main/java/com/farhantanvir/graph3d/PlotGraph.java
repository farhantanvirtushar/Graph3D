package com.farhantanvir.graph3d;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class PlotGraph extends AppCompatActivity {

    private GLSurfaceView gLView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);


    }
}
