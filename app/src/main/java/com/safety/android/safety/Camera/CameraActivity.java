package com.safety.android.safety.Camera;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.safety.android.safety.SingleFragmentActivity;

import java.util.Date;
import java.util.UUID;

/**
 * Created by WangJing on 2017/9/29.
 */

public class CameraActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID=
            "com.safety.android.safety.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent=new Intent(packageContext,CameraActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return  intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID crimeId= (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CameraFragment.newInstance(crimeId);
    }

    @Override
    protected Fragment createFragment(Date date) {
        return null;
    }
}
