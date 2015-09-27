package tuannguyen.csci342.com.project.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by tuannguyen on 21/10/2015.
 */
public class DialogCreator {
    public static void createDialog(Context context, String title, String message, View customView, String positiveButton, DialogInterface.OnClickListener positiveListener, String negativeButton, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);
        if (message != null)
            builder.setMessage(message);
        if (customView != null)
            builder.setView(customView);
        if (positiveButton != null)
        {
            builder.setPositiveButton(positiveButton, positiveListener);
        }
        if (negativeButton != null)
        {
            builder.setNegativeButton(negativeButton, negativeListener);
        }
        builder.create().show();
    }
}
