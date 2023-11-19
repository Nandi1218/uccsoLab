import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;

public class TagCounter extends org.xml.sax.helpers.DefaultHandler{

    enum state{
        defaultState,
        node,
        tag
    }
    static double lat;
    static double lon;
    private state currentState = state.defaultState;
    static HashMap<String,Integer> tagCount = new HashMap<String,Integer>();
    public static void main(String[] args) {
        String filename = "test.xml";
        if(args.length==4 && args[0].equals("-i")) {
            filename = args[1];
             lat = Double.parseDouble(args[2]);
             lon = Double.parseDouble(args[3]);
        }

        DefaultHandler h = new TagCounter();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser p = factory.newSAXParser();
            p.parse(new java.io.File(filename), h);
        } catch (Exception e) {e.printStackTrace();}
        for ( String key : tagCount.keySet()) {
            System.out.println(key + ": " + tagCount.get(key));

        }
        busStops.sort((o1, o2) -> {if(o1.getDistance()>o2.getDistance())return 1;else if(o1.getDistance()<o2.getDistance())return -1;else return 0;});
        for (BusStop busStop:busStops)
            if(busStop.getOldName().equals("")&&busStop.getWheelchair().equals(""))
                System.out.println("Megálló:\n\t"+busStop.getName() + "\n\tTávolság: " + busStop.getDistance());
            else if(busStop.getOldName().equals(""))
                System.out.println("Megálló:\n\t"+busStop.getName() + "\n\tKerekesszék: " + busStop.getWheelchair() + "\n\tTávolság: " + busStop.getDistance());
            else if(busStop.getWheelchair().equals(""))
                System.out.println("Megálló:\n\t"+busStop.getName() + " (" + busStop.getOldName() + ")\n\tTávolság: " + busStop.getDistance());
            else
            System.out.println("Megálló:\n\t"+busStop.getName() + " (" + busStop.getOldName() + ")\n\tKerekesszék: " + busStop.getWheelchair() + "\n\tTávolság: " + busStop.getDistance());
    }
    public BusStop busStop = null;
    public static ArrayList<BusStop> busStops = new ArrayList<BusStop>();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(tagCount.containsKey(qName)){
            tagCount.put(qName,tagCount.get(qName)+1);
        }else{
            tagCount.put(qName,1);
        }
        switch (currentState) {
            case defaultState:

                if (qName.equals("node")) {
                    busStop = new BusStop();
                    busStop.setDistance(dist1(Double.parseDouble(attributes.getValue("lat")),Double.parseDouble(attributes.getValue("lon")),lat,lon));
                    busStop.setValid(false);
                    currentState = state.node;
                }
            case node:
                if (qName.equals("tag")) {
                    if (attributes.getValue("v").equals("bus_stop")) {
                        busStop.setValid(true);
                    }
                    if (attributes.getValue("k")!=null && attributes.getValue("k").equals("old_name")) {
                        busStop.setOldName(attributes.getValue("v"));
                    }
                    if (attributes.getValue("k").equals("name")) {
                        busStop.setName(attributes.getValue("v"));
                    }
                    if (attributes.getValue("k").equals("wheelchair")) {
                        busStop.setWheelchair(attributes.getValue("v"));
                    }
                    if(attributes.getValue("k").equals("name")||attributes.getValue("k").equals("wheelchair")||attributes.getValue("k").equals("old_name")) {
                        if(busStop.getValid()) {
                            busStops.add(busStop);
                        }
                        currentState = state.defaultState;
                    }
                }
        }
    }
    public double dist1(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // metres
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dphi = phi2-phi1;
        double dl = Math.toRadians(lon2-lon1);
        double a = Math.sin(dphi/2) * Math.sin(dphi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(dl/2) * Math.sin(dl/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }




}
