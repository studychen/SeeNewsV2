# SeeNewsV2

## 新闻Android客户端 基于Material Design


## 效果图
<img src="http://img.blog.csdn.net/20160117162640833" width="280" height="220" alt="http://blog.csdn.net/never_cxb" title="">
<img src="http://7xqo2w.com1.z0.glb.clouddn.com/history_slide_delete.png" width="400" height="220" alt="http://blog.csdn.net/never_cxb" title="">
<img src="http://7xqo2w.com1.z0.glb.clouddn.com/recyclerview_refresh.png" width="280" height="220" alt="http://blog.csdn.net/never_cxb" title="">


## 功能开发记录
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
 
