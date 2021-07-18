package com.dajustock;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dajustock.stockchart.R;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    //声明控件
    private Button mybutlogin;
    private EditText mETUser;
    private EditText mETpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //找控件
        mybutlogin  = findViewById(R.id.btn_login);
        mETUser = findViewById(R.id.et_1);
        mETpassword = findViewById(R.id.et_2);


        //直接跳转-方法1
//        mybutlogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent Intent=null;
//                Intent = new Intent(MainActivity.this,FunctionActivity.class);
//                startActivity(Intent);
//
//            }
//        });
        mybutlogin.setOnClickListener(this);
    }
    public void onClick(View v){
        //获取用户输入
        String username = mETUser.getText().toString();
        String password = mETpassword.getText().toString();
        //弹出内容
        String ok = "登陆成功";
        String fail="登陆失败";
        Intent intent = null;
        //判断用户名密码是否正确
        if(username.equals("123")&&password.equals("123")){
            //toast普通
            Toast.makeText(getApplicationContext(),ok,Toast.LENGTH_SHORT).show();
            //如果正确跳转
            intent = new Intent(MainActivity2.this, mainpage.class);
            startActivity(intent);

        }else{
            //不正确
            //Toast升级版
            Toast toastCenter = Toast.makeText(getApplicationContext(),fail,Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();


        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}