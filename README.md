# Ok2Curl [![Build Status](https://travis-ci.org/mrmike/Ok2Curl.svg)](https://travis-ci.org/mrmike/Ok2Curl) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Ok2Curl-green.svg?style=flat)](https://android-arsenal.com/details/1/2653) [![Release](https://jitpack.io/v/mrmike/Ok2Curl.svg)](https://jitpack.io/#mrmike/Ok2Curl)

Convert OkHttp requests into curl logs.

## Usage
Add library to project dependencies.
```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    // snapshot version
    compile 'com.github.mrmike:Ok2Curl:master-SNAPSHOT'
    // or use specific version
    compile 'com.github.mrmike:Ok2Curl:0.3.0'
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

## License

    Copyright 2015 Michał Moczulski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
