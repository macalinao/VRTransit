package pw.ian.vrtransit.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class TransitDataAccessor {
	private Firebase ref = new Firebase(
			"https://publicdata-transit.firebaseio.com/sf-muni/vehicles");

	private ArrayBlockingQueue<BusUpdate> pendingUpdates = new ArrayBlockingQueue<>(
			10000);

	public TransitDataAccessor() {

		ref.addChildEventListener(new ChildEventListener() {

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildAdded(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildChanged(DataSnapshot ds, String prevKey) {
				String route = ds.child("routeTag").getValue(String.class);
				double lat = ds.child("lat").getValue(Double.class);
				double lon = ds.child("lon").getValue(Double.class);
				String type = ds.child("type").getValue(String.class);
				pendingUpdates.add(new BusUpdate(route, lat, lon, type));
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub

			}
		});

	}
	
	public List<BusUpdate> initVehicles() {
		Query q = ref.limitToLast(200);
		return null;
	}

	public List<BusUpdate> nextUpdates() {
		List<BusUpdate> ret = new ArrayList<>();
		pendingUpdates.drainTo(ret);
		return ret;
	}
}
