
# Lab9 实验报告：为 Dessert Clicker 添加 ViewModel

## 1. ViewModel 在 Android 架构中的作用简述
ViewModel 是 Android Jetpack 组件之一，专门用于**存储和管理与界面相关的数据**，并**独立于 Activity 或 Fragment 的生命周期**。其核心作用包括：
- **分离 UI 与业务逻辑**：将数据、状态和业务逻辑从 Activity 中移出，让 UI 只负责显示与事件分发。
- **状态持久化**：屏幕旋转、语言切换等配置变更时，ViewModel 不会重建，自动保留数据，避免状态丢失。
- **可测试性提升**：逻辑集中在 ViewModel，不依赖 Context，方便编写单元测试。
- **生命周期安全**：ViewModel 与 UI 生命周期解绑，不会因 UI 重建而丢失数据。

## 2. DessertUiState 数据类的字段设计说明
本次实验创建了 `DessertUiState` 数据类，用于**集中描述界面所有状态**，字段设计如下：
- **revenue: Int**：总收入，界面顶部显示的金额，是核心业务数据。
- **dessertsSold: Int**：已售出甜品总数，控制甜品升级规则。
- **currentDessertIndex: Int**：当前甜品在列表中的索引，用于定位甜品数据。
- **currentDessertImageId: Int**：当前甜品图片资源 ID，用于界面显示。
- **currentDessertPrice: Int**：当前甜品单价，用于每次点击计算收入。

所有字段均设**默认初始值**，与应用启动状态一致；使用 `data class` 可自动生成 `copy()` 方法，便于**不可变状态更新**，保证 Compose 能正确感知状态变化并触发重组。

## 3. DessertViewModel 的设计思路，包括状态管理和方法设计
### 3.1 状态管理
- ViewModel 内部使用 `mutableStateOf` 包装 `DessertUiState`，并通过 `by` 委托暴露为 `uiState`。
- 设置 **private set**，确保**外部只能读取、不能直接修改状态**，所有变更必须通过 ViewModel 方法执行，保证状态一致性。
- 持有甜品数据源 `desserts`，作为业务逻辑的基础数据。

### 3.2 方法设计
- **onDessertClicked()**：处理甜品点击事件。计算新收入、新销量，调用判断逻辑更新甜品类型，最后通过 `copy()` 生成新状态对象，触发 UI 更新。
- **determineDessertToShow(dessertsSold: Int)**：私有业务方法，根据销量判断当前应展示的甜品，逻辑从原 MainActivity 迁移至此，实现 UI 与业务解耦。

整体设计遵循**单一职责原则**：ViewModel 只负责状态管理与业务逻辑，不持有 Context、不处理 UI 显示。

## 4. MainActivity 重构前后对比分析
### 4.1 重构前
- 所有状态变量（`revenue`、`dessertsSold`、图片 ID 等）通过 `rememberSaveable` 内联在 Composable 函数中。
- 甜品点击逻辑、甜品判断逻辑直接写在 UI 回调中，**UI 与业务高度耦合**。
- 代码混乱、可读性差，配置变更时需手动保存状态，维护成本高。

### 4.2 重构后
- 移除所有 `rememberSaveable`、`mutableStateOf` 状态变量，UI 仅通过 `viewModel()` 获取 ViewModel 实例。
- UI 仅读取 `viewModel.uiState` 展示数据，点击事件通过 `viewModel.onDessertClicked()` 回调给 ViewModel。
- 业务逻辑（如甜品升级判断）完全迁移到 ViewModel，MainActivity 仅保留 UI 布局、分享功能，职责清晰。

## 5. 重构前后代码结构的区别和感受
### 5.1 结构区别
- **重构前**：所有代码集中在 `MainActivity.kt`，状态、逻辑、UI 混杂，无分层。
- **重构后**：
  - `MainActivity.kt`：仅负责 UI 组合、事件分发、分享功能。
  - `DessertViewModel.kt`：管理状态与业务逻辑。
  - `ui/DessertUiState.kt`：集中定义 UI 状态。
  - `model/`、`data/`：数据模型与数据源保持独立。

### 5.2 感受
重构后代码**结构清晰、职责分明**，符合 Android 架构最佳实践。修改业务逻辑时无需改动 UI 代码，降低出错风险；阅读代码时可快速定位 UI、状态、逻辑所在位置，可维护性大幅提升。同时，ViewModel 自动处理配置变更，无需手动保存状态，开发效率更高。

## 6. 遇到的问题与解决过程
### 6.1 问题1：导入 viewModel() 函数失败
- **现象**：添加依赖后，Composable 中无法识别 `viewModel()` 方法，提示未导入。
- **原因**：未正确导入 `androidx.lifecycle.viewmodel.compose.viewModel`。
- **解决**：手动添加对应 import 语句，同步 Gradle 后正常使用。

### 6.2 问题2：DessertUiState 包路径错误
- **现象**：ViewModel 无法引用 `DessertUiState`，提示找不到类。
- **原因**：创建文件时误将 `DessertUiState.kt` 放在 `ui/theme/` 而非 `ui/` 目录。
- **解决**：调整文件包路径为 `com.example.dessertclicker.ui`，修正 ViewModel 中的 import。

### 6.3 问题3：屏幕旋转后状态丢失
- **现象**：重构初期，旋转屏幕后收入、销量重置为 0。
- **原因**：误将状态变量写在 Composable 内部，而非 ViewModel 中。
- **解决**：删除 UI 中所有状态变量，统一由 ViewModel 管理，旋转后状态正常保留。

### 6.4 问题4：甜品升级逻辑异常
- **现象**：销量达标后甜品未自动升级。
- **原因**：ViewModel 中 `determineDessertToShow` 循环逻辑判断错误。
- **解决**：修正循环逻辑，遍历列表找到最后一个满足销量条件的甜品，问题解决。

---
