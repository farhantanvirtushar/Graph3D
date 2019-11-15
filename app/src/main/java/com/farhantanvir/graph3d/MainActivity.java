package com.farhantanvir.graph3d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static Data data;
    EquationEvaluation equationEvaluation;
    EditText equation;
    TextView warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        warning = (TextView)findViewById(R.id.invalidWarning);
        equation = (EditText)findViewById(R.id.equation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.graph_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.about)
        {
            //Toast.makeText(this,"hello world",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void plot(View view){

        String equationStr = equation.getText().toString();
        equationEvaluation = new EquationEvaluation(equationStr);



        if (equationEvaluation.parenthesesMismatch){
            warning.setText("Parentheses is not matched");
            return;
        }
        float test = equationEvaluation.evaluate(1,1);
        if((equationEvaluation.invalid))
        {
            warning.setText("Invalid Equation");
            return;
        }
        data = new Data(equationEvaluation);

        Intent intent = new Intent(this,PlotGraph.class);
        startActivity(intent);
    }

    public void input(View view){
        String str="";
        int id = view.getId();
        if(id == R.id.zero)
        {
            str="0";
        }
        else if(id == R.id.one)
        {
            str="1";
        }
        else if(id == R.id.two)
        {
            str="2";
        }
        else if(id == R.id.three)
        {
            str="3";
        }else if(id == R.id.four)
        {
            str="4";
        }else if(id == R.id.five)
        {
            str="5";
        }else if(id == R.id.six)
        {
            str="6";
        }else if(id == R.id.seven)
        {
            str="7";
        }else if(id == R.id.eight)
        {
            str="8";
        }
        else if(id == R.id.nine)
        {
            str="9";
        }
        else if((id == R.id.point)||(id == R.id.plus)||(id == R.id.minus)||(id == R.id.division)||(id == R.id.mul)||(id == R.id.par_open)||(id == R.id.par_close)||(id == R.id.power)||(id == R.id.x)||(id == R.id.y))
        {
            Button b = (Button)view;
            str= b.getText().toString();
        }
        else if((id == R.id.sqrt))
        {
            str="sqrt(";
        }
        else if((id == R.id.exp))
        {
            str="exp(";
        }
        else if(id == R.id.clear){
            equation.setText("");
            return;
        }
        else {
            Button b = (Button)view;
            str= b.getText().toString();
            str+="(";
        }
        String text = equation.getText().toString();
        text=text+str;
        equation.setText(text);
    }
    @Override
    protected void onResume() {
        super.onResume();
        warning.setText("");
    }
}
