package com.udacity.asteroidradar.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.R

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("podSrc")
fun bindPictureOfDaySrc(imageView: ImageView, url: String?) {
    url?.let {
        val context = imageView.context
        Picasso.with(context).load(url).into(imageView)
    }
}

@BindingAdapter("podTitle")
fun bindPictureOfDayTitle(textView: TextView, title: String?) {
    title?.let {
        textView.text = title
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidRowAdapter
    adapter.submitList(data)
}

@BindingAdapter("asteroidSrc")
fun bindAsteroidSrc(imageView: ImageView, isPotentiallyHazardous: Boolean?) {
    isPotentiallyHazardous?.let {
        val context = imageView.context
        if (isPotentiallyHazardous) {
            Picasso.with(context).load(R.drawable.asteroid_hazardous).into(imageView)
        } else {
            Picasso.with(context).load(R.drawable.asteroid_safe).into(imageView)
        }
    }
}
