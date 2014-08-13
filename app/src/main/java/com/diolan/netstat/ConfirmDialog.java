package com.diolan.netstat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by d.barkalov on 13.08.2014.
 */
public class ConfirmDialog  extends DialogFragment {

    public static ConfirmDialog newInstance(int title) {
        ConfirmDialog frag = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (getTargetFragment() != null) {
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // empty
                            }
                        }
                )
                .create();
    }

}
