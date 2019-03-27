package com.safety.android.safety.HiddenNeaten;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.safety.android.http.OKHttpFetch;
import com.safety.android.safety.R;

import okhttp3.FormBody;

public class HiddenNeatenListFragment extends Fragment {
    TextView textView;

    TextView textView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_assetlist, container, false);

        textView= (TextView) v.findViewById(R.id.tv_show);
        textView1= (TextView) v.findViewById(R.id.tv_show2);

        final Button btGet= (Button) v.findViewById(R.id.bt_get);
        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchItemsTask().execute();
            }
        });
        Button btPost= (Button) v.findViewById(R.id.bt_post);
        btPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new FetchItemsTask().execute();
            }
        });



        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {

            FormBody formBody = new FormBody
                    .Builder()
                    .add("limit","1")//设置参数名称和参数值
                    .add("offset","0")
                    .build();

            return new OKHttpFetch(getActivity()).get("/voucher/android/selectAllNeaten.do?limit=10&offset=0&search=");
        }


        @Override
        protected void onPostExecute(String items) {
            textView.setText(items);
        }

    }
}
