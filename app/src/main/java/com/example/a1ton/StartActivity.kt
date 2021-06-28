package com.example.a1ton

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.a1ton.databinding.ActivityMainBinding
import com.example.a1ton.databinding.ActivityStartBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*


class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    var density: Float? = null
    var standardSize_X: Int? = null
    var standardSize_Y: Int? = null
    var levelSelect = false
    var nToSelect = false
    var nTOn = 0
    var level = 1
    val btnArray = ArrayList<AppCompatButton>()
    val levelBtnArray = ArrayList<AppCompatButton>()
    var preButton : AppCompatButton? = null
    var preLevelButton : AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getScreenSize(this)
        getStandardSize()

        btnArray.add(binding.to8BtnView)
        btnArray.add(binding.to18BtnView)
        btnArray.add(binding.to32BtnView)
        btnArray.add(binding.to50BtnView)
        btnArray.add(binding.to72BtnView)
        levelBtnArray.add(binding.superEasyBtnView)
        levelBtnArray.add(binding.easyBtnView)
        levelBtnArray.add(binding.normalBtnView)
        levelBtnArray.add(binding.hardBtnView)
        levelBtnArray.add(binding.startBtnView)

        binding.to8BtnView.text = "1 To 8"
        binding.to18BtnView.text = "1 To 18"
        binding.to32BtnView.text = "1 To 32"
        binding.to50BtnView.text = "1 To 50"
        binding.to72BtnView.text = "1 To 72"
        binding.superEasyBtnView.text = "SuperEasy"
        binding.easyBtnView.text = "Easy"
        binding.normalBtnView.text = "Normal8"
        binding.hardBtnView.text = "Hard"
        binding.startBtnView.text = "게임 시작"

        binding.to8BtnView.tag = "8"
        binding.to18BtnView.tag = "18"
        binding.to32BtnView.tag = "32"
        binding.to50BtnView.tag = "50"
        binding.to72BtnView.tag = "72"

        binding.superEasyBtnView.tag = "1"
        binding.easyBtnView.tag = "2"
        binding.normalBtnView.tag = "3"
        binding.hardBtnView.tag = "4"

        for (i in 0 until btnArray.size) {
            btnArray[i].setOnClickListener(onClickListener)
        }

        for (i in 0 until levelBtnArray.size) {
            levelBtnArray[i].setOnClickListener(levelClickListener)
        }

        binding.startBtnView.setOnClickListener {
            if(levelSelect&&nToSelect){
                val nextIntent = Intent(this, MainActivity::class.java)
                nextIntent.putExtra("nTOn", nTOn)
                nextIntent.putExtra("level", level)
                startActivity(nextIntent)
                finish()
            }
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.to8BtnView.textSize = standardSize_X!! / 20.0f
            binding.to18BtnView.textSize = standardSize_X!! / 20.0f
            binding.to32BtnView.textSize = standardSize_X!! / 20.0f
            binding.to50BtnView.textSize = standardSize_X!! / 20.0f
            binding.to72BtnView.textSize = standardSize_X!! / 20.0f
            binding.superEasyBtnView.textSize = standardSize_X!! / 20.0f
            binding.easyBtnView.textSize = standardSize_X!! / 20.0f
            binding.normalBtnView.textSize = standardSize_X!! / 20.0f
            binding.hardBtnView.textSize = standardSize_X!! / 20.0f
            binding.startBtnView.textSize = standardSize_X!! / 10.0f
        } else {
            binding.to8BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to18BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to32BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to50BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to72BtnView.textSize = standardSize_Y!! / 20.0f
            binding.superEasyBtnView.textSize = standardSize_Y!! / 20.0f
            binding.easyBtnView.textSize = standardSize_Y!! / 20.0f
            binding.normalBtnView.textSize = standardSize_Y!! / 20.0f
            binding.hardBtnView.textSize = standardSize_Y!! / 20.0f
            binding.startBtnView.textSize = standardSize_Y!! / 10.0f
        }

    }

    fun getScreenSize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    fun getStandardSize() {
        val ScreenSize: Point = getScreenSize(this)
        density = resources.displayMetrics.density
        standardSize_X = (ScreenSize.x / density!!).toInt()
        standardSize_Y = (ScreenSize.y / density!!).toInt()
    }


    private val onClickListener = View.OnClickListener { view ->
        nTOn = view.tag.toString().toInt()
        if(preButton!=null){
            preButton!!.isEnabled = true
        }
        preButton = view as AppCompatButton
        view.isEnabled = false
        nToSelect = true
        Log.e("제곱근","${sqrt(nTOn/2f)}")
    }

    private val levelClickListener = View.OnClickListener { view ->
        level = view.tag.toString().toInt()
        if(preLevelButton!=null){
            preLevelButton!!.isEnabled = true
        }
        preLevelButton = view as AppCompatButton
        view.isEnabled = false
        levelSelect = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("nTOn", nTOn)
        outState.putInt("level", level)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nTOn = savedInstanceState.getInt("nTOn")
        level = savedInstanceState.getInt("level")


        getScreenSize(this)
        getStandardSize()

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.to8BtnView.textSize = standardSize_X!! / 20.0f
            binding.to18BtnView.textSize = standardSize_X!! / 20.0f
            binding.to32BtnView.textSize = standardSize_X!! / 20.0f
            binding.to50BtnView.textSize = standardSize_X!! / 20.0f
            binding.to72BtnView.textSize = standardSize_X!! / 20.0f
            binding.superEasyBtnView.textSize = standardSize_X!! / 20.0f
            binding.easyBtnView.textSize = standardSize_X!! / 20.0f
            binding.normalBtnView.textSize = standardSize_X!! / 20.0f
            binding.hardBtnView.textSize = standardSize_X!! / 20.0f
            binding.startBtnView.textSize = standardSize_X!! / 10.0f
        } else {
            binding.to8BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to18BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to32BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to50BtnView.textSize = standardSize_Y!! / 20.0f
            binding.to72BtnView.textSize = standardSize_Y!! / 20.0f
            binding.superEasyBtnView.textSize = standardSize_Y!! / 20.0f
            binding.easyBtnView.textSize = standardSize_Y!! / 20.0f
            binding.normalBtnView.textSize = standardSize_Y!! / 20.0f
            binding.hardBtnView.textSize = standardSize_Y!! / 20.0f
            binding.startBtnView.textSize = standardSize_Y!! / 10.0f
        }
    }

}