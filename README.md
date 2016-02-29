# SeeNewsV2

## 新闻Android客户端 基于Material Design


## 效果图
<img src="/screenshots/refresh_endless_list.gif" alt="blog.csdn.net/never_cxb" title="screenshot" width="270" height="486" /> <br>

<img src="http://img.blog.csdn.net/20160117162640833" width="280" height="220" alt="http://blog.csdn.net/never_cxb" title="">
<img src="http://7xqo2w.com1.z0.glb.clouddn.com/recyclerview_refresh.png" width="280" height="300" alt="http://blog.csdn.net/never_cxb" title=""><br>

<img src="http://7xqo2w.com1.z0.glb.clouddn.com/recyclerview_slide_remove.png" width="270" height="486" alt="http://blog.csdn.net/never_cxb" title="">
<img src="http://7xqo2w.com1.z0.glb.clouddn.com/recyclerview_loadmore.png" width="270" height="486" alt="http://blog.csdn.net/never_cxb" title="">


## 数据来源API接口 ##

基于Jsoup爬虫实现，托管于新浪云，图片存于七牛

新闻 list 数据示例：

```
[
    {
        "id": 7937,
        "imageUrls": [],
        "title": "青岛鼎信通讯消防安全有限公司2016年招聘简章",
        "publishDate": "2016-01-20",
        "readTimes": 298,
        "summary": "技术问鼎 服务铸信 产品理念, 公司位于青岛市核心区域市南区青岛软件园, 企业理念。"
    },
	......
    {
        "id": 7948,
        "imageUrls": [
            "6a96b96982189e1fcb439b944cea0ce4",
            "48c119220b4d3df451dd03cb1f70b3ef",
            "27a911d81f7b13eeaf3ad49206c5f3d4"
        ],
        "title": "救助郭燕-电院2000级校友，参与互联网众筹，通过网络传递爱心！",
        "publishDate": "2016-02-02",
        "readTimes": 410,
        "summary": "西电小喇叭、西电青年、西电学工等等转发帮助郭燕同学, 我替姐姐感谢大家, ---西电导航编辑注） 感谢我的同学。"
    }
]
```


新闻详情Json数据示例：

```
{
    "source": "SeeNews",
    "body": "<div id=\"article_content\"><p>\n\t校内各有线电视用户：\n</p>\n<p>\n\t为确保全校教职工员度过一个愉快的假期，保障南、北校区有线电视信号的正常传送，按照学校对校内有线电视维护工作安排，现将寒假期间有线电视信号维护工作通知如下：..wbtreeid=1227&amp;wbnewsid=13892</a></div>",
    "id": 7946,
    "imageUrls": [],
    "title": "关于寒假期间南、北校区有线电视信号维护工作的通知",
    "publishDate": "2016-01-26",
    "readTimes": 225
}
```


## 功能开发记录
### V 0.7
- 省流量模式，移动网络点击加载图片
- 把CheckBoxPreference改为SwitchCompat
- 修复官方的PreferenceScreen设置值无法传递的问题
- 完成集成极光推送demo效果


### V 0.7
- 历史记录list显示（按照阅读时间排序）
- 修复使用ActiveAndroid，Sqlite某个字段id unique的问题
- 当历史记录中某条新闻再次阅读时候，要把阅读时间更新
- 自己实现长按删除，从Sqlite中删除
- 利用开源库实现，侧滑删除Recyclerview某一项

### V 0.6
- 修复轮播图片小圆点重复问题<br> 
  当 RecyclerView 的 Item 超出屏幕后，会重新执行onBindViewHolder
- 完成新闻详情页面xml和Activity
- CollapsingToolbarLayout 实现图片和toolbar上拉隐藏
- 解决webview内容过少，上滑失效的问题

### V 0.5
- 完善RecyclerView列表各个新闻的字体颜色
- 利用自然语言工具包 完成新闻自动摘要
- 多多Item布局实现，有多幅图片的新闻、无图片的分开展示
- 基于七牛，获取随机优美图片给无图片新闻使用

### V 0.4
- 完善TabLayout，展示6个栏目
- Endless RecyclerView 实现，上拉加载更多
- Material Design 的圆形 Progress Bar

### V 0.3
- 增加启动图标
- 修改 ViewPage 作为 RecyclerView 的第一项
- TabLayout + ViewPager 页卡滑动效果，展示本科、研究生等栏目的新闻
- 修复部分设备 optionsMenu遮挡ToolBar的问题

### V 0.2
- 上传图片到七牛
- 新浪云实现 API 接口，数据缓存在 Mysql
- 夜间模式 （切换不同 Theme）
- 异步获取新闻 Json 数据 

### V 0.1
- DrawerLayout 实现抽屉菜单
- Navigation 实现抽屉左边的导航
- ToolBar 实现沉浸式布局
- ViewPager 展示轮播图片（首尾循环，自动轮播）
- RecyclerView + CardView 展示新闻列表


## 依赖的开源类库、工具

 - View注入框架，绑定控件和 OnClick 方法[butterknife](https://github.com/JakeWharton/butterknife)
 - facebook 的图片库，支持圆形、方形，各种缩放等[fresco](https://github.com/facebook/fresco)
 - 启动图片在线生成工具 [Launcher Icon Generator](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html)
 - 圆形 ProgressBar [materialish-progress](https://github.com/pnikosis/materialish-progress)
 - 自动摘要 [HanLP](https://github.com/hankcs/HanLP)
 - Sqlite 轻量级ORM [ActiveAndroid](https://github.com/pardom/ActiveAndroid)
 - 访问http请求 [okhttp](https://github.com/square/okhttp)
 - Pretty日志输出 [logger](https://github.com/orhanobut/logger)

## 借鉴的 APP

 - [BuildingBlocks](https://github.com/tangqi92/BuildingBlocks)
 
