package c.lawless.gameoflife.statics;

import android.media.Image;
import com.threed.jpct.*;
import c.lawless.gameoflife.statics.TextureNames;

/* This is a singleton class i am making for holding the grid image when the user has left the app
   otherwise android loses the image*/

public class GridMemory {


    /* not sure how well this will work as it isn't java awt image */
    //  public static Image memory;

    // public NPOTTexture store;


    FrameBuffer fb ;
    World world;


    public GridMemory(FrameBuffer fb, World world)
    {
        this.fb=fb;
        this.world=world;
    }


    /*store image by rendering it to a texture */
    public void store()
    {
        fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.INSTANCE.getGridstore()));
        world.renderScene(fb);
        world.draw(fb);
        fb.display();
        fb.removeRenderTarget();
    }


    public void open()
    {
        fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.INSTANCE.getFrameone()));
        fb.blit(TextureManager.getInstance().getTexture("gridstore"),
                0,0,0,200,512,512,
                300,-200,-1,false);

        fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.INSTANCE.getFrametwo()));
        fb.blit(TextureManager.getInstance().getTexture("gridstore"),
                0,0,0,200,512,512,
                300,-200,-1,false);

    }


}
