package com.test.asm_demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.lib.MethodEventManager;
import com.test.lib.MethodObserver;
import com.test.lib.TrackMethod;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_TEST = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MethodEventManager.getInstance().registerMethodObserver(TAG_TEST, new MethodObserver() {
            @Override
            public void onMethodEnter(String tag, String methodName) {
                Log.d("MethodEvent", "method "+ methodName + " enter at time " + System.currentTimeMillis());
            }

            @Override
            public void onMethodExit(String tag, String methodName) {
                Log.d("MethodEvent", "method "+ methodName + " exit at time " + System.currentTimeMillis());
            }
        });


        test();
    }

    @TrackMethod(tag = TAG_TEST)
    public void test() {
        Toast.makeText(this,"撒赖科技了",Toast.LENGTH_SHORT).show();
    }
}