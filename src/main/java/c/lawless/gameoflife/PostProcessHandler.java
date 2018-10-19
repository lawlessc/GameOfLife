package c.lawless.gameoflife;

import android.content.res.Resources;
import com.threed.jpct.*;


/**
 * Created by lawless on 12/10/2015.
 */
public class PostProcessHandler {

    public NPOTTexture outPutTexture = null; //if not a null we output texture to this.


    TextureManager tm = TextureManager.getInstance();

    Boolean textureCompression= true;
    Boolean textureFiltering= false;
    Boolean textureMipMap= false;
    Boolean setGLFiltering=false;






    public NPOTTexture frame_one;//Textures for alternating frames.
    public NPOTTexture frame_two;




    public GLSLShader GOF_shader;
    GOLRenderHook  GOF_Hook;
    public Object3D  fameObjOne = null;
    public Object3D  fameObjTwo = null;


//    public TextureInfo frame_one_ti= null;
//    public TextureInfo frame_two_ti= null;


    public World displayWorld;
    public Camera displayCam = null;
    public GLSLShader displayShader = null;
    //public Object3D displayObj = null;


    public  Boolean switcher= true;

    SimpleVector InverseSize;
    SimpleVector splatPos;
    float AspectRatio;
    float splatRadius;

    boolean first_run = true;




    public PostProcessHandler(Resources res, FrameBuffer fb) {
     fb.freeMemory();
    loadShaders(res);
    setUpCameras();//worlds
    setupTextures(fb.getWidth(),fb.getHeight());

     InverseSize = new SimpleVector(1.0f/ fb.getWidth() ,1.0f/ fb.getHeight() ,0);
     splatRadius =   fb.getWidth() /8.0f;
     splatPos    =  new SimpleVector(  fb.getWidth() / 2.0f, fb.getWidth() /2.0f , 0);
     AspectRatio = fb.getWidth()/fb.getHeight();
     setupObjects();

     displayWorld.compileAllObjects();

    }


    public void Process(FrameBuffer fb) {

        if(switcher) {

            fb.setRenderTarget(frame_two);

            fameObjOne.setVisibility(true);
            fb.clear();
            displayWorld.renderScene(fb);
            displayWorld.draw(fb);
            fb.display();
            fameObjOne.setVisibility(false);
            fb.removeRenderTarget();
            //advectingObj.setTexture(advecting_ti);


            //Render to screen here
            fameObjOne.setVisibility(true);
            fb.removeRenderTarget();
            fb.clear();
            displayWorld.renderScene(fb);//
            displayWorld.draw(fb);
            fb.display();
            fameObjOne.setVisibility(false);

        }
       else
        {



            fb.setRenderTarget(frame_one);
            fameObjTwo.setVisibility(true);
            fb.clear();
            displayWorld.renderScene(fb);
            displayWorld.draw(fb);
            fb.display();
            fameObjTwo.setVisibility(false);
            fb.removeRenderTarget();

            //Render to screen here
            fameObjTwo.setVisibility(true);
            fb.removeRenderTarget();
            fb.clear();
            displayWorld.renderScene(fb);//
            displayWorld.draw(fb);
            fb.display();
            fameObjTwo.setVisibility(false);

         //   first_run =false;
           // switcher = !switcher;
        }



        first_run =false;
        switcher = !switcher;


    }


    public void setupObjects()
    {

        GOF_Hook = new GOLRenderHook(this,GOF_shader);
        fameObjOne = Primitives.getPlane(4,10);
        fameObjOne.setOrigin(new SimpleVector(0.01, 0, 0));

        fameObjOne.setRenderHook(GOF_Hook);
        fameObjOne.setShader(GOF_shader);
       // advecting_ti  =  new TextureInfo(TextureManager.getInstance().getTextureID(VELOCITY_TEXTURE_TAG));
      //  advecting_ti.add(TextureManager.getInstance().getTextureID(VELOCITY_TEXTURE_TAG), TextureInfo.MODE_ADD);
        fameObjOne.setTexture("frameone");
        fameObjOne.setCulling(false);
        fameObjOne.compile();
       // advectingObj.strip();
        displayWorld.addObject(fameObjOne);
        fameObjOne.setVisibility(false);

        fameObjTwo = Primitives.getPlane(4,10);
        fameObjTwo.setOrigin(new SimpleVector(0.01, 0, 0));

        fameObjTwo.setRenderHook(GOF_Hook);
        fameObjTwo.setShader(GOF_shader);
       // advecting_tiTwo   =  new TextureInfo(TextureManager.getInstance().getTextureID(VELOCITY_TEXTURE_TAG_TWO));
       // advecting_tiTwo.add(TextureManager.getInstance().getTextureID(VELOCITY_TEXTURE_TAG_TWO), TextureInfo.MODE_ADD);
        fameObjTwo.setTexture("frametwo");
        fameObjTwo.setCulling(false);
        fameObjTwo.compile();
        //advectingObjTwo.strip();
        displayWorld.addObject(fameObjTwo);
        fameObjTwo.setVisibility(false);


    }



    public void setupTextures(int w, int h)
    {

        int width= w;
        int height= h;

//        textureless = new NPOTTexture(width , height, RGBColor.BLACK);
//        textureless.setFiltering(false);
//        textureless.setMipmap(false);
//        textureless.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
//        tm.addTexture("textureless", textureless);


        frame_one = new NPOTTexture(width , height, RGBColor.BLACK);
        frame_one.setFiltering(textureFiltering);
        frame_one.setMipmap(textureMipMap);
        frame_one.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frameone", frame_one);

        frame_two = new NPOTTexture(width , height, RGBColor.BLACK);
        frame_two.setFiltering(textureFiltering);
        frame_two.setMipmap(textureMipMap);
        frame_two.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frametwo", frame_two);

    }

    public void setUpCameras()
    {
        displayWorld = new World();
        displayCam=displayWorld.getCamera();
        displayCam.setPosition(-10, 0, 0);
        displayCam.lookAt(new SimpleVector(0, 0, 0));
    }

    public void loadShaders(Resources res)
    {
        String vertexShader =   Loader.loadTextFile(res.openRawResource(R.raw.gof_vertex));
        String FragmentShader =   Loader.loadTextFile(res.openRawResource(R.raw.gof_frag));
        GOF_shader = new GLSLShader(vertexShader,FragmentShader);
    }




    public void setOutPutTexture(NPOTTexture outPutTexture)
    {

        this.outPutTexture = outPutTexture;
       InverseSize = new SimpleVector(1.0f/ outPutTexture.getWidth() ,1.0f/ outPutTexture.getHeight() ,0);
    }



    public void setSplatPos(float x , float y)
    {

        y = frame_one.getHeight() -y;
        splatPos = new SimpleVector(x,y,0);
    }



    public void switchView()
    {
//        switch (viewtype)
//        {
//            case DENSITY:
//                displayObj.setTexture(DENSITY_TEXTURE_TAG);
//                viewtype = Viewtype.VELOCITY;
//                break;
//            case VELOCITY:
//                displayObj.setTexture(VELOCITY_TEXTURE_TAG);
//                viewtype = Viewtype.DIVERGENCE;
//                break;
//            case DIVERGENCE:
//                displayObj.setTexture(DIVERGENCE_TEXTURE_TAG);
//                viewtype = Viewtype.PRESSURE;
//                break;
//
//            case PRESSURE:
//                displayObj.setTexture(PRESSURE_TEXTURE_TAG);
//                viewtype = Viewtype.DENSITY;
//                break;
//        }
    }





    //
    public void AddDensity()
    {


    }










}
