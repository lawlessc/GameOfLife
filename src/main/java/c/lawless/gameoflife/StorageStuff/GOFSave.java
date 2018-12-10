package c.lawless.gameoflife.StorageStuff;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

import java.io.ByteArrayOutputStream;

@Entity
public class GOFSave {

    @Id
    public long id;

    String saveName;
    int  size;


    Bitmap savedgImage;
   byte[] savedImage;



    public  GOFSave(long id, String saveName,int  size,  byte[] savedImage )
    {
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        bmp.recycle();


        this.id = id;
        this.saveName= saveName;
        this.size = size;
        this.savedImage = savedImage;
    }



    public Bitmap getImage()
    {

        return BitmapFactory.decodeByteArray(savedImage, 0, savedImage.length);
    }

}
