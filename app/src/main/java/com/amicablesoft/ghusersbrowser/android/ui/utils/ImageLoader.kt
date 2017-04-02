package com.amicablesoft.ghusersbrowser.android.ui.utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.widget.ImageView
import com.amicablesoft.ghusersbrowser.android.BuildConfig
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

class ImageLoader(ctx: Context) {

    val mPicasso: Picasso = Picasso.with(ctx)

    init {
        mPicasso.isLoggingEnabled = BuildConfig.DEBUG
    }

    fun loadCircleImageAsync(imageUrl: String?, target: ImageView) {
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            val imageUri = Uri.parse(imageUrl)
            mPicasso.load(imageUri)
                .fit()
                .centerCrop()
                .transform(CropCircleTransformation)
                .into(target)
        }
        else {
            target.setImageDrawable(null)
        }
    }

//    fun loadCircleImageAsync(imageUri: Uri?, target: ImageView, width: Int, height: Int) {
//        if (imageUri != null) {
//            mPicasso.load(imageUri)
//                .resize(width, height)
//                .centerCrop()
//                .transform(CropCircleTransformation)
//                .into(target)
//        } else {
//            target.setImageDrawable(null)
//        }
//    }

    private object CropCircleTransformation : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, source.width, source.height)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawCircle((source.width / 2).toFloat(), (source.height / 2).toFloat(), (source.width / 2).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(source, rect, rect, paint)
            source.recycle()
            return output

        }

        override fun key(): String {
            return "circle()"
        }
    }
}
