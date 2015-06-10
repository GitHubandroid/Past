package Presenter;

import android.database.sqlite.SQLiteDatabase;

import com.dexafree.materialList.model.Card;

import java.util.ArrayList;
import java.util.List;

import Bean.SinceBean;
import DAO.SinceDAO;
import Model.SinceModel;
import ViewInterface.SinceInterface;

/**
 * Created by SHLSY on 2015/6/2.
 */
public class Presenter {
    public static final String TAG = Presenter.class.getSimpleName();
    SinceInterface sinceInterface;
    SinceModel sinceModel;

    public Presenter(final SinceInterface sinceInterface) {
        this.sinceInterface = sinceInterface;
        sinceModel = new SinceModel();
        sinceModel.setListener(new SinceModel.getSinceListener() {
            @Override
            public void getFinished(ArrayList<SinceBean> list) {
                sinceInterface.Display(list);
            }
        });
    }

    public void getALLSince(final SQLiteDatabase DB) {
        sinceModel.GetAllSince(DB);
    }

    public void Add_ResultHandle(SinceBean sinceBean, SQLiteDatabase DB) {
        sinceModel.InsertSince(sinceBean, DB);
    }
    public void Modify_ResultHandle(SinceBean sinceBean,SQLiteDatabase DB,String OldContent){
        SinceDAO.Update(DB,sinceBean,OldContent);
    }
    public void delete_since(SinceBean sinceBean, SQLiteDatabase DB) {
        SinceDAO.delete(DB,sinceBean);
    }

    public void delete_allCards(List<Card> list,SQLiteDatabase DB){
        sinceModel.DeleteAllCards(DB,list);
    }

    public void Start_Add() {
        sinceInterface.Add();
    }

    public void Share() {
        sinceInterface.Share();
    }

    public void Modify(SinceBean sinceBean){sinceInterface.Modify(sinceBean);}


}
