package c.lawless.gameoflife;

import android.content.res.Resources;
import c.lawless.gameoflife.RenderHooks.GOLRenderHook;
import c.lawless.gameoflife.RenderHooks.MainRender_hook;
import c.lawless.gameoflife.RenderHooks.SplatHook;
import c.lawless.gameoflife.StorageStuff.GOFSave;
import c.lawless.gameoflife.StorageStuff.SaverFKt;
import c.lawless.gameoflife.statics.ColorSchemes;
import c.lawless.gameoflife.statics.GridSizes;
import c.lawless.gameoflife.statics.Rules;
import com.threed.jpct.*;


/**
 * Created by lawless on 12/10/2015.
 */
public class PostProcessHandler {
    public MainActivity main ;

   // public NPOTTexture outPutTexture = null; //if not a null we output texture to this.


    TextureManager tm = TextureManager.getInstance();

    Boolean textureCompression= false;//Setting this true will cause loading of saves to fail.
    Boolean textureFiltering= false;
    Boolean textureMipMap= false;
    Boolean setGLFiltering=false;


    public NPOTTexture splat_tex;

    
    public GLSLShader GOF_shader;
    public GLSLShader random_shader;
    public GLSLShader render_shader;
    public GLSLShader draw_shader;

    GOLRenderHook GOF_Hook;
    SplatHook Splat_Hook;
    MainRender_hook render_hook;

    public Object3D  random_obj = null;
    public Object3D  draw_obj_one = null;

    Processing processing_;

    public Object3D  render_to_screen_obj_one = null;
    public Object3D  render_to_screen_obj_two = null;


    //public World processWorld;
    public World displayWorld;
    public Camera displayCam = null;
    //public Camera processCam = null;
    FrameBuffer FB = null;


    public  Boolean switcher= true;

    public SimpleVector InverseSizex;
    public SimpleVector InverseSizey;

    public SimpleVector splatPos;
    //float AspectRatio;
    public  float splatRadius;
    public boolean splat_on=false;


    boolean random_fill = false;
    boolean clear_grid = false;
    boolean do_resize=false;



    int colour_mode=0;

     Rules rules = new Rules();


    public PostProcessHandler(Resources res, FrameBuffer fb, MainActivity main) {
    fb.freeMemory();
    FB= fb;
        GridSizes.INSTANCE.setSize_modifier(1);

    GridSizes.INSTANCE.setScreenHeight(FB.getHeight());
    GridSizes.INSTANCE.setScreenWidth(FB.getWidth());

    GridSizes.INSTANCE.setGridHeight(FB.getHeight() / GridSizes.INSTANCE.getSize_modifier());
    GridSizes.INSTANCE.setGridWidth(FB.getWidth() / GridSizes.INSTANCE.getSize_modifier());
    loadShaders(res);
    setUpCameras();//worlds

    processing_= new Processing(displayWorld);
    setupTextures();
    this.main=main;

    // InverseSize = new SimpleVector(1.0f/ fb.getWidth() ,1.0f/ fb.getHeight() ,0);

     InverseSizex = new SimpleVector(1.0f/ GridSizes.INSTANCE.getGridWidth(),0 ,0);
     InverseSizey = new SimpleVector(0 ,1.0f/ GridSizes.INSTANCE.getGridHeight(),0);

     splatRadius =   GridSizes.INSTANCE.getGridWidth() /16.0f;
     splatPos    =  new SimpleVector(  GridSizes.INSTANCE.getGridWidth() / 2.0f, GridSizes.INSTANCE.getGridHeight() /2.0f , 0);
     setupObjects();

    // displayWorld.compileAllObjects();
    }



    //This is for loading a save
    public PostProcessHandler(Resources res, FrameBuffer fb, MainActivity main, Long id) {
        fb.freeMemory();
        FB= fb;
        GridSizes.INSTANCE.setSize_modifier(1);

        GridSizes.INSTANCE.setScreenHeight(FB.getHeight());
        GridSizes.INSTANCE.setScreenWidth(FB.getWidth());

        GridSizes.INSTANCE.setGridHeight(FB.getHeight() / GridSizes.INSTANCE.getSize_modifier());
        GridSizes.INSTANCE.setGridWidth(FB.getWidth() / GridSizes.INSTANCE.getSize_modifier());
        loadShaders(res);
        setUpCameras();//worlds
        //we replace textures here to avoid errors.
        processing_= new Processing(displayWorld,id);
        replaceTextures();

        this.main=main;

        InverseSizex = new SimpleVector(1.0f/ GridSizes.INSTANCE.getGridWidth(),0 ,0);
        InverseSizey = new SimpleVector(0 ,1.0f/ GridSizes.INSTANCE.getGridHeight(),0);

        splatRadius =   GridSizes.INSTANCE.getGridWidth() /16.0f;
        splatPos    =  new SimpleVector(  GridSizes.INSTANCE.getGridWidth() / 2.0f, GridSizes.INSTANCE.getGridHeight() /2.0f , 0);

        setupObjects();

    }


    public void update() {

        if(do_resize)
        {
            resizeGrid();
            System.gc();
            do_resize = false;
        }

      fills_and_draws(FB);
        processing_.process(FB,switcher);

      FB.resize(GridSizes.INSTANCE.getScreenWidth(), GridSizes.INSTANCE.getScreenHeight());

      render_to_screen(FB);

    switcher = !switcher;
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
                fb.setRenderTarget(processing_.frame_one);
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
                fb.setRenderTarget(processing_.frame_one);
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
                fb.setRenderTarget(processing_.frame_two);
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
                fb.setRenderTarget(processing_.frame_two);
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


        processing_.setupObjects(this,GOF_shader);



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


        splat_tex = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        splat_tex.setFiltering(textureFiltering);
        splat_tex.setMipmap(textureMipMap);
        splat_tex.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("splatt", splat_tex);

    }


    public void replaceTextures()
    {

 processing_.replaceTextures();


        NPOTTexture splat_tex_ = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        splat_tex_.setFiltering(textureFiltering);
        splat_tex_.setMipmap(textureMipMap);
        splat_tex_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
       // splat_tex_.add(splat_tex,1f);
        tm.replaceTexture("splatt", splat_tex_);



        splat_tex= splat_tex_;



        FB.flush();
        FB.freeMemory();
        System.gc();
    }

    public void replaceTextures(int width, int height)
    {

        processing_.replaceTextures(width,height);


        NPOTTexture splat_tex_ = new NPOTTexture(width , height, RGBColor.BLACK);
        splat_tex_.setFiltering(textureFiltering);
        splat_tex_.setMipmap(textureMipMap);
        splat_tex_.setTextureCompression(textureCompression);// texture compression eliminates the artifacts

        tm.replaceTexture("splatt", splat_tex_);



        splat_tex= splat_tex_;



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
        GridSizes.INSTANCE.setSize_level(GridSizes.INSTANCE.getSize_level() - 1);

                if(GridSizes.INSTANCE.getSize_level() < 1)
                {
                    GridSizes.INSTANCE.setSize_level(1);
                }
                else
                {
                    setSize();
                    do_resize= true;
                }
    }

    public void decreaseSize()
    {

        GridSizes.INSTANCE.setSize_level(GridSizes.INSTANCE.getSize_level() + 1);

        if(GridSizes.INSTANCE.getSize_level() > 8)
        {
            GridSizes.INSTANCE.setSize_level(8);
        }
        else
        {
            setSize();
            do_resize= true;
        }

    }

    public void setSize()
    {

        switch(GridSizes.INSTANCE.getSize_level()) {
            case 1:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getScreenResolutionSize());
                break;
            case 2:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getHalf());
                break;
            case 3:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getQuarter());
                break;
            case 4:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getEight());
                break;
            case 5:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getSixtieenth());
                break;
            case 6:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getThirtysecond());
                break;
            case 7:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getSixtyfourth());
                break;
            case 8:
                GridSizes.INSTANCE.setSize_modifier(GridSizes.INSTANCE.getSmallest());
                break;
        }
    }



    public void resizeGrid()
    {

        GridSizes.INSTANCE.setGridHeight(GridSizes.INSTANCE.getScreenHeight() / GridSizes.INSTANCE.getSize_modifier());
        GridSizes.INSTANCE.setGridWidth(GridSizes.INSTANCE.getScreenWidth() / GridSizes.INSTANCE.getSize_modifier());


        InverseSizex = new SimpleVector(1.0f/ GridSizes.INSTANCE.getGridWidth(),0 ,0);
        InverseSizey = new SimpleVector(0 ,1.0f/ GridSizes.INSTANCE.getGridHeight(),0);

        splatRadius =   GridSizes.INSTANCE.getGridWidth() /16.0f;
        splatPos    =  new SimpleVector(  GridSizes.INSTANCE.getGridWidth() / 2.0f, GridSizes.INSTANCE.getGridHeight() /2.0f , 0);


        FB.resize(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight());
        replaceTextures();
        processing_.replaceTextures(GridSizes.INSTANCE.getGridWidth(),GridSizes.INSTANCE.getGridHeight());

    }


    public void resizeFromSave(int width, int height)
    {


        GridSizes.INSTANCE.setGridWidth(width);
        GridSizes.INSTANCE.setGridHeight(height);

        replaceTextures(width,height);

        InverseSizex = new SimpleVector(1.0f/ GridSizes.INSTANCE.getGridWidth(),0 ,0);
        InverseSizey = new SimpleVector(0 ,1.0f/ GridSizes.INSTANCE.getGridHeight(),0);

        splatRadius =   GridSizes.INSTANCE.getGridWidth() /16.0f;
        splatPos    =  new SimpleVector(  GridSizes.INSTANCE.getGridWidth() / 2.0f, GridSizes.INSTANCE.getGridHeight() /2.0f , 0);

        FB.resize( width,height);

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
        y = processing_.frame_one.getHeight() -(y/ GridSizes.INSTANCE.getSize_modifier());

        x = x/ GridSizes.INSTANCE.getSize_modifier();
        //x =  frame_one.getWidth()/x;
        //        x= ((frame_one.getWidth()) +(x/size_modifier));
//        x= 1f-x;
        splatPos = new SimpleVector(x,y,0);
        splat_on=true;
    }


    public void blitLoad(FrameBuffer fb, int[]  tex,int width, int height )
    {

      //  fb.resize(width, height);
        fb.blit(tex ,width,height, 0, 0, 0,0,
                width, height,
                false);//if set to true the blit overlays the previous screen.


    }



}
