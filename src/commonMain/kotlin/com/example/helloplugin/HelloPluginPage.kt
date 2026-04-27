package com.example.helloplugin

import androidx.compose.runtime.Composable
import com.suqi8.imagestudio.plugin.api.ImageStudioPlugin
import com.suqi8.imagestudio.plugin.api.PluginContext
import com.suqi8.imagestudio.plugin.api.PluginUiContext

/**
 * 备用全屏入口。
 *
 * 和 [HelloPlugin] 一样，直接复用统一的示例界面，避免两套示例实现分叉。
 */
class HelloPluginPage : ImageStudioPlugin {

    override fun onLoad(context: PluginContext) {
        context.logger.info("HelloPluginPage onLoad")
    }

    @Composable
    override fun Content(context: PluginUiContext) {
        HelloPluginDemoScreen(context)
    }
}
