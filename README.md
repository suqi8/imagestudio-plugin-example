# HelloPlugin — ImageStudio Native Plugin Example

`plugin-example` 是一个可独立构建的 ImageStudio 原生插件示例工程。

它演示了当前插件系统中最常用的一组能力：

- 插件生命周期
- Compose UI 挂载
- 宿主题色适配
- 插件私有设置读写
- 插件私有存储
- 项目文件读写与二进制文件 API
- 用户文件系统访问 API
- HTTP 网络 API
- 剪贴板 API
- 宿主设置与导航 API
- 资源读取
- Shell 调用
- 后台任务与权限申请

构建产物为 ImageStudio 可直接安装的 `.isp` 插件包。

## 项目结构

```text
plugin-example/
├── plugin.toml
├── build.gradle.kts
├── README.md
├── gradle/
└── src/
    └── commonMain/
        ├── kotlin/com/example/helloplugin/
        │   ├── HelloPlugin.kt
        │   ├── HelloPluginPage.kt
        │   ├── HelloPluginSupport.kt
        │   └── PluginDemoTheme.kt
        ├── composeResources/
        └── pluginAssets/
```

## 当前示例覆盖的能力

| 能力        | 说明                                              |
|-----------|-------------------------------------------------|
| 生命周期      | `onLoad` / `onEnable` / `onDisable` / `Content` |
| UI 挂载     | 通过 `PluginUiContext` 渲染插件界面                     |
| 主题适配      | 跟随宿主亮暗色和主色                                      |
| 插件设置      | `PluginSettings` 的字符串、布尔、整数、长整型、浮点、键枚举能力        |
| 插件存储      | `PluginStorageService` 的 data/cache/tmp 文件访问    |
| 项目访问      | 文本文件读写、目录创建、删除、权限调整                             |
| 二进制文件 API | `exists` / `readBytes` / `writeBytes`           |
| 用户文件系统    | `PluginFileService` 的受权限控制文件访问                  |
| HTTP 网络   | `PluginNetworkService` 请求、文本和二进制响应              |
| 剪贴板       | `PluginClipboardService` 文本读写                   |
| 宿主能力      | `PluginHostService` 的设置读取/写入、导航和 URL 打开         |
| 插件资源      | `readAsset` / `readTextAsset`                   |
| Shell     | 通过宿主执行跨平台命令，支持等待结果和实时输出回调                       |
| 后台任务      | `runTask` / `PluginTaskService`                 |
| 权限        | 运行时申请与宿主侧权限校验                                   |

## 清单文件说明

插件元数据定义在根目录的 `plugin.toml` 中。

当前示例使用的关键字段：

| 字段 | 说明 |
| --- | --- |
| `[plugin].id` | 插件唯一 ID |
| `[plugin].entryClass` | 插件入口类 |
| `[plugin].supportedPlatforms` | 支持的平台列表 |
| `[plugin].customPage` | 是否使用全页 UI |
| `[runtime].desktopJar` | Desktop 运行时产物路径 |
| `[runtime].androidDexJar` | Android dex-in-jar 产物路径 |
| `[permissions].declared` | 声明的权限列表 |
| `[permissions].defaultEnabled` | 默认开启的权限 |

## 构建要求

- JDK 21
- Gradle Wrapper（仓库已提供）
- Android SDK
- Android build-tools（用于 `d8`）

优先通过以下方式提供 Android SDK 路径：

- `ANDROID_HOME`
- `ANDROID_SDK_ROOT`
- `local.properties` 中的 `sdk.dir`

## 构建命令

在 `plugin-example` 目录下执行：

```bash
# 编译整个示例工程
./gradlew build

# 生成 Desktop JAR
./gradlew jvmJar

# 生成 Android classes JAR
./gradlew androidClassesJar

# 生成 Android dex-in-jar
./gradlew androidDexJar

# 打包最终 .isp
./gradlew packageIsp
```

Windows PowerShell 下可使用：

```powershell
.\gradlew.bat build
.\gradlew.bat packageIsp
```

## 输出产物

主要输出位置如下：

```text
build/
├── isp/
│   └── helloplugin-1.0.0.isp
└── plugin/
    └── android/
        ├── helloplugin-android-classes.jar
        └── helloplugin-android.jar
```

`packageIsp` 会自动将以下内容打包进 `.isp`：

- `plugin.toml`
- `libs/helloplugin-desktop.jar`
- `libs/helloplugin-android.jar`
- `assets/` 下的插件资源

## 安装与验证

将构建出的 `.isp` 导入 ImageStudio 后，可以重点验证以下内容：

- 插件能否正常显示页面
- 主题是否跟随宿主变化
- `project.read` / `project.write` / `project.output.write` 权限开关是否生效
- Shell 调用是否按权限受控
- 后台任务是否能正常提交到宿主任务面板
- 文本/二进制项目文件、插件存储、资源读取等 API 是否正常工作

## 权限列表

当前示例声明了这些权限：

| 权限 | 用途 |
| --- | --- |
| `project.read` | 读取当前项目文件 |
| `project.write` | 写入当前项目文件 |
| `project.output.write` | 写入项目输出目录 |
| `plugin.storage` | 读写插件 data/cache/tmp 私有存储 |
| `file.read` | 读取用户文件系统路径 |
| `file.write` | 写入用户文件系统路径 |
| `network.http` | 发起 HTTP 请求 |
| `clipboard.read` | 读取剪贴板文本 |
| `clipboard.write` | 写入剪贴板文本 |
| `settings.read` | 读取宿主公开设置键 |
| `settings.write` | 写入宿主公开设置键 |
| `host.navigation` | 打开 URL 或请求宿主导航 |
| `shell.execute` | 执行宿主 Shell 命令 |
| `task.background` | 提交后台任务 |

## 开发说明

- `plugin-api` 的接口源码随示例一同放在 `src/commonMain/kotlin/com/suqi8/imagestudio/plugin/api/`，用于独立构建示例工程；正式发布 Maven 包后可替换为 Maven 依赖。
- 运行时真正由宿主提供这些 API 实现，因此打包时会从最终 JAR 中排除宿主 API 类。
- Compose 运行时相关依赖在示例工程中按宿主提供模式使用，避免重复打包核心运行时。

## 发布前建议

- 确认 `plugin.toml` 中的 `version`、`author`、`description` 已更新为你准备公开发布的内容。
- 运行一次 `./gradlew build packageIsp`，确认最终 `.isp` 可生成。
- 如需作为公开仓库发布，建议补充 LICENSE、变更日志和插件截图。

## License

此目录当前未附带单独 License 文件；如果你准备公开发布到 GitHub，建议在发布前补充对应许可证。
