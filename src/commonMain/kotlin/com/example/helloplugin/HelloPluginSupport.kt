package com.example.helloplugin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.helloplugin.generated.resources.Res
import com.example.helloplugin.generated.resources.clear_output
import com.example.helloplugin.generated.resources.close
import com.example.helloplugin.generated.resources.command_label
import com.example.helloplugin.generated.resources.current_project_label
import com.example.helloplugin.generated.resources.error_result
import com.example.helloplugin.generated.resources.api_extras_asset_empty
import com.example.helloplugin.generated.resources.api_extras_asset_preview_label
import com.example.helloplugin.generated.resources.api_extras_binary_empty
import com.example.helloplugin.generated.resources.api_extras_binary_missing
import com.example.helloplugin.generated.resources.api_extras_binary_preview_label
import com.example.helloplugin.generated.resources.api_extras_binary_written
import com.example.helloplugin.generated.resources.api_extras_read_asset
import com.example.helloplugin.generated.resources.api_extras_read_binary
import com.example.helloplugin.generated.resources.api_extras_refresh
import com.example.helloplugin.generated.resources.api_extras_section_description
import com.example.helloplugin.generated.resources.api_extras_section_title
import com.example.helloplugin.generated.resources.api_extras_settings_snapshot_label
import com.example.helloplugin.generated.resources.api_extras_snapshot_empty
import com.example.helloplugin.generated.resources.api_extras_snapshot_refreshed
import com.example.helloplugin.generated.resources.api_extras_status_label
import com.example.helloplugin.generated.resources.api_extras_write_binary
import com.example.helloplugin.generated.resources.asset_missing
import com.example.helloplugin.generated.resources.hello_subtitle
import com.example.helloplugin.generated.resources.hello_title
import com.example.helloplugin.generated.resources.load_count_label
import com.example.helloplugin.generated.resources.no_shell_permission
import com.example.helloplugin.generated.resources.no_shell_permission_detail
import com.example.helloplugin.generated.resources.output_label
import com.example.helloplugin.generated.resources.permission_denied
import com.example.helloplugin.generated.resources.permission_granted
import com.example.helloplugin.generated.resources.permission_label
import com.example.helloplugin.generated.resources.platform_label
import com.example.helloplugin.generated.resources.project_demo_append_file
import com.example.helloplugin.generated.resources.project_demo_content_empty
import com.example.helloplugin.generated.resources.project_demo_create_dir
import com.example.helloplugin.generated.resources.project_demo_delete_file
import com.example.helloplugin.generated.resources.project_demo_description
import com.example.helloplugin.generated.resources.project_demo_file_deleted
import com.example.helloplugin.generated.resources.project_demo_file_missing
import com.example.helloplugin.generated.resources.project_demo_file_written
import com.example.helloplugin.generated.resources.project_demo_listing_empty
import com.example.helloplugin.generated.resources.project_demo_make_readonly
import com.example.helloplugin.generated.resources.project_demo_path_label
import com.example.helloplugin.generated.resources.project_demo_read_file
import com.example.helloplugin.generated.resources.project_demo_read_permission_needed
import com.example.helloplugin.generated.resources.project_demo_refreshed
import com.example.helloplugin.generated.resources.project_demo_restore_writable
import com.example.helloplugin.generated.resources.project_demo_section_title
import com.example.helloplugin.generated.resources.project_demo_status_label
import com.example.helloplugin.generated.resources.project_demo_writable_restored
import com.example.helloplugin.generated.resources.project_demo_dir_created
import com.example.helloplugin.generated.resources.project_demo_readonly_updated
import com.example.helloplugin.generated.resources.project_demo_refresh
import com.example.helloplugin.generated.resources.project_demo_write_file
import com.example.helloplugin.generated.resources.project_demo_file_appended
import com.example.helloplugin.generated.resources.project_empty
import com.example.helloplugin.generated.resources.primary_color_label
import com.example.helloplugin.generated.resources.project_permission_label
import com.example.helloplugin.generated.resources.run_command_description
import com.example.helloplugin.generated.resources.run_command_empty
import com.example.helloplugin.generated.resources.run_command_failed
import com.example.helloplugin.generated.resources.run_command_hint
import com.example.helloplugin.generated.resources.run_command_running
import com.example.helloplugin.generated.resources.run_command_success
import com.example.helloplugin.generated.resources.run_uname
import com.example.helloplugin.generated.resources.settings_clear
import com.example.helloplugin.generated.resources.settings_cleared
import com.example.helloplugin.generated.resources.settings_note_label
import com.example.helloplugin.generated.resources.settings_reload
import com.example.helloplugin.generated.resources.settings_reloaded
import com.example.helloplugin.generated.resources.settings_save
import com.example.helloplugin.generated.resources.settings_saved
import com.example.helloplugin.generated.resources.settings_section_description
import com.example.helloplugin.generated.resources.settings_section_title
import com.example.helloplugin.generated.resources.submit_task
import com.example.helloplugin.generated.resources.task_idle_status
import com.example.helloplugin.generated.resources.task_queued_status
import com.example.helloplugin.generated.resources.task_running_step_1
import com.example.helloplugin.generated.resources.task_running_step_2
import com.example.helloplugin.generated.resources.task_running_step_3
import com.example.helloplugin.generated.resources.task_permission_needed
import com.example.helloplugin.generated.resources.task_section_description
import com.example.helloplugin.generated.resources.task_section_title
import com.example.helloplugin.generated.resources.task_submitted_detail
import com.example.helloplugin.generated.resources.task_submitted_snackbar
import com.example.helloplugin.generated.resources.theme_dark
import com.example.helloplugin.generated.resources.theme_label
import com.example.helloplugin.generated.resources.theme_light
import com.example.helloplugin.generated.resources.window_mode_detail
import com.example.helloplugin.generated.resources.window_mode_label
import com.suqi8.imagestudio.plugin.api.PluginPlatform
import com.suqi8.imagestudio.plugin.api.PluginUiContext
import com.suqi8.imagestudio.plugin.api.WindowMode
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

