package c.lawless.gameoflife;

import c.lawless.gameoflife.RenderHooks.GOLRenderHook;
import c.lawless.gameoflife.StorageStuff.GOFSave;
import c.lawless.gameoflife.StorageStuff.SaverFKt;
import c.lawless.gameoflife.statics.GridSizes;
import com.threed.jpct.*;

public class Processing {

    TextureManager tm = TextureManager.getInstance();

    Boolean textureCompression= false;//Setting this true will cause loading of saves to fail.
    Boolean textureFiltering= false;
    Boolean textureMipMap= false;
    Boolean setGLFiltering=false;


    public NPOTTexture frame_one;//Textures for alternating frames.
    public NPOTTexture frame_two;

    public Object3D fameObjOne = null;
    public Object3D fameObjTwo = null;


    GOLRenderHook GOF_Hook;
    public GLSLShader GOF_shader;

    public World displayWorld;

    //public  Boolean switcher= true;

    public boolean save=false;
    public boolean loadTest=false;
    public Long   load_id = null;



    public Processing(World world)
    {
       // this.GOF_shader= gof_shader;
        this.displayWorld= world;

        setupTextures();






    }


    public Processing(World world, long load_id)
    {
        //this.GOF_shader= gof_shader;
        this.displayWorld= world;
        this.load_id= load_id;
        this.loadTest=true;
       //  setupTextures();


        replaceTextures();


    }



    public void setupObjects(PostProcessHandler pp , GLSLShader GOF_shader)
    {
        GOF_Hook = new GOLRenderHook(pp,GOF_shader);

        fameObjOne = Primitives.getPlane(4,10);
        fameObjOne.setOrigin(new SimpleVector(0.01, 0, 0));
        fameObjOne.setRenderHook(GOF_Hook);
        fameObjOne.setShader(GOF_shader);
        TextureInfo one  =  new TextureInfo(TextureManager.getInstance().getTextureID("frameone"));
        one.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD);
        fameObjOne.setTexture(one);
        fameObjOne.setCulling(false);
        fameObjOne.compile();
        // advectingObj.strip();
        displayWorld.addObject(fameObjOne);
        fameObjOne.setVisibility(false);

        fameObjTwo = Primitives.getPlane(4,10);
        fameObjTwo.setOrigin(new SimpleVector(0.01, 0, 0));
        fameObjTwo.setRenderHook(GOF_Hook);
        fameObjTwo.setShader(GOF_shader);
        TextureInfo two  =  new TextureInfo(TextureManager.getInstance().getTextureID("frametwo"));
        two.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD);
        fameObjTwo.setTexture(two);
        fameObjTwo.setCulling(false);
        fameObjTwo.compile();
        displayWorld.addObject(fameObjTwo);
        fameObjTwo.setVisibility(false);


    }

    public void setupTextures()
    {
        frame_one = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        frame_one.setFiltering(textureFiltering);
        frame_one.setMipmap(textureMipMap);
        frame_one.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frameone", frame_one);

        frame_two = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        frame_two.setFiltering(textureFiltering);
        frame_two.setMipmap(textureMipMap);
        frame_two.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frametwo", frame_two);
    }




    public void process(FrameBuffer FB,boolean switcher)
    {



        if(switcher) {

            FB.setRenderTarget(frame_two);
            fameObjOne.setVisibility(true);
            FB.clear();
            displayWorld.renderScene(FB);
            displayWorld.draw(FB);
            FB.display();
            fameObjOne.setVisibility(false);
            save_Frame(FB);
            FB.removeRenderTarget();
        }
        else
        {
            FB.setRenderTarget(frame_one);
            fameObjTwo.setVisibility(true);
            FB.clear();
            displayWorld.renderScene(FB);
            displayWorld.draw(FB);
            FB.display();
            fameObjTwo.setVisibility(false);
            save_Frame(FB);
            FB.removeRenderTarget();
            load__test_Frame(FB, load_id);

        }

    }

    public void save_Frame(FrameBuffer fb) {
        if(save) {
            fb.resize(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight());
            SaverFKt.frameSaver(fb, GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight());
            save= false;
        }
    }


    public void load__test_Frame(FrameBuffer FB,Long id) {
        if(loadTest) {
            GOFSave save=   SaverFKt.loadFile(id);
            int[]  tex = SaverFKt.convertBytestoIntegers(save.getSavedImage());

            replaceTextures(save.getWidth(),save.getHeight());
            FB.setRenderTarget(frame_one);
            fameObjTwo.setVisibility(true);
            FB.clear();
            displayWorld.renderScene(FB);
            displayWorld.draw(FB);
            blitLoad(FB,tex, save.getWidth(), save.getHeight());
            FB.display();
            fameObjTwo.setVisibility(false);

            FB.removeRenderTarget();
            loadTest= false;
        }
    }



    public void replaceTextures()
    {

        NPOTTexture frame_one_ = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        frame_one_.setFiltering(textureFiltering);
        frame_one_.setMipmap(textureMipMap);
        frame_one_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.replaceTexture("frameone", frame_one_);

        NPOTTexture frame_two_ = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        frame_two_.setFiltering(textureFiltering);
        frame_two_.setMipmap(textureMipMap);
        frame_two_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.replaceTexture("frametwo", frame_two_);

    }

    public void replaceTextures(int width, int height)
    {
        NPOTTexture frame_one_ = new NPOTTexture(width , height, RGBColor.BLACK);
        frame_one_.setFiltering(textureFiltering);
        frame_one_.setMipmap(textureMipMap);
        frame_one_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.replaceTexture("frameone", frame_one_);

        NPOTTexture frame_two_ = new NPOTTexture(width , height, RGBColor.BLACK);
        frame_two_.setFiltering(textureFiltering);
        frame_two_.setMipmap(textureMipMap);
        frame_two_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.replaceTexture("frametwo", frame_two_);

        frame_one= frame_one_;
        frame_two= frame_two_;
    }


    public void blitLoad(FrameBuffer fb, int[]  tex,int width, int height )
    {
        fb.blit(tex ,width,height, 0, 0, 0,0,width, height,false);//if set to true the blit overlays the previous screen.
    }

}
