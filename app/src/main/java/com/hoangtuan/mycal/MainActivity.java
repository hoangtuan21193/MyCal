package com.hoangtuan.mycal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

//    setting variable
    private static final int RESULT_SETTING = 1;
    int screenColor = 0;
    int maxInputNumber = 9; // set max number in screen default = 9
    int languageCode = 1;
//    GUI component
    TextView tvScreenLine1, tvScreenLine2;
    Button bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, btDot;
    Button btAdd, btSub, btDiv, btMul, btSquareRoot, btAddSub, bt1DivX, btPercent, btEqual;
    Button btMC, btMR, btMS, btMPlus, btMSub, btDel, btC, btCE;

    String screenLine1 = "";
    String screenLine2 = "";
    String formula = "";

    double memory = Double.NaN;
    double result = 0;
    int numberDisplayed = 0;

    boolean isNewNumber = false;
    boolean isDoubleNumber = false;
    boolean isFinish = false;
    boolean isOperation = false;
    private boolean isKeepFomat = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        settingButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSetting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTING);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SETTING) {
            if(resultCode == RESULT_OK){
                restartActivity();
            }
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void loadSetting(){
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

//        load setting values from sharedPreference
        screenColor = Integer.parseInt(sharedPrefs.getString("pref_screen_color", "1"));
        maxInputNumber = Integer.parseInt(sharedPrefs.getString("pref_max_input_number", "9"));
        languageCode = Integer.parseInt(sharedPrefs.getString("pref_language", "1"));

//        setting screen color
        switch (screenColor){
            case 1:
                tvScreenLine1.setBackgroundColor(Color.parseColor("#ff99cc00"));
                tvScreenLine2.setBackgroundColor(Color.parseColor("#ff99cc00"));
                tvScreenLine1.setTextColor(Color.DKGRAY);
                tvScreenLine2.setTextColor(Color.DKGRAY);
                break;
            case 2:
                tvScreenLine1.setBackgroundColor(Color.DKGRAY);
                tvScreenLine2.setBackgroundColor(Color.DKGRAY);
                tvScreenLine1.setTextColor(Color.WHITE);
                tvScreenLine2.setTextColor(Color.WHITE);
                break;
            case 3:
                tvScreenLine1.setBackgroundColor(Color.WHITE);
                tvScreenLine2.setBackgroundColor(Color.WHITE);
                tvScreenLine1.setTextColor(Color.DKGRAY);
                tvScreenLine2.setTextColor(Color.DKGRAY);
                break;
            default:
                break;
        }

//        setting language
        Configuration config = new Configuration();
        switch (languageCode){
            case 1: //English
                config.locale = Locale.ENGLISH;
                break;
            case 2: // Vietnamese
                config.locale = new Locale("vi");
                break;
            default:
                break;
        }
        getApplicationContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void init() {
        tvScreenLine1 = (TextView) findViewById(R.id.tvScreenLine1);
        tvScreenLine2 = (TextView) findViewById(R.id.tvScreenLine2);
//        number button
        bt0 = (Button) findViewById(R.id.bt0);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        bt5 = (Button) findViewById(R.id.bt5);
        bt6 = (Button) findViewById(R.id.bt6);
        bt7 = (Button) findViewById(R.id.bt7);
        bt8 = (Button) findViewById(R.id.bt8);
        bt9 = (Button) findViewById(R.id.bt9);
        btDot = (Button) findViewById(R.id.btDot);

//        operator button
        btAdd = (Button) findViewById(R.id.btAdd);
        btSub = (Button) findViewById(R.id.btSub);
        btMul = (Button) findViewById(R.id.btMul);
        btDiv = (Button) findViewById(R.id.btDiv);
        btSquareRoot = (Button) findViewById(R.id.btSquareRoot);
        bt1DivX = (Button) findViewById(R.id.bt1DivX);
        btPercent = (Button) findViewById(R.id.btPercent);
        btAddSub = (Button) findViewById(R.id.btAddSub);
        btEqual = (Button) findViewById(R.id.btEqual);

//        function button
        btMC = (Button) findViewById(R.id.btMC);
        btMR = (Button) findViewById(R.id.btMR);
        btMS = (Button) findViewById(R.id.btMS);
        btMPlus = (Button) findViewById(R.id.btMPlus);
        btMSub = (Button) findViewById(R.id.btMSub);
        btDel = (Button) findViewById(R.id.btDel);
        btCE = (Button) findViewById(R.id.btCE);
        btC = (Button) findViewById(R.id.btC);
    }

    public void settingButton() {
//        number button
        this.bt0.setOnClickListener(this);
        this.bt1.setOnClickListener(this);
        this.bt2.setOnClickListener(this);
        this.bt3.setOnClickListener(this);
        this.bt4.setOnClickListener(this);
        this.bt5.setOnClickListener(this);
        this.bt6.setOnClickListener(this);
        this.bt7.setOnClickListener(this);
        this.bt8.setOnClickListener(this);
        this.bt9.setOnClickListener(this);
        this.btDot.setOnClickListener(this);

//        operator button
        this.btAdd.setOnClickListener(this);
        this.btSub.setOnClickListener(this);
        this.btMul.setOnClickListener(this);
        this.btDiv.setOnClickListener(this);
        this.btSquareRoot.setOnClickListener(this);
        this.btPercent.setOnClickListener(this);
        this.bt1DivX.setOnClickListener(this);
        this.btEqual.setOnClickListener(this);
        this.btAddSub.setOnClickListener(this);

//        function button
        this.btMR.setOnClickListener(this);
        this.btMS.setOnClickListener(this);
        this.btMC.setOnClickListener(this);
        this.btMPlus.setOnClickListener(this);
        this.btMSub.setOnClickListener(this);
        this.btDel.setOnClickListener(this);
        this.btC.setOnClickListener(this);
        this.btCE.setOnClickListener(this);

        btMC.setEnabled(false);
        btMR.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
//            number button
            case R.id.bt0:
                displayLine2("0");
                break;
            case R.id.bt1:
                displayLine2("1");
                break;
            case R.id.bt2:
                displayLine2("2");
                break;
            case R.id.bt3:
                displayLine2("3");
                break;
            case R.id.bt4:
                displayLine2("4");
                break;
            case R.id.bt5:
                displayLine2("5");
                break;
            case R.id.bt6:
                displayLine2("6");
                break;
            case R.id.bt7:
                displayLine2("7");
                break;
            case R.id.bt8:
                displayLine2("8");
                break;
            case R.id.bt9:
                displayLine2("9");
                break;
            case R.id.btDot:
                displayLine2(".");
                break;

//            operator button
            case R.id.btAdd:
                calculate(" + ");
                break;
            case R.id.btSub:
                calculate(" - ");
                break;
            case R.id.btMul:
                calculate(" * ");
                break;
            case R.id.btDiv:
                calculate(" / ");
                break;
            case R.id.btPercent:
                performPercent();
                break;
            case R.id.bt1DivX:
                perform1DivX();
                break;
            case R.id.btSquareRoot:
                performSquareRoot();
                break;
            case R.id.btAddSub:
                performAddSub();
                break;
            case R.id.btEqual:
                performEqual();
                break;

//            function button
            case R.id.btMC:
                performMC();
                break;
            case R.id.btMR:
                performMR();
                break;
            case R.id.btMS:
                performMS();
                break;
            case R.id.btMSub:
                performMSub();
                break;
            case R.id.btMPlus:
                performMPlus();
                break;
            case R.id.btC:
                performC();
                break;
            case R.id.btCE:
                performCE();
                break;
            case R.id.btDel:
                performDel();
                break;
            default:
                break;
        }
    }

//    print screenLine1 to textView
    public void displayLine1(){
        tvScreenLine1.setText(screenLine1);
    }

//    display preview result of % and +/-
    public void displayPreviewLine1(double number){
        if ("0.0".equals(number%1 +"") || "-0.0".equals(number%1 + "")){
            tvScreenLine2.setText((int)number + "");
            tvScreenLine1.setText(screenLine1 + (int)number);
        }else {
            tvScreenLine2.setText(number + "");
            tvScreenLine1.setText(screenLine1 + number);
        }
        isNewNumber = true;
    }

    public void displayLine2(String number) {
        if (isFinish) {
            tvScreenLine1.setText("");
            isFinish = false;
        }
        if (number.equals(".") && !isDoubleNumber) {
            isDoubleNumber = true;
        } else if (number.equals(".") && isDoubleNumber) {
            return;
        }
        if (isNewNumber) {
            screenLine2 = "";
            tvScreenLine2.setText("");
            isNewNumber = false;
            numberDisplayed = 0;
            isKeepFomat = false;
        }
        numberDisplayed++;
        if (numberDisplayed > maxInputNumber) {
            return;
        }
        screenLine2 += number;
        tvScreenLine2.setText(screenLine2);
        isOperation = false;
    }

    public double getCurrentNumber() {
        double number = Double.NaN;
        try {
            number = Double.parseDouble(tvScreenLine2.getText().toString());
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return number;
    }

    /*
    List function perform Operator
    */

    public void performPercent() {
        double number = getCurrentNumber() * 0.01;
        displayPreviewLine1(number);
    }

    public void performSquareRoot() {
        double current = getCurrentNumber();
        double number = Math.sqrt(current);
        if ("0.0".equals(number%1 +"") || "-0.0".equals(number%1 + "")){
            tvScreenLine2.setText((int)number + "");
        }else {
            tvScreenLine2.setText(number + "");
        }

        if ("0.0".equals(current%1 +"") || "-0.0".equals(current%1 + "")){
            tvScreenLine1.setText(screenLine1 + "√ (" + (int)current + ")");
        }else {
            tvScreenLine1.setText(screenLine1 + "√ (" + current + ")");
        }

        isKeepFomat = true;
        isNewNumber = true;
    }

    public void performAddSub() {
        double number = getCurrentNumber() * (-1);
        displayPreviewLine1(number);
    }

    public void perform1DivX() {
        double current = getCurrentNumber();
        double number = 1/current;
        if ("0.0".equals(number%1 +"") || "-0.0".equals(number%1 + "")){
            tvScreenLine2.setText((int)number + "");
        }else {
            tvScreenLine2.setText(number + "");
        }

        if ("0.0".equals(current%1 +"") || "-0.0".equals(current%1 + "")){
            tvScreenLine1.setText(screenLine1 + "1/(" + (int)current + ")");
        }else {
            tvScreenLine1.setText(screenLine1 + "1/(" + current + ")");
        }

        isKeepFomat = true;
        isNewNumber = true;
    }

    public void performEqual() {
        calculate(" ");
        screenLine1 = "";
        formula = "";
        displayLine1();
        isFinish = true;
        isKeepFomat = false;
    }

    public void calculate(String operation) {
        if ((operation.equals(" + ") || operation.equals(" - ") || operation.equals(" * ") || operation.equals(" / ")) && !isOperation) {
            isOperation = true;
            isFinish = false;
        } else if ((operation.equals(" + ") || operation.equals(" - ") || operation.equals(" * ") || operation.equals(" / ")) && isOperation) {
            return;
        }
        double number = getCurrentNumber();
        formula += number;
        if(!isKeepFomat){
            if ("0.0".equals(number%1 +"") || "-0.0".equals(number%1 + "")){
                screenLine1 += (int)number;
            }else {
                screenLine1 += number;
            }
        }else {
            screenLine1 = tvScreenLine1.getText().toString();
        }

        Expression e = new ExpressionBuilder(formula).build();
        result = e.evaluate();
        formula = result + "";
        if ("0.0".equals(result%1 +"") || "-0.0".equals(result%1 + "")){
            tvScreenLine2.setText((int)result + "");
        }else {
            tvScreenLine2.setText(result + "");
        }


        formula += operation;
        screenLine1 += operation ;
        displayLine1();
        isNewNumber = true;
    }

    /*
    List function perform Function
    */
//    Clear the memory register (set to zero).
    public void performMC() {
        memory = Double.NaN;
        btMC.setEnabled(false);
        btMR.setEnabled(false);
        isNewNumber = true;
    }
//    Set current value in screen line 2 to memory
    public void performMS() {
        memory = getCurrentNumber();
        btMC.setEnabled(true);
        btMR.setEnabled(true);
        if ("0.0".equals(memory%1 +"") || "-0.0".equals(memory % 1 + "")){
            tvScreenLine1.setText("M = " + (int)memory);
        } else {
            tvScreenLine1.setText("M = " + memory);
        }

        isNewNumber = true;
    }
//    Recall the current memory register value.
    public void performMR() {
        if ("0.0".equals(memory%1 +"") || "-0.0".equals(memory % 1 + "")){
            screenLine2 = (int)memory + "";
            tvScreenLine2.setText((int)memory + "");
        }else {
            screenLine2 = memory + "";
            tvScreenLine2.setText(memory + "");
        }
        isNewNumber = true;
    }
//    Add the current value to (or from) the stored value in the memory register.
    public void performMPlus() {
        memory += getCurrentNumber();
        if ("0.0".equals(memory%1 +"") || "-0.0".equals(memory % 1 + "")){
            tvScreenLine1.setText("M = " + (int)memory);
        } else {
            tvScreenLine1.setText("M = " + memory);
        }
        isNewNumber = true;
    }
//    Subtract the current value to (or from) the stored value in the memory register.
    public void performMSub() {
        memory -= getCurrentNumber();
        if ("0.0".equals(memory%1 +"") || "-0.0".equals(memory % 1 + "")){
            tvScreenLine1.setText("M = " + (int)memory);
        } else {
            tvScreenLine1.setText("M = " + memory);
        }
        isNewNumber = true;
    }

    public void performC() {
        tvScreenLine1.setText("");
        tvScreenLine2.setText("0");
        screenLine1 = "";
        screenLine2 = "";
        result = 0;
        formula = "";
        numberDisplayed = 0;
        isOperation = false;
    }

    public void performCE() {
        tvScreenLine2.setText("0");
        screenLine2 = "";
        numberDisplayed = 0;
    }

    public void performDel() {
        String input = tvScreenLine2.getText().toString();
        if (input.length() > 0) {
            tvScreenLine2.setText(input.substring(0, input.length() - 1));
            screenLine2 = tvScreenLine2.getText().toString();
        }
    }
}
