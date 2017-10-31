package net.bewusstlos.mybudget.services

import android.app.Activity
import android.os.Environment
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.File

/**
 * Created by bewusstlos on 10/23/2017.
 */
class FileLoaderService {
    companion object {
        val TESS_DATA_FILE_NAME = "ukr.traineddata"
        fun getTessData(activity: Activity): Boolean {
            val tessDataRef = FirebaseStorage.getInstance().getReference(TESS_DATA_FILE_NAME)
            val tsc = TaskCompletionSource<Boolean>()

            val file = File("${Environment.getExternalStorageDirectory()}/tessdata/$TESS_DATA_FILE_NAME")

            tessDataRef.getFile(file).addOnSuccessListener { downloadTask ->
                Log.e("TessData", "Successfully downloaded")
                file.setLastModified(System.currentTimeMillis())
                tsc.setResult(true)

            }.addOnFailureListener({
                OnFailureListener { error ->
                    Log.e("TessData", error.message)
                    tsc.setResult(false)
                }
            }).addOnProgressListener { progress ->
                Log.e("TessData downloading: ", "${progress.bytesTransferred}/${progress.totalByteCount}")
            }

            var t = tsc.task
            try {
                Tasks.await(t)
            } catch (e: Exception) {
                t = Tasks.forException(e)
            }
            if (t.isSuccessful)
                return t.result
            else
                return false
        }

        fun shouldUpdateTessData(): Boolean {
            val tessData = File("${Environment.getExternalStorageDirectory()}/tessdata/$TESS_DATA_FILE_NAME")
            if (!tessData.exists())
                return true
            val metadata = getFileMetadata()
            if (metadata != null) {
                if (tessData.lastModified() < metadata.creationTimeMillis)
                    return true
            }
            return false
        }

        fun getFileMetadata(): StorageMetadata? {
            val tcs = TaskCompletionSource<StorageMetadata>()
            val tessDataRef = FirebaseStorage.getInstance().getReference(TESS_DATA_FILE_NAME)
            tessDataRef.metadata.addOnSuccessListener {
                tcs.trySetResult(it)
            }.addOnFailureListener {
                tcs.setException(it)
            }

            var t = tcs.task

            try {
                Tasks.await(t)
            } catch (e: Exception) {
                t = Tasks.forException(e)
            }

            if (t.isSuccessful)
                return t.result
            else
                return null
        }
    }
}