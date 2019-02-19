package c.lawless.gameoflife.StorageStuff

import android.graphics.Bitmap
import c.lawless.gameoflife.MainActivity
import c.lawless.gameoflife.StorageStuff.ObjectBox.boxStore
import c.lawless.gameoflife.statics.GridSizes
import c.lawless.gameoflife.statics.TextureNames
import io.objectbox.kotlin.boxFor
import java.io.ByteArrayOutputStream
import java.nio.IntBuffer
import android.graphics.BitmapFactory
import com.threed.jpct.*
import com.threed.jpct.util.BitmapHelper
import java.nio.ByteBuffer
//import com.sun.deploy.trace.Trace.flush
import java.io.DataOutputStream
import java.nio.ByteOrder
import java.nio.ByteOrder.BIG_ENDIAN
import javax.microedition.khronos.opengles.GL11


/*Christopher Lawless 2018/2019 */


fun frameSaver(fb :FrameBuffer ,world: World)
{



    //We render to the save texture here
    fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.savetexture))
    world.renderScene(fb)
    world.draw(fb)
    fb.display()
    fb.removeRenderTarget()

    //retrieve rendered to texture
    var texture_to_save=  TextureManager.getInstance().getTexture(TextureNames.savetexture)

     //create a bitmap of the image
    //var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()

   var byteArray=   convertIntegersToBytes(vec);
//    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));
//
//
//    //Convert the bitmap to a byte array
//    val stream = ByteArrayOutputStream()
//    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//    val byteArray = stream.toByteArray()
//    bitmap.recycle()


    //Save the byte array to a boxstore , useing current date time as a filename.
    val box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0, java.util.Calendar.getInstance().toString(),
        GridSizes.size_level,
        byteArray,
        texture_to_save.width,
        texture_to_save.height))
    System.out.println("SAVER CALLED")

}






fun frameTestLoader(fb :FrameBuffer ,world: World)
{
    //Save the byte array to a boxstore , useing current date time as a filename.
   val box = boxStore.boxFor<GOFSave>()
   val  savefile = box.get(1)
   val vec= savefile.savedImage

   val fin = convertBytestoIntegers(vec)

   doBlit(fb,fin,savefile.width, savefile.height);
}




fun doBlit(fb: FrameBuffer, tex : IntArray, width: Int, height: Int) {

  //fb.b
   // GL11.glEnable(GL11.GL_DEPTH_TEST)
  //  GL11.


    fb.resize(width, height)
    fb.blit(tex ,width,height, 0, 0, 0,0,
        width, height,
        false)//if set to true the blit overlays the previous screen.


}


fun convertIntegersToBytes(integers: IntArray?): ByteArray? {
    if (integers != null) {
        val outputBytes = ByteArray(integers.size * 4)

        var i = 0
        var k = 0
        while (i < integers.size) {
            val integerTemp = integers[i]
            var j = 0
            while (j < 4) {
                outputBytes[k] = (integerTemp shr 8 * j and 0xFF).toByte()
                j++
                k++
            }
            i++
        }
        return outputBytes
    } else {
        return null
    }
}

fun convertBytestoIntegers( bytes : ByteArray? ): IntArray
{

    val intBuf = ByteBuffer.wrap(bytes)
        .order(ByteOrder.LITTLE_ENDIAN)
        .asIntBuffer()
    val array = IntArray(intBuf.remaining())
    intBuf.get(array)

    return array
}