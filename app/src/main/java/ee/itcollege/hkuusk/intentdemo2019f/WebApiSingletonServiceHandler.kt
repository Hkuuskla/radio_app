package ee.itcollege.hkuusk.intentdemo2019f

import android.content.Context
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class WebApiSingletonServiceHandler {
    constructor(context: Context){
        mContext = context
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(mContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = WebApiSingletonServiceHandler::class.java.simpleName
        private var instance: WebApiSingletonServiceHandler? = null
        private var mContext : Context? = null

        // Singleton pattern
        @Synchronized
        fun getInstance(context : Context) : WebApiSingletonServiceHandler {
            if (instance == null) {
                instance = WebApiSingletonServiceHandler(context)
            }
            return instance!!
        }

    }
}