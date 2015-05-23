package pw.ian.vrtransit;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by ian on 5/23/15.
 */
public class FirebaseHelper {
    static Firebase firebase = new Firebase("https://publicdata-transit.firebaseio.com/sf-muni/vehicles");

    static ArrayBlockingQueue<Map<String, Object>> updates = new ArrayBlockingQueue<Map<String, Object>>(10000);

    static {
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updates.add((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
