package com.android.demoeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.navigation.NavController


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)




        /**
         * This Only for ViewPager 2 ...
         */
        for(i in 0 until 2000) ViewCompat.generateViewId()
        setContentView(R.layout.activity_main)

     }

   // This for Up Button
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp()|| super.onSupportNavigateUp()
//    }


}