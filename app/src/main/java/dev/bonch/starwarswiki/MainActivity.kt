package dev.bonch.starwarswiki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.bonch.starwarswiki.fragments.PeopleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, PeopleFragment())
            .commit()
    }
}
