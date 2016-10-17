package com.earthgee.residemenu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import library.ResideMenu;
import library.ResideMenuItem;

public class MainActivity extends AppCompatActivity {

    private ResideMenu resideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        resideMenu= (ResideMenu) findViewById(R.id.reside_menu);
        initMenu();
    }

    private void initMenu(){
        resideMenu.setContentView(R.layout.reside_content);
        ResideMenuItem itemHome=new ResideMenuItem(this,R.drawable.icon_home,"home");
        ResideMenuItem itemProfile=new ResideMenuItem(this,R.drawable.icon_profile,"profile");
        resideMenu.addItemMenu(itemHome);
        resideMenu.addItemMenu(itemProfile);
    }



}
