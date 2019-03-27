package com.safety.android.safety.HiddenCheck;

import android.support.v4.app.Fragment;

import com.safety.android.safety.SingleFragmentActivity;

/**
 * Created by WangJing on 2018/5/15.
 */

public class HiddenCheckListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new HiddenCheckListFragment();
    }
}
