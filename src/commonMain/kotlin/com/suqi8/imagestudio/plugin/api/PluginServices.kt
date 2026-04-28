package com.suqi8.imagestudio.plugin.api

import java.util.Collections.emptyMap


/**
 * 插件各目录的绝对路径。宿主负责在插件加载前创建所有目录。
 */
interface PluginPaths {
    /** 插件安装根目录（versions/x.x.x/） */
    val pluginDir: String
    /** 持久化数据目录（卸载时可选保留） */
    val dataDir: String
    /** 缓存目录（可清理，不应存放重要数据） */
    val cacheDir: String
    /** 运行临时目录（可清理） */
    val tmpDir: String
    /** 静态资源目录（assets/） */
    val assetsDir: String
}


/** 插件日志服务，日志会同时写入插件独立日志文件和宿主调试控制台 */
interface PluginLogger {
    fun info(message: String)
    fun warning(message: String)
    fun error(message: String, throwable: Throwable? = null)
    fun debug(message: String)
}


/**
 * 插件配置读写服务。
 * 数据持久化到插件 data 目录，仅当前插件可访问。
 */
interface PluginSettings {
    fun getString(key: String, default: String = ""): String
    fun putString(key: String, value: String)
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun getInt(key: String, default: Int = 0): Int
    fun putInt(key: String, value: Int)
    fun getLong(key: String, default: Long = 0L): Long
    fun putLong(key: String, value: Long)
    fun getFloat(key: String, default: Float = 0f): Float
    fun putFloat(key: String, value: Float)
    fun contains(key: String): Boolean
    fun keys(): Set<String>
    /** 删除指定 key */
    fun remove(key: String)
    /** 清空插件所有配置 */
    fun clear()
}


/** 宿主公开设置访问服务。需要 `settings.read` / `settings.write` 权限。 */
interface PluginHostSettingsService {
    fun getString(key: String, default: String = ""): String
    fun putString(key: String, value: String)
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun getInt(key: String, default: Int = 0): Int
    fun putInt(key: String, value: Int)
    fun getFloat(key: String, default: Float = 0f): Float
    fun putFloat(key: String, value: Float)
}


/**
 * 插件权限服务。插件通过此接口检查或申请权限。
 * 宿主 API 实现在执行前都会再次校验权限，插件不能绕过权限检查。
 */
interface PluginPermissionService {
    /** 检查某个权限是否已声明 */
    fun isDeclared(permission: String): Boolean
    /** 检查某个已声明权限是否已被用户开启 */
    fun hasPermission(permission: String): Boolean
    /** 获取某个权限的详细状态 */
    fun getPermissionState(permission: String): PermissionState
    /** 获取所有已声明权限列表 */
    fun getDeclaredPermissions(): List<String>
    /** 获取所有已开启权限列表 */
    fun getEnabledPermissions(): List<String>
    /** 获取所有已关闭权限列表 */
    fun getDisabledPermissions(): List<String>
    /** 在运行时向用户申请开启某个已声明权限，返回是否成功 */
    suspend fun requestPermission(permission: String): Boolean
}


/**
 * Shell 执行服务。需要插件声明 `shell.execute` 权限。
 * 宿主在执行前会校验权限，未授权时抛出 [PluginPermissionException]。
 */
interface PluginShellService {
    /** 执行命令并等待结果 */
    suspend fun execute(command: List<String>, workingDir: String? = null): PluginShellResult
    /** 获取宿主工具目录（包含 apktool、smali 等工具） */
    fun getBinDir(): String
    /** 获取指定工具的完整路径 */
    fun getToolPath(toolName: String): String
}


/**
 * 项目访问服务。需要 `project.read` / `project.write` 权限。
 */
interface PluginProjectService {
    /** 获取项目工作目录（解包/打包用，内部不可见存储） */
    fun getProjectWorkingDir(projectName: String): String
    /** 获取项目输出目录（用户可见存储） */
    fun getProjectStorageDir(projectName: String): String
    /** 列出所有已有项目名称 */
    fun listProjects(): List<String>

    /** 列出项目目录下的文件相对路径。 */
    fun listFiles(
        projectName: String,
        relativeDir: String = "",
        storageDir: Boolean = false,
        recursive: Boolean = false
    ): List<String>

    /** 判断项目内文件或目录是否存在。 */
    fun exists(
        projectName: String,
        relativePath: String,
        storageDir: Boolean = false
    ): Boolean

    /** 读取项目内 UTF-8 文本文件，不存在时返回 null。 */
    fun readText(
        projectName: String,
        relativePath: String,
        storageDir: Boolean = false
    ): String?

    /** 读取项目内二进制文件，不存在时返回 null。 */
    fun readBytes(
        projectName: String,
        relativePath: String,
        storageDir: Boolean = false
    ): ByteArray?

    /** 写入项目内 UTF-8 文本文件，必要时自动创建父目录。 */
    fun writeText(
        projectName: String,
        relativePath: String,
        content: String,
        append: Boolean = false,
        storageDir: Boolean = false
    )

    /** 写入项目内二进制文件，必要时自动创建父目录。 */
    fun writeBytes(
        projectName: String,
        relativePath: String,
        content: ByteArray,
        append: Boolean = false,
        storageDir: Boolean = false
    )

    /** 创建项目内目录，必要时自动递归创建父目录。 */
    fun createDirectory(
        projectName: String,
        relativePath: String,
        storageDir: Boolean = false
    )

    /** 删除项目内文件或目录。目录删除需显式传入 recursive=true。 */
    fun delete(
        projectName: String,
        relativePath: String,
        storageDir: Boolean = false,
        recursive: Boolean = false
    ): Boolean

