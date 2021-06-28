package com.example.a1ton.adapter

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a1ton.databinding.ItemNumberBinding

class NumberAdapter : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    private var numbers = ArrayList<Int>()
    private var itemNumberTxtViews = ArrayList<AppCompatTextView>()
    private var size = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val binding =
            ItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NumberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val binding = holder.binding

        binding.itemNumberTxtView.layoutParams.width = size
        binding.itemNumberTxtView.layoutParams.height = size

        if(position<numbers.size){
            binding.itemNumberTxtView.text = numbers[position].toString()
        }else{
            binding.itemNumberTxtView.text = ""
        }

        binding.itemNumberTxtView.textSize = size / 4f
        if(position==0){
            binding.itemNumberTxtView.scaleX = 1.7f
            binding.itemNumberTxtView.scaleY = 1.7f
            binding.itemNumberTxtView.setTextColor(Color.parseColor("#AA4E19"))
        }else{
            binding.itemNumberTxtView.scaleX = 0.8f
            binding.itemNumberTxtView.scaleY = 0.8f
            binding.itemNumberTxtView.setTextColor(Color.BLACK)
        }

        itemNumberTxtViews.add(binding.itemNumberTxtView)
    }

    fun nowNumber(position:Int){
        itemNumberTxtViews[position].animate()
            .setDuration(150)
            .setStartDelay(0)
            .scaleX(1.7f)
            .scaleY(1.7f)
            .withEndAction {
                itemNumberTxtViews[position].setTextColor(Color.parseColor("#AA4E19"))
            }
            .start()
    }

    fun update(numbers:ArrayList<Int>,size:Int) {
        this.numbers = numbers
        this.size = size
        itemNumberTxtViews = ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = numbers.size+3

    class NumberViewHolder(val binding: ItemNumberBinding) :
        RecyclerView.ViewHolder(binding.root)
}
