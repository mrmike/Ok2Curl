# Ok2Curl [![Build Status](https://travis-ci.org/mrmike/Ok2Curl.svg)](https://travis-ci.org/mrmike/Ok2Curl) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Ok2Curl-green.svg?style=flat)](https://android-arsenal.com/details/1/2653) [![Release](https://jitpack.io/v/mrmike/Ok2Curl.svg)](https://jitpack.io/#mrmike/Ok2Curl)

Convert OkHttp requests into curl logs.

## Usage
Add library to project dependencies. Library is hosted on jcenter.
```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'com.github.mrmike:ok2curl:0.4.5'
}
```

To start logging requests with Ok2Curl add interceptor to OkHttp client.
```java
OkHttpClient okHttp = new OkHttpClient.Builder()
    .addInterceptor(new CurlInterceptor(new Loggable() {
            @Override
            public void log(String message) {
                Log.v("Ok2Curl", message);
            }
        }))
    .build();
```

## Result
With Ok2Curl set up correctly every executed request will be transformed into curl log e.g.
```shell
adb logcat -s "Ok2Curl"
curl -X GET -H "Cache-Control:max-stale=2147483647, only-if-cached" https://api.github.com/repos/vmg/redcarpet/issues?state=closed
```

## Network interceptors
By default Ok2Curl uses application interceptors from OkHttp which is adequate for most cases. But sometimes you may want to use network interceptor e.g. to log Cookies set via [CookieHandler](http://docs.oracle.com/javase/6/docs/api/java/net/CookieHandler.html). In such a case add interceptor the same way as below:  

```
OkHttpClient okHttp = new OkHttpClient.Builder()
    .addNetworkInterceptor(new CurlInterceptor())
    .build();
```

To get know more about Interceptor in OkHttp take a look here: https://github.com/square/okhttp/wiki/Interceptors

## Header modifiers
Ok2Curl allows you to modify any header before creating curl command. All you have to do is create your own modifier that implements [HeaderModifier](https://github.com/mrmike/Ok2Curl/blob/master/ok2curl/src/main/java/com/moczul/ok2curl/modifier/HeaderModifier.java)
and add this modifier to CurlInterceptor. See [sample](https://github.com/mrmike/Ok2Curl/blob/master/sample/src/main/java/com/moczul/sample/RequestService.java) for reference.
```
final BasicAuthorizationHeaderModifier modifier = new BasicAuthorizationHeaderModifier(new Base64Decoder());
final List<HeaderModifier> modifiers = Collections.<HeaderModifier>singletonList(modifier);

final CurlInterceptor curlInterceptor = new CurlInterceptor(new AndroidLogger(), modifiers);
```

## Options
Ok2Curl supports basic Curl options. In order to use options use the following code:
```
final Options options = Options.builder()
                .insecure()
                .connectTimeout(120)
                .retry(5)
                .build();

final CurlInterceptor interceptor = new CurlInterceptor(logger, options);
```
Since now every Curl command will start with `curl --insecure --connect-timeout 120 --retry 5...`

### Supported options
* --insecure
* --max-time seconds
* --connect-timeout seconds
* --retry num
* --compressed
* --location

If would like to support any new options please feel free to open PR. Full list of curl options is
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
