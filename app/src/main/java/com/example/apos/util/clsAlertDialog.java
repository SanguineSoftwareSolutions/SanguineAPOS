package com.example.apos.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.apos.activity.R;

/**
 * Created by Prashant on 9/21/2015.
 */
public class clsAlertDialog {

    public static void funOpenAlertDialogBox(Context context,String title, String message)
    {
        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setTitle(title);
        // aBuilder.setIcon(R.drawable.icon);
        aBuilder.setMessage(message);

        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });

        aBuilder.show();
    }
}
