package org.niralmehta.camerauploadexample.utils.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LoggingInterceptors : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        var tryCount = 0
        while (!response.isSuccessful && tryCount < 3) {
            tryCount++
            // Do anything with response here
            // retry the request
            response = chain.proceed(request)
        }
        return response
    }
}

