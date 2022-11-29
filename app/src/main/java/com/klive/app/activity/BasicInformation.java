package com.klive.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.klive.app.R;
import com.klive.app.dialogs.cityDialog;
import com.klive.app.dialogs.languageDialog;
import com.klive.app.utils.NetworkCheck;
import com.klive.app.utils.SessionManager;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicInformation extends AppCompatActivity {
    private NetworkCheck networkCheck;
    DatePickerDialog picker;
    TextView age, city, language, next;
    String langId = "";
    EditText name;

    private boolean isNameFill = false;
    private boolean isAgeSelected = false;
    private boolean isCitySelected = false;
    private boolean isLanguageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_basic_information);
        networkCheck = new NetworkCheck();
        init();
        //datePicker = findViewById(R.id.datePicker);
    }

    private void init() {
        name = findViewById(R.id.name);
        age = findViewById(R.id.ageView);
        city = findViewById(R.id.cityView);
        language = findViewById(R.id.languageView);
        next = findViewById(R.id.next);

        name.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        //name.setLongClickable(false);
        //name.setTextIsSelectable(false);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    isNameFill = false;
                    next.setBackground(getApplicationContext().getDrawable(R.drawable.inactive));
                    next.setEnabled(false);
                } else {
                    isNameFill = true;
                    next.setEnabled(true);
                    enableButton();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        name.setFilters(new InputFilter[]{acceptonlyAlphabetValuesnotNumbersMethod()});
    }

    public static InputFilter acceptonlyAlphabetValuesnotNumbersMethod() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean isCheck = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) {
                        sb.append(c);
                    } else {
                        isCheck = false;
                    }
                }
                if (isCheck)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString spannableString = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, spannableString, 0);
                        return spannableString;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
                Matcher match = pattern.matcher(String.valueOf(c));
                return match.matches();
            }
        };
    }

    private void enableButton() {
        if (isNameFill && isAgeSelected && isCitySelected && isLanguageSelected) {
            next.setBackground(getApplicationContext().getDrawable(R.drawable.active));
        }
    }

    public void confirm(View view) {
        if (networkCheck.isNetworkAvailable(getApplicationContext())) {
            if (name.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (age.getText().toString().equals("Age")) {
                Toast.makeText(getApplicationContext(), "Select Age", Toast.LENGTH_SHORT).show();
                return;
            }
            if (city.getText().toString().equals("City")) {
                Toast.makeText(getApplicationContext(), "Select City", Toast.LENGTH_SHORT).show();
                return;
            }
            if (language.getText().toString().equals("Language")) {
                Toast.makeText(getApplicationContext(), "Select Language", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = getSharedPreferences("BASIC_DATA", MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("name", name.getText().toString());
            editor.putString("age", age.getText().toString());
            editor.putString("city", city.getText().toString());
            editor.putString("languageName", language.getText().toString());
            editor.putString("language", langId);
            editor.apply();

            Log.e("nameLog", "nameFromControl = " + name.getText().toString());

            new SessionManager(getApplicationContext()).setUserName(name.getText().toString());

            Log.e("nameLog", "nameFromSession = " + new SessionManager(getApplicationContext()).getName());
            Log.e("nameLog", "nameFromSession 2 = " + new SessionManager(getApplicationContext()).getUserName());

            startActivity(new Intent(getApplicationContext(), ProfilePhoto.class));
        } else {
            Toast.makeText(getApplicationContext(), "Check your connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void age(View view) {
        final Calendar cldr = Calendar.getInstance();
        cldr.add(Calendar.YEAR, -18);
        long upperLimit = cldr.getTimeInMillis();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(BasicInformation.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        age.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        isAgeSelected = true;
                        enableButton();
                    }
                }, year, month, day);
        picker.getDatePicker().setMaxDate(upperLimit);
        picker.show();
    }

    public void city(View view) {
        SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        cityDialog cityDialog = new cityDialog(BasicInformation.this, token);
        cityDialog.show();
        cityDialog.setDialogResult(new cityDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                city.setText(result);
                isCitySelected = true;
                enableButton();
            }
        });
       /* new cityDialog(BasicInformation.this);
        back*/
    }

    public void language(View view) {
        SharedPreferences prefs = getSharedPreferences("OTP_DATA", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        languageDialog languageDialog = new languageDialog(BasicInformation.this, token);
        languageDialog.show();
        languageDialog.setDialogResult(new languageDialog.OnMyDialogResult() {
            @Override
            public void finish(String result, String id) {
                language.setText(result);
                langId = id;
                isLanguageSelected = true;
                enableButton();
            }
        });
    }

    public void onBackPressed() {
        finish();
    }

    public void back(View view) {
        onBackPressed();
    }
}