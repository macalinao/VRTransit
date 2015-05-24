package pw.ian.vrtransit;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;

import pw.ian.vrtransit.data.TransitDataAccessor;

public class MUNIVisualizerScript extends GVRScript {
	
	private MainActivity core;
	
	private GVRAndroidResource busMesh;
	
	private GVRAndroidResource busTex;
	
	private GVRBitmapTexture mapTex;
	
	private TransitDataAccessor tda;

	public MUNIVisualizerScript(MainActivity core) {
		this.core = core;
	}

	@Override
	public void onInit(GVRContext ctx) throws Throwable {
		
		tda = new TransitDataAccessor();

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
		
		mapTex = ctx.loadTexture(new GVRAndroidResource(ctx, "map2.jpg"));
		
		GVRSceneObject root = new GVRSceneObject(ctx);
		scene.addSceneObject(root);

		GVRSceneObject map = new GVRSceneObject(ctx, 10f, 10f, mapTex);
		map.getTransform().setPosition(0f, 0f, -5f);
		root.addChildObject(map);
		
		root.addChildObject(constructBus(ctx, 0f, 0f));

	}

	@Override
	public void onStep() {
	}

	private GVRSceneObject constructBus(GVRContext ctx, double lat, double lon) {
		GVRSceneObject bus = new GVRSceneObject(ctx, busMesh, busTex);
		
		//37.809607, -122.387515
		//37.734027, -122.514716

//		lat = scaleCoord(37.734027f, 37.809607f, (float) lat, 50f);
//		lon = scaleCoord(-122.514716f, -122.387515f, (float) lon, 50f);
		
		bus.getTransform().setPosition((float) lat, (float) lon, -4f);
		bus.getTransform().setScale(0.06f, 0.06f, 0.06f);
		return bus;
	}
	
	/**
	 * Scales a coordinate
	 * 
	 * @param min
	 * @param max
	 * @param val
	 * @param extent
	 * @return
	 */
	private float scaleCoord(float min, float max, float val, float extent) {
		float fmin = Math.min(min, max);
		float fmax = Math.max(min, max);
		
		float diff = fmax - fmin;
		float scale = (val - fmin) / diff;
		return (extent * 2 * scale) - extent;
	}
}
