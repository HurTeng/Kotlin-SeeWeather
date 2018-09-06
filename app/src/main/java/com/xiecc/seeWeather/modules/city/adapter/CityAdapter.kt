package com.xiecc.seeWeather.modules.city.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseViewHolder
import com.xiecc.seeWeather.component.AnimRecyclerViewAdapter
import java.util.*

class CityAdapter(private val mContext: Context, private val mDataList: ArrayList<String>) : AnimRecyclerViewAdapter<CityAdapter.CityViewHolder>() {
    private var mOnItemClickListener: OnRecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder? {
        return CityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_city, parent, false))
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {

        holder.bind(mDataList[position])
        holder.mCardView?.setOnClickListener { v -> mOnItemClickListener?.onItemClick(v, position) }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.mOnItemClickListener = listener
    }

    interface OnRecyclerViewItemClickListener {
        fun onItemClick(view: View, pos: Int)
    }

    inner class CityViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {

        @BindView(R.id.item_city)
        var mItemCity: TextView? = null
        @BindView(R.id.cardView)
        var mCardView: CardView? = null

        public override fun bind(s: String) {
            mItemCity?.text = s
        }
    }
}
