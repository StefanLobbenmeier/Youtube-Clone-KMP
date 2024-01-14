package org.company.app

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.awt.Desktop
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.Locale
import javax.swing.JOptionPane

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}


@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    //VideoPlayerFFMpeg(modifier = modifier, file = url.toString())
    /* Box(
         modifier = modifier,
         contentAlignment = Alignment.Center
     ) {
         val image: Resource<androidx.compose.ui.graphics.painter.Painter> =
             asyncPainterResource(data = thumbnail.toString())
         KamelImage(
             resource = image,
             modifier = modifier.fillMaxWidth(),
             contentDescription = null
         )
         CircularProgressIndicator()
     }*/
    val videoId = splitLinkForVideoId(url.toString())
    DesktopWebView(modifier, "https://www.youtube.com/embed/$videoId")
}

fun splitLinkForVideoId(
    url: String?
): String {
    return (url!!.split("="))[1]
}

private fun openYouTubeVideo(videoUrl: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI(videoUrl))
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal actual fun Notify(message: String) {
    if (SystemTray.isSupported()) {
        val tray = SystemTray.getSystemTray()
        val image = Toolkit.getDefaultToolkit().createImage("logo.webp")
        val trayIcon = TrayIcon(image, "Desktop Notification")
        tray.add(trayIcon)
        trayIcon.displayMessage("Desktop Notification", message, TrayIcon.MessageType.INFO)
    } else {
        // Fallback for systems that don't support SystemTray
        JOptionPane.showMessageDialog(
            null,
            message,
            "Desktop Notification",
            JOptionPane.INFORMATION_MESSAGE
        )
    }
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI(videoUrl))
    }
}

@Composable
internal actual fun ShortsVideoPlayer(url: String?) {
    val videoId = splitLinkForVideoId(url.toString())
    DesktopWebView(
        modifier = Modifier.fillMaxWidth(),
        "https://www.youtube.com/embed/$videoId"
    )
}

internal actual fun UserRegion(): String {
    val currentLocale: Locale = Locale.getDefault()
    return currentLocale.country
}

@Composable
internal actual fun isConnected(): Boolean {
    return try {
        val url = URL("https://youtube.com")
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 30000
        connection.connect()
        connection.responseCode == 200
    } catch (e: Exception) {
        false
    }
}