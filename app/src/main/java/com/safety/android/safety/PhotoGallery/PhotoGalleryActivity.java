package com.safety.android.safety.PhotoGallery;

import android.support.v4.app.Fragment;

import com.safety.android.safety.SingleFragmentActivity;


public class PhotoGalleryActivity extends SingleFragmentActivity {
    
    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

}
