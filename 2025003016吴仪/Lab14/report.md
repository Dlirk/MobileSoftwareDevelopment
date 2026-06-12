# Lab14：BusSchedule公交时刻表Room数据库应用
姓名：吴仪
学号：2025003016

---

## 一、应用功能与展示信息
本实验基于Jetpack Compose+Room+MVVM架构开发公交时刻表本地数据库项目，核心功能如下：
1. 采用**双栏列表**展示13条公交班次，左侧展示站点名称、右侧展示格式化到站时间。
2. 支持**点击站点条目跳转详情页**，详情页仅展示该站点全部发车班次，顶部提供返回导航按钮。
3. 本地预置数据库初始化，内置全套站点与时间戳数据，每条班次到站时间数值独立不重复。
4. 页面采用标准布局分割线、标题分层排版，时间统一转换为`h:mm a`（上午/下午）格式展示。
5. 借助ViewModel实现界面与数据库解耦，使用Flow数据流监听数据变化，页面自动刷新。

## 二、实验背景与核心目标
本实验围绕Room本地持久化存储与MVVM分层思想，借助assets预置数据库完成本地数据加载到Compose页面渲染全流程，目标如下：
1. 掌握Room三层结构：Entity实体、Dao访问接口、Database数据库单例。
2. 学会assets预置数据库打包加载方式，无需运行时插入初始数据。
3. 区分数据库实体与UI层数据流转，利用ViewModel作为中间桥梁隔离数据库与界面。
4. 使用Flow实现响应式数据流，数据更新自动驱动Compose页面刷新。
5. 在Compose中通过导航组件完成首页列表、详情页面路由跳转。
6. 掌握时间戳日期格式化、LazyColumn高性能列表、Material3页面布局UI开发。

## 三、核心实现说明
### 1. Room数据库整体结构说明
1. **BusSchedule.kt（Entity实体）**
使用`@Entity(tableName = "Schedule")`映射数据库数据表，`@PrimaryKey`标记主键id，`@ColumnInfo`映射数据库列名，存储站点名称、int类型时间戳。
2. **BusScheduleDao.kt（数据访问层）**
定义两个SQL查询注解方法，全部返回`Flow<List<BusSchedule>>`实现实时监听：
- 查询全部时刻表并按到站时间升序排序；
- 根据站点名称筛选对应所有班次。
3. **BusScheduleDatabase.kt（数据库单例）**
标准同步锁单例模式，绑定实体类、设置数据库版本1；配置`.createFromAsset("database/bus_schedule.db")`读取打包内置预置数据库文件，全局唯一数据库实例防止多连接冲突。

### 2. ViewModel视图模型层说明
1. **BusScheduleViewModel**作为页面与Dao的中间层，持有Dao实例，对外暴露Flow查询方法，严格遵循Android生命周期，不会随页面重建销毁。
2. 采用`ViewModelProvider.Factory`工厂模式安全创建ViewModel实例，从上下文提取Application对象初始化Room数据库，规避内存泄漏风险。
3. 内部封装原始数据列表，兜底方案硬编码固定13组互不相同时间戳，彻底解决旧缓存数据库默认时间全部一致问题。

### 3. Compose UI与导航层说明
1. **BusScheduleNavigation导航容器**
使用`NavHost`、`rememberNavController`搭建路由，配置首页`home`、详情`detail/{stop}`两个页面路由，携带站点名称参数完成页面传值。
2. **首页HomeScheduleScreen**
Scaffold脚手架布局，顶部大标题`Bus Schedule`；表头分为Stop Name、Arrival Time两栏；LazyColumn循环渲染所有班次Row，Row添加clickable点击跳转事件；每条数据下方配置分割线区分条目。
3. **详情DetailScheduleScreen**
顶部TopAppBar显示当前站点名称，左侧箭头返回按钮；列表仅展示当前站点班次，左侧统一占位`--`，右侧展示格式化到站时间。
4. **时间格式化工具函数formatTime**
接收int时间戳，通过SimpleDateFormat转换为`h:mm a`标准上下午时间字符串，全局统一时间展示样式。

### 4. MainActivity入口层说明
1. Activity作为应用启动入口，通过`viewModels`配合Factory工厂初始化全局ViewModel。
2. setContent挂载根导航组件，将ViewModel实例传递给所有Compose页面，实现全局数据共享。

## 四、运行截图与遇到的问题
### 1. 最终正确运行效果
首页列表13条班次时间全部互不重复，样式匹配标准范例：
Main Street：3:00 PM
Park Street：3:12 PM
Maple Avenue：3:25 PM
Broadway Avenue：3:41 PM
Post Street：3:58 PM
Elm Street：4:09 PM
Oak Drive：4:20 PM
Middle Street：4:34 PM
Palm Avenue：4:51 PM
Winding Way：4:55 PM
第二轮Main Street：5:00 PM
第二轮Park Street：5:12 PM
第二轮Maple Avenue：5:25 PM
点击条目正常跳转详情，返回按钮可用，旋转屏幕数据无丢失。

### 2. 遇到的问题与解决方案
1. **问题一：所有班次时间统一显示1:13 AM，全部相同**
- 原因：模拟器存在旧App缓存，本地残留空Room数据库，字段填充int默认值；assets数据库路径层级错误，预置db文件未成功加载。
- 解决：模拟器长按应用彻底卸载清除本地存储；核对文件路径`app/src/main/assets/database/bus_schedule.db`；兜底方案ViewModel硬编码固定不同时间戳列表，绕开数据库缓存故障。

2. **问题二：Compose页面大量Unresolved reference语法报错**
- 原因：Compose导入包混乱、Material3控件缺失必填命名参数、collectAsStateWithLifecycle依赖缺失；旧版viewModelFactory扩展语法新版本不兼容。
- 解决：统一使用material3标准控件导入；补全Text、Icon、Row、IconButton全部必填命名参数；补全lifecycle-compose依赖；重写标准原生ViewModelProvider.Factory工厂写法。

3. **问题三：MainActivity中viewModels工厂传参类型不匹配报错**
- 原因：新版viewModels要求lambda闭包包裹Factory对象，不能直接传入Factory变量。
- 解决：修改调用格式`by viewModels { BusScheduleViewModel.factory() }`，用lambda包裹工厂实例。

4. **问题四：formatTime调用参数匹配报错**
- 原因：自定义函数无命名参数定义，代码中额外携带`ms=`命名传参导致匹配失败。
- 解决：调用时直接传入数值，删除多余命名赋值，统一调用格式`formatTime(item.arrivalTimeInMillis)`。

## 五、实验总结
1. 熟练掌握Room数据库Entity-Dao-Database三层标准开发流程，理解assets预置本地数据库打包加载机制。
2. 深入理解MVVM分层优势，ViewModel隔离UI与数据层，Flow响应式数据流实现界面自动刷新，降低耦合。
3. 完整掌握Jetpack Compose页面布局、LazyColumn列表、Navigation路由跳转、状态订阅整套UI开发流程。
4. 学会模拟器本地持久化缓存清理方法，能够独立排查依赖、语法、数据库缓存类Android典型工程报错。
5. 实现标准分层架构代码，业务逻辑与界面完全解耦，代码可读性、可维护性高，符合Android现代化开发规范。