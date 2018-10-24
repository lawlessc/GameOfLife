package c.lawless.gameoflife;

import android.content.res.Resources;
import c.lawless.gameoflife.ColourSchemes.ColorSchemes;
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
    SplatHook  Splat_Hook;
    MainRender_hook render_hook;

    public Object3D  random_obj = null;
    public Object3D  draw_obj_one = null;
   // public Object3D  draw_obj_two = null;


    public Object3D  fameObjOne = null;
    public Object3D  fameObjTwo = null;

    public Object3D  render_to_screen_obj_one = null;
    public Object3D  render_to_screen_obj_two = null;


    public World displayWorld;
    public Camera displayCam = null;


    public  Boolean switcher= true;

    SimpleVector InverseSizex;
    SimpleVector InverseSizey;

    SimpleVector splatPos;
    float AspectRatio;
    float splatRadius;
    boolean splat_on=false;

    boolean first_run = true;

    int size_modifier =1;// setting this to one keeps cells to 1 per pixel.
                         //higher resolutions are unpleasant to look at

    int colour_mode=0;


    public PostProcessHandler(Resources res, FrameBuffer fb, MainActivity main) {
    fb.freeMemory();
    loadShaders(res);
    setUpCameras();//worlds
    setupTextures(fb.getWidth()*size_modifier,fb.getHeight()*size_modifier);
    this.main=main;

    // InverseSize = new SimpleVector(1.0f/ fb.getWidth() ,1.0f/ fb.getHeight() ,0);

     InverseSizex = new SimpleVector(1.0f/ fb.getWidth()*size_modifier ,0 ,0);
     InverseSizey = new SimpleVector(0 ,1.0f/ fb.getHeight()*size_modifier ,0);

     splatRadius =   fb.getWidth() /16.0f;
     splatPos    =  new SimpleVector(  fb.getWidth() / 2.0f, fb.getWidth() /2.0f , 0);
     AspectRatio = fb.getWidth()/fb.getHeight();
     setupObjects();

     displayWorld.compileAllObjects();

    }


    public void Process(FrameBuffer fb) {



     //   if(splat_on)
     //   {
            fb.setRenderTarget(splat_tex);
            draw_obj_one.setVisibility(true);
            fb.clear();
            displayWorld.renderScene(fb);
            displayWorld.draw(fb);
            fb.display();
            draw_obj_one.setVisibility(false);
            fb.removeRenderTarget();
     //   }



        if(first_run)
        {
            fb.setRenderTarget(frame_one);
            random_obj.setVisibility(true);
            fb.clear();
            displayWorld.renderScene(fb);
            displayWorld.draw(fb);
            fb.display();
            random_obj.setVisibility(false);
            fb.removeRenderTarget();
        }


        if(switcher) {

            if(main.isActionPaused()) {
                fb.setRenderTarget(frame_two);
                fameObjOne.setVisibility(true);
                fb.clear();
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                fameObjOne.setVisibility(false);
                fb.removeRenderTarget();


                //Render to screen here
                render_to_screen_obj_two.setVisibility(true);
                fb.removeRenderTarget();
                fb.clear();
                displayWorld.renderScene(fb);//
                displayWorld.draw(fb);
                fb.display();
                render_to_screen_obj_two.setVisibility(false);
            }
        }
       else
        {
            if(first_run)
            {
                fb.setRenderTarget(frame_two);
                random_obj.setVisibility(true);
                fb.clear();
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                random_obj.setVisibility(false);
                fb.removeRenderTarget();
            }


            //displayWorld.

            if(main.isActionPaused()) {
                fb.setRenderTarget(frame_one);
                fameObjTwo.setVisibility(true);
                fb.clear();
                displayWorld.renderScene(fb);
                displayWorld.draw(fb);
                fb.display();
                fameObjTwo.setVisibility(false);
                fb.removeRenderTarget();
            }


                //Render to screen here
                render_to_screen_obj_one.setVisibility(true);
                fb.removeRenderTarget();
                fb.clear();
                displayWorld.renderScene(fb);//
                displayWorld.draw(fb);
                fb.display();
                render_to_screen_obj_one.setVisibility(false);
        }

        first_run =false;
        switcher = !switcher;

        System.out.println(splat_on + " splat STATE");

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


        splat_tex = new NPOTTexture(width , height, RGBColor.BLACK);
        splat_tex.setFiltering(textureFiltering);
        splat_tex.setMipmap(textureMipMap);
        splat_tex.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("splatt", splat_tex);


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








//    public void setOutPutTexture(NPOTTexture outPutTexture)
//    {
//
//        this.outPutTexture = outPutTexture;
//       //InverseSize = new SimpleVector(1.0f/ outPutTexture.getWidth() ,1.0f/ outPutTexture.getHeight() ,0);
//    }


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
               // render_hook.setColours(ColorSchemes.white,ColorSchemes.white);
                colour_mode=0;
                break;
        }
    }



    public void setSplatPos(float x , float y)
    {
        y = frame_one.getHeight() -y;
        splatPos = new SimpleVector(x,y,0);
        splat_on=true;
    }


    public void splatOff()
    {
        splat_on=false;
    }








}
