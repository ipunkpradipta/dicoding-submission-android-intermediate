package com.ipunkpradipta.submissionstoryapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ipunkpradipta.submissionstoryapp.R
import com.ipunkpradipta.submissionstoryapp.databinding.ActivityDetailStoryBinding
import com.ipunkpradipta.submissionstoryapp.utils.loadImage

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.title_story_detail)


        binding.apply {
            tvDetailName.text = intent.getStringExtra(EXTRA_NAME)
            tvDetailDescription.text = intent.getStringExtra(EXTRA_DESCRIPTION)
            tvDetailPostDate.text = intent.getStringExtra(EXTRA_CREATED_AT)
            binding.ivDetailPhoto.loadImage(intent.getStringExtra(EXTRA_PHOTO_URL))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_PHOTO_URL = "extra_photo_url"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_CREATED_AT = "extra_created_at"
    }
}