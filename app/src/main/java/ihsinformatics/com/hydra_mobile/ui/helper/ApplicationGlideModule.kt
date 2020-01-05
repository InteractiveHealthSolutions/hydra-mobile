package ihsinformatics.com.hydra_mobile.ui.helper

import android.content.Context
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


@GlideModule
class ApplicationGlideModule : AppGlideModule() {

    override fun applyOptions(@NonNull context: Context, @NonNull builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
    }

    override fun registerComponents(
        @NonNull context: Context, @NonNull glide: Glide,
        @NonNull registry: Registry
    ) {
       //registry.append(ContactsContract.CommonDataKinds.Photo::class.java, InputStream::class.java, FlickrModelLoader.Factory())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}