package com.example.madine.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madine.R;

public class Simplify {
    public static void showToastMessageWHITE(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_custom, null);

        ImageView imageViewIcon = layout.findViewById(R.id.imageViewIcon);
        imageViewIcon.setImageResource(R.drawable.baseline_info_24);

        TextView textViewMessage = layout.findViewById(android.R.id.message);
        textViewMessage.setText(message);

        toast.setView(layout);
        toast.show();
    }

    public static void showToastMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_custom, null);

        ImageView imageViewIcon = layout.findViewById(R.id.imageViewIcon);
        imageViewIcon.setImageResource(R.drawable.baseline_info_25);

        TextView textViewMessage = layout.findViewById(R.id.message2);
        textViewMessage.setText(message);

        toast.setView(layout);
        toast.show();
    }
}
