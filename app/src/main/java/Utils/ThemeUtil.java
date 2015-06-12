package Utils;

import android.app.Activity;
import android.content.Intent;

import com.dexafree.materiallistviewexample.R;

/**
 * Created by SHLSY on 2015/6/13.
 */
public class ThemeUtil {

    public static void changeToTheme(Activity activity, int theme)
    {


        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void SetTheme(Activity activity, int theme)
    {
        switch (theme){
            case -12627531:
                activity.setTheme(R.style.CustomTheme1);
                break;
            case -16738680:
                activity.setTheme(R.style.CustomTheme2);
                break;
            case -1499549:
                activity.setTheme(R.style.CustomTheme3);
                break;
            case -3285959:
                activity.setTheme(R.style.CustomTheme6);
                break;
            case -16728876:
                activity.setTheme(R.style.CustomTheme5);
                break;
            case -6543440:
                activity.setTheme(R.style.CustomTheme4);
                break;
        }
    }
}
