package com.safety.android.safety.Asset;

import android.support.v4.app.Fragment;

import com.safety.android.safety.SingleFragmentActivity;

public class AssetListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AssetListFragment.newInstance();
    }

}