    /** 修改项目内文件权限；传 null 表示保留原值。 */
    fun setPermissions(
        projectName: String,
        relativePath: String,
        readable: Boolean? = null,
        writable: Boolean? = null,
        executable: Boolean? = null,
        ownerOnly: Boolean = false,
        storageDir: Boolean = false
    ): Boolean
}


/** 插件私有存储服务。需要 `plugin.storage` 权限。 */
interface PluginStorageService {
    fun listFiles(
        relativeDir: String = "",
        area: PluginStorageArea = PluginStorageArea.Data,
        recursive: Boolean = false
    ): List<String>

    fun exists(
        relativePath: String,
        area: PluginStorageArea = PluginStorageArea.Data
    ): Boolean

    fun readText(
        relativePath: String,
        area: PluginStorageArea = PluginStorageArea.Data
    ): String?

    fun readBytes(
        relativePath: String,
        area: PluginStorageArea = PluginStorageArea.Data
    ): ByteArray?

    fun writeText(
        relativePath: String,
        content: String,
        append: Boolean = false,
        area: PluginStorageArea = PluginStorageArea.Data
    )

    fun writeBytes(
        relativePath: String,
        content: ByteArray,
        append: Boolean = false,
        area: PluginStorageArea = PluginStorageArea.Data
    )

    fun createDirectory(
        relativePath: String,
        area: PluginStorageArea = PluginStorageArea.Data
    )

    fun delete(
        relativePath: String,
        area: PluginStorageArea = PluginStorageArea.Data,
        recursive: Boolean = false
    ): Boolean
}


/** 用户文件系统访问服务。需要 `file.read` / `file.write` 权限。 */
interface PluginFileService {
    fun exists(path: String): Boolean
    fun listFiles(path: String, recursive: Boolean = false): List<String>
    fun readText(path: String, charsetName: String = "UTF-8"): String?
    fun readBytes(path: String): ByteArray?
    fun writeText(path: String, content: String, append: Boolean = false, charsetName: String = "UTF-8")
    fun writeBytes(path: String, content: ByteArray, append: Boolean = false)
    fun createDirectory(path: String)
    fun delete(path: String, recursive: Boolean = false): Boolean
}


/** HTTP 网络服务。需要 `network.http` 权限。 */
interface PluginNetworkService {
    suspend fun request(request: PluginHttpRequest): PluginHttpResponse
    suspend fun getText(url: String, headers: Map<String, String> = emptyMap()): String
    suspend fun getBytes(url: String, headers: Map<String, String> = emptyMap()): ByteArray
}


/** 剪贴板文本服务。需要 `clipboard.read` / `clipboard.write` 权限。 */
interface PluginClipboardService {
    fun readText(): String?
    fun writeText(text: String)
}


/** 宿主通用能力服务。导航和 URL 打开需要 `host.navigation` 权限。 */
interface PluginHostService {
    val settings: PluginHostSettingsService
    fun navigate(target: HostNavigationTarget, projectName: String = "", pluginId: String = "")
    fun openUrl(url: String)
}


/** 插件静态资源服务，用于读取插件 assets/ 目录下的文件 */
interface PluginResourceService {
    /** 读取 assets/ 目录下指定文件的字节内容，文件不存在时返回 null */
    fun readAsset(path: String): ByteArray?
    /** 读取 assets/ 目录下指定文本文件，文件不存在时返回 null */
    fun readTextAsset(path: String, charsetName: String = "UTF-8"): String?
    /** 列出 assets/ 目录（或子目录）下的所有文件相对路径 */
    fun listAssets(directory: String = ""): List<String>
}


/**
 * 插件后台任务服务。插件的所有长时间任务必须通过此服务启动，
 * 以便宿主在全局任务列表中展示、监控和取消。
 * 需要 `task.background` 权限。
 */
interface PluginTaskService {
    /**
     * 提交一个后台任务，任务在协程中执行。
     * @param title        任务标题（展示在全局任务列表）
     * @param cancellable  是否允许用户取消
     * @param block        任务执行体，通过 [PluginTaskScope] 更新进度和日志
     * @return 任务 ID，可用于后续取消
     */
    fun submit(
        title: String,
        cancellable: Boolean = true,
        block: suspend PluginTaskScope.() -> Unit
    ): String

    /** 取消指定任务 */
    fun cancel(taskId: String)

    /** 获取当前正在运行的任务列表 */
    fun getRunningTasks(): List<PluginTaskInfo>
}

/**
 * 插件任务执行作用域，由宿主注入到任务执行体中。
 */
interface PluginTaskScope {
    /** 当前任务是否已被取消 */
    val isCancelled: Boolean

    /**
     * 更新任务进度。
     * @param progress 进度值 0.0～1.0
     * @param message  进度描述文字
     */
    fun updateProgress(progress: Float, message: String = "")

    /** 向任务日志追加一行 */
    fun log(message: String)

    /**
     * 检查取消状态，若已取消则抛出 [kotlinx.coroutines.CancellationException]。
     * 建议在循环或耗时操作前调用。
     */
    fun checkCancelled()
}


/** 调用宿主 API 时权限未开启 */
class PluginPermissionException(permission: String) :
    SecurityException("权限未开启：$permission。请在插件 plugin.toml 中声明，并引导用户授权。")

/** 插件加载失败 */
class PluginLoadException(pluginId: String, cause: Throwable) :
    Exception("插件 [$pluginId] 加载失败：${cause.message}", cause)
