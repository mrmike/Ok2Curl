package com.moczul.ok2curl

import java.util.Locale

class Flags private constructor(options: Collection<String>) {

    private val options: List<String>

    init {
        this.options = options.toList()
    }

    fun list(): List<String> {
        return options
    }

    class Builder {
        private val options: MutableSet<String> = mutableSetOf()
        fun insecure(): Builder {
            options.add("--insecure")
            return this
        }

        fun maxTime(seconds: Int): Builder {
            options.add(String.format(Locale.getDefault(), "--max-time %d", seconds))
            return this
        }

        fun connectTimeout(seconds: Int): Builder {
            options.add(String.format(Locale.getDefault(), "--connect-timeout %d", seconds))
            return this
        }

        fun retry(num: Int): Builder {
            options.add(String.format(Locale.getDefault(), "--retry %d", num))
            return this
        }

        fun compressed(): Builder {
            options.add("--compressed")
            return this
        }

        fun location(): Builder {
            options.add("--location")
            return this
        }

        fun build() = Flags(options)
    }

    companion object {
        @JvmField
        val EMPTY = Flags(emptyList())

        @JvmStatic
        fun builder() = Builder()
    }
}