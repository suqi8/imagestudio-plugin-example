package com.suqi8.imagestudio.plugin.api

import androidx.compose.runtime.Composable

/**
 * 所有原生插件的入口接口。
 *
 * 插件作者需要创建一个实现此接口的类，并在 plugin.toml 中通过 `entryClass` 字段指定其全限定名。
 * 宿主会通过反射实例化该类，并按生命周期顺序调用各回调。
 *
 * ## 生命周期顺序
 * ```
 * onLoad → onEnable → onSessionStart → [Content 渲染] → onSessionStop → onDisable → onUnload
 * ```
 *
 * ## 注意事项
 * - 生命周期回调（除 [Content]）均在主线程调用，不要做耗时操作
 * - 耗时任务必须通过 [PluginContext.tasks] 提交，以便宿主管理取消和进度
 * - 插件 jar 中不应重复打包宿主已提供的 Compose Runtime / Kotlin stdlib 等依赖
 */
interface ImageStudioPlugin {

    /**
     * 插件类加载完成后调用，做轻量级初始化。
     * 此时插件尚未对用户可见，不应在此启动任务或申请权限。
     */
    fun onLoad(context: PluginContext) {}

    /**
     * 插件启用时调用（用户手动启用，或安装后根据 enabledByDefault 自动启用）。
     * 可以在此注册能力、监听器等。
     */
    fun onEnable(context: PluginContext) {}

    /**
     * 插件禁用时调用。应在此停止新任务，但不必释放所有资源。
     */
    fun onDisable(context: PluginContext) {}

    /**
     * 插件卸载前调用。必须在此释放所有资源（文件句柄、网络连接、线程等）。
     * 调用完毕后宿主会卸载插件 ClassLoader。
     */
    fun onUnload() {}

    /**
     * 用户打开插件 UI 时调用（每次打开都会调用）。
     * 可以在此准备本次会话所需的数据。
     */
    fun onSessionStart(context: PluginContext) {}

    /**
     * 用户关闭插件 UI 时调用。
     * @param reason 关闭原因，参见 [PluginStopReason]
     */
    fun onSessionStop(reason: PluginStopReason) {}

    /**
     * 插件的 Compose UI 入口。由宿主在 Compose 树中调用。
     * - [WindowMode.Dialog]：内容会放入宿主 Dialog 容器，可使用任意 Compose 布局
     * - [WindowMode.Page]  ：内容占据完整页面，可自定义 Scaffold / TopBar
     *
     * @param context UI 上下文，包含项目信息、主题、关闭能力等
     */
    @Composable
    fun Content(context: PluginUiContext)
}
