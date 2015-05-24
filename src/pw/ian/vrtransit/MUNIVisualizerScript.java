package pw.ian.vrtransit;

import java.util.concurrent.Future;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRCylinderSceneObject;

public class MUNIVisualizerScript extends GVRScript {

	private MainActivity core;
	
	private GVRAndroidResource busMesh;
	
	private GVRAndroidResource busTex;

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
		scene.getMainCameraRig().getOwnerObject().getTransform()
				.setPosition(0f, 0f, 0f);

		busMesh = new GVRAndroidResource(ctx, "sphere.obj");
		busTex = new GVRAndroidResource(ctx, "bus.jpg");
		
		GVRSceneObject root = new GVRSceneObject(ctx);
		scene.addSceneObject(root);

		root.addChildObject(constructBus(ctx, 22f, 0f));

	}

	@Override
	public void onStep() {
	}

	private GVRSceneObject constructBus(GVRContext ctx, double lat, double lon) {
		GVRSceneObject bus = new GVRSceneObject(ctx, busMesh, busTex);
		bus.getTransform().setPosition((float) lat, -50f, (float) lon);
		bus.getTransform().setScale(2.5f, 2.5f, 2.5f);
		return bus;
	}
}
