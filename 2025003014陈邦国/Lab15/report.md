\# Lab15 Flight Search 实验报告



\## 1. Entity 设计说明

\- Airport 数据类对应 airport 表，字段 id, iataCode, name, passengers。

\- Favorite 数据类对应 favorite 表，字段 id, departureCode, destinationCode。

\- FavoriteWithNames 用于联合查询结果，额外包含 departureName 和 destinationName。



\## 2. DAO 查询方法设计说明

\- searchAirports()：使用 LIKE 模糊匹配机场名称或 IATA 代码，按客流量降序。

\- getDestinations()：获取除当前机场外的所有机场。

\- getFavoritesWithNames()：联合查询 favorite 和 airport 表，显示名称。

\- isFavorite()、addFavorite()、removeFavorite()：收藏的增删查操作。



\## 3. LIKE 关键字的使用方法和作用

使用 `%query%` 通配符实现模糊搜索，用户输入部分文字即可获得自动补全建议。



\## 4. 联合查询的实现和作用

通过 `INNER JOIN` 将 favorite 表与 airport 表连接，查询出收藏航线的出发地与目的地名称，供 UI 展示。



\## 5. Preferences DataStore 的使用场景和实现

用于保存用户最后输入的搜索文本，重启应用后自动填充。通过 `stringPreferencesKey` 存储字符串，ViewModel 初始化时读取并恢复。



\## 6. ViewModel 状态管理设计

管理搜索文本、选中机场、建议列表、航班列表、收藏列表等状态，使用 StateFlow 驱动 UI 更新。



\## 7. UI 切换逻辑说明

\- 搜索框为空且未选机场 → 显示收藏列表

\- 搜索框非空且未选机场 → 显示自动补全建议

\- 选中机场 → 显示航班列表

\- 修改搜索文本自动清除已选机场，返回建议界面



\## 8. 实验中遇到的问题与解决过程

\- 由于环境 AGP 9.1 + Kotlin 2.2 不支持 KSP 及 kapt 插件，无法使用 Room 编译。

\- 解决方案：改用原生 SQLiteOpenHelper 实现数据库访问，保留 DAO 和 Database 类的结构，确保提交文件名符合要求，同时应用功能完全正常。

