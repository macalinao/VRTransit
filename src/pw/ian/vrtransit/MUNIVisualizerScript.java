package pw.ian.vrtransit;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRScript;
import org.gearvrf.scene_objects.GVRWebViewSceneObject;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MUNIVisualizerScript extends GVRScript {
	
	private MainActivity core;
	
	public MUNIVisualizerScript(MainActivity core) {
		this.core = core;
	}

	@Override
	public void onInit(GVRContext ctx) throws Throwable {
		
		GVRScene scene = ctx.getMainScene();
		scene.setFrustumCulling(true);
		
		scene.getMainCameraRig().getLeftCamera()
                .setBackgroundColor(0.0f, 0.0f, 0.0f, 1.0f);
		scene.getMainCameraRig().getRightCamera()
                .setBackgroundColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void onStep() {
	}

}
