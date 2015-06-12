package com.dexafree.materiallistviewexample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.cards.BasicButtonsCard;
import com.dexafree.materialList.controller.MaterialListAdapter;
import com.dexafree.materialList.controller.OnDismissCallback;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;
import com.dexafree.materialList.view.MaterialListView;
import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Bean.SinceBean;
import DB.DBHelper;
import Presenter.Presenter;
import Utils.CalendarUtils;
import Utils.ThemeUtil;
import ViewInterface.SinceInterface;


public class MainActivity extends FragmentActivity implements SinceInterface, View.OnClickListener {
    ArrayList<SinceBean> list;
    private MaterialListView mListView;
    private DBHelper dbHelper;
    private SQLiteDatabase DB;
    private Presenter presenter;
    FloatingActionButton AddButton, ShareButton;
    private BasicButtonsCard ModifiedCard;
    static final int AddRequestCode = 1;
    static final int ModifyRequestCode = 2;
    private static boolean DBCanRemove = true;
    final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    public  static int defaultcolor =-12627531;
    private SharedPreferences sp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp =getSharedPreferences("Past",Activity.MODE_PRIVATE);
        if(sp.getInt("color",0)!=0){
            defaultcolor=sp.getInt("color",-12627531);
        }
        ThemeUtil.SetTheme(this,defaultcolor);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowHomeEnabled(false);
        InitView();
        SetListeners();
        // Fill the array with mock content
        fillArray();

    }

    private void InitView() {
        presenter = new Presenter(this);
        dbHelper = new DBHelper(this);
        DB = dbHelper.getWritableDatabase();

        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        AddButton = (FloatingActionButton) findViewById(R.id.action_a);
        AddButton.setTitle("添加Past");
        AddButton.setOnClickListener(this);
        ShareButton = (FloatingActionButton) findViewById(R.id.action_b);
        ShareButton.setOnClickListener(this);
        ShareButton.setTitle("分享");

        colorPickerDialog.initialize(R.string.dialog_title, getThemecolor(), defaultcolor, 3, 2);

    }

    private void SetListeners() {
        // Set the dismiss listener
        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int position) {
                final int index = position;
                final BasicButtonsCard ComeBackcard = (BasicButtonsCard) card;
                final SnackBar snackbar = new SnackBar(MainActivity.this, "撤销删除操作？", "YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DBCanRemove = false;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DBCanRemove = true;
                            }
                        }, 4000);
                        //撤销并重新加入List的操作
                        mListView.addAtPosition(index, ComeBackcard);
                    }
                });
                snackbar.setOnhideListener(new SnackBar.OnHideListener() {
                    @Override
                    public void onHide() {
                        if (DBCanRemove) {
                            presenter.delete_since(list.get(list.size() - index - 1), DB);
                        }

                    }
                });
                snackbar.show();


            }
        });

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardItemView view, int position) {
                ModifiedCard = (BasicButtonsCard) view.getCard();
                presenter.Modify((SinceBean) view.getTag());


            }

            @Override
            public void onItemLongClick(CardItemView view, int position) {
                //Doing Nothing
            }
        });

        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
               if(defaultcolor!=color){
                   defaultcolor=color;
                   SharedPreferences.Editor  editor=sp.edit();
                   editor.putInt("color",defaultcolor);
                   editor.commit();
                   ThemeUtil.changeToTheme(MainActivity.this,defaultcolor);
               }
            }
        });


    }


    private int[] getThemecolor() {
        return new int[]{getResources().getColor(R.color.theme1), getResources().getColor(R.color.theme2), getResources().getColor(R.color.theme3),
                getResources().getColor(R.color.theme4), getResources().getColor(R.color.theme5), getResources().getColor(R.color.theme6)};
    }

    private void fillArray() {
        presenter.getALLSince(DB);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                final Dialog dialog = new Dialog(this, "Past", "将清除所有非永恒的Past，是否继续？");
                dialog.addCancelButton("CANCEL");
                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListView.clearDismiss(new MaterialListAdapter.ClearCallback() {
                            @Override
                            public void onClearDismiss(List<Card> list) {
                                //新启一个线程去删除所有非永恒的Card
                                presenter.delete_allCards(list, DB);
                            }
                        });
                        Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.action_theme:
                colorPickerDialog.show(getSupportFragmentManager(), "ColorPicker");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Add() {
        Intent i = new Intent(this, AddActivity.class);
        startActivityForResult(i, AddRequestCode);
    }

    @Override
    public void Modify(SinceBean sinceBean) {
        Intent i = new Intent(this, ModifyActivity.class);
        i.putExtra("Since", sinceBean);
        startActivityForResult(i, ModifyRequestCode);
    }

    @Override
    public void Share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.app_name));
        share.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.app_description) + "---"
                        + "\n" + "Past，一款充满回忆的APP" + "\n"
        );
        startActivity(Intent.createChooser(share,
                getString(R.string.app_name)));

    }


    @Override
    public void Display(ArrayList<SinceBean> list) {
        this.list = list;
        if (list.size() == 0) {
            Toast.makeText(this, "您在这的回忆空空如也~", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < list.size(); i++) {
                SinceBean since = list.get(i);
                GenerateCards(since);
            }
        }
    }

    private void GenerateCards(SinceBean since) {
        BasicButtonsCard card = new BasicButtonsCard(this);
        card.setTitle(CalendarUtils.get_between_days(new Date(), since.getDate()) + "     DAYS!");
        card.setDescription("The  " + since.getContent() + "  has passed ");
        card.setLeftButtonText("");
        card.setRightButtonText("");
        card.setTag(since);
        SetDisMissibleAndDivide(card, since);
        mListView.addAtStart(card);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AddRequestCode:
                if (resultCode == RESULT_OK) {
                    SinceBean since = (SinceBean) data.getSerializableExtra("Since");
                    GenerateCards(since);
                    presenter.Add_ResultHandle(since, DB);
                }
                break;
            case ModifyRequestCode:
                if (resultCode == RESULT_OK) {
                    SinceBean since = (SinceBean) data.getSerializableExtra("Since");
                    ModifiedCard.setTitle(CalendarUtils.get_between_days(new Date(), since.getDate()) + "     DAYS!");
                    ModifiedCard.setDescription("The  " + since.getContent() + "  has passed ");
                    ModifiedCard.setTag(since);
                    presenter.Modify_ResultHandle(since, DB, data.getStringExtra("OldContent"));
                }
                break;
        }

    }

    private void SetDisMissibleAndDivide(BasicButtonsCard card, SinceBean since) {

        card.setDismissible(PastIsDismissible(since));
        card.setDividerVisible(!PastIsDismissible(since));

    }

    private boolean PastIsDismissible(SinceBean since) {
        //为永恒，不能删除
        if (since.getIs_forever() == 1) {
            return false;
        }
        //默认为可以删除
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_a:
                presenter.Start_Add();
                break;
            case R.id.action_b:
                presenter.Share();
                break;
        }
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
