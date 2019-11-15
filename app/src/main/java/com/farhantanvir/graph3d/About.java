package com.farhantanvir.graph3d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textView = (TextView)findViewById(R.id.about_name);
        String info="";
        info+="Graph3D";
        textView.setText(info);

        TextView app = (TextView)findViewById(R.id.about_app);
        String appStr = "";
        appStr +="Graph3D is an open source app. This app is designed with OpenglES 2.0\n";
        appStr+="The source code can be found at : https://github.com/farhantanvirtushar/Graph3D";
        app.setText(appStr);

        TextView dev = (TextView)findViewById(R.id.developer);
        String devStr = "";
        devStr+="Developer:\n";
        devStr+="Md. Farhan Tanvir\n";
        devStr+="University Of Dhaka\n";
        devStr+="Email : farhantanvir.2340.csedu@gmail.com\n";
        devStr+="github : github.com/farhantanvirtushar\n";
        devStr+="Linked in : www.linkedin.com/in/farhan-tanvir-bb64b014b/\n";
        dev.setText(devStr);

    }
}
