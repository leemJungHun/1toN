package com.example.a1ton

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1ton.adapter.NumberAdapter
import com.example.a1ton.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val context = this
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    var density: Float? = null
    private var standardSizeX: Int? = null
    private var standardSizeY: Int? = null

    private var timerTask: Timer? = null

    var mAdapter = NumberAdapter()

    var nTOn = 50 // n To n (ex: 1 TO 8 = 8)
    var level = 1
    var sqrtNum = 5.0f
    var useCount = 0

    private var time = 0
    val mContext = this
    var size: Float? = null

    var nowNumber = 1
    var countNumbers = ArrayList<Int>()
    var nowNumbers = ArrayList<Int>()
    var afterNumbers = ArrayList<Int>()
    var nowGameLayout = ArrayList<GameLayout>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getScreenSize(this)
        getStandardSize()

        nTOn = intent.getIntExtra("nTOn", 50)
        level = intent.getIntExtra("level", 0)

        sqrtNum = sqrt(nTOn / 2f)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.nowCountTxtView.textSize = standardSizeY!! / 10.0f
            binding.secTimerTxtView.textSize = standardSizeY!! / 10.0f
            binding.milTimerTxtView.textSize = standardSizeY!! / 10.0f
            binding.playImgView.layoutParams.width = standardSizeY!! / sqrtNum.toInt()
            binding.playImgView.layoutParams.height = standardSizeY!! / sqrtNum.toInt()
            binding.startBtnView.textSize = standardSizeY!! / 20.0f
            binding.selectBtnView.textSize = standardSizeY!! / 20.0f
        } else {
            binding.nowCountTxtView.textSize = standardSizeX!! / 10.0f
            binding.secTimerTxtView.textSize = standardSizeX!! / 10.0f
            binding.milTimerTxtView.textSize = standardSizeX!! / 10.0f
            binding.playImgView.layoutParams.width = standardSizeX!! / sqrtNum.toInt()
            binding.playImgView.layoutParams.height = standardSizeX!! / sqrtNum.toInt()
            binding.startBtnView.textSize = standardSizeX!! / 20.0f
            binding.selectBtnView.textSize = standardSizeX!! / 20.0f
        }

        binding.countListView.setOnTouchListener { _, _ -> true }

        if (savedInstanceState == null) {
            binding.startBtnView.visibility = View.VISIBLE
            binding.startBtnView.text = "Start"

            binding.startBtnView.setOnClickListener {
                start()
                gameStart()
                binding.nowCountTxtView.text = nowNumber.toString()
                binding.startBtnView.visibility = View.GONE
                binding.selectBtnView.visibility = View.GONE
            }
        }
    }

    private fun getScreenSize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    private fun getStandardSize() {
        val screenSize: Point = getScreenSize(this)
        density = resources.displayMetrics.density
        standardSizeX = (screenSize.x / density!!).toInt()
        standardSizeY = (screenSize.y / density!!).toInt()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.e("width", "${binding.gameLayout.width}")
        size = binding.gameLayout.width / sqrtNum
    }

    private fun gameStart() {
        time = 0
        nowNumber = 1
        nowNumbers = ArrayList()
        afterNumbers = ArrayList()
        nowGameLayout = ArrayList()
        countNumbers = ArrayList()
        for (i in 1..nTOn) {
            if (i <= nTOn / 2) {
                nowNumbers.add(i)
                Log.e("startNum", "$i")
            } else {
                afterNumbers.add(i)
                Log.e("afterNum", "$i")
            }
        }
        countNumbers.addAll(nowNumbers)
        countNumbers.addAll(afterNumbers)

        nowNumbers.shuffle()
        afterNumbers.shuffle()
        gameSetting()
    }

    private fun gameSetting() {
        val mLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mAdapter = NumberAdapter()

        binding.countListView.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }

        mAdapter.update(countNumbers, binding.countListView.width / 3)

        for (i in 0 until nowNumbers.size) {
            val gameLayout = GameLayout(this)
            gameLayout.setQuestNum(nowNumbers[i])
                .setXY(size!! * (i % sqrtNum.toInt()), size!! * (i / sqrtNum.toInt()))
                .setVisible(true)
            gameLayout.setFinally()
            binding.gameLayout.addView(gameLayout)
            gameLayout.tag = nowNumbers[i]
            gameLayout.setOnClickListener(onClickListener)
            gameLayout.layoutParams.width = size!!.toInt()
            gameLayout.layoutParams.height = size!!.toInt()
            gameLayout.nowNumber(nowNumber, level, nTOn)
            gameLayout.setTextSize(size!! / 3)
            nowGameLayout.add(gameLayout)
        }
    }

    private val onClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            val clickNumber = view!!.tag as Int
            if (clickNumber == nowNumber) {
                binding.countListView.smoothScrollToPosition(nowNumber + 2 - useCount)
                mAdapter.nowNumber(nowNumber - useCount)
                view.animate()
                    .setDuration(150)
                    .setStartDelay(0)
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .withEndAction {
                        val clickPosition = nowNumbers.indexOf(clickNumber)
                        if (nowNumber <= nTOn / 2) {
                            nowNumbers[clickPosition] = afterNumbers[nowNumber - 1]
                            val gameLayout = GameLayout(mContext)
                            gameLayout.setQuestNum(nowNumbers[clickPosition]).setXY(view.x, view.y)
                                .setVisible(true)
                            gameLayout.setFinally()
                            binding.gameLayout.addView(gameLayout)
                            gameLayout.tag = nowNumbers[clickPosition]
                            binding.gameLayout.removeView(view)
                            gameLayout.setOnClickListener(this)
                            gameLayout.layoutParams.width = size!!.toInt()
                            gameLayout.layoutParams.height = size!!.toInt()
                            gameLayout.setTextSize(size!! / 3)
                            nowGameLayout[clickPosition] = gameLayout
                            nowNumber++
                        } else {
                            nowNumbers[clickPosition] = 0
                            val gameLayout = GameLayout(mContext)
                            gameLayout.setQuestNum(0).setXY(view.x, view.y).setVisible(true)
                            gameLayout.setFinally()
                            binding.gameLayout.addView(gameLayout)
                            gameLayout.tag = nowNumbers[clickPosition]
                            binding.gameLayout.removeView(view)
                            gameLayout.setOnClickListener(this)
                            gameLayout.layoutParams.width = size!!.toInt()
                            gameLayout.layoutParams.height = size!!.toInt()
                            gameLayout.setTextSize(size!! / 3)
                            nowNumber++
                        }
                        for (i in 0 until nowGameLayout.size) {
                            nowGameLayout[i].nowNumber(nowNumber, level, nTOn)
                        }

                        binding.nowCountTxtView.text = nowNumber.toString()

                        if (nowNumber == nTOn + 1) {
                            pause()
                            binding.nowCountTxtView.text = "clear"
                            binding.gameLayout.removeAllViews()
                            binding.gameLayout.addView(binding.gameBackImgView)
                            binding.gameBackImgView.visibility = View.INVISIBLE
                            binding.startBtnView.visibility = View.VISIBLE
                            binding.selectBtnView.visibility = View.VISIBLE
                            binding.selectBtnView.text = "newGame"
                            binding.selectBtnView.setOnClickListener {
                                val nextIntent = Intent(context, StartActivity::class.java)
                                startActivity(nextIntent)
                                finish()
                            }
                            binding.startBtnView.text = "restart"
                        }
                    }
                    .start()
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                for (i in 0 until nowGameLayout.size) {
                    nowGameLayout[i].stop()
                }
                CoroutineScope(mainDispatcher).launch {
                    delay(500L)
                    for (i in 0 until nowGameLayout.size) {
                        nowGameLayout[i].nowNumber(nowNumber, level, nTOn)
                    }
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("nowNumbers", nowNumbers)
        outState.putIntegerArrayList("afterNumbers", afterNumbers)
        outState.putIntegerArrayList("countNumbers", countNumbers)
        outState.putInt("nowNumber", nowNumber)
        outState.putInt("nowTime", time)
        outState.putInt("nTOn", nTOn)
        outState.putInt("level", level)
        for (i in 0 until nowNumber) {
            countNumbers.remove(i)
        }
        useCount = nTOn - countNumbers.size
        outState.putInt("useCount", useCount)

        outState.putString("secTime", binding.secTimerTxtView.text.toString())
        outState.putString("milTime", binding.milTimerTxtView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nowNumbers = savedInstanceState.getIntegerArrayList("nowNumbers")!!
        afterNumbers = savedInstanceState.getIntegerArrayList("afterNumbers")!!
        countNumbers = savedInstanceState.getIntegerArrayList("countNumbers")!!
        nowNumber = savedInstanceState.getInt("nowNumber")
        useCount = savedInstanceState.getInt("useCount")
        time = savedInstanceState.getInt("nowTime")
        nTOn = savedInstanceState.getInt("nTOn")
        level = savedInstanceState.getInt("level")

        getScreenSize(this)
        getStandardSize()

        binding.playImgView.setOnClickListener {
            binding.pauseLayout.visibility = View.GONE
            start()
            Log.e("countNumber SIZE", "${countNumbers.size}")
            gameSetting()
        }

        binding.secTimerTxtView.text = savedInstanceState.getString("secTime")
        binding.milTimerTxtView.text = savedInstanceState.getString("milTime")

        binding.startBtnView.text = "restart"
        binding.nowCountTxtView.text = "clear"
        binding.selectBtnView.text = "newGame"

        binding.startBtnView.setOnClickListener {
            start()
            gameStart()
            binding.nowCountTxtView.text = nowNumber.toString()
            binding.startBtnView.visibility = View.GONE
            binding.selectBtnView.visibility = View.GONE
        }

        if (nowNumber <= nTOn) {
            binding.nowCountTxtView.text = nowNumber.toString()
            binding.pauseLayout.visibility = View.VISIBLE
        } else {
            binding.startBtnView.visibility = View.VISIBLE
            binding.selectBtnView.visibility = View.VISIBLE
            binding.selectBtnView.visibility = View.VISIBLE
            binding.selectBtnView.setOnClickListener {
                val nextIntent = Intent(context, StartActivity::class.java)
                startActivity(nextIntent)
                finish()
            }
        }

    }

    private fun start() {

        timerTask = kotlin.concurrent.timer(period = 10) {    // timer() 호출
            time++    // period=10, 0.01초마다 time를 1씩 증가
            val sec = time / 100    // time/100, 나눗셈의 몫 (초 부분)
            val milli = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)

            // UI조작을 위한 메서드
            runOnUiThread {
                val secTimeTxt: String = when {
                    sec < 10 -> {
                        "00$sec."
                    }
                    sec < 100 -> {
                        "0$sec."
                    }
                    else -> {
                        "$sec."
                    }
                }
                val milTimeTxt: String = if (milli < 10) {
                    "0$milli"
                } else {
                    "$milli"
                }
                binding.secTimerTxtView.text = secTimeTxt    // TextView 세팅
                binding.milTimerTxtView.text = milTimeTxt
            }
        }
    }

    private fun pause() {
        timerTask!!.cancel()
    }
}