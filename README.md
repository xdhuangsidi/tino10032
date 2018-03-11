本app是一款集社交发帖功能为一体的校园app
========
使用第三方sdk的功能：
-----
* 社交功能：集成腾讯云通信im sdk  详情https://cloud.tencent.com/product/im#sdk  10w日活量以下免费
* 对象储存功能：图片使用腾讯云对象储存，原因也是因为腾讯云的对象储存免费额度，小规模的应用基本免费.
* 短信验证功能：使用mob免费短信验证，通过审核后免费使用短信验证登录

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

* 上传图片功能，上传图片使用http协议，把图片转成byte[]加入到application/octet-stream中。这是主要的，上传时同时需要鉴权toekn，由该应用的后端服务器生成，具体查看腾讯云储存文档，http请求体的构建代码为https://github.com/xdhuangsidi/tino10032/blob/r4/app/src/main/java/com/tino/core/net/UploadImp.java  

* 



