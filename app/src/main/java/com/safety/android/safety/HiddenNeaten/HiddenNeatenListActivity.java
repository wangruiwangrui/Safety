package com.safety.android.safety.HiddenNeaten;

import android.support.v4.app.Fragment;

import com.safety.android.safety.SingleFragmentActivity;

public class HiddenNeatenListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new HiddenNeatenListFragment();
    }
}
