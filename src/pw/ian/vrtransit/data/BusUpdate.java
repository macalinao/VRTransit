package pw.ian.vrtransit.data;

public class BusUpdate {
	private int id;

	private String route;

	private double lat;

	private double lon;

	private String type;

	public boolean remove = false;

	public BusUpdate(int id, String route, double lat, double lon, String type) {
		this.id = id;
		this.route = route;
		this.lat = lat;
		this.lon = lon;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getRoute() {
		return route;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getType() {
		return type;
	}

}
