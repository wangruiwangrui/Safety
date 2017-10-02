package com.safety.android.safety;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.safety.android.safety.Camera.CameraActivity;
import com.safety.android.safety.SQLite3.SafeInfo;
import com.safety.android.safety.SafeList.SafeListActivity;

import java.io.File;
import java.util.List;
import java.util.UUID;


/**
 * Created by WangJing on 2017/6/19.
 */

public class SafeBoxFragment extends Fragment {
    private static final String ARG_Safe_ID="safe_id";
    private SafeBox mBeatBox;
    private AlertDialog dialog;
    private SafeInfo mSafeInfo;
    private File mPhotoFile;

    private static final int REQUEST_PHOTO= 2;


    public static SafeBoxFragment newInstance(){
        return new SafeBoxFragment();
    }

    public static SafeBoxFragment newInstance(UUID uuid){
        Bundle args=new Bundle();
        args.putSerializable(ARG_Safe_ID,uuid);

       SafeBoxFragment fragment=new SafeBoxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mBeatBox=new SafeBox(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_safe_box,container,false);

        RecyclerView recycleListView= (RecyclerView) view
                .findViewById(R.id.fragment_safe_box_recycler_view);
        recycleListView.setLayoutManager(new GridLayoutManager(null,3));
        recycleListView.setAdapter(new SafeAdapter(mBeatBox.getmSafe()));

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    private class SafeHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private ImageButton mButton;
        private TextView mTextView;
        private View.OnClickListener onClickListener=null;

        public SafeHolder(LayoutInflater inflater,ViewGroup container){
            super(inflater.inflate(R.layout.list_item_box,container,false));

           mButton= (ImageButton) itemView.findViewById(R.id.list_item_imageButton);
            mTextView= (TextView) itemView.findViewById(R.id.list_item_textView);
           mButton.setOnClickListener(this);
        }

        public void bindSafe(SafeInfo safe){
           int i=safe.getId();
            i++;
            if(i==1) {
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box));
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new AlertDialog.Builder(v.getContext()).setTitle("多选对话框")
                                .setNegativeButton("取消", null).setPositiveButton("确定", null)
                                .create();
                        dialog.show();
                    }
                };
            }else
            if(i==2) {
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box2));
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new AlertDialog.Builder(v.getContext()).setTitle("多选对话框")
                                .setNegativeButton("取消", null).setPositiveButton("确定", null)
                                .create();
                        dialog.show();
                    }
                };
            }else
            if(i==3) {
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box3));
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), SafeListActivity.class);
                        startActivity(intent);
                    }
                };
            }else
            if(i==4)
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box4));
            else
            if(i==5)
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box5));
            else
            if(i==6)
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box6));
            else
            if(i==7) {
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box7));

            }else
            if(i==8) {
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box8));

                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   startActivityForResult(captureImage, REQUEST_PHOTO);
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        startActivity(intent);
                    }
                };
            }else
            if(i==9)
               // mButton.setImageDrawable(getResources().getDrawable(R.drawable.menu9));
                mButton.setBackground(getResources().getDrawable(R.drawable.button_box9));

            mTextView.setText(safe.getmName());

        }

        @Override
        public void onClick(View v) {
            if(onClickListener!=null)
               onClickListener.onClick(v);
        }
    }

    private class SafeAdapter extends RecyclerView.Adapter<SafeHolder>{
        private List<SafeInfo> mSafeInfo;

        public SafeAdapter(List<SafeInfo> safeInfo){
            mSafeInfo=safeInfo;
        }

        @Override
        public SafeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new SafeHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SafeHolder holder, int position) {
            SafeInfo msafeInfo=mSafeInfo.get(position);
            holder.bindSafe(msafeInfo);
        }

        @Override
        public int getItemCount() {
            return mSafeInfo.size();
        }
    }
}
