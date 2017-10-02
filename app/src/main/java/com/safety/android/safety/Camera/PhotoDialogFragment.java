package com.safety.android.safety.Camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.safety.android.safety.R;

/**
 * Created by WangJing on 2017/6/16.
 */

public class PhotoDialogFragment extends DialogFragment {
    Bitmap bitmap;

    public PhotoDialogFragment newInstance(Bitmap bitmap){
        Bundle args=new Bundle();
        this.bitmap=bitmap;
        PhotoDialogFragment fragment=new PhotoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.photo_dialog,null);
        ImageView v= (ImageView) view.findViewById(R.id.photo_dialog);
        v.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity())
                .setView(view).create();
    }

}