private const val DemoRelativeDir = "plugin-demo"
private const val DemoRelativeFile = "plugin-demo/demo.txt"
private const val DemoBinaryFile = "plugin-demo/demo.bin"

@Composable
internal fun HelloPluginDemoScreen(context: PluginUiContext) {
    PluginDemoTheme(context.theme) {
        HelloPluginDemoContent(context)
    }
}

@Composable
private fun HelloPluginDemoContent(context: PluginUiContext) {
    val pluginContext = context.pluginContext
    val scope = rememberCoroutineScope()
    val isFullscreenPage = context.windowMode == WindowMode.Page
    val runCount = remember(pluginContext) { pluginContext.settings.getInt("run_count", 0) }
    val command = remember(pluginContext.platform) { demoSystemInfoCommand(pluginContext.platform) }
    val platformName = remember(pluginContext.platform) { demoPlatformLabel(pluginContext.platform) }
    val currentProject = context.projectName.takeIf { it.isNotBlank() }
    val projectEmptyText = stringResource(Res.string.project_empty)
    val displayProject = currentProject ?: projectEmptyText
    val themeModeText = if (context.theme.isDarkTheme) {
        stringResource(Res.string.theme_dark)
    } else {
        stringResource(Res.string.theme_light)
    }
    val primaryColorText = remember(context.theme.primaryColor) { formatArgb(context.theme.primaryColor) }
    val shellHint = stringResource(Res.string.run_command_hint)
    val noShellPermission = stringResource(Res.string.no_shell_permission)
    val noShellPermissionDetail = stringResource(Res.string.no_shell_permission_detail)
    val errorResult = stringResource(Res.string.error_result)
    val runningText = stringResource(Res.string.run_command_running)
    val emptyOutput = stringResource(Res.string.run_command_empty)
    val successText = stringResource(Res.string.run_command_success)
    val failedText = stringResource(Res.string.run_command_failed)
    val settingsSavedText = stringResource(Res.string.settings_saved)
    val settingsReloadedText = stringResource(Res.string.settings_reloaded)
    val settingsClearedText = stringResource(Res.string.settings_cleared)
    val assetMissingText = stringResource(Res.string.asset_missing)
    val apiExtrasSnapshotEmpty = stringResource(Res.string.api_extras_snapshot_empty)
    val apiExtrasAssetEmpty = stringResource(Res.string.api_extras_asset_empty)
    val apiExtrasBinaryEmpty = stringResource(Res.string.api_extras_binary_empty)
    val apiExtrasSnapshotRefreshed = stringResource(Res.string.api_extras_snapshot_refreshed)
    val apiExtrasBinaryWritten = stringResource(Res.string.api_extras_binary_written)
    val apiExtrasBinaryMissing = stringResource(Res.string.api_extras_binary_missing)
    val projectReadPermissionNeeded = stringResource(Res.string.project_demo_read_permission_needed)
    val projectContentEmpty = stringResource(Res.string.project_demo_content_empty)
    val projectListingEmpty = stringResource(Res.string.project_demo_listing_empty)
    val projectRefreshed = stringResource(Res.string.project_demo_refreshed)
    val projectDirCreated = stringResource(Res.string.project_demo_dir_created)
    val projectFileWritten = stringResource(Res.string.project_demo_file_written)
    val projectFileAppended = stringResource(Res.string.project_demo_file_appended)
    val projectFileDeleted = stringResource(Res.string.project_demo_file_deleted)
    val projectFileMissing = stringResource(Res.string.project_demo_file_missing)
    val projectReadonlyUpdated = stringResource(Res.string.project_demo_readonly_updated)
    val projectWritableRestored = stringResource(Res.string.project_demo_writable_restored)
    val taskQueuedStatus = stringResource(Res.string.task_queued_status)
    val taskRunningStep1 = stringResource(Res.string.task_running_step_1)
    val taskRunningStep2 = stringResource(Res.string.task_running_step_2)
    val taskRunningStep3 = stringResource(Res.string.task_running_step_3)
    val taskDoneStatus = stringResource(Res.string.task_submitted_detail)
    val taskSubmittedMsg = stringResource(Res.string.task_submitted_snackbar)
    val taskPermissionNeeded = stringResource(Res.string.task_permission_needed)
    var shellOutput by remember(shellHint) { mutableStateOf(shellHint) }
    var shellRunning by remember { mutableStateOf(false) }
    var shellHasOutput by remember { mutableStateOf(false) }
    var shellPermissionGranted by remember { mutableStateOf(pluginContext.permissions.hasPermission("shell.execute")) }
    var projectReadGranted by remember { mutableStateOf(pluginContext.permissions.hasPermission("project.read")) }
    var projectWriteGranted by remember { mutableStateOf(pluginContext.permissions.hasPermission("project.write")) }
    var settingsNote by remember(pluginContext) { mutableStateOf(pluginContext.settings.getString("demo_note", "")) }
    var settingsStatus by remember { mutableStateOf("") }
    var apiExtrasStatus by remember { mutableStateOf(apiExtrasSnapshotEmpty) }
    var settingsSnapshot by remember { mutableStateOf(apiExtrasSnapshotEmpty) }
    var assetPreview by remember { mutableStateOf(apiExtrasAssetEmpty) }
    var binaryPreview by remember { mutableStateOf(apiExtrasBinaryEmpty) }
    var projectStatus by remember(currentProject, projectReadPermissionNeeded) {
        mutableStateOf(if (currentProject == null) projectEmptyText else projectReadPermissionNeeded)
    }
    var projectContent by remember(projectContentEmpty) { mutableStateOf(projectContentEmpty) }
    var projectFiles by remember { mutableStateOf(emptyList<String>()) }
    var taskStatus by remember { mutableStateOf(stringResource(Res.string.task_idle_status)) }
    var refreshTick by remember { mutableIntStateOf(0) }

    fun refreshPermissionStates() {
        shellPermissionGranted = pluginContext.permissions.hasPermission("shell.execute")
        projectReadGranted = pluginContext.permissions.hasPermission("project.read")
        projectWriteGranted = pluginContext.permissions.hasPermission("project.write")
    }

    suspend fun ensurePermission(permission: String): Boolean {
        val granted = pluginContext.permissions.hasPermission(permission) || context.requestPermission(permission)
        refreshPermissionStates()
        return granted
    }

    suspend fun refreshProjectSnapshot(requestPermission: Boolean) {
        if (currentProject == null) {
            projectFiles = emptyList()
            projectContent = projectContentEmpty
            projectStatus = projectEmptyText
            return
        }
        val canRead = if (pluginContext.permissions.hasPermission("project.read")) {
            true
        } else if (requestPermission) {
            ensurePermission("project.read")
        } else {
            false
        }
        if (!canRead) {
            projectFiles = emptyList()
            projectContent = projectContentEmpty
            projectStatus = projectReadPermissionNeeded
            return
        }
        runCatching {
            val files = pluginContext.projects.listFiles(
                projectName = currentProject,
                relativeDir = DemoRelativeDir,
                recursive = true
            )
            projectFiles = files
            projectContent = pluginContext.projects.readText(currentProject, DemoRelativeFile) ?: projectContentEmpty
            projectStatus = if (files.isEmpty()) projectListingEmpty else projectRefreshed
        }.onFailure { throwable ->
            val message = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
            projectFiles = emptyList()
            projectContent = message
            projectStatus = message
            context.showError(message, throwable)
        }
    }

    fun refreshSettingsSnapshot() {
        val settingsKeys = pluginContext.settings.keys().sorted()
        val lastRefresh = pluginContext.settings.getLong("demo_last_refresh", 0L)
        val scale = pluginContext.settings.getFloat("demo_scale", 0f)
        val hasNote = pluginContext.settings.contains("demo_note")
        settingsSnapshot = buildString {
            appendLine("keys=${if (settingsKeys.isEmpty()) "(empty)" else settingsKeys.joinToString()}")
            appendLine("contains(demo_note)=$hasNote")
            appendLine("demo_last_refresh=$lastRefresh")
            appendLine("demo_scale=$scale")
        }.trimEnd()
    }

    suspend fun refreshExtendedApiSnapshot() {
        pluginContext.settings.putLong("demo_last_refresh", System.currentTimeMillis())
        pluginContext.settings.putFloat("demo_scale", 1.25f)
        refreshSettingsSnapshot()
        assetPreview = pluginContext.resources.readTextAsset("greeting.txt")
            ?: assetMissingText
        binaryPreview = if (currentProject != null && projectReadGranted) {
            val bytes = pluginContext.projects.readBytes(currentProject, DemoBinaryFile)
            bytes?.let(::formatBinaryPreview) ?: apiExtrasBinaryEmpty
        } else {
            apiExtrasBinaryEmpty
        }
        apiExtrasStatus = apiExtrasSnapshotRefreshed
    }

    LaunchedEffect(currentProject, refreshTick) {
        refreshPermissionStates()
        refreshSettingsSnapshot()
        if (currentProject != null && projectReadGranted) {
            refreshProjectSnapshot(requestPermission = false)
        }
        runCatching {
            refreshExtendedApiSnapshot()
        }.onFailure { throwable ->
            apiExtrasStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
        }
    }

    val content: @Composable ColumnScope.() -> Unit = {
        if (!isFullscreenPage) {
            Text(
                text = stringResource(Res.string.hello_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = stringResource(Res.string.hello_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        DemoSectionCard(title = stringResource(Res.string.hello_title)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.load_count_label),
                    value = runCount.toString()
                )
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.platform_label),
                    value = platformName
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.current_project_label),
                    value = displayProject
                )
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.window_mode_label),
                    value = context.windowMode.name
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.theme_label),
                    value = themeModeText
                )
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.primary_color_label),
                    value = primaryColorText
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.permission_label),
                    value = if (shellPermissionGranted) {
                        stringResource(Res.string.permission_granted)
                    } else {
                        stringResource(Res.string.permission_denied)
                    }
                )
                DemoInfoTile(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.project_permission_label),
                    value = buildString {
                        append(if (projectReadGranted) stringResource(Res.string.permission_granted) else stringResource(Res.string.permission_denied))
                        append(" / ")
                        append(if (projectWriteGranted) stringResource(Res.string.permission_granted) else stringResource(Res.string.permission_denied))
                    }
                )
            }
        }

        DemoSectionCard(
            title = stringResource(Res.string.settings_section_title),
            description = stringResource(Res.string.settings_section_description)
        ) {
            OutlinedTextField(
                value = settingsNote,
                onValueChange = { settingsNote = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.settings_note_label)) }
            )
            if (settingsStatus.isNotBlank()) {
                StatusSurface(settingsStatus)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        pluginContext.settings.putString("demo_note", settingsNote)
                        refreshSettingsSnapshot()
                        settingsStatus = settingsSavedText
                        context.showMessage(settingsSavedText)
                    }
                ) {
                    Text(stringResource(Res.string.settings_save))
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        settingsNote = pluginContext.settings.getString("demo_note", "")
                        refreshSettingsSnapshot()
                        settingsStatus = settingsReloadedText
                    }
                ) {
                    Text(stringResource(Res.string.settings_reload))
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        pluginContext.settings.remove("demo_note")
                        refreshSettingsSnapshot()
                        settingsNote = ""
                        settingsStatus = settingsClearedText
                    }
                ) {
                    Text(stringResource(Res.string.settings_clear))
                }
            }
        }

        DemoSectionCard(
            title = stringResource(Res.string.api_extras_section_title),
            description = stringResource(Res.string.api_extras_section_description)
        ) {
            StatusSurface(
                label = stringResource(Res.string.api_extras_status_label),
                value = apiExtrasStatus
            )
            StatusSurface(
                label = stringResource(Res.string.api_extras_settings_snapshot_label),
                value = settingsSnapshot
            )
            StatusSurface(
                label = stringResource(Res.string.api_extras_asset_preview_label),
                value = assetPreview
            )
            StatusSurface(
                label = stringResource(Res.string.api_extras_binary_preview_label),
                value = binaryPreview
            )
            ActionButtonRow(
                primaryText = stringResource(Res.string.api_extras_refresh),
                primaryAction = {
                    scope.launch {
                        runCatching {
                            refreshExtendedApiSnapshot()
                        }.onFailure { throwable ->
                            apiExtrasStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(apiExtrasStatus, throwable)
                        }
                    }
                },
                secondaryText = stringResource(Res.string.api_extras_read_asset),
                secondaryAction = {
                    scope.launch {
                        runCatching {
                            assetPreview = pluginContext.resources.readTextAsset("greeting.txt")
                                ?: assetMissingText
                            apiExtrasStatus = apiExtrasSnapshotRefreshed
                        }.onFailure { throwable ->
                            apiExtrasStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(apiExtrasStatus, throwable)
                        }
                    }
                }
            )
            ActionButtonRow(
                primaryText = stringResource(Res.string.api_extras_write_binary),
                primaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            apiExtrasStatus = projectEmptyText
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            apiExtrasStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            pluginContext.projects.writeBytes(
                                projectName = currentProject,
                                relativePath = DemoBinaryFile,
                                content = buildDemoBinaryContent(displayProject)
                            )
                            binaryPreview = formatBinaryPreview(
                                pluginContext.projects.readBytes(currentProject, DemoBinaryFile) ?: byteArrayOf()
                            )
                            apiExtrasStatus = apiExtrasBinaryWritten
                        }.onFailure { throwable ->
                            apiExtrasStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(apiExtrasStatus, throwable)
                        }
                    }
                },
                secondaryText = stringResource(Res.string.api_extras_read_binary),
                secondaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            apiExtrasStatus = stringResource(Res.string.project_empty)
                            return@launch
                        }
                        if (!ensurePermission("project.read")) {
                            apiExtrasStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            val exists = pluginContext.projects.exists(currentProject, DemoBinaryFile)
                            val bytes = pluginContext.projects.readBytes(currentProject, DemoBinaryFile)
                            binaryPreview = if (exists && bytes != null) {
                                formatBinaryPreview(bytes)
                            } else {
                                apiExtrasBinaryMissing
                            }
                            apiExtrasStatus = apiExtrasSnapshotRefreshed
                        }.onFailure { throwable ->
                            apiExtrasStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(apiExtrasStatus, throwable)
                        }
                    }
                }
            )
        }

        DemoSectionCard(
            title = stringResource(Res.string.project_demo_section_title),
            description = stringResource(Res.string.project_demo_description)
        ) {
            Text(
                text = stringResource(Res.string.project_demo_path_label) + DemoRelativeFile,
                style = MaterialTheme.typography.bodySmall
            )
            StatusSurface(
                label = stringResource(Res.string.project_demo_status_label),
                value = projectStatus
            )
            StatusSurface(
                label = stringResource(Res.string.output_label),
                value = projectContent
            )
            if (projectFiles.isEmpty()) {
                Text(
                    text = projectListingEmpty,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    projectFiles.forEach { path ->
                        Text(
                            text = path,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            ActionButtonRow(
                primaryText = stringResource(Res.string.project_demo_refresh),
                primaryAction = {
                    scope.launch {
                        refreshProjectSnapshot(requestPermission = true)
                    }
                },
                secondaryText = stringResource(Res.string.project_demo_create_dir),
                secondaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            projectStatus = projectEmptyText
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            projectStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            pluginContext.projects.createDirectory(currentProject, DemoRelativeDir)
                            projectStatus = projectDirCreated
                            refreshProjectSnapshot(requestPermission = true)
                        }.onFailure { throwable ->
                            projectStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(projectStatus, throwable)
                        }
                    }
                }
            )
            ActionButtonRow(
                primaryText = stringResource(Res.string.project_demo_write_file),
                primaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            projectStatus = stringResource(Res.string.project_empty)
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            projectStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            pluginContext.projects.writeText(
                                projectName = currentProject,
                                relativePath = DemoRelativeFile,
                                content = buildDemoFileContent(displayProject, themeModeText, settingsNote)
                            )
                            projectStatus = projectFileWritten
                            refreshProjectSnapshot(requestPermission = true)
                        }.onFailure { throwable ->
                            projectStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(projectStatus, throwable)
                        }
                    }
                },
                secondaryText = stringResource(Res.string.project_demo_append_file),
                secondaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            projectStatus = stringResource(Res.string.project_empty)
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            projectStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            pluginContext.projects.writeText(
                                projectName = currentProject,
                                relativePath = DemoRelativeFile,
                                content = "\nAppended at ${System.currentTimeMillis()}",
                                append = true
                            )
                            projectStatus = projectFileAppended
                            refreshProjectSnapshot(requestPermission = true)
                        }.onFailure { throwable ->
                            projectStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(projectStatus, throwable)
                        }
                    }
                }
            )
            ActionButtonRow(
                primaryText = stringResource(Res.string.project_demo_read_file),
                primaryAction = {
                    scope.launch {
                        refreshProjectSnapshot(requestPermission = true)
                    }
                },
                secondaryText = stringResource(Res.string.project_demo_delete_file),
                secondaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            projectStatus = stringResource(Res.string.project_empty)
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            projectStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            val deleted = pluginContext.projects.delete(currentProject, DemoRelativeFile)
                            projectStatus = if (deleted) projectFileDeleted else projectFileMissing
                            refreshProjectSnapshot(requestPermission = true)
                        }.onFailure { throwable ->
                            projectStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(projectStatus, throwable)
                        }
                    }
                }
            )
            ActionButtonRow(
                primaryText = stringResource(Res.string.project_demo_make_readonly),
                primaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            projectStatus = stringResource(Res.string.project_empty)
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            projectStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            val updated = pluginContext.projects.setPermissions(
                                projectName = currentProject,
                                relativePath = DemoRelativeFile,
                                readable = true,
                                writable = false
                            )
                            projectStatus = if (updated) projectReadonlyUpdated else projectFileMissing
                            refreshProjectSnapshot(requestPermission = true)
                        }.onFailure { throwable ->
                            projectStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(projectStatus, throwable)
                        }
                    }
                },
                secondaryText = stringResource(Res.string.project_demo_restore_writable),
                secondaryAction = {
                    scope.launch {
                        if (currentProject == null) {
                            projectStatus = stringResource(Res.string.project_empty)
                            return@launch
                        }
                        if (!ensurePermission("project.write")) {
                            projectStatus = projectReadPermissionNeeded
                            return@launch
                        }
                        runCatching {
                            val updated = pluginContext.projects.setPermissions(
                                projectName = currentProject,
                                relativePath = DemoRelativeFile,
                                readable = true,
                                writable = true
                            )
                            projectStatus = if (updated) projectWritableRestored else projectFileMissing
                            refreshProjectSnapshot(requestPermission = true)
                        }.onFailure { throwable ->
                            projectStatus = errorResult.format(throwable.message ?: throwable.javaClass.simpleName)
                            context.showError(projectStatus, throwable)
                        }
                    }
                }
            )
        }

        DemoSectionCard(
            title = stringResource(Res.string.run_uname),
            description = stringResource(Res.string.run_command_description)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.command_label),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = command.joinToString(" "),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (shellRunning) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            StatusSurface(
                label = stringResource(Res.string.output_label),
                value = shellOutput
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    enabled = !shellRunning,
                    onClick = {
                        scope.launch {
                            if (!ensurePermission("shell.execute")) {
                                shellOutput = noShellPermissionDetail
                                shellHasOutput = true
                                context.showError(noShellPermission)
                                return@launch
                            }
                            shellRunning = true
                            shellHasOutput = true
                            shellOutput = runningText
                            runCatching {
                                pluginContext.shell.execute(command)
                            }.onSuccess { result ->
                                shellOutput = if (result.exitCode == 0) {
                                    result.stdout.trim().ifBlank { emptyOutput }
                                } else {
                                    errorResult.format(result.stderr.ifBlank { "$failedText (${result.exitCode})" })
                                }
                                if (result.exitCode == 0) {
                                    context.showMessage(successText)
                                    pluginContext.logger.info("system info: $shellOutput")
                                } else {
                                    context.showError(shellOutput)
                                    pluginContext.logger.warning("system info failed: $shellOutput")
                                }
                                shellRunning = false
                            }.onFailure { throwable ->
                                shellOutput = errorResult.format(
                                    throwable.message ?: throwable.javaClass.simpleName
                                )
                                context.showError(failedText, throwable)
                                pluginContext.logger.error("system info failed", throwable)
                                shellRunning = false
                            }
                        }
                    }
                ) {
                    Text(stringResource(Res.string.run_uname))
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = shellHasOutput && !shellRunning,
                    onClick = {
                        shellOutput = shellHint
                        shellHasOutput = false
                    }
                ) {
                    Text(stringResource(Res.string.clear_output))
                }
            }
        }

        DemoSectionCard(
            title = stringResource(Res.string.task_section_title),
            description = stringResource(Res.string.task_section_description)
        ) {
            StatusSurface(value = taskStatus)
            FilledTonalButton(
                onClick = {
                    scope.launch {
                        if (!ensurePermission("task.background")) {
                            taskStatus = taskPermissionNeeded
                            context.showError(taskPermissionNeeded)
                            return@launch
                        }
                        taskStatus = taskQueuedStatus
                        context.runTask("HelloPlugin: demo task") {
                            taskStatus = taskRunningStep1
                            updateProgress(0f, "Step 1/3: Preparing...")
                            kotlinx.coroutines.delay(500.milliseconds)
                            checkCancelled()
                            taskStatus = taskRunningStep2
                            updateProgress(0.5f, "Step 2/3: Processing...")
                            kotlinx.coroutines.delay(500.milliseconds)
                            checkCancelled()
                            taskStatus = taskRunningStep3
                            log("Step 3/3: Done!")
                            updateProgress(1f, "Done")
                            taskStatus = taskDoneStatus
                        }
                        context.showMessage(taskSubmittedMsg)
                    }
                }
            ) {
                Text(stringResource(Res.string.submit_task))
            }
        }

        if (!isFullscreenPage) {
            OutlinedButton(onClick = { context.close() }) {
                Text(stringResource(Res.string.close))
            }
        }
    }

    if (isFullscreenPage) {
        Scaffold(
            topBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 3.dp,
                    shadowElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(onClick = { context.close() }) {
                            Text(stringResource(Res.string.close))
                        }
                        Column {
                            Text(
                                text = stringResource(Res.string.hello_title),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(Res.string.window_mode_detail),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
private fun DemoSectionCard(
    title: String,
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (!description.isNullOrBlank()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                content()
            }
        )
    }
}

@Composable
private fun DemoInfoTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun StatusSurface(
    label: String? = null,
    value: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!label.isNullOrBlank()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ActionButtonRow(
    primaryText: String,
    primaryAction: () -> Unit,
    secondaryText: String,
    secondaryAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalButton(
            modifier = Modifier.weight(1f),
            onClick = primaryAction
        ) {
            Text(primaryText)
        }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = secondaryAction
        ) {
            Text(secondaryText)
        }
    }
}

private fun buildDemoFileContent(
    projectName: String,
    themeMode: String,
    savedNote: String
): String {
    return buildString {
        appendLine("ImageStudio plugin demo file")
        appendLine("project=$projectName")
        appendLine("theme=$themeMode")
        appendLine("timestamp=${System.currentTimeMillis()}")
        if (savedNote.isNotBlank()) {
            appendLine("note=$savedNote")
        }
    }
}

private fun buildDemoBinaryContent(projectName: String): ByteArray {
    return "binary-demo:$projectName:${System.currentTimeMillis()}".encodeToByteArray()
}

private fun formatBinaryPreview(bytes: ByteArray): String {
    if (bytes.isEmpty()) return "0 B"
    val preview = bytes.take(16).joinToString(" ") { byte ->
        byte.toUByte().toString(16).uppercase().padStart(2, '0')
    }
    return "${bytes.size} B | $preview"
}

private fun formatArgb(argb: Long): String {
    return "#" + argb.toString(16).uppercase().padStart(8, '0')
}

private fun demoSystemInfoCommand(platform: PluginPlatform): List<String> {
    return when (platform) {
        PluginPlatform.Windows -> listOf("cmd", "/c", "ver")
        PluginPlatform.MacOS, PluginPlatform.Linux -> listOf("uname", "-a")
        PluginPlatform.Android -> listOf("getprop", "ro.build.version.release")
    }
}

private fun demoPlatformLabel(platform: PluginPlatform): String {
    return when (platform) {
        PluginPlatform.Android -> "Android"
        PluginPlatform.Windows -> "Windows"
        PluginPlatform.MacOS -> "macOS"
        PluginPlatform.Linux -> "Linux"
    }
}
