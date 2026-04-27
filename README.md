# HelloPlugin — ImageStudio 原生插件示例

这是一个演示 ImageStudio 原生插件（`.isp` 格式）完整用法的示例项目。

## 目录结构

```
plugin-example/
├── plugin.toml                    # 插件清单（宿主读取此文件识别插件）
└── src/main/kotlin/com/example/helloplugin/
    ├── HelloPlugin.kt             # customPage=false：在宿主 Dialog 内展示 UI
    └── HelloPluginPage.kt         # customPage=true ：宿主打开全屏页面
```

## 插件清单字段速查

| 字段 | 说明 |
|------|------|
| `[plugin] id` | 唯一标识符，反向域名格式 |
| `[plugin] version` | SemVer 版本号（`major.minor.patch`） |
| `[runtime] mainClass` | 实现 `ImageStudioPlugin` 的全限定类名 |
| `[runtime] android` / `desktop` | 各平台的 JAR 相对路径 |
| `[ui] customPage` | `false` = Dialog 容器；`true` = 全屏页面容器 |
| `[permissions] declared` | 插件可能使用的全部权限 |
| `[permissions] requiredForRun` | 缺少则无法运行的权限 |
| `[permissions] defaultEnabled` | 安装时默认开启的权限 |

## 构建步骤

### 1. 添加宿主 API 依赖

在插件的 `build.gradle.kts` 中添加 ImageStudio Plugin API 依赖：

```kotlin
// 暂时通过本地 jar 引入（待 API 模块发布到 Maven 后替换）
dependencies {
    compileOnly(files("path/to/imagestudio-plugin-api.jar"))
    // Compose Multiplatform（与宿主版本保持一致）
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.ui)
}
```

### 2. 编译 JAR

```bash
# Desktop（普通 JVM jar）
./gradlew :plugin-example:jvmJar

# Android（dex-in-jar，需 Android SDK + D8）
./gradlew :plugin-example:assembleRelease
# 输出：build/outputs/aar/plugin-example-release.aar
# 提取其中的 classes.jar 并重命名为 helloplugin-android.jar
```

### 3. 打包 .isp

`.isp` 是一个标准 ZIP 文件，扩展名改为 `.isp`：

```
helloplugin.isp  (ZIP)
├── plugin.toml
└── libs/
    ├── helloplugin-android.jar
    └── helloplugin-desktop.jar
```

```bash
# 打包命令（在 plugin-example/ 目录下）
zip -r ../helloplugin.isp plugin.toml libs/
# 或使用 Python
python -c "
import zipfile, os
with zipfile.ZipFile('../helloplugin.isp', 'w', zipfile.ZIP_DEFLATED) as z:
    z.write('plugin.toml')
    for f in os.listdir('libs'):
        z.write(f'libs/{f}')
"
```

### 4. 安装

将 `helloplugin.isp` 传输到设备，在 ImageStudio 插件列表页面点击「安装」并选择该文件。

## 权限说明

| 权限名 | 说明 |
|--------|------|
| `SHELL` | 执行系统 Shell 命令 |
| `STORAGE_READ` | 读取存储（项目文件等） |
| `STORAGE_WRITE` | 写入存储 |
| `NETWORK` | 访问网络 |

## 生命周期

```
宿主启动 → onCreate()
用户启用 → onEnable()
用户点击运行 → Content() [Compose UI]
用户禁用 → onDisable()
插件卸载/宿主退出 → onDestroy()
```

## 注意事项

- 插件代码在宿主的 ClassLoader 下运行，可直接使用宿主已有的 Compose/Kotlin 运行时
- 不要在插件中初始化全局单例（Koin、反射缓存等），避免与宿主冲突
- 耗时操作必须放在 `runTask` / 协程中，**禁止阻塞主线程**
- 崩溃超过 3 次后宿主会自动禁用此插件，请妥善处理异常
