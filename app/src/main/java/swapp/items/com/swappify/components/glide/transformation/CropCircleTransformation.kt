package swapp.items.com.swappify.components.glide.transformation

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.NonNull
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

class CropCircleTransformation : BitmapTransformation() {

    override fun transform(@NonNull context: Context, @NonNull pool: BitmapPool,
                           @NonNull toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap =
            TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight)

    override fun key(): String = "CropCircleTransformation()"
}