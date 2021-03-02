package com.moczul.ok2curl

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import okio.buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class CurlGenerator(private val configuration: Configuration) {

    fun generateCommand(request: Request): String {
        return configuration.components
            .flatMap { generateCommandComponent(it, request) }
            .joinToString(separator = configuration.delimiter)
    }

    private fun generateCommandComponent(
        commandComponent: CommandComponent,
        request: Request
    ): List<String> {
        return when (commandComponent) {
            CommandComponent.Curl -> listOf("curl")
            CommandComponent.Url -> generateUrl(request.url)
            CommandComponent.Flags -> generateFlags()
            CommandComponent.Body -> generateBody(request.body)
            CommandComponent.Method -> generateMethod(request.method)
            CommandComponent.Header -> generateHeaders(request.headers, request.body)
        }
    }

    private fun generateBody(body: RequestBody?): List<String> {
        return if (body != null) {
            val bodyString = getBodyAsString(body)
            listOf(bodyString)
        } else {
            emptyList()
        }
    }

    private fun getBodyAsString(body: RequestBody) = try {
        val sink = Buffer()
        val mediaType = body.contentType()
        val charset: Charset = getCharset(mediaType)
        if (configuration.limit > 0) {
            val buffer = LimitedSink(sink, configuration.limit).buffer()
            body.writeTo(buffer)
            buffer.flush()
        } else {
            body.writeTo(sink)
        }
        FORMAT_BODY.format(sink.readString(charset))
    } catch (e: IOException) {
        "Error while reading body: $e"
    }

    private fun generateHeaders(headers: Headers, body: RequestBody?): List<String> {
        return headers
            .map { Header(name = it.first, value = it.second) }
            .mapNotNull { header -> modifyHeader(header) }
            .applyContentTypeHeader(body)
            .map { header -> FORMAT_HEADER.format(header.name, header.value) }
    }


    private fun modifyHeader(header: Header): Header? {
        val modifier = configuration.headerModifiers.find { it.matches(header) }
        return if (modifier != null) {
            modifier.modify(header)
        } else {
            header
        }
    }

    private fun generateMethod(method: String): List<String> {
        return listOf(FORMAT_METHOD.format(method.toUpperCase(Locale.getDefault())))
    }

    private fun generateFlags(): List<String> = configuration.flags.list()

    private fun generateUrl(url: HttpUrl): List<String> = listOf(FORMAT_URL.format(url.toString()))

    private fun getCharset(mediaType: MediaType?): Charset {
        val default = Charset.defaultCharset()
        return mediaType?.charset(default) ?: default
    }


    private fun List<Header>.applyContentTypeHeader(body: RequestBody?): List<Header> {
        val contentTypeHeader = find { it.name.equals(CONTENT_TYPE, ignoreCase = false) }
        val contentType = body?.contentType()?.toString()

        return if (contentTypeHeader == null && contentType != null) {
            this + listOf(Header(CONTENT_TYPE, contentType))
        } else {
            this
        }
    }

    private companion object {
        const val FORMAT_HEADER = "-H \"%1\$s:%2\$s\""
        const val FORMAT_METHOD = "-X %1\$s"
        const val FORMAT_BODY = "-d '%1\$s'"
        const val FORMAT_URL = "\"%1\$s\""
        const val CONTENT_TYPE = "Content-Type"
    }
}
