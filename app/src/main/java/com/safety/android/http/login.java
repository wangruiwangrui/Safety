package com.safety.android.http;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.safety.android.safety.MainActivity;
import com.safety.android.safety.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login extends AppCompatActivity {

    final String URL=OKHttpFetch.URL;

    private EditText mUsername;
    private EditText mPassWord;
    private CheckBox mCheckbox;

    String username=null;
    String password=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);

        Button mButton=(Button)findViewById(R.id.mButton);
        mUsername=(EditText)findViewById(R.id.mUserName);
        mPassWord=(EditText)findViewById(R.id.mPassWord);
        mCheckbox =(CheckBox)findViewById(R.id.mCheckBox);

        readfrominfo();     //调用读内部存储函数

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    username=mUsername.getText().toString();
                    password=mPassWord.getText().toString();

                   new FetchItemsTask().execute();

            }
        });

    }

    //读内部存储函数
    private void readfrominfo(){
        File file = new File(getFilesDir(),"info.txt");
        if(file.exists()){
            BufferedReader br= null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));      //将字节流转化为输入流然后转化为字符流
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String str = null;
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String [] up= str.split("##");      //将字符串按##分割的部分依次存入字符串型数组up中

            mUsername.setText(up[0]);       //填写username与password
            mPassWord.setText(up[1]);
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String  doInBackground(Void... params) {

            OkHttpClient client=OKHttpFetch.getOkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("campusAdmin", username)
                    .add("password", password)
                    .build();

            final Request loginRequest = new Request.Builder()
                    .url(URL + "/voucher/seller/toLogin.do")
                    .post(formBody)
                    .build();


            Call loginCall = client.newCall(loginRequest);

            String s = null;

            try {
                //非异步执行
                Response loginResponse = loginCall.execute();
                //获取返回数据的头部
                Headers headers = loginResponse.headers();
                HttpUrl loginUrl = loginRequest.url();
                //获取头部的Cookie,注意：可以通过Cooke.parseAll()来获取
                List<Cookie> cookies = Cookie.parseAll(loginUrl, headers);
                System.out.print("cookies=" + cookies);
                //防止header没有Cookie的情况
                if (cookies != null) {
                    //存储到Cookie管理器中
                    client.cookieJar().saveFromResponse(loginUrl, cookies);//这样就将Cookie存储到缓存中了
                    s=loginResponse.body().string();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return s;

        }

        @Override
        protected void onPostExecute(String items) {

            try {
                JSONObject jsonObject=new JSONObject(items);

                if(jsonObject!=null){

                    if (mCheckbox.isChecked()) {
                        File file = new File(getFilesDir(), "info.txt");     //写内部存储到info.txt文件
                        FileOutputStream fos = null;     //创建输出流
                        try {
                            fos = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write((username + "##" + password).getBytes());     //将username##password按位写入文件中
                            fos.close();                                           //关闭流
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Toast.makeText(login.this,"登陆成功",Toast.LENGTH_SHORT).show();        //吐司界面，参数依次为提示发出Activity,提示内容,提示时长

                    Intent intent=new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);

                }else{

                    Toast.makeText(login.this,"登陆失败",Toast.LENGTH_SHORT).show();        //吐司界面，参数依次为提示发出Activity,提示内容,提示时长

                }

            } catch (JSONException e) {

                Toast.makeText(login.this,"登陆失败",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }catch (NullPointerException e){

                Toast.makeText(login.this,"登陆失败",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }


        }

    }



}
