package pw.ian.vrtransit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;

import pw.ian.vrtransit.data.BusUpdate;
import pw.ian.vrtransit.data.TransitDataAccessor;

public class MUNIVisualizerScript extends GVRScript {

	private MainActivity core;

	private GVRContext mCtx;

	private GVRAndroidResource busMesh;

	private GVRAndroidResource busTex;

	private GVRBitmapTexture mapTex;

	private TransitDataAccessor tda;

	private GVRSceneObject root;

	private Map<Integer, GVRSceneObject> buses = new HashMap<>();

	public MUNIVisualizerScript(MainActivity core) {
		this.core = core;
	}

	@Override
	public void onInit(GVRContext ctx) throws Throwable {

		mCtx = ctx;
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

		root = new GVRSceneObject(ctx);
		scene.addSceneObject(root);

		GVRSceneObject map = new GVRSceneObject(ctx, 10f, 10f, mapTex);
		map.getTransform().setPosition(0f, 0f, -5f);
		root.addChildObject(map);

		root.addChildObject(constructBus(ctx, 0f, 0f));

	}

	@Override
	public void onStep() {
		List<BusUpdate> bs = tda.nextUpdates();
		for (BusUpdate bu : bs) {
			if (buses.containsKey(bu.getId())) {

				if (bu.remove) {
					GVRSceneObject bus = buses.remove(bu.getId());
					root.removeChildObject(bus);
				} else {
					GVRSceneObject bus = buses.get(bu.getId());
					setBusPos(bus, bu.getLat(), bu.getLon());
				}

			} else {
				GVRSceneObject bus = constructBus(mCtx, bu.getLat(),
						bu.getLon());
				root.addChildObject(bus);
			}
		}
	}

	private GVRSceneObject constructBus(GVRContext ctx, double lat, double lon) {
		GVRSceneObject bus = new GVRSceneObject(ctx, busMesh, busTex);
		setBusPos(bus, lat, lon);
		bus.getTransform().setScale(0.06f, 0.06f, 0.06f);
		return bus;
	}

	public void setBusPos(GVRSceneObject bus, double lat, double lon) {

		// 37.809607, -122.387515
		// 37.734027, -122.514716

		lat = scaleCoord(37.734027f, 37.809607f, (float) lat, 10f);
		lon = scaleCoord(-122.514716f, -122.387515f, (float) lon, 10f);
		bus.getTransform().setPosition((float) lat, (float) lon, -4f);
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
