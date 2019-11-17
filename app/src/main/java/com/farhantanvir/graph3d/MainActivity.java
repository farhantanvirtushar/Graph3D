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

import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    static Data data;
    EquationEvaluation equationEvaluation;
    EditText equation;
    TextView warning;
    static Stack<Integer> prevTypedChars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        warning = (TextView)findViewById(R.id.invalidWarning);
        equation = (EditText)findViewById(R.id.equation);
        prevTypedChars = new Stack<>();
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
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
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
        if((id == R.id.zero)||(id == R.id.one)||(id == R.id.two)||(id == R.id.three)||(id == R.id.four)||(id == R.id.five)||(id == R.id.six)||(id == R.id.seven)||(id == R.id.eight)||(id == R.id.nine))
        {
            Button b = (Button)view;
            str= b.getText().toString();
            prevTypedChars.push(1);
        }

        else if((id == R.id.point)||(id == R.id.plus)||(id == R.id.minus)||(id == R.id.division)||(id == R.id.mul)||(id == R.id.par_open)||(id == R.id.par_close)||(id == R.id.power)||(id == R.id.x)||(id == R.id.y))
        {
            Button b = (Button)view;
            str= b.getText().toString();
            prevTypedChars.push(1);
        }
        else if((id == R.id.sqrt))
        {
            str="sqrt(";
            prevTypedChars.push(5);
        }
        else if((id == R.id.exp))
        {
            str="exp(";
            prevTypedChars.push(4);
        }
        else if(id == R.id.del){
            byte chars[] = equation.getText().toString().getBytes();

            //Log.e(TAG, " string size : "+chars.length);
            if(!prevTypedChars.empty())
            {
                int n = prevTypedChars.pop();

                int len = chars.length;
                int i=len-1;
                while (n>0){
                    chars[i]='\0';
                    i--;
                    n--;
                }
                //Log.e(TAG, " stack size : "+prevTypedChars.size());
                equation.setText((new String(chars)).trim());
            }

            return;
        }
        else {
            Button b = (Button)view;
            str= b.getText().toString();
            str+="(";
            int len = str.length();
            prevTypedChars.push(len);
        }
        String text = equation.getText().toString();
        text=text+str;
        equation.setText(text);
        //Log.e(TAG, " stack size : "+prevTypedChars.size());
    }
    @Override
    protected void onResume() {
        super.onResume();
        warning.setText("");
    }
}
