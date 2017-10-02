package com.safety.android.safety.SafeList;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.safety.android.safety.R;
import com.safety.android.safety.SafeBoxFragment;
import com.safety.android.safety.SQLite3.SafeInfo;
import com.safety.android.safety.SingleFragmentActivity;

import java.util.Date;

public class SafeListActivity extends SingleFragmentActivity
        implements SafeListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new SafeListFragment();
    }


    @Override
    protected Fragment createFragment(Date date) {
        return null;
    }



    @Override
    public void onCrimeSelected(SafeInfo safeInfo) {
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent=SafePagerActivity.newIntent(this,safeInfo.getmId());
            startActivity(intent);
        }else {
            Fragment newDetail=SafeBoxFragment.newInstance(safeInfo.getmId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }
}
