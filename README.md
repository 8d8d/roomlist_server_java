#腾讯云音视频PC与小程序互通解决方案服务端
当您测试体验完完 “WebRTC 互通”或者“web exe 互通”功能之后，想开发属于自己的“ WebRTC 互通”或者“web exe 互通”功能。这时需要部署属于自己账号体系的服务器。本文主要说明如何部署 服务器。

## 1 开通服务

### 开通实时音视频服务（WebRTC 互通）

#### step1:  申请开通实时音视频服务
进入 [实时音视频管理控制台](https://console.qcloud.com/rav)，如果服务还没有开通，则会有如下提示:
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/rav_open.png)
点击申请开通，之后会进入腾讯云人工审核阶段，审核通过后即可开通。

####  step2:  创建实时音视频应用
实时音视频开通后，进入[【实时音视频管理控制台】](https://console.qcloud.com/rav) 创建实时音视频应用 ：
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/rav_new.png)
点击【**确定**】按钮即可。

####  step3: 获取实时音视频配置信息
从实时音视频控制台获取`sdkAppID、accountType、privateKey`，后面配置服务器会用到：
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/rav_config.png)

### 开通云通信（web exe 互通）

进入[云通讯管理控制台](https://console.cloud.tencent.com/avc)，如果还没有服务，直接点击**直接开通云通讯**按钮即可。新认证的腾讯云账号，云通讯的应用列表是空的，如下图：
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/im_open.png)

点击**创建应用接入**按钮创建一个新的应用接入，即您要接入腾讯云IM通讯服务的App的名字，我们的测试应用名称叫做“RTMPRoom”，如下图所示：
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/im_new.png)

点击确定按钮，之后就可以在应用列表中看到刚刚添加的项目了，如下图所示：
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/im_list.png)

#### 配置独立模式
上图的列表中，右侧有一个**应用配置**按钮，点击这里进入下一步的配置工作，如下图所示。
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/im_config.png)

#### 获取云通讯服务配置信息
从云通信控制台获取`sdkAppID`、`accountType`、`privateKey`，后面配置服务器会用到：
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/im_config_info.png)

从验证方式中下载公私钥，解压出来将private_key用文本编辑器打开，如：

```bash
-----BEGIN PRIVATE KEY-----
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-----END PRIVATE KEY-----
```

将其转换成字符串形式如下所示，后面在server配置文件中使用，<font color='red'>请注意每行后面要加入\r\n</font>：

```bash
"-----BEGIN PRIVATE KEY-----\r\n"+
"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
"-----END PRIVATE KEY-----\r\n"
```


## 2 修改配置信息
后台使用 spring 框架搭建，开发环境是 IntelliJ IDEA，**java 环境要求使用<font color='red'>1.8_52 或以上</font>**。用 IntelliJ IDEA 导入工程源码，修改`\src\main\java\com\tencent\qcloud\roomservice\roomlist\common\Config.java` 中`sdkAppID、accountType、privateKey`等配置项。

```java
public class Config {

    /**
     * webrtc互通需要开通 实时音视频 服务 ，web exe互通 需要开通云通信服务
     * 有介绍appid 和 accType的获取方法。以及私钥文件的下载方法。
     */
    public class iLive {
        public final static long sdkAppID = 0;

        public final static String accountType = "0";

        /**
         * 派发userSig 和 privateMapKey 采用非对称加密算法RSA，用私钥生成签名。privateKey就是用于生成签名的私钥，私钥文件可以在互动直播控制台获取
         * 配置privateKey
         * 将private_key文件的内容按下面的方式填写到 privateKey字段。
         */
        public final static String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "xxxx\n" +
                "xxxx\n" +
                "xxxx\n" +
                "-----END PRIVATE KEY-----";
    }


    /**
     * 房间相关参数
     */
    public class Room {
        // 房间容量上限
        public final static int maxMembers = 4;

        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;
    }

}
```

## 3 服务器部署

以 CentOS 系统为例，描述部署过程。建议环境： CentOS + nginx + Apache Tomcat + java 。小程序和 IOS 都要求服务器支持 HTTPS 请求。

### 3.1 准备发布包
修改好 Config.java 中的配置，然后选择 Build -> Build Artifacts 开始打包，打包完成后到输出路径拿到 roomlist.war 包。

![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/build_war.png)

### 3.2 war包部署到服务器

#### 1) 新建 CVM 主机
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/new_cvm.png)

#### 2) 从服务市场选取镜像。
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/cvm_image.png)

#### 3) 配置硬盘、网络、云主机访问密码，并且妥善保管好密码，然后设置安全组。
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/config_cvm.png)

#### 4) 查看/切换 JDK 版本。
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/change_jdk.png)

#### 5) 使用 FileZilla 连接云服务器
因为需要上传文件到云服务器，建议使用 FileZilla 或者 FlashFXP 等可视化界面工具。
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/upload_war.png)

#### 6）将打包好的 roomlist.war 包上传 tomcat 的 webapps 目录下
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/upload_war_2.png)

#### 7) 通过 tomcat/bin 目录下的 startup.sh 脚本重新启动 tomcat。 
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/restart_tomcat.png)


### 3.3 nginx 配置
如果您已经有**域名**以及域名对应的**SSL证书**存放在`/data/release/nginx/`目录下，请将下面配置内容中的
- [1] 替换成您自己的域名
- [2-1] 替换成SSL证书的crt文件名
- [2-2] 替换成SSL证书的key文件名

```
upstream app_weapp {
    server localhost:5757;
    keepalive 8;
}

#http请求转为 https请求
server {
    listen      80;
    server_name [1]; 

    rewrite ^(.*)$ https://$server_name$1 permanent;
}

#https请求
server {
    listen      443;
    server_name [1];

    ssl on;

    ssl_certificate           /data/release/nginx/[2-1];
    ssl_certificate_key       /data/release/nginx/[2-2];
    ssl_session_timeout       5m;
    ssl_protocols             TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers               ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA;
    ssl_session_cache         shared:SSL:50m;
    ssl_prefer_server_ciphers on;

    # tomcat默认端口是8080，nginx 将请求转发给tomcat处理
    location / {
        proxy_pass   http://127.0.0.1:8080;
        proxy_redirect  off;
        proxy_set_header  X-Real-IP $remote_addr;
        proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

### 3.4 运行服务
输入命令，启动Nginx服务。
```
nginx -s reload
```
使用 Postman 通过 POST 方式访问接口，如果在返回的 json 数据带有**请求成功**字样，说明部署成功。
以获取登录信息为例，请求地址是 https://您自己的域名/roomlist/weapp/webrtc_room/get_login_info
![](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/raw/master/image/postman.png)

## 4 小程序和web端部署
### 4.1 小程序部署

下载 [小程序](https://github.com/TencentVideoCloudMLVBDev/RTCRoomDemo) 源码，将wxlite/config.js文件中的`webrtcServerUrl`修改成：
```
https://您自己的域名/roomlist/weapp/webrtc_room
```

### 4.2 **webrtc** web端部署

下载 [web端](https://github.com/TencentVideoCloudMLVBDev/webrtc_pc) 源码，将component/WebRTCRoom.js文件中的`serverDomain`修改成：
```
https://您自己的域名/roomlist/weapp/webrtc_room
```

## 5 开发者资源
* [项目结构](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/blob/master/doc/codeStructure.md) - 后台源码结构
* [协议文档](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/blob/master/doc/protocol.md) - 后台协议文档
* [常见错误码](https://github.com/TencentVideoCloudMLVBDev/roomlist_server_java/blob/master/doc/errorCode.md) - 错误码文档