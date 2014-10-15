package game.androidgame2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class GameActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsES2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsES2) {
            game = new Game(this);
            setContentView(game.getGLSurfaceView());
        }
        else {
            this.finish();
        }
    }

    public void onSurfaceReady() {
        //setContentView(game.getGLSurfaceView());
    }

    protected void onRestart() {
        Log.i("MainActivity", "onRestart");
        super.onRestart();
    }


    protected void onStart() {
        Log.i("MainActivity", "onStart");
        super.onStart();
        game.start();
    }

    protected void onResume() {
        Log.i("MainActivity", "onResume");
        super.onResume();
        game.resume();

    }

    protected void onPause() {
        Log.i("MainActivity", "onPause");
        super.onPause();
        game.pause();
    }

    protected void onStop() {
        Log.i("MainActivity", "onStop");
        super.onStop();
        game.stop();
    }

    protected void onDestroy() {
        Log.i("MainActivity", "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
