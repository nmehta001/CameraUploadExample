package org.niralmehta.camerauploadexample.utils.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp 3 Interceptor
 */
@Singleton
class OutsideInterceptor @Inject constructor() : Interceptor {

    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (token != null) {
            val bearerToken = "Bearer $token"
            val requestBuilder = request.newBuilder()
            requestBuilder.addHeader("Authorization", bearerToken)
            request = requestBuilder.build()
        }

        val response = chain.proceed(request)

        if (response.code() == 401) {
//            if (authUtil.activity != null && authUtil.activity is MainActivity) {
            // unset stored token and everything related to auth should be unset
//                authUtil.clearSession()
            // close main activity and launch SignInActivity
//                val mainActivity = authUtil.activity as MainActivity
//                val intent = Intent(mainActivity, SignInActivity::class.java)
//                mainActivity.startActivity(intent)
//                mainActivity.finish()
//            }
        }

        return response
    }
}
