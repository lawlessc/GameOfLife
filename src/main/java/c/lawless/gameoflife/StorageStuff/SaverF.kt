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
    var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()
    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));


    //Convert the bitmap to a byte array
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    bitmap.recycle()


    //Save the byte array to a boxstore , useing current date time as a filename.
    val box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0, java.util.Calendar.getInstance().toString(), GridSizes.size_level, byteArray))


}

fun frameTestSaver(fb :FrameBuffer ,world: World)
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
    var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()
    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));


    //Convert the bitmap to a byte array
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    bitmap.recycle()


    //Save the byte array to a boxstore , useing current date time as a filename.
    val box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0, "test", GridSizes.size_level, byteArray))


}





fun frameTestLoader(fb :FrameBuffer ,world: World)
{
//    //We render to the save texture here
//    fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.savetexture))
//    world.renderScene(fb)
//    world.draw(fb)
//    fb.display()
//    fb.removeRenderTarget()
//
//    //retrieve rendered to texture
//    var texture_to_save=  TextureManager.getInstance().getTexture(TextureNames.savetexture)
//
//    //create a bitmap of the image
//    var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
//    var vec= fb.getPixels()
//    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));
//
//
//    //Convert the bitmap to a byte array
//    val stream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//    val byteArray = stream.toByteArray()
//    bitmap.recycle()


    //Save the byte array to a boxstore , useing current date time as a filename.
    val box = boxStore.boxFor<GOFSave>()
    //box.put(GOFSave(0, "testname", GridSizes.size_level, byteArray))


   val  savefile = box.get(0)
   val byteArray= savefile.savedImage


    val image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    image.allocationByteCount


    var tex = Texture(image) as NPOTTexture;//very good ods this will fail if texture is not square
                                        //consider changing to a a square framebuffer for all processing
                                       //display can still be in screen size


    //todo when file is loaded all textures in Process handler to tbe resized




}




fun doBlit(fb: FrameBuffer, tex : NPOTTexture) {

    fb.resize(tex.getWidth(), tex.getHeight())
    fb.blit(
        tex, 0, 0, 0, fb.height,
        tex.getWidth(), tex.getHeight(), fb.width, -fb.height, 100, true, null
    )


}