package com.suqi8.imagestudio.plugin.api

/**
 * 宿主传给插件的基础上下文。
 * 插件通过此接口访问所有宿主能力，不能直接依赖宿主内部模块。
 */
interface PluginContext {
    /** 当前插件 ID（与 plugin.toml 中 id 字段一致） */
    val pluginId: String
    /** 当前运行平台 */
    val platform: PluginPlatform
    /** 宿主版本号字符串（例如 "1.0.100.abc1234"） */
    val hostVersion: String
    /** Plugin API 版本（例如 "1.0"） */
    val apiVersion: String
    /** 插件各目录路径 */
    val paths: PluginPaths
    /** 权限服务 */
    val permissions: PluginPermissionService
    /** 日志服务 */
    val logger: PluginLogger
    /** 配置读写服务 */
    val settings: PluginSettings
    /** 项目访问服务 */
    val projects: PluginProjectService
    /** Shell 执行服务 */
    val shell: PluginShellService
    /** 后台任务服务 */
    val tasks: PluginTaskService
    /** 静态资源读取服务 */
    val resources: PluginResourceService
}
