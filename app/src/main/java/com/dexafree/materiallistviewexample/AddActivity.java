package com.dexafree.materiallistviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.Dialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import Bean.SinceBean;
import Utils.CalendarUtils;
import Utils.ThemeUtil;


public class AddActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    final Calendar calendar = Calendar.getInstance();
    public static final String DATEPICKER_TAG = "datepicker";
    MaterialEditText DateEdit, ContentEdit;
    ButtonFlat SureButton, CencelButton;
    private Switch mswitch;
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.SetTheme(this,MainActivity.defaultcolor);
        setContentView(R.layout.create);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        InitViews();
        SetListeners();
    }

    private void InitViews() {
        ContentEdit = (MaterialEditText) findViewById(R.id.ContentEdit);
        DateEdit = (MaterialEditText) findViewById(R.id.DateEdit);
        DateEdit.setText(CalendarUtils.format.format(new Date()));
        SureButton = (ButtonFlat) findViewById(R.id.button);
        CencelButton = (ButtonFlat) findViewById(R.id.button2);
        mswitch= (Switch) findViewById(R.id.switchView);
    }

    private void SetListeners() {
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, calendar.get(Calendar.YEAR));
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        SureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int days = 0;

                try {
                    days = CalendarUtils.get_between_days(new Date(), CalendarUtils.format.parse(DateEdit.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (days >= 0) {
                    String content = ContentEdit.getText().toString();
                    if (content.length() > 15) {
                        final Dialog dialog = new Dialog(AddActivity.this, "Past", "描述过长");
                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        if (content.equals("") == false) {
                            SinceBean since = new SinceBean();
                            since.setContent(ContentEdit.getText().toString());
                            since.setDays_num(0);
                            since.setIs_forever(Is_Forever());
                            try {
                                since.setDate(CalendarUtils.format.parse(DateEdit.getText().toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Intent i = new Intent();
                            i.putExtra("Since", since);
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            final Dialog dialog = new Dialog(AddActivity.this, "Past", "请输入描述");
                            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    }
                }else {
                    final Dialog dialog = new Dialog(AddActivity.this, "Past", "请选择今天之前的日期");
                    dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        CencelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });
    }

    private int Is_Forever(){
        if(mswitch.isCheck()){
            return 1;
        }
        return 0;
    }
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {
        DateEdit.setText(new StringBuffer().append(i).append("-").append(i2 + 1).append("-").append(i3).toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_since, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
