# Ok2Curl [![Commit check](https://github.com/mrmike/Ok2Curl/actions/workflows/push.yml/badge.svg)](https://github.com/mrmike/Ok2Curl/actions/workflows/push.yml) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Ok2Curl-green.svg?style=flat)](https://android-arsenal.com/details/1/2653) [![Release](https://jitpack.io/v/mrmike/Ok2Curl.svg)](https://jitpack.io/#mrmike/Ok2Curl)

Simply way to transform OkHttp requests into curl logs.

## Supported OkHttp versions
Currently Ok2Curl requires OkHttp in version 4.x.

## Usage
Add library to project dependencies. Library is hosted on Maven Central.
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.github.mrmike:ok2curl:0.8.0'
}
```

To start logging requests with Ok2Curl add interceptor to OkHttp client.
```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(CurlInterceptor(object : Logger {
        override fun log(message: String) {
            Log.v("Ok2Curl", message)
        }
    }))
    .build()
```

## Result
With Ok2Curl set up correctly every executed request will be transformed into curl log e.g.
```shell
adb logcat -s "Ok2Curl"
curl -X GET -H "Cache-Control:max-stale=2147483647, only-if-cached" https://api.github.com/repos/vmg/redcarpet/issues?state=closed
```

## Network interceptors
By default Ok2Curl uses application interceptors from OkHttp which is adequate for most cases. But sometimes you may want to use network interceptor e.g. to log Cookies set via [CookieHandler](http://docs.oracle.com/javase/6/docs/api/java/net/CookieHandler.html). In such a case add interceptor the same way as below:  

```kotlin
val client = OkHttpClient.Builder()
    .addNetworkInterceptor(CurlInterceptor(logger))
    .build()
```

To get know more about Interceptor in OkHttp take a look here: https://github.com/square/okhttp/wiki/Interceptors

## Configuration

`CurlInterceptor` accepts an optional configuration object. With `Configuration` you can specify various options like:
* header modifiers - custom logic for modifying header values
* components - list of required command components
* flags - optional curl flags
* limit - bytes limit for body
* delimiter for command components

```kotlin
class Configuration(
    val headerModifiers: List<HeaderModifier> = emptyList(),
    val components: List<CommandComponent> = CommandComponent.DEFAULT,
    val flags: Flags = Flags.EMPTY,
    val limit: Long = 1024L * 1024L,
    val delimiter: String = " "
)
```

## Header modifiers
Ok2Curl allows you to modify any header before creating curl command. All you have to do is create your own modifier that implements [HeaderModifier](https://github.com/mrmike/Ok2Curl/blob/master/ok2curl/src/main/java/com/moczul/ok2curl/modifier/HeaderModifier.kt)
and add this modifier to CurlInterceptor. See [sample](https://github.com/mrmike/Ok2Curl/blob/master/sample/src/main/java/com/moczul/sample/RequestService.kt) for reference.
```
val modifier = BasicAuthorizationHeaderModifier(Base64Decoder())
val configuration = Configuration(headerModifiers = listOf(modifier))
val curlInterceptor = CurlInterceptor(AndroidLogger(), configuration)
```

## Command Components
With Ok2Curl configuration you can specify a list of components for curl command. For instance,
you can skip header, body, and flag components.
```kotlin
val components = listOf(Curl, Method, Url)
val configuration = Configuration(components = components)
val curlInterceptor = CurlInterceptor(AndroidLogger(), configuration)
```

As a result, CurlInterceptor will receive given simplified command
```shell
// Headers, body and flags are skipped
curl -X GET https://api.github.com/repos/vmg/redcarpet/issues?state=closed
```

## Flags
Ok2Curl supports basic Curl options. To use options use the following code:
```kotlin
val flags = Flags.builder()
            .insecure()
            .connectTimeout(seconds = 120)
            .retry(5)
            .build()
val configuration = Configuration(flags = flags)
val curlInterceptor = CurlInterceptor(AndroidLogger(), configuration)
```
Since now every Curl command will start with `curl --insecure --connect-timeout 120 --retry 5...`

### Supported options
* --insecure
* --max-time seconds
* --connect-timeout seconds
* --retry num
* --compressed
* --location

If would like to support any new options please feel free to open PR. A full list of curl options is
available [here](https://curl.haxx.se/docs/manpage.html).


## License

    Copyright 2018 Michał Moczulski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
