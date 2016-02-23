# SeeNewsV2

## 新闻Android客户端 基于Material Design

## 功能开发记录

### V 0.4
- 完善TabLayout，展示6个栏目
- endless RecyclerView 实现，上拉加载更多
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


## 借鉴的 APP

 - [BuildingBlocks](https://github.com/tangqi92/BuildingBlocks)
 
