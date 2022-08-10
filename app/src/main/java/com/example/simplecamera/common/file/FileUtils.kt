package com.example.simplecamera.common.file

import android.content.Context
import androidx.camera.core.ImageCapture
import java.io.File
import javax.inject.Inject

class FileUtils @Inject constructor(
    private val context: Context
) {

    private val myDirectoryName = "my_photos"
    private val fileExtension = ".jpg"

    fun createOutputFileOptions(): ImageCapture.OutputFileOptions {
        val directoryName = createMyPhotosDirectory()
        val outPutFileName = getOutPutFileName(directoryName)

        return ImageCapture.OutputFileOptions
            .Builder(
                File(outPutFileName)
            )
            .build()
    }


    private fun createMyPhotosDirectory(): String {
        val dirName = getFileDirectoryName()
        val dir = File(dirName)
        dir.mkdir()
        return dirName
    }

    private fun getFileDirectoryName(): String =
        context.filesDir.absolutePath + File.separator + myDirectoryName + File.separator

    private fun getOutPutFileName(directoryName: String): String =
        directoryName + System.currentTimeMillis().toString() + fileExtension


}