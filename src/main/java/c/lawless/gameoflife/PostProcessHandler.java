package c.lawless.gameoflife;

import android.content.res.Resources;
import c.lawless.gameoflife.RenderHooks.MainRender_hook;
import c.lawless.gameoflife.RenderHooks.SplatHook;
import c.lawless.gameoflife.StorageStuff.SaverFKt;
import c.lawless.gameoflife.statics.ColorSchemes;
import c.lawless.gameoflife.statics.GridSizes;
import c.lawless.gameoflife.statics.Rules;
import c.lawless.gameoflife.statics.TextureNames;
import com.threed.jpct.*;


/**
 * Created by lawless on 12/10/2015.
 */
public class PostProcessHandler {
    MainActivity main ;

   // public NPOTTexture outPutTexture = null; //if not a null we output texture to this.


    TextureManager tm = TextureManager.getInstance();

    Boolean textureCompression= true;
    Boolean textureFiltering= false;
    Boolean textureMipMap= false;
    Boolean setGLFiltering=false;


    public NPOTTexture frame_one;//Textures for alternating frames.
    public NPOTTexture frame_two;

    public NPOTTexture splat_tex;


    public GLSLShader GOF_shader;
    public GLSLShader random_shader;
    public GLSLShader render_shader;

    public GLSLShader draw_shader;

    GOLRenderHook  GOF_Hook;
    SplatHook Splat_Hook;
    MainRender_hook render_hook;

    public Object3D  random_obj = null;
    public Object3D  draw_obj_one = null;


    public Object3D  fameObjOne = null;
    public Object3D  fameObjTwo = null;

    public Object3D  render_to_screen_obj_one = null;
    public Object3D  render_to_screen_obj_two = null;


    public World processWorld;
    public World displayWorld;
    public Camera displayCam = null;
    public Camera processCam = null;
    FrameBuffer FB = null;


    public  Boolean switcher= true;

    SimpleVector InverseSizex;
    SimpleVector InverseSizey;

    public SimpleVector splatPos;
    //float AspectRatio;
    public  float splatRadius;
    public boolean splat_on=false;
   // boolean random_spray=false;

    boolean random_fill = false;
    boolean clear_grid = false;
    boolean do_resize=false;
    boolean save=false;




    int colour_mode=0;

     Rules rules = new Rules();


    public PostProcessHandler(Resources res, FrameBuffer fb, MainActivity main) {
    fb.freeMemory();
    FB= fb;
        GridSizes.size_modifier = 1;

    GridSizes.ScreenHeight = FB.getHeight();
    GridSizes.ScreenWidth = FB.getWidth();

    GridSizes.GridHeight = FB.getHeight()/GridSizes.size_modifier;
    GridSizes.GridWidth = FB.getWidth()/  GridSizes.size_modifier;
    loadShaders(res);
    setUpCameras();//worlds
    setupTextures();
    this.main=main;

    // InverseSize = new SimpleVector(1.0f/ fb.getWidth() ,1.0f/ fb.getHeight() ,0);

     InverseSizex = new SimpleVector(1.0f/ GridSizes.GridWidth ,0 ,0);
     InverseSizey = new SimpleVector(0 ,1.0f/ GridSizes.GridHeight ,0);

     splatRadius =   GridSizes.GridWidth /16.0f;
     splatPos    =  new SimpleVector(  GridSizes.GridWidth / 2.0f, GridSizes.GridHeight /2.0f , 0);
     //AspectRatio = FB.getWidth()/FB.getHeight();
     setupObjects();

    // displayWorld.compileAllObjects();

    }





    public void update() {

        if(do_resize)
        {
            resizeGrid();
            System.gc();
            do_resize = false;
        }

     FB.resize(GridSizes.GridWidth,GridSizes.GridHeight);

      fills_and_draws(FB);
      process();

      FB.resize(GridSizes.ScreenWidth,GridSizes.ScreenHeight);
      render_to_screen(FB);

        switcher = !switcher;
    }

