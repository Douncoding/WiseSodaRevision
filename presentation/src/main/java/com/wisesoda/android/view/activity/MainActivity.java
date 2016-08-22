package com.wisesoda.android.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wisesoda.android.R;
import com.wisesoda.android.view.fragment.SignFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new SignFragment());
        fragmentTransaction.commit();
    }
}
