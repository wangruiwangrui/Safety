package com.safety.android.safety.HiddenCheck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safety.android.http.OKHttpFetch;
import com.safety.android.safety.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by WangJing on 2018/5/15.
 */

public class HiddenCheckListFragment extends Fragment{

    private TextView aaa;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.activitiy_hiddenchecklist,container,false);

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        aaa= (TextView) view.findViewById(R.id.aaa);

        return  view;

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {

            String url="/voucher/test/getAllCheck.do?limit=10&offset=0";

            return  new OKHttpFetch(getActivity()).get(url);

        }

        @Override
        protected void onPostExecute(String items) {
            aaa.setText(items);
        }
    }

}