    public void process()
    {
        if(switcher) {

            FB.setRenderTarget(frame_two);
            fameObjOne.setVisibility(true);
            FB.clear();
            displayWorld.renderScene(FB);
            displayWorld.draw(FB);
            FB.display();
            fameObjOne.setVisibility(false);
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
            FB.removeRenderTarget();
        }
    }




    public void render_to_screen(FrameBuffer fb)
    {
        if(switcher) {

            //Render to screen here
            render_to_screen_obj_two.setVisibility(true);
            fb.removeRenderTarget();
            fb.clear();
            displayWorld.renderScene(fb);//
            displayWorld.draw(fb);
            fb.display();
            render_to_screen_obj_two.setVisibility(false);
        }
        else
        {
            //Render to screen here
            render_to_screen_obj_one.setVisibility(true);
            fb.removeRenderTarget();
            fb.clear();
            displayWorld.renderScene(fb);//
            displayWorld.draw(fb);
            fb.display();
            render_to_screen_obj_one.setVisibility(false);
        }


    }



    public void save_Frame(FrameBuffer fb) {

        //TODO still have to create the save texture on start up,

        if(save) {

            SaverFKt.frameSaver(fb,displayWorld);
        }


    }


    public void fills_and_draws(FrameBuffer fb)
    {



        fb.setRenderTarget(splat_tex);
        draw_obj_one.setVisibility(true);
        fb.clear();
        displayWorld.renderScene(fb);
        displayWorld.draw(fb);
        fb.display();
        draw_obj_one.setVisibility(false);
        fb.removeRenderTarget();




        if(switcher) {


            if(random_fill)
            {
                fb.setRenderTarget(frame_one);
                random_obj.setVisibility(true);
                fb.clear();
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                random_obj.setVisibility(false);
                fb.removeRenderTarget();
                random_fill =false;
            }

            if(clear_grid)
            {
                fb.setRenderTarget(frame_one);
                fb.clear();
                fb.clear(RGBColor.BLACK);
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                fb.removeRenderTarget();
                clear_grid=false;
            }




        }
        else
        {
            if(random_fill)
            {
                fb.setRenderTarget(frame_two);
                random_obj.setVisibility(true);
                fb.clear();
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                random_obj.setVisibility(false);
                fb.removeRenderTarget();
                random_fill =false;
            }

            if(clear_grid)
            {
                fb.setRenderTarget(frame_two);
                fb.clear();
                fb.clear(RGBColor.BLACK);
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                fb.removeRenderTarget();
                clear_grid=false;
            }
        }

    }


    public void setupObjects()
    {

        GOF_Hook = new GOLRenderHook(this,GOF_shader);
        Splat_Hook = new SplatHook(this, draw_shader);
        render_hook = new MainRender_hook(this, render_shader);



        draw_obj_one = Primitives.getPlane(4,10);
        draw_obj_one.setOrigin(new SimpleVector(0.01, 0, 0));
        draw_obj_one.setRenderHook(Splat_Hook);
        draw_obj_one.setShader(draw_shader);
        draw_obj_one.setCulling(false);
        draw_obj_one.compile();
        displayWorld.addObject(draw_obj_one);
        draw_obj_one.setVisibility(false);

        random_obj = Primitives.getPlane(4,10);
        random_obj.setOrigin(new SimpleVector(0.01, 0, 0));
        random_obj.setRenderHook(GOF_Hook);
        random_obj.setShader(random_shader);
        random_obj.setCulling(false);
        random_obj.compile();
        // advectingObj.strip();
        displayWorld.addObject(random_obj);
        random_obj.setVisibility(false);

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


        render_to_screen_obj_one = Primitives.getPlane(4,10);
        render_to_screen_obj_one.setOrigin(new SimpleVector(0.01, 0, 0));
        render_to_screen_obj_one.setRenderHook(render_hook);
        render_to_screen_obj_one.setShader(render_shader);

        render_to_screen_obj_one.setTexture("frameone");
        render_to_screen_obj_one.setCulling(false);
        render_to_screen_obj_one.compile();
        displayWorld.addObject(render_to_screen_obj_one);
        render_to_screen_obj_one.setVisibility(false);

        render_to_screen_obj_two = Primitives.getPlane(4,10);
        render_to_screen_obj_two.setOrigin(new SimpleVector(0.01, 0, 0));
        render_to_screen_obj_two.setRenderHook(render_hook);
        render_to_screen_obj_two.setShader(render_shader);
        render_to_screen_obj_two.setTexture("frametwo");
        render_to_screen_obj_two.setCulling(false);
        render_to_screen_obj_two.compile();
        displayWorld.addObject(render_to_screen_obj_two);
        render_to_screen_obj_two.setVisibility(false);
    }



