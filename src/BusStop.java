public class BusStop {
     String name = "";
     String oldName ="";
     String wheelchair ="";
     boolean valid = false;
     double distance = 0;

    public  void setValid(boolean valid) {
        this.valid = valid;
    }

    public  void setName(String name) {
        this.name = name;
    }

    public  void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public  void setWheelchair(String wheelchair) {
        this.wheelchair = wheelchair;
    }
    boolean getValid(){
        return valid;
    }

    public String getName() {
        return name;
    }

    public String getOldName() {
        return oldName;
    }

    public String getWheelchair() {
        return wheelchair;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
