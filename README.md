# AudioStream
Record and stream audio from Android device's mic to receiver.

一个简单的声音实时传输应用（MinSdkVersion：21，TargetSdkVersion：24）
（需要搭配[ PC 接收端](https://github.com/anzhehong/Happy-KTV)使用）。
## 模块功能划分

+ app

Activity、Fragment、XML 布局文件以及 UI 相关的交互逻辑代码。
+ database

Javabean 以及对应的 Helper 类等，处理数据库的增删改查。
+ network

  - 处理网络以及声音实时传输相关的逻辑；
  - 使用两个 Socket 连接[ PC 接收端](https://github.com/anzhehong/Happy-KTV)；
  [`SocketHelper`](network/src/main/java/us/ktv/network/SocketHelper.java) 负责向 PC 发送指令，如播放、暂停歌曲；
  [`RecordSocketHelper`](network/src/main/java/us/ktv/network/RecordSocketHelper.java) 负责调用 `AudioRecord` 通过手机麦克风录制声音，缓存后传输到 PC 。


## 相关技术

+ [Data binding](app/src/main/res/layout)（item XML 布局文件）
+ [自定义 View](app/src/main/java/us/ktv/android/view)
+ AsyncTask
+ 定义 [BaseListFragment](app/src/main/java/us/ktv/android/fragment/BaseListFragment.java)
+ [Socket](network/src/main/java/us/ktv/network)

## 第三方依赖
  
+ Gson
+ Fresco
+ Drawee
+ Butterknife
+ Fancybuttons
+ SwipeToLoadLayout
+ Materialloadingprogressbar

## LICENSE

+ MIT
