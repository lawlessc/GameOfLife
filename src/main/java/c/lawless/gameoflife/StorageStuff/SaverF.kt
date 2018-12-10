package c.lawless.gameoflife.StorageStuff

import android.graphics.Bitmap
import c.lawless.gameoflife.statics.TextureNames
import com.threed.jpct.FrameBuffer
import com.threed.jpct.TextureManager
import com.threed.jpct.World
import java.io.ByteArrayOutputStream
import java.nio.IntBuffer


fun frameSaver(fb :FrameBuffer ,world: World)
{

 //var texture_to_save=  TextureManager.getInstance().getTexture(TextureNames.frameone)


    fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.savetexture))
    world.renderScene(fb)
    world.draw(fb)
    fb.display()
    fb.removeRenderTarget()

    var bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    var vec= fb.getPixels()
    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));


    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    bitmap.recycle()

    //Get ID and name etc here then save object to box.
 //   saveObj = GOFSave()


}

fun store(fb :FrameBuffer, world: World) {


}