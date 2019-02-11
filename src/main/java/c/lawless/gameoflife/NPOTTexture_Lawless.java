package c.lawless.gameoflife;

import android.graphics.Bitmap;
import com.threed.jpct.Logger;
import com.threed.jpct.Texture;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

public class NPOTTexture_Lawless extends Texture {
    private final int[] texels;

    public NPOTTexture_Lawless(Bitmap image) {

        super(image);
        super.texel ;


        this.texels = new int[this.width * this.height];
        Logger.log("Texture loaded..." + this.texels.length * 4 + " bytes/" + w + "*" + h + " pixels!", 2);
        image.getPixels(this.texels, 0, this.width, 0, 0, this.width, this.height);
        if (recycleMe) {
            image.recycle();
        }



    }


    
}
