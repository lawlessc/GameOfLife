package c.lawless.gameoflife

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.opengl.GLSurfaceView

import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View

import com.threed.jpct.Camera
import com.threed.jpct.Config
import com.threed.jpct.FrameBuffer
import com.threed.jpct.Logger
import com.threed.jpct.RGBColor
import com.threed.jpct.SimpleVector
import com.threed.jpct.Texture
import com.threed.jpct.World
import com.threed.jpct.util.AAConfigChooser
import com.threed.jpct.util.MemoryHelper

import java.lang.reflect.Field

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * @author Christopher Lawless
 */
class MainActivity : Activity(), OnScaleGestureListener /*,Observer */ {
    private var mGLView: GLSurfaceView? = null
    private var renderer: MyRenderer? = null
    private var fb: FrameBuffer? = null

    private var useRetroRender = false
    internal var MS_PER_UPDATE: Long = 16

    var previous: Long = 0
    var current: Long = 0
    var lag: Long = 0
    var elapsed: Long = 0


    private var mScaleDetector: ScaleGestureDetector? = null
    private var tapdetection: GestureDetector? = null

    //private Texture font = null;
    private var texturesLoaded: Boolean? = false

    enum class allGameObjects {
        INSTANCE;
        //might make sense to start this at 0 anyway
        var runningTime: Long = 0
        var runningTimeSeconds: Long = 0
        var isActionPaused: Boolean? = false
        var processHandler: PostProcessHandler? = null
    }
    //////////////////////////////////This needs to be seperated from the graphical stuff above somehow.


    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.log("onCreate")
        //Logger.setLogLevel(Logger.LL_DEBUG);
        //Logger.setLogLevel(Logger.);

        Logger.setLogLevel(Logger.DEBUG)

        //Context baseContext= this.getBaseContext();
        //Resources res = getResources();


        if (master != null) {
            copy(master!!)
        }

        super.onCreate(savedInstanceState)



        if (master == null) {
            //This Sets Renderer , I may want to seperate these classes!
            setContentView(R.layout.activity_main) //or whatever the layout you want to use
            mGLView = findViewById<View>(R.id.graphics_glsurfaceview1) as GLSurfaceView


            //mGLView = new GLSurfaceView(getApplication());
            // Enable the OpenGL ES2.0 context
            mGLView!!.setEGLContextClientVersion(2)
            //
            //mGLView.setEGLConfigChooser(new AAConfigChooser(mGLView));

            renderer = MyRenderer()
            mGLView!!.setRenderer(renderer)

            Texture.defaultToMipmapping(false)
            Texture.defaultTo4bpp(true)
            Texture.defaultToKeepPixels(true)
            Config.maxTextureLayers = 4
            Config.unloadImmediately = true

            //	Config.maxPolysVisible = 5000;
            //	Config.farPlane = 100;
            //Config.nearPlane = 0;
        }


        if ((!texturesLoaded!!)!!) {
            val baseContext = this.baseContext


            texturesLoaded = true
        }

        mScaleDetector = ScaleGestureDetector(this, ScaleListener())
        tapdetection = GestureDetector(this, TapListener())
        // master = this;
    }


    override fun onPause() {
        allGameObjects.INSTANCE.isActionPaused = true
        super.onPause()
        mGLView!!.onPause()
    }

    override fun onResume() {
        //		if(allGameObjects.INSTANCE.isActionPaused)
        //		{
        //			copy(master);
        //		}
        super.onResume()
        mGLView!!.onResume()
    }

    override fun onStop() {
        Logger.log("onStop")
        super.onStop()
    }

    private fun copy(src: Any) {
        try {
            Logger.log("Copying data from master Activity!")
            val fs = src.javaClass.declaredFields
            for (f in fs) {
                f.isAccessible = true
                f.set(this, f.get(src))
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    //IMPORTANT
    override fun onTouchEvent(me: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(me)
        tapdetection!!.onTouchEvent(me)
        //    int left = mGLView.getLeft();
        //    int top  =mGLView.getTop();


        allGameObjects.INSTANCE.processHandler!!.setSplatPos(me.x, me.y)

        return super.onTouchEvent(me)
    }


    internal inner class MyRenderer : GLSurfaceView.Renderer {
        private var fps = 0
        private var lfps = 0
        private var time = System.currentTimeMillis()

        override fun onSurfaceChanged(gl: GL10, w: Int, h: Int) {

            if (fb != null) {
                //	fb.dispose();
                fb!!.resize(w, h)
            }
            // else{
            fb = FrameBuffer(w, h)
            //	}


            //  fb.setBlittingShader(EntityFactory.gameboy_shader);

            if (master == null) {
                //	System.out.println("MASTER IS NULLLLL");
                master = this@MainActivity


                val res = resources
 ;
                allGameObjects.INSTANCE.processHandler = PostProcessHandler(res, fb)


                current = System.currentTimeMillis()
                lag = 0
                previous = System.currentTimeMillis()


                MemoryHelper.compact()
                //Main.allGameObjects.INSTANCE.world.compileAllObjects();
            }

        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

            //	Logger.log("onSurfaceCreated");
        }

        override fun onDrawFrame(gl: GL10) {
            allGameObjects.INSTANCE.processHandler!!.Process(fb)
        }
    }


    override fun onScale(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        // TODO Auto-generated method stub
        return true
    }


    override fun onScaleEnd(detector: ScaleGestureDetector) {
        // TODO Auto-generated method stub
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //I NEED TO FIND A WAY TO CHECK IF THIS IS ON AN OBJECT OR NOT?
            //allGameObjects.INSTANCE.cameraCursor.setScaleFactor(detector);
            return true
        }
    }




    private inner class TapListener : OnGestureListener, GestureDetector.OnDoubleTapListener {
        override fun onDown(e: MotionEvent): Boolean {


            // TODO Auto-generated method stub
            return false
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float,
            velocityY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent) {
            // TODO Auto-generated method stub
        }

        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent, distanceX: Float,
            distanceY: Float
        ): Boolean {
            // TODO Auto-generated method stub
            return false
        }

        override fun onShowPress(e: MotionEvent) {
            // TODO Auto-generated method stub
            //allGameObjects.INSTANCE.processHandler.setSplatPos(e.getX() , e.getY());
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {

            // TODO Auto-generated method stub
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val left = mGLView!!.left
            val top = mGLView!!.top

            allGameObjects.INSTANCE.processHandler!!.switchView()


            allGameObjects.INSTANCE.isActionPaused = allGameObjects.INSTANCE.isActionPaused

            if(allGameObjects.INSTANCE.isActionPaused!!)
            {
                allGameObjects.INSTANCE.isActionPaused = false
            }
            else
            {

                allGameObjects.INSTANCE.isActionPaused = true;
            }

            //allGameObjects.INSTANCE.cameraCursor.onDoubleTap(e,fb, left, top);
            return false
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val left = mGLView!!.left
            val top = mGLView!!.top
            //a/llGameObjects.INSTANCE.cameraCursor.onSingleTapConfirmed(e,fb, left, top);
            //pauseAction();
            //cameraCursor.onDoubleTap(e,fb);
            //allGameObjects.INSTANCE.processHandler.setSplatPos(e.getX() , e.getY());
            return false
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            ///DONT USE THIS EVER!
            //IT FIRES FAR TOO MANY TIMES ON EACH PRESS, WILL FUCK UP YOUR DAY.
            return false
        }

    }

    companion object {
        // Used to handle pause and resume...
        private var master: MainActivity? = null
    }
}
