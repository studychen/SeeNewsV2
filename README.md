# SeeNewsV2

## News Android client based on Material Design


## renderings
<img src="/screenshots/refresh_endless_list.gif" alt="blog.csdn.net/never_cxb" title="screenshot" width="270" height="486" /> <br>

<img src="http://img.blog.csdn.net/20160117162640833" width="280" height="220" alt="http://blog.csdn.net/never_cxb" title="">


## Data source API interface ##

Implemented based on Jsoup crawler, hosted on Sina Cloud, and images stored in Qiniu Cloud.

News list data example:

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


News details Json data example:

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


## Function development record
### V 0.7
- Data saving mode, click on mobile network to load pictures
- Change CheckBoxPreference to SwitchCompat
- Fixed the problem that the official PreferenceScreen setting value cannot be passed
- Completed integrated Aurora push demo effect


### V 0.7
- History list display (sorted by reading time)
- Fix the problem of unique field id in Sqlite using ActiveAndroid
- When a certain news in the history is read again, the reading time must be updated
- Implement long press deletion yourself and delete from Sqlite
- Implemented using open source libraries to slide and delete an item in Recyclerview

### V 0.6
- Fixed the problem of repeated small dots in carousel images<br>
   When the RecyclerView's Item goes beyond the screen, onBindViewHolder will be re-executed.
- Complete the news details page xml and Activity
- CollapsingToolbarLayout implements pull-up and hiding of pictures and toolbars
- Solve the problem of too little webview content and failure to slide up

### V 0.5
- Improve the font color of each news in the RecyclerView list
- Use natural language toolkit to complete automatic summary of news
- Duoduo Item layout is implemented, and news with multiple pictures and news without pictures are displayed separately.
- Based on Qiniu, obtain random beautiful pictures for use in picture-free news

### V 0.4
- Improve TabLayout and display 6 columns
- Endless RecyclerView implementation, pull up to load more
- Material Design’s round Progress Bar

### V 0.3
- Add startup icon
- Modify ViewPage as the first item of RecyclerView
- TabLayout + ViewPager page card sliding effect to display news from undergraduate, graduate and other columns
- Fixed the issue where optionsMenu blocks ToolBar on some devices

### V 0.2
- Upload pictures to Qiniu
- Sina Cloud implements API interface and data is cached in Mysql
- Night mode (switch to different themes)
- Asynchronously obtain news Json data

### V 0.1
- DrawerLayout implements drawer menu
- Navigation implements navigation on the left side of the drawer
- ToolBar implements immersive layout
- ViewPager displays carousel images (first and last loop, automatic carousel)
- RecyclerView + CardView displays news list


## 依赖的开源类库、工具

- View injection framework, bound controls and OnClick method [butterknife](https://github.com/JakeWharton/butterknife)
- Facebook's picture library, supporting circles, squares, various zooms, etc. [fresco](https://github.com/facebook/fresco)
- Launch the online image generation tool [Launcher Icon Generator](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html)
- Circular ProgressBar [materialish-progress](https://github.com/pnikosis/materialish-progress)
- Automatic summary [HanLP](https://github.com/hankcs/HanLP)
- Sqlite lightweight ORM [ActiveAndroid](https://github.com/pardom/ActiveAndroid)
- Access http request [okhttp](https://github.com/square/okhttp)
- Pretty log output [logger](https://github.com/orhanobut/logger)

## APPs to learn from

 - [BuildingBlocks](https://github.com/tangqi92/BuildingBlocks)
 
