package com.ipunkpradipta.submissionstoryapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipunkpradipta.submissionstoryapp.adapter.LoadingStateAdapter
import com.ipunkpradipta.submissionstoryapp.adapter.StoriesPagerAdapter
import com.ipunkpradipta.submissionstoryapp.data.AuthPreferences
import com.ipunkpradipta.submissionstoryapp.ui.auth.LoginActivity
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityMainBinding
import com.ipunkpradipta.submissionstoryapp.ui.NewStory
import com.ipunkpradipta.submissionstoryapp.ui.MapsActivity
import com.ipunkpradipta.submissionstoryapp.ui.StoriesViewModel
import com.ipunkpradipta.submissionstoryapp.ui.ViewModelFactory
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val storiesViewModel: StoriesViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.title_story_list)

        binding.rvStories.layoutManager = LinearLayoutManager(this)

        val authPref = AuthPreferences.getInstance(dataStore)

        mainViewModel = MainViewModel(authPref)
        mainViewModel.getTokenString().observe(this) { tokenString: String ->
            if (tokenString.isEmpty()) {
                Log.d("TOKEN_EMPTY",tokenString)
                val intentAuth = Intent(this@MainActivity, LoginActivity::class.java)
                intentAuth.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentAuth,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }else{
                Log.d("TOKEN_NOT_EMPTY",tokenString)
                getStories(tokenString)
            }
        }


    }

    private fun getStories(token:String){
        val adapter = StoriesPagerAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        lifecycleScope.launch {
            storiesViewModel.getStories("Bearer $token").observe(this@MainActivity){
                Log.d(TAG,it.toString())
                adapter.submitData(lifecycle,it)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                mainViewModel.removeTokenString()
                true
            }
            R.id.menu_add_story -> {
                val intent = Intent(this@MainActivity, NewStory::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_change -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            R.id.action_open_maps -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                true
            }
            else -> true
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}