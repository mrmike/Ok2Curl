# Ok2Curl [![Build Status](https://travis-ci.org/mrmike/Ok2Curl.svg)](https://travis-ci.org/mrmike/Ok2Curl)
Convert OkHttp requests into curl logs.


## Usage
Add library to project dependencies.
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.mrmike:Ok2Curl:v0.0.1'
}
```

Then call Ok2Curl set method with OkHttpClient as an argument.
```
OkHttpClient client = new OkHttpClient();
Ok2Curl.set(client);
```

By default Ok2Curl generate logs with `Ok2Curl` tag and log level set to`Log.DEBUG`. You can easily change this by calling
```
Ok2Curl.set(client, "MyTag", Log.DEBUG);
```

## Result
With Ok2Curl set up correctly every executed request will be transformed into curl log e.g.
```
adb logcat -s "Ok2Curl"
--------- beginning of main
--------- beginning of system
09-26 21:13:54.380  2225  2725 D Ok2Curl : curl -X GET -H "Cache-Control:max-stale=2147483647, only-if-cached" https://api.github.com/repos/vmg/redcarpet/issues?state=closed
```
