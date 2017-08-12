package com.bignerdranch.android.lianxi;

import android.support.v4.app.Fragment;

public class ShangPingListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragmnet(){
        return new ShangPingListFragment();
    }

}
