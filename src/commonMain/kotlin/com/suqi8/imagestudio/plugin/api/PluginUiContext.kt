package com.suqi8.imagestudio.plugin.api

/**
 * 插件 UI 渲染时可用的上下文，包含基础上下文和 UI 专用能力。
 * 仅在 [ImageStudioPlugin.Content] 被调用期间有效。
 */
interface PluginUiContext {
    /** 基础插件上下文（包含全部服务接口） */
    val pluginContext: PluginContext
    /** 当前项目名称（宿主打开此插件时传入） */
    val projectName: String
    /** 当前 UI 展示模式 */
    val windowMode: WindowMode
    /** 宿主当前主题信息，用于适配亮/暗色 */
    val theme: HostThemeInfo
    /** 宿主当前语言标签，例如 "zh-CN" / "en-US" */
    val locale: String

    /** 关闭插件 UI（Dialog 或 Page） */
    fun close()

    /** 在宿主提供的 Snackbar 或 Toast 中显示提示信息 */
    fun showMessage(message: String)

    /** 在宿主提供的错误展示区域显示错误信息 */
    fun showError(message: String, throwable: Throwable? = null)

    /**
     * 在 UI 中向用户申请开启某个已声明权限。
     * 会弹出宿主的权限确认对话框，返回用户是否同意。
     */
    suspend fun requestPermission(permission: String): Boolean

    /**
     * 从 UI 启动一个后台任务（等效于 [PluginContext.tasks.submit]，但会自动绑定到当前项目）。
     * @param title       任务标题
     * @param cancellable 是否允许取消
     * @param block       任务执行体
     */
    fun runTask(
        title: String,
        cancellable: Boolean = true,
        block: suspend PluginTaskScope.() -> Unit
    )
}
