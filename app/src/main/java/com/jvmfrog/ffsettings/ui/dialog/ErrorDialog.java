package com.jvmfrog.ffsettings.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.utils.OtherUtils;

public class ErrorDialog extends MaterialAlertDialogBuilder {
    MaterialAlertDialogBuilder builder;

    public ErrorDialog(@NonNull Context context) {
        super(context);
        builder = new MaterialAlertDialogBuilder(new ContextThemeWrapper(context, R.style.Theme_FFSettings_MaterialAlertDialog));
    }

    public void showWith(String title, String message) {
        builder.setIcon(R.drawable.ic_round_error_outline_24);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Copy error", (dialog, which) -> {
            new OtherUtils(builder.getContext()).copyTextToClipboard(message);
            Toast.makeText(builder.getContext(), "Copied", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Dismiss", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }
}
