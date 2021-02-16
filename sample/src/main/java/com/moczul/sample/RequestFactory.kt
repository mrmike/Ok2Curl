package com.moczul.sample

import okhttp3.CacheControl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object RequestFactory {
    const val TYPE_GET = "type_get"
    const val TYPE_POST = "type_post"
    const val TYPE_GET_MODIFIED = "type_get_modified"

    private const val NEW_REPO_BODY = "{" +
            "  \"name\": \"Hello-World\"," +
            "  \"description\": \"This is your first repository\"," +
            "  \"homepage\": \"https://github.com\"," +
            "  \"private\": false," +
            "  \"has_issues\": true," +
            "  \"has_wiki\": true," +
            "  \"has_downloads\": true" +
            "}"

    @JvmStatic
    fun getRequest(type: String?): Request {
        return when (type) {
            TYPE_GET -> sampleGetRequest()
            TYPE_POST -> samplePostRequest()
            TYPE_GET_MODIFIED -> modifiedGetRequest()
            else -> throw IllegalArgumentException("Invalid request type")
        }
    }

    private fun sampleGetRequest(): Request {
        return Request.Builder()
            .url("https://api.github.com/repos/vmg/redcarpet/issues?state=closed")
            .cacheControl(CacheControl.FORCE_CACHE)
            .build()
    }

    private fun samplePostRequest(): Request {
        return Request.Builder()
            .url("https://api.github.com/user/repos")
            .post(NEW_REPO_BODY.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
    }

    private fun modifiedGetRequest(): Request {
        return Request.Builder()
            .url("https://api.github.com/repos/vmg/redcarpet/issues?state=closed")
            .header("Authorization", "Basic bWFjaWVrOnRham5laGFzbG8xMjM=")
            .build()
    }
}