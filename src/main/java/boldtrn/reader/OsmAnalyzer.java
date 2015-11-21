package boldtrn.reader;

import com.graphhopper.reader.OSMWay;

import java.util.*;

/**
 * Created by robin on 21/11/15.
 */
public class OsmAnalyzer {

    protected static final Set<String> restrictedHighwayTags = getRestrictedHighwayTags();
    protected static final Set<String> pedestrianHighwayTags = getPedestrianHighwayTags();
    protected static final Map<String, Short> carSpeedMap = getCarSpeedMap();
    protected static final Set<String> sidewalkTags = getSidewalkTags();

    public static final short pedestrianMaxSpeed = 7;
    public static final short carMaxSpeed = 127;


    public static short getSpeed(OSMWay way) {
        String maxSpeed = way.getTag("maxspeed");

        if (maxSpeed == null || maxSpeed.isEmpty())
            return carSpeedMap.getOrDefault(way.getTag("highway"), (short) 1);

        try {
            return Short.parseShort(maxSpeed);
        } catch (NumberFormatException e) {
            if ("none".equals(maxSpeed))
                return carMaxSpeed;

            if (maxSpeed.equals("walk") || maxSpeed.endsWith(":living_street"))
                return pedestrianMaxSpeed;
            return carSpeedMap.getOrDefault(way.getTag("highway"), (short) 1);
        }
    }

    public static boolean wayIsCarAllowed(OSMWay way) {
        String highway = way.getTag("highway");
        if(carSpeedMap.containsKey(highway))
            return true;
        //Logger.info("Not a valid Car Way: "+way);
        return false;
    }

    public static boolean wayIsPedestrianAllowed(OSMWay way) {
        String highway = way.getTag("highway");
        if (pedestrianHighwayTags.contains(highway))
            return true;

        if (wayIsCarAllowed(way)) {
            String sidewalk = way.getTag("sidewalk");
            return sidewalkTags.contains(sidewalk);
        }
        return false;
    }

    public static boolean wayIsOneway(OSMWay way) {
        return way.hasTag("oneway", "yes") || way.hasTag("highway", "motorway") || way.hasTag("junction", "roundabout")
                || way.hasTag("highway", "motorway_link")
                || way.hasTag("highway", "trunk_link")
                || way.hasTag("highway", "primary_link");
    }

    public static boolean isValid(OSMWay way) {
        if (way.getNodes().size() < 2)
            return false;

        if (!way.hasTags())
            return false;

        String highway = way.getTag("highway");
        if (highway == null || restrictedHighwayTags.contains(highway))
            return false;

        // TODO Consider more complex filtering
        return true;
    }

    protected static Set<String> getPedestrianHighwayTags(){
        Set<String> pedestrianHighwayTags = new HashSet<>();

        pedestrianHighwayTags.add("living_street");
        pedestrianHighwayTags.add("pedestrian");
        pedestrianHighwayTags.add("track");
        pedestrianHighwayTags.add("footway");
        pedestrianHighwayTags.add("steps");
        pedestrianHighwayTags.add("path");
        pedestrianHighwayTags.add("residential");
        pedestrianHighwayTags.add("service");
        pedestrianHighwayTags.add("cycleway");
        pedestrianHighwayTags.add("unclassified");
        pedestrianHighwayTags.add("road");

        return pedestrianHighwayTags;
    }

    protected static Set<String> getSidewalkTags(){
        Set<String> sidewalkTags = new HashSet<>();

        sidewalkTags.add("both");
        sidewalkTags.add("left");
        sidewalkTags.add("right");
        sidewalkTags.add("yes");

        return sidewalkTags;
    }

    protected static Map<String, Short> getCarSpeedMap(){
        Map<String, Short> carSpeedMap = new HashMap<>();

        // autobahn
        carSpeedMap.put("motorway", (short) 100);
        carSpeedMap.put("motorway_link", (short) 70);
        carSpeedMap.put("motorroad", (short) 90);
        // bundesstraße
        carSpeedMap.put("trunk", (short) 70);
        carSpeedMap.put("trunk_link", (short) 65);
        // linking bigger town
        carSpeedMap.put("primary", (short) 65);
        carSpeedMap.put("primary_link", (short) 60);
        // linking towns + villages
        carSpeedMap.put("secondary", (short) 60);
        carSpeedMap.put("secondary_link", (short) 50);
        // streets without middle line separation
        carSpeedMap.put("tertiary", (short) 50);
        carSpeedMap.put("tertiary_link", (short) 40);
        carSpeedMap.put("unclassified", (short) 30);
        carSpeedMap.put("residential", (short) 30);
        // spielstraße
        carSpeedMap.put("living_street", (short) 5);
        carSpeedMap.put("service", (short) 20);
        // unknown road
        carSpeedMap.put("road", (short) 20);
        // forestry stuff
        carSpeedMap.put("track", (short) 15);

        return carSpeedMap;
    }

    protected static Set<String> getRestrictedHighwayTags(){
        return new HashSet<>(Arrays.asList("private", "ford", "no", "restricted", "military"));
    }

}