    public void setupTextures()
    {


        frame_one = new NPOTTexture(GridSizes.GridWidth , GridSizes.GridHeight, RGBColor.BLACK);
        frame_one.setFiltering(textureFiltering);
        frame_one.setMipmap(textureMipMap);
        frame_one.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frameone", frame_one);

        frame_two = new NPOTTexture(GridSizes.GridWidth , GridSizes.GridHeight, RGBColor.BLACK);
        frame_two.setFiltering(textureFiltering);
        frame_two.setMipmap(textureMipMap);
        frame_two.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frametwo", frame_two);


        splat_tex = new NPOTTexture(GridSizes.GridWidth , GridSizes.GridHeight, RGBColor.BLACK);
        splat_tex.setFiltering(textureFiltering);
        splat_tex.setMipmap(textureMipMap);
        splat_tex.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("splatt", splat_tex);

    }


    public void replaceTextures()
    {



//        tm.unloadTexture(FB,frame_one);
//        tm.unloadTexture(FB,frame_two);
//        tm.unloadTexture(FB,splat_tex);

        NPOTTexture frame_one_ = new NPOTTexture(GridSizes.GridWidth , GridSizes.GridHeight, RGBColor.BLACK);
        frame_one_.setFiltering(textureFiltering);
        frame_one_.setMipmap(textureMipMap);
        frame_one_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
       // frame_one_.add(frame_one,1f);
        tm.replaceTexture("frameone", frame_one_);


        NPOTTexture frame_two_ = new NPOTTexture(GridSizes.GridWidth , GridSizes.GridHeight, RGBColor.BLACK);
        frame_two_.setFiltering(textureFiltering);
        frame_two_.setMipmap(textureMipMap);
        frame_two_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
      //  frame_two_.add(frame_two,1f);
        tm.replaceTexture("frametwo", frame_two_);


        NPOTTexture splat_tex_ = new NPOTTexture(GridSizes.GridWidth , GridSizes.GridHeight, RGBColor.BLACK);
        splat_tex_.setFiltering(textureFiltering);
        splat_tex_.setMipmap(textureMipMap);
        splat_tex_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
       // splat_tex_.add(splat_tex,1f);
        tm.replaceTexture("splatt", splat_tex_);


        frame_one= frame_one_;
        frame_two= frame_two_;
        splat_tex= splat_tex_;


      // setupTextures();


        FB.flush();
        FB.freeMemory();
        System.gc();


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


        String vertexShaderr =   Loader.loadTextFile(res.openRawResource(R.raw.gof_vertex));
        String FragmentShaderr =   Loader.loadTextFile(res.openRawResource(R.raw.random_frag));
        random_shader = new GLSLShader(vertexShaderr,FragmentShaderr);

        render_shader = new GLSLShader(Loader.loadTextFile(res.openRawResource(R.raw.gof_vertex))
                ,Loader.loadTextFile(res.openRawResource(R.raw.render_to_screen_frag)) );


        draw_shader = new GLSLShader(Loader.loadTextFile(res.openRawResource(R.raw.gof_vertex))
            ,Loader.loadTextFile(res.openRawResource(R.raw.splat_frag)) );

    }



