package com.example.myapplication

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri

// SongParser.kt
object SongParser {
    fun parseRawSong(context: Context, resId: Int, index: Int): Song {
        val retriever = MediaMetadataRetriever()
        // 获取 raw 文件的 Uri
        val uri = Uri.parse("android.resource://${context.packageName}/$resId")

        return try {
            retriever.setDataSource(context, uri)

            // 1. 提取元数据
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "未知歌曲"
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "未知歌手"
            val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L

            // 2. 格式化时长 (ms -> mm:ss)
            val seconds = (durationMs / 1000) % 60
            val minutes = (durationMs / 1000) / 60
            val durationStr = String.format("%02d:%02d", minutes, seconds)

            // 3. 提取封面图 (可选)
            val art = retriever.embeddedPicture
            val bitmap = if (art != null) BitmapFactory.decodeByteArray(art, 0, art.size) else null

            Song(index, title, artist, durationStr, resId, bitmap)
        } catch (e: Exception) {
            Song(index, "解析失败", "未知", "00:00", resId)
        } finally {
            retriever.release()
        }
    }
}