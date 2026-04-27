package com.example.helloplugin

import androidx.compose.runtime.Composable
import com.suqi8.imagestudio.plugin.api.ImageStudioPlugin
import com.suqi8.imagestudio.plugin.api.PluginContext
import com.suqi8.imagestudio.plugin.api.PluginUiContext

/**
 * HelloPlugin 入口。
 *
 * UI 演示实现位于 `HelloPluginSupport.kt`，这样入口类只负责生命周期与委托，
 * 便于在 `customPage = false/true` 两种模式下复用同一套示例界面。
 */
class HelloPlugin : ImageStudioPlugin {

    override fun onLoad(context: PluginContext) {
        context.logger.info("HelloPlugin onLoad — id=${context.pluginId}")
        val count = context.settings.getInt("run_count", 0) + 1
        context.settings.putInt("run_count", count)
        context.logger.debug("Loaded $count time(s)")
    }

    override fun onEnable(context: PluginContext) {
        context.logger.info("HelloPlugin onEnable")
    }

    override fun onDisable(context: PluginContext) {
        context.logger.info("HelloPlugin onDisable")
    }

    override fun onUnload() {
    }

    @Composable
    override fun Content(context: PluginUiContext) {
        HelloPluginDemoScreen(context)
    }
}
