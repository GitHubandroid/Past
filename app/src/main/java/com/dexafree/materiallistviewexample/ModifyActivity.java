package com.dexafree.materiallistviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import Bean.SinceBean;
import Utils.CalendarUtils;
import Utils.ThemeUtil;


public class ModifyActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    final Calendar calendar = Calendar.getInstance();
    public static final String DATEPICKER_TAG = "datepicker";
    ButtonFlat SureButton, CencelButton;
    MaterialEditText DateEdit, ContentEdit;
    private LinearLayout linearLayout;
    private SinceBean since;
    private String OldContent;
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.SetTheme(this, MainActivity.defaultcolor);
        setContentView(R.layout.modify);
        InitViews();
        SetListeners();
    }
    private void InitViews() {
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        since= (SinceBean) getIntent().getSerializableExtra("Since");
        OldContent=since.getContent();
        ContentEdit = (MaterialEditText) findViewById(R.id.ContentEdit);
        ContentEdit.setText(OldContent);
        DateEdit = (MaterialEditText) findViewById(R.id.DateEdit);
        DateEdit.setText(CalendarUtils.format.format(since.getDate()));
        SureButton = (ButtonFlat) findViewById(R.id.Modify_button1);
        CencelButton = (ButtonFlat) findViewById(R.id.Modify_button2);
        linearLayout = (LinearLayout) findViewById(R.id.ResetSinceTime);
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
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateEdit.setText(CalendarUtils.format.format(new Date()));
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
                        final Dialog dialog = new Dialog(ModifyActivity.this, "Past", "描述过长");
                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        if (content.equals("") == false) {

                            since.setContent(ContentEdit.getText().toString());

                            try {
                                since.setDate(CalendarUtils.format.parse(DateEdit.getText().toString()));
                                since.setDays_num(CalendarUtils.get_between_days(new Date(),CalendarUtils.format.parse(DateEdit.getText().toString())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Intent i = new Intent();
                            i.putExtra("Since", since);
                            i.putExtra("OldContent",OldContent);
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            final Dialog dialog = new Dialog(ModifyActivity.this, "Past", "请输入描述");
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
                    final Dialog dialog = new Dialog(ModifyActivity.this, "Past", "请选择今天之前的日期");
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
                ModifyActivity.this.finish();
            }
        });
    }
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {
        DateEdit.setText(new StringBuffer().append(i).append("-").append(i2 + 1).append("-").append(i3).toString());
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
