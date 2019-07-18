package ihsinformatics.com.hydra_mobile.common

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import ihsinformatics.com.hydra_mobile.common.Status.*


class Resource<T>
private constructor(
    val status: Status,
    val data: T?,
    val message: String
) {

    val isSuccess: Boolean
        get() = status == SUCCESS && data != null

    val isLoading: Boolean
        get() = status == LOADING

    val isLoaded: Boolean
        get() = status != LOADING

    companion object {

        fun <T> success(@NonNull data: T): Resource<T> {
            return Resource(SUCCESS, data, "")
        }

        fun <T> error(msg: String, @Nullable data: T): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(@Nullable data: T): Resource<T> {
            return Resource(LOADING, data, "")
        }
    }
}