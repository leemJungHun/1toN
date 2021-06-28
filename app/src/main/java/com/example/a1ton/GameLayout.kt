package com.example.a1ton

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout

class GameLayout(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private var mGameTxtView: AppCompatTextView
    private var mBackground: ConstraintLayout

    private var mQuestNum = 0
    private var mSize = 0
    private var mX = 0.0f
    private var mY = 0.0f
    var mWidth = 0

    init {
        val inflaterService = Context.LAYOUT_INFLATER_SERVICE
        val inflater = context.getSystemService(inflaterService) as LayoutInflater
        val view = inflater.inflate(R.layout.item_game_layout, this, false)
        addView(view)

        mGameTxtView = view.findViewById(R.id.gameTxtView)
        mBackground = view.findViewById(R.id.background)
        mWidth = view.layoutParams.width
    }

    fun setTextSize(size: Float) {
        mGameTxtView.textSize = size
    }

    fun setQuestNum(questNum: Int): GameLayout {
        this.mQuestNum = questNum
        return this
    }

    fun setXY(x: Float, y: Float): GameLayout {
        this.mX = x
        this.mY = y
        return this
    }

    fun setFinally() {
        x = this.mX
        y = this.mY
        if(this.mQuestNum!=0) {
            mGameTxtView.text = this.mQuestNum.toString()
        }else{
            mGameTxtView.text = ""
        }
    }

    fun nowNumber(nowNumber:Int, level:Int) {
        when(level){
            1 -> {
                if(mGameTxtView.text!=""&&nowNumber==mGameTxtView.text.toString().toInt()) {
                    mGameTxtView.setTextColor(Color.parseColor("#AA4E19"))
                }else{
                    mGameTxtView.setTextColor(Color.BLACK)
                }
            }
            2 -> {
                if(mGameTxtView.text!=""&&nowNumber==mGameTxtView.text.toString().toInt()) {
                    mGameTxtView.setTextColor(Color.parseColor("#AA4E19"))
                }else{
                    mGameTxtView.setTextColor(Color.BLACK)
                }
            }
            3 -> {
                if(mGameTxtView.text!=""&&nowNumber==mGameTxtView.text.toString().toInt()) {
                    mGameTxtView.setTextColor(Color.parseColor("#AA4E19"))
                }else{
                    mGameTxtView.setTextColor(Color.BLACK)
                }
            }
            4 -> {
                mGameTxtView.setTextColor(Color.BLACK)
            }
        }

    }

    fun stop() {
        mGameTxtView.setTextColor(Color.parseColor("#FF0000"))
    }

    fun setVisible(visible: Boolean) {
        mGameTxtView.visibility = if(visible) View.VISIBLE else View.GONE
    }

}
