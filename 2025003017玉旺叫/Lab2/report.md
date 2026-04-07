Lab2 实验报告
一、实验目的
使用 Jetpack Compose 实现一个个人名片界面，掌握 Column、Row、Image、Text、Icon、Modifier 等基础组件的使用，能够完成界面布局、颜色设置、间距调整与资源引用。
二、实验环境
操作系统：Windows 11
开发工具：Android Studio
编写工具：VS Code（Markdown 报告）
开发语言：Kotlin + Jetpack Compose
三、功能实现
本实验实现了一个个人电子名片，包含以下内容：
顶部显示 Logo 图片
显示个人信息：
姓名：玉旺叫
职位：学生
显示联系方式：
电话：18849630281
社交账号：@yuwangjiao
邮箱：2541505945@qq.com
使用蓝色背景、白色文字、粉色标题、绿色图标，界面美观整齐。
四、界面布局说明
整体使用 Column 垂直布局，内部包含：
顶部：Image 展示头像 / Logo
中间：Text 显示姓名与职位
底部：多个 Row 组合实现联系方式（图标 + 文字）
通过 Modifier 设置背景色、大小、内边距、居中对齐；
通过 Spacer 控制组件之间的间距。
五、遇到的问题及解决方法
问题：backgroundColor 报红，提示未定义
解决：将颜色变量定义在全局位置，保证所有函数可访问。
问题：R.drawable.ic_android 找不到资源
解决：确认图片放在 drawable 目录，并正确导入项目 R 类。
问题：界面排版不整齐
解决：使用 Arrangement 和 Alignment 实现居中，调整 Spacer 高度。
六、实验总结
通过本次实验，我掌握了 Jetpack Compose 最基础的界面开发方法，学会了：
使用 Column / Row 进行界面布局
使用 Image、Text、Icon 展示内容
使用 Modifier 设置样式、间距、对齐方式
引用项目图片资源
完成一个完整的名片界面