package ViewInterface;

import java.util.ArrayList;

import Bean.SinceBean;

/**
 * Created by SHLSY on 2015/6/1.
 */
public interface SinceInterface {
    public void Add();
    public void Share();
    public void Modify(SinceBean sinceBean);
    public void Display(ArrayList<SinceBean> list);
}
