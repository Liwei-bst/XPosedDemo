package com.bst.cloud.bstxposeddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnStratXposed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStratXposed = (Button) findViewById(R.id.start_xposed);
        btnStratXposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, toastMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String toastMessage() {
        return "Hello, I'm Xposed!";
    }
}