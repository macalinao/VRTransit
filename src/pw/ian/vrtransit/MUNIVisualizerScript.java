package pw.ian.vrtransit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVRRelativeMotionAnimation;

import pw.ian.vrtransit.data.BusUpdate;
import pw.ian.vrtransit.data.TransitDataAccessor;

public class MUNIVisualizerScript extends GVRScript {

	private MainActivity core;

	private GVRContext mCtx;

	private GVRMesh busMesh;

	private GVRBitmapTexture busTex;

	private GVRBitmapTexture mapTex;

	private TransitDataAccessor tda;

	private GVRSceneObject root;

	private Map<String, GVRSceneObject> buses = new HashMap<>();

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

		busMesh = ctx.loadMesh(new GVRAndroidResource(ctx, "sphere.obj"));
		busTex = ctx.loadTexture(new GVRAndroidResource(ctx, "bus.jpg"));
		busTex.setKeepWrapper(true);

		mapTex = ctx.loadTexture(new GVRAndroidResource(ctx, "map2.jpg"));

		root = new GVRSceneObject(ctx);
		scene.addSceneObject(root);

		GVRSceneObject map = new GVRSceneObject(ctx, 10f, 10f, mapTex);
		map.getTransform().setPosition(0f, 0f, -5f);
		root.addChildObject(map);

		initBusObjectPool(100);
	}

	@Override
	public void onStep() {
		List<BusUpdate> bs = tda.nextUpdates();
		for (BusUpdate bu : bs) {
			if (buses.containsKey(bu.getId())) {

				if (bu.remove) {
					GVRSceneObject bus = buses.remove(bu.getId());
				} else {
					GVRSceneObject bus = buses.get(bu.getId());
					smoothSetBusPos(bus, bu.getLat(), bu.getLon());
				}

			} else {
				GVRSceneObject bus = setBusPos(nextBus(), bu.getLat(),
						bu.getLon());
				if (buses.containsValue(bus)) {
					String key = null;
					for (Entry<String, GVRSceneObject> e : buses.entrySet()) {
						if (e.getValue().equals(bus)) {
							key = e.getKey();
							break;
						}
					}
					buses.remove(key);
				}
				buses.put(bu.getId(), bus);
			}
		}
	}

	private Queue<GVRSceneObject> busPool = new LinkedList<>();

	private void initBusObjectPool(int amt) {
		for (int i = 0; i < amt; i++) {
			busPool.add(constructBus(mCtx));
		}
	}

	private GVRSceneObject nextBus() {
		GVRSceneObject bus = busPool.poll();
		busPool.add(bus);
		return bus;
	}

	private GVRSceneObject constructBus(GVRContext ctx) {
		GVRSceneObject bus = new GVRSceneObject(ctx, busMesh, busTex);
		bus.getTransform().setScale(0.05f, 0.05f, 0.05f);
		root.addChildObject(bus);
		return bus;
	}

	public GVRSceneObject smoothSetBusPos(GVRSceneObject bus, double lat,
			double lon) {

		// 37.809607, -122.387515
		// 37.734027, -122.514716

		float dx = scaleCoordX((float) lat, 5f)
				- bus.getTransform().getPositionX();
		float dy = scaleCoordY((float) lon, 5f)
				- bus.getTransform().getPositionY();

		GVRAnimation anim = new GVRRelativeMotionAnimation(bus, 3.0f, dx, dy,
				0f);
		anim.start(mCtx.getAnimationEngine());
		return bus;
	}

	public GVRSceneObject setBusPos(GVRSceneObject bus, double lat, double lon) {

		// 37.809607, -122.387515
		// 37.734027, -122.514716

		lat = scaleCoordX((float) lat, 5f);
		lon = scaleCoordY((float) lon, 5f);
		bus.getTransform().setPosition((float) lat, (float) lon, -5f);
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

	private float scaleCoordX(float val, float extent) {
		return -scaleCoord(37.702100f, 37.814604f, val, extent);
	}

	private float scaleCoordY(float val, float extent) {
		return scaleCoord(-122.553643f, -122.35528f, val, extent);
	}
}
