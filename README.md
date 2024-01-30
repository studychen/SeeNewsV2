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
        "id":7937,
        "imageUrls":[

        ],
        "title":"Qingdao Dingxin Communications Fire Safety Co., Ltd. 2016 Recruitment Brochure",
        "publishDate":"2016-01-20",
        "readTimes":298,
        "summary":"Technology strives for excellence, service creates trust, product philosophy, the company is located in Qingdao Software Park, Shinan District, the core area of Qingdao City, corporate philosophy."
    },
    {
        "id":7948,
        "imageUrls":[
            "6a96b96982189e1fcb439b944cea0ce4",
            "48c119220b4d3df451dd03cb1f70b3ef",
            "27a911d81f7b13eeaf3ad49206c5f3d4"
        ],
        "title":"Save Guo Yan, a 2000-level alumnus of the School of Electrical and Electronics Engineering, participate in Internet crowdfunding, and spread love through the Internet!",
        "publishDate":"2016-02-02",
        "readTimes":410,
        "summary":"Xidian Small Speaker, Xidian Youth, Xidian Students and Workers, etc. forwarded to help classmate Guo Yan. I would like to thank everyone on behalf of my sister. ---Xidian Navigation Editor's Note) Thank you to my classmates."
    }
]
```


News details Json data example:

```
{
     "source": "SeeNews",
     "body": "<div id=\"article_content\"><p>\n\tCable TV users in the school:\n</p>\n<p>\n\tIn order to ensure the integrity of the school's faculty and staff Have a pleasant holiday and ensure the normal transmission of cable TV signals in the south and north campuses. According to the school’s arrangements for on-campus cable TV maintenance work, the cable TV signal maintenance work during the winter vacation is now notified as follows: ..wbtreeid=1227&amp;wbnewsid=13892< /a></div>",
     "id": 7946,
     "imageUrls": [],
     "title": "Notice on cable TV signal maintenance work in the South and North Campuses during the winter vacation",
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


## Dependent open source libraries and tools

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
 
