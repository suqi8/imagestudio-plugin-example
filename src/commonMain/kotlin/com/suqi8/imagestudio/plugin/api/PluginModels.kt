package com.suqi8.imagestudio.plugin.api


/** 插件支持的运行平台 */
enum class PluginPlatform {
    Android,
    Windows,
    MacOS,
    Linux;

    companion object {
        /** 将 plugin.toml 中的字符串解析为 PluginPlatform，大小写不敏感 */
        fun fromString(value: String): PluginPlatform? = when (value.lowercase()) {
            "android" -> Android
            "windows" -> Windows
            "macos"   -> MacOS
            "linux"   -> Linux
            else      -> null
        }
    }
}


/** 插件的生命周期状态 */
enum class PluginState {
    Discovered,   // 已发现目录，尚未完成解析
    Installed,    // 已安装，尚未加载 ClassLoader
    Loaded,       // ClassLoader 已加载，尚未调用 onEnable
    Enabled,      // 已启用，可以接受 UI 会话
    Disabled,     // 已禁用（用户主动或权限不足）
    Error         // 加载或运行出错
}

/** 插件的类型（兼容旧系统） */
enum class PluginType {
    Native,      // 原生 .isp 插件（本系统）
    MioKitchen   // mio-kitchen 兼容插件（旧格式）
}


/** 插件后台任务的生命周期状态 */
enum class TaskState {
    Created,    // 已创建，尚未开始
    Running,    // 运行中
    Succeeded,  // 成功完成
    Failed,     // 失败
    Cancelled   // 已取消
}


/**
 * 插件 UI 的展示模式，由 plugin.toml 中 customPage 字段决定。
 * - [Dialog] ：宿主提供 Dialog 容器，插件只负责内容区域（customPage = false）
 * - [Page]   ：插件占据完整页面，可自定义 Scaffold/TopBar（customPage = true）
 */
enum class WindowMode { Dialog, Page }


/** 插件 UI 会话结束的原因 */
enum class PluginStopReason {
    UserClosed,        // 用户主动关闭
    HostShutdown,      // 宿主退出
    PluginDisabled,    // 插件被禁用
    PluginUninstalled, // 插件被卸载
    Error              // 插件自身异常
}


/** 某个权限的当前授权状态 */
enum class PermissionState {
    NotDeclared,     // 插件未在 plugin.toml 中声明该权限
    Enabled,         // 已声明且用户已授权
    Disabled,        // 已声明但用户未授权（或默认关闭）
    DeniedByPolicy   // 被系统/策略禁止
}


/**
 * 宿主当前的主题信息，供插件适配亮/暗色和主色调。
 * @param isDarkTheme 是否暗色模式
 * @param primaryColor 宿主主色调（ARGB Long）
 */
data class HostThemeInfo(
    val isDarkTheme: Boolean,
    val primaryColor: Long
)


/**
 * 插件后台任务的元数据快照。
 * @param taskId    任务唯一 ID（由宿主生成）
 * @param pluginId  所属插件 ID
 * @param title     任务标题（展示在全局任务列表）
 * @param state     当前任务状态
 * @param progress  进度 0.0～1.0，null 表示未知进度
 * @param startTime 开始时间戳（毫秒）
 * @param endTime   结束时间戳，未结束时为 null
 */
data class PluginTaskInfo(
    val taskId: String,
    val pluginId: String,
    val title: String,
    val state: TaskState,
    val progress: Float?,
    val startTime: Long,
    val endTime: Long? = null
)


/**
 * Shell 命令的执行结果。
 * @param exitCode 进程退出码，0 表示成功
 * @param stdout   标准输出
 * @param stderr   标准错误
 */
data class PluginShellResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String
) {
    val isSuccess: Boolean get() = exitCode == 0
}
