package com.jsoft.f_test.core.network

import com.jsoft.f_test.core.common.result.AppError
import com.jsoft.f_test.core.common.result.AppResult
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): AppResult<T> {
    return try {
        AppResult.Success(apiCall())
    } catch (e: UnknownHostException) {
        AppResult.Error(AppError.NoInternet)
    } catch (e: SocketTimeoutException) {
        AppResult.Error(AppError.Timeout)
    } catch (e: HttpException) {
        AppResult.Error(AppError.Http(code = e.code(), message = e.message()))
    } catch (e: IOException) {
        AppResult.Error(AppError.NoInternet)
    } catch (e: Exception) {
        AppResult.Error(AppError.Unknown(e))
    }
}