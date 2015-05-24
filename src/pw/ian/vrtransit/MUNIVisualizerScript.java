package pw.ian.vrtransit;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;

public class MUNIVisualizerScript extends GVRScript {

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
