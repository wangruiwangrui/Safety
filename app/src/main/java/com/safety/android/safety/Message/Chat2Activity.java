package com.safety.android.safety.Message;

import android.support.v4.app.Fragment;

import com.safety.android.safety.SingleFragmentActivity;

/**
 * Created by WangJing on 2017/10/11.
 */

public class Chat2Activity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new ChatFragment();
    }
}
