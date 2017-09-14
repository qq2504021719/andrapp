# Android编程权威指南 学习

## GeoQuiz应用 显示5个问题,回答问题,问题切换,查看答案
## 所用技术:Button TextView drawable资源 点击事件
### GeoQuiz未解决问题
#### 1:点击查看答案按钮,进入查看答案页面,点击显示答案,显示出来的了答案,但旋转屏幕后，启动的Activity是第一个页面了。不点击显示答案,旋转屏幕启动的Activity就没有问题。
#### 2:显示答案添加了动画，但没有效果。102页的代码

## CriminalIntent应用 详细记录办公室陋习,随手将脏盘子丢在休息室水槽,以及打印完自己的文件便径直走开,不管打印机有没有缺纸。能记录陋习的标题、日期以及照片，也支持在联系人中查找当事人,通过E-mail、Twitter、Facebook或其他应用提出抗议。把陋习处理完后。
### 所用技术:(EditText) (UI fragment) (fragment) (RecyclerView、Adapter、ViewHolder) (AlertDialog) (ViewPage) (同一个Activity托管的fragment传递数据,子fragment返回数据给父Fragment) (工具栏/菜单栏,层级式导航) (SQLite使用增删改查)(隐式Intent调用其他应用，照片保存,照片生成缩略图)(平板同一屏幕显示列表页和详情页)

## 练习App LianXi (主要练习"CriminalIntent应用"所学的技术)

## BeatBox  声音播放应用 
### 所用技术 (Assets微型文件系统) (SoundPool播放音频) (样式继承) (shape drawable 按钮样式背景设置) (state list drawable 默认状态按钮样式,点击状态按钮样式) (layer list drawable 对应样式加边框) (9-patch 图像)

## NerdLauncher 用NerdLauncher应用作为设备主屏幕,能列出设备上的其他应用。点选任意列表项会启动相应应用
### 所用技术 (RecyclerView、Adapter、ViewHolder) (通过定义 AndroidManifest-category的HOME、DEFAULT NerdLauncher应用的activity会成为可选的主界面)

## PhotoGallery 网络图片请求,显示
### 所用技术 (创建新的线程请求网络图片) (AsyncTask 创建新的线程) (onPostExecute 后台线程请求完成刷新数据) (消息队列 RecyclerView只下载需要显示的图片)(RecyclerView 九宫格布局)(IntentService 后台服务)