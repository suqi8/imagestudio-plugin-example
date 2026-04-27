package com.example.helloplugin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.suqi8.imagestudio.plugin.api.HostThemeInfo

@Composable
internal fun PluginDemoTheme(
    theme: HostThemeInfo,
    content: @Composable () -> Unit
) {
    val primary = Color(theme.primaryColor)
    val colorScheme = if (theme.isDarkTheme) {
        darkColorScheme(primary = primary)
    } else {
        lightColorScheme(primary = primary)
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