    public void increaseSize()
    {
        GridSizes.size_level--;

                if(GridSizes.size_level < 1)
                {
                    GridSizes.size_level=1;
                }
                else
                {
                    setSize();
                    do_resize= true;
                }

    //   setSize();
    //    do_resize= true;
    }

    public void decreaseSize()
    {


        GridSizes.size_level++;

        if(GridSizes.size_level > 8)
        {
            GridSizes.size_level=8;
        }
        else
        {
            setSize();
            do_resize= true;
        }



    }


    public void setSize()
    {

        switch(GridSizes.size_level) {
            case 1:
                GridSizes.size_modifier= GridSizes.screenResolutionSize;
                break;
            case 2:
                GridSizes.size_modifier= GridSizes.half;
                break;
            case 3:
                GridSizes.size_modifier= GridSizes.quarter;
                break;
            case 4:
                GridSizes.size_modifier= GridSizes.eight;
                break;
            case 5:
                GridSizes.size_modifier= GridSizes.sixtieenth;
                break;
            case 6:
                GridSizes.size_modifier= GridSizes.thirtysecond;
                break;
            case 7:
                GridSizes.size_modifier= GridSizes.sixtyfourth;
                break;
            case 8:
                GridSizes.size_modifier= GridSizes.smallest;
                break;
        }
    }



    public void resizeGrid()
    {

        GridSizes.GridHeight = GridSizes.ScreenHeight / GridSizes.size_modifier;
        GridSizes.GridWidth = GridSizes.ScreenWidth  /  GridSizes.size_modifier;
        replaceTextures();


        InverseSizex = new SimpleVector(1.0f/ GridSizes.GridWidth ,0 ,0);
        InverseSizey = new SimpleVector(0 ,1.0f/ GridSizes.GridHeight ,0);

        splatRadius =   GridSizes.GridWidth /16.0f;
        splatPos    =  new SimpleVector(  GridSizes.GridWidth / 2.0f, GridSizes.GridHeight /2.0f , 0);


//        TextureInfo one  =  new TextureInfo(TextureManager.getInstance().getTextureID("frameone"));
//        one.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD);
//        fameObjOne.setTexture(one);
//        TextureInfo two  =  new TextureInfo(TextureManager.getInstance().getTextureID("frametwo"));
//        two.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD);
//        fameObjTwo.setTexture(two);
//
//        render_to_screen_obj_one.setTexture("frameone");
//        render_to_screen_obj_two.setTexture("frametwo");




        FB.setRenderTarget(frame_one);
        FB.clear();
        FB.clear(RGBColor.BLACK);
        displayWorld.renderScene(FB);
        displayWorld.draw(FB);
        FB.display();
        FB.removeRenderTarget();

    }


    public void changeColours()
    {

        switch (colour_mode)
        {
            case 0:
                render_hook.setColours(ColorSchemes.white,ColorSchemes.black);
                colour_mode++;
                break;
            case 1 :
                render_hook.setColours(ColorSchemes.ice,ColorSchemes.sea);
                colour_mode++;
                break;
            case 2 :
                render_hook.setColours(ColorSchemes.jungle_light,ColorSchemes.jundle_dark);
                colour_mode++;
                break;
            case 3 :
                colour_mode=0;
                break;
        }
    }

    public void setSplatPos(float x , float y)
    {
        y = frame_one.getHeight() -(y/GridSizes.size_modifier);

        x = x/GridSizes.size_modifier;
        //x =  frame_one.getWidth()/x;
        //        x= ((frame_one.getWidth()) +(x/size_modifier));
//        x= 1f-x;
        splatPos = new SimpleVector(x,y,0);
        splat_on=true;
    }
}
