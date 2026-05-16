package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val TAG = "LifecycleActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Đảm bảo rằng bạn đang tải activity_main.xml
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate() called: Activity đang được tạo và hiển thị layout phức tạp.")
        // Các logic khác cho Activity của bạn
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() called.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called.")
    }
}