package com.xiecc.seeWeather.modules.main.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.xiecc.seeWeather.R
import com.xiecc.seeWeather.base.BaseViewHolder
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil
import com.xiecc.seeWeather.common.utils.Util
import com.xiecc.seeWeather.component.PLog
import com.xiecc.seeWeather.modules.main.domain.Weather

class MultiCityAdapter(private val mWeatherList: List<Weather>) : RecyclerView.Adapter<MultiCityAdapter.MultiCityViewHolder>() {
    private var mContext: Context? = null
    private var mMultiCityClick: onMultiCityClick? = null

    val isEmpty: Boolean
        get() = mWeatherList.isEmpty()

    fun setMultiCityClick(multiCityClick: onMultiCityClick) {
        this.mMultiCityClick = multiCityClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiCityViewHolder {
        mContext = parent.context
        return MultiCityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_multi_city, parent, false))
    }

    override fun onBindViewHolder(holder: MultiCityViewHolder, position: Int) {

        holder.bind(mWeatherList[position])
        holder.itemView.setOnLongClickListener { v ->
            mMultiCityClick!!.longClick(mWeatherList[holder.adapterPosition].basic!!.city!!)
            true
        }
        holder.itemView.setOnClickListener { v -> mMultiCityClick!!.click(mWeatherList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return mWeatherList.size
    }

    inner class MultiCityViewHolder(itemView: View) : BaseViewHolder<Weather>(itemView) {

        var mDialogCity: TextView? = itemView.findViewById(R.id.dialog_city)
        var mDialogIcon: ImageView? = itemView.findViewById(R.id.dialog_icon)
        var mDialogTemp: TextView? = itemView.findViewById(R.id.dialog_temp)
        var mCardView: CardView? = itemView.findViewById(R.id.cardView)

        public override fun bind(weather: Weather) {

            try {
                mDialogCity?.text = Util.safeText(weather.basic!!.city!!)
                mDialogTemp?.text = String.format("%s℃", weather.now!!.tmp)
            } catch (e: NullPointerException) {
                PLog.e(e.message!!)
            }

            Glide.with(mContext)
                    .load(SharedPreferenceUtil.instance.getInt(weather.now!!.cond!!.txt!!, R.mipmap.none))
                    .asBitmap()
                    .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            mDialogIcon!!.setImageBitmap(resource)
                            mDialogIcon!!.setColorFilter(Color.WHITE)
                        }
                    })

            val code = Integer.valueOf(weather.now?.cond?.code)!!
            CardCityHelper().applyStatus(code, weather.basic!!.city!!, mCardView!!)
        }
    }

    interface onMultiCityClick {
        fun longClick(city: String)

        fun click(weather: Weather)
    }
}
