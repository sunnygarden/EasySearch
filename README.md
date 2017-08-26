之前公司入职前的一个大作业，今天整理电脑发现了，拿来分享一下吧。感兴趣都可以下载源码阅读一下。本软件的数据来源是豆瓣图书API，地址如下：https://developers.douban.com/wiki/?title=book_v2
在这里感谢豆瓣开放的接口，实现过程中也查阅了不少网上的资料，感谢这些贡献者们。
</br>一、软件运行效果：

</br>![首页展示功能](http://upload-images.jianshu.io/upload_images/3050329-a0f2163f538ac367.gif?imageMogr2/auto-orient/strip)

</br>![图书搜索功能](http://upload-images.jianshu.io/upload_images/3050329-241d8a7320b6e5fb.gif?imageMogr2/auto-orient/strip)

</br>![二维码扫描功能](http://upload-images.jianshu.io/upload_images/3050329-16733cd78c0d8190.gif?imageMogr2/auto-orient/strip)


</br>![主题色切换功能](http://upload-images.jianshu.io/upload_images/3050329-eaf1d9b895887e94.gif?imageMogr2/auto-orient/strip)

</br>本应用基于豆瓣API，由于访问次数有限制，若一分钟单个IP请求超过40次，IP会被暂时封禁，请求的数据返回为null，会显示如下界面，一般等个十几分钟半小时就又好了。

</br>![请求太过频繁IP被封禁导致暂时取不到数据](http://upload-images.jianshu.io/upload_images/3050329-bd522b32b5028849.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

￼￼</br>二、软件简单介绍：
</br>1、功能：
</br>条形码扫描查询、图书名查询，书籍详情功能、接入一个图片框架，网络框架，apk进行安全加固，代码进行混淆。运行稳定，崩溃率极低。
</br>实现的其他附加功能有：
</br>实现了书籍分类罗列功能
</br>实现了书籍详情页背景色跟随书籍图片颜色变化功能
</br>实现了下拉刷新功能
</br>实现了主题换肤功能
</br>实现了回到顶部功能
</br>实现了再按一次退出功能
</br>实现了从本地图库扫描图片条形码搜索图书功能
</br>实现了闪屏动画功能
</br>实现了中英文语言适配
</br>2、主要用到的第三方框架
</br>图片加载框架—Glide
</br>网络访问框架—Retrofit
</br>二维码扫描框架—BGAQRCode
</br>注解框架—ButterKnife
</br>3、界面风格
</br>主要采用Google推出的全新的Material Design风格的控件，如AppBarLayout、ToolBar、CoordinatorLayout等，其中：
</br>CardView用于包裹图书的显示来增加圆角和阴影功能
</br>SwipeRefreshLayout实现下拉刷新功能
</br>DrawerLayout实现抽屉效果
</br>NavigationView实现抽屉布局
</br>RecyclerView用于显示大量图书数据
</br>CollapsingToolbarLayout实现可伸缩折叠的toolbar,主要应用在书籍详情页界面
</br>TabLayout加上viewPager实现可横向切换的选项卡
</br>FloatingActionButton实现回到顶部按钮
</br>4、代码架构
</br>代码编写采用MVP架构，清晰易懂，降低耦合。
</br>5、代码保护
</br>代码进行了混淆，apk进行了加固并签名，能有效防止防止恶意反编译源码，窃取劳动成果。
</br>三、项目APK地址：
</br>https://github.com/sunnygarden/EasySearch/blob/master/EasySearch.apk
</br>四、项目源码地址：
</br>https://github.com/sunnygarden/EasySearch
