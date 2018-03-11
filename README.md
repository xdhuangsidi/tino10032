本app是一款集社交发帖功能为一体的校园app
========
使用第三方sdk的功能：
-----
* 社交功能：集成腾讯云通信im sdk 从而实现发图片 语音 文字功能 详情https://cloud.tencent.com/product/im#sdk  10w日活量以下免费
* 对象储存功能：图片使用腾讯云对象储存，原因也是因为腾讯云的对象储存免费额度，小规模的应用基本免费.
* 短信验证功能：使用mob免费短信验证，通过审核后免费使用短信验证登录
* 分享功能：使用mob封装了分享功能，由于需要在腾讯平台中申请对应的appid，这里实现了qq空间的分享

应用截图
-----  
<div class="row"  float: left width: 12.5% >
<img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/login.jpeg" width = "180" height = "300"  align=center />
<img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/main1.jpeg" width = "180" height = "300"  align=center />
<img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/main2.jpeg" width = "180" height = "300"  align=center />
<img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/chat1.jpeg" width = "180" height = "300"  align=center />
  <img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/chat2.jpeg" width = "180" height = "300"  align=center />
<img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/upload.jpeg" width = "180" height = "300"  align=center />
<img src="https://github.com/xdhuangsidi/tino10032/blob/r4/app/detail.jpeg" width = "180" height = "300"  align=center />
</div>


代码详情
------

* 上传图片功能，上传图片使用http协议，把图片转成byte[]加入到application/octet-stream中。这是主要的，上传时同时需要鉴权signature，由该应用的后端服务器生成，具体查看腾讯云储存文档，http请求体的构建代码为https://github.com/xdhuangsidi/tino10032/blob/r4/app/src/main/java/com/tino/core/net/UploadImp.java  ，需要到signature相当于动态口令，具体生成方式参考https://github.com/xdhuangsidi/signature_generate


* 图片轮播代码位于https://github.com/xdhuangsidi/tino10032/blob/r4/app/src/main/java/com/tino/ui/home/HomePageFragment.java  82-92行，采用了https://github.com/Jude95/RollViewPager 的控件，本代码中封装成aar文件 需在gardle中加入 compile(name: 'rollviewpager', ext: 'aar')引入
 
* 获取短信并验证登录的代码位于https://github.com/xdhuangsidi/tino10032/blob/r4/app/src/main/java/com/tino/ui/login/AuthLoginFragment.java  65-175行，appid及secretkey 放置在AndroidManifest中 

* https://github.com/xdhuangsidi/tino10032/tree/r4/app/src/main/java/com/tino/core/model 中存放了model模型 im模块中的图像 语音 文字消息对象  会话对象 用户资料对象   blog模块的博客 评论对象都放在里面

*  Glide方面 为了能加载url以https开头的图片，在androidmanifest.xml文件18-20行添加com.tino.core.Glide.UnsafeOkHttpGlideModule信任证书

* im的功能实现需要在https://github.com/xdhuangsidi/tino10032/blob/r4/app/src/main/java/com/tino/core/TinoApplication.java  中初始化，比较重要的是https://github.com/xdhuangsidi/tino10032/tree/r4/app/src/main/java/com/tino/im/event 中的采用观察者模式监听是否收到消息，然后view包中包含表情包 语音输入 图片放缩显示等控件  present包含了处理消息的逻辑





