package com.jsoft.f_test.core.common.result

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
}

sealed class AppError(open val message: String? = null) {
    data object NoInternet : AppError("Internet aloqasi yo'q")
    data object Timeout : AppError("So'rov vaqti tugadi")
    data class Http(val code: Int, override val message: String?) : AppError(message)

    data object NotFound : AppError("Ma'lumot topilmadi")
    data object CacheMiss : AppError("Cache bo'sh")

    data object InvalidCredentials : AppError("Login yoki parol noto'g'ri")
    data object FaceNotDetected : AppError("Yuz aniqlanmadi")

    data class Unknown(val throwable: Throwable? = null) : AppError(throwable?.message)

    data object PermissionDenied : AppError("Ruxsat berilmadi")
    data object LocationDisabled : AppError("Joylashuv xizmati o'chirilgan")
}