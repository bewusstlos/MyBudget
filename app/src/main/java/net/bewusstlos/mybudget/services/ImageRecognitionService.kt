package net.bewusstlos.mybudget.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.googlecode.tesseract.android.TessBaseAPI
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream


/**
 * Created by bewusstlos on 10/23/2017.
 */
class ImageRecognitionService {
    companion object {
        fun getReceiptItems(imageFile: File): String? {
            val patterns = arrayOf(
                    Regex("(.*?)грн\\."),
                    Regex("(.*)\\s(\\d*[.|,]\\d2)[\\s|-][А|БГ]"),
                    Regex("(.*)\\s(\\d*\\s[x|X]\\s\\d[.|,]\\d*\\s=\\s.*)"),
                    Regex("(.*?)\\d*?\\s.*?\\s?.*?=(.*?)\\sгрн.\\n") // Ferma rozdacha pattern
            )
            val recognizedText = recognizePhoto(imageFile)
            for (pattern in patterns) {
                val s = pattern.findAll(recognizedText.toString()).toList()
                null
            }
            return recognizedText
        }

        fun recognizePhoto(imageFile: File? = null, bitmap: Bitmap? = null): String? {
            val bmpOptions = BitmapFactory.Options()
            bmpOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
            var bmp: Bitmap
            if (bitmap == null)
                bmp = BitmapFactory.decodeFile(imageFile?.path, bmpOptions)
            else
                bmp = bitmap

            bmp = denoiseImage(bmp)
            val tessBaseApi = TessBaseAPI()
            val tessData = File("${Environment.getExternalStorageDirectory()}")
            if (!tessData.exists())
                tessData.mkdir()
            tessBaseApi.init(tessData.absolutePath, "ukr")
            tessBaseApi.setImage(bmp)
            val res = tessBaseApi.utF8Text
            Log.d("Text segment", res)
            return res
        }

        fun denoiseImage(bitmap: Bitmap): Bitmap {
            var src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
            Utils.bitmapToMat(bitmap, src)

            var grey: Mat = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
            Imgproc.cvtColor(src, grey, Imgproc.COLOR_RGB2GRAY)
            val resizeFactor = grey.width() / 2000.0
            Imgproc.resize(grey, grey, Size(grey.width() / resizeFactor, grey.height() / resizeFactor))
            Imgproc.GaussianBlur(grey, grey, Size(11.0, 11.0), 0.0)


            //var sobel = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
            //Imgproc.Sobel(grey, sobel, CvType.CV_8U, 1,0,3,1.0,0.0, Imgproc.BORDER_DEFAULT)


            var thresholded = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
            Imgproc.adaptiveThreshold(grey, thresholded, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 5.0)

            //Imgproc.adaptiveThreshold(grey, thresholded, 250.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 10.0)
            //var thresholdedWithoutEx = thresholded
            //Imgproc.morphologyEx(thresholded, thresholded, Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(3.0,3.0)))
            saveImage("thresholded", thresholded)
            val bmp = Bitmap.createBitmap(thresholded.width(), thresholded.height(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(thresholded, bmp)
            return bmp
            /*
            val contours = ArrayList<MatOfPoint>()
            val hierarchy = Mat()
            Imgproc.findContours(thresholded, contours, hierarchy, 0, 1)
            val contoursPoly = ArrayList<MatOfPoint>(contours.size)

            val imgWithRects = thresholded
            for(contour in contours){
                val mMOP2f1 = MatOfPoint2f()
                val mMOP2f2 = MatOfPoint2f()

                contour.convertTo(mMOP2f1, CvType.CV_32FC2)
                Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 2.0, true)
                mMOP2f2.convertTo(contour, CvType.CV_32S)

                val appRect = Imgproc.boundingRect(contour)
                if (appRect.width > appRect.height) {
                    Core.rectangle(imgWithRects, Point(
                            appRect.x.toDouble(), appRect.y.toDouble()),
                            Point(appRect.x.toDouble() + appRect.width, appRect.y + appRect.height.toDouble()),
                            Scalar(255.0, 0.0, 0.0))
                    Log.i(null, "cut Roi")
                    val roiMat = Mat(src, appRect)
                    Log.i(null, "OCR and puttext")
                    var bmpRect: Bitmap? = Bitmap.createBitmap(roiMat.width(),roiMat.height(), Bitmap.Config.ARGB_4444)
                    Utils.matToBitmap(roiMat, bmpRect)
                }
            }*/

            //saveImage("sobel", sobel)
            //saveImage("thresholded_EX", thresholdedWithoutEx)

        }

        fun saveImage(fileName: String, mat: Mat) {
            var bitmap: Bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, bitmap)
            val processedImage = File("${Environment.getExternalStorageDirectory()}/$fileName.png")
            val stream = FileOutputStream(processedImage)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
    }
}