package com.example.berit.kalkulaator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BroadcastReceiver myReceiver;
    private String display = "";
    private String op = "";
    private String no1 = "";
    private String no2 = "";
    private double n1, n2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate called");
        }

        setContentView(R.layout.activity_main);

        Resources res = getResources();
        float fontSize = res.getDimension(R.dimen.font_size);
    }

    //Arvu salvestamine muutujasse ja kuvamine ekraanil.
    public void onClick(View view){
        Button button = (Button) view;
        String str = button.getText().toString();
        str = validation(str);

        if (op.isEmpty()){
            no1 += str;
        } else {
            if(no2.isEmpty()){
                display = "";
            }
            no2 += str;
        }
        display += str;
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(display);
    }

    //Numbritega teostatava operatsiooni valik
    public void selectOperation(View view){
        if (!no1.isEmpty() && !no2.isEmpty()) {
            Button button = (Button) view;
            broadcastIntent(view);
            String str = button.getText().toString();
            n1 = Double.parseDouble(no1);
            op = str;
            return;
        }
        n1 = Double.parseDouble(no1);
        Button button = (Button) view;
        op = button.getText().toString();
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(no1);
    }

    //Arvutusteenuse kasutamine
    public void broadcastIntent(View view){
        if (no1.isEmpty() || no2.isEmpty()) {
            return;
        }
        final EditText editText = (EditText) findViewById(R.id.editText);
        n2 = Double.parseDouble(no2);
        n1 = Double.parseDouble(no1);
        Intent intent = new Intent();
        intent.setAction("com.example.berit.broadcastsender");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("n1", n1);
        intent.putExtra("n2", n2);
        intent.putExtra("op", op);
        sendOrderedBroadcast(intent, null, myReceiver, null, Activity.RESULT_OK, null, null);
        no2 ="";
        op = "";
    }

    //Tehte kustutamine mälust
    public void doDelete(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        display = "";
        no1 = "";
        op = "";
        no2 = "";
        editText.setText(display);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final EditText editText = (EditText) findViewById(R.id.editText);
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = getResultData();
                editText.setText(result);
                display = result;
                no1 = result;
            }
        };
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStart called");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume called");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onPause called");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStop called");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDestroy called");
        }
    }

    //Valideerimine
    public String validation(String str) {
        if (display.isEmpty() && ".".equals(str)) {
            str = "0.";
        }
        if (!display.isEmpty() && !op.isEmpty() && no2.isEmpty() && ".".equals(str)) {
            str = "0.";
        }
        if (no1.equals("0") && !".".equals(str)) {
            str = "";
        }
        if (display.contains(".") && ".".equals(str)) {
            str = "";
        }
        return str;
    }
}