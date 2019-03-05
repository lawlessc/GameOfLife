package c.lawless.gameoflife.StorageStuff

import c.lawless.gameoflife.StorageStuff.ObjectBox.boxStore
import io.objectbox.kotlin.boxFor

import com.threed.jpct.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/*Christopher Lawless 2018/2019 */
fun frameSaver(fb :FrameBuffer, width: Int, height :Int)
{
   var vec= fb.getPixels()
   var byteArray=   convertIntegersToBytes(vec)
    //We get date time for title, for now
    var now = LocalDateTime.now()
    var time_now = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC))
    //Save the byte array to a boxstore , useing current date time as a filename.
    var box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0, time_now, byteArray, width,height))
}

fun frameConversionTest(fb :FrameBuffer, width: Int, height :Int)
{
    var vec= fb.getPixels()
    var byteArray=   convertIntegersToBytes(vec)
    val tex = convertBytestoIntegers(byteArray)
    compareArrays(vec,tex)
}

fun loadFile(id :Long):GOFSave
{
    var box = boxStore.boxFor<GOFSave>()
    var savefile = box.get(id)
    return  savefile
}

fun deleteAllFiles()
{
   boxStore.boxFor<GOFSave>().removeAll()
}

//https://stackoverflow.com/questions/1086054/how-to-convert-int-to-byte
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

//Got from http://www.java2s.com/Tutorial/Java/0180__File/ConvertBytearraytoInt.htm
fun convertBytestoIntegers( bytes : ByteArray? ): IntArray
{
    val intBuf = ByteBuffer.wrap(bytes)
        .order(ByteOrder.LITTLE_ENDIAN)
        .asIntBuffer()
    val array = IntArray(intBuf.remaining())
    intBuf.get(array)
    return array
}

//https://stackoverflow.com/questions/14897366/comparing-two-integer-arrays-in-java 
fun compareArrays(array1: IntArray?, array2: IntArray?) {
    var b = true
    if (array1 != null && array2 != null) {
        if (array1.size != array2.size)
            b = false
        else
            for (i in array2.indices) {
                if (array2[i] != array1[i]) {
                    b = false
                }
            }
    } else {
        b = false
    }
}