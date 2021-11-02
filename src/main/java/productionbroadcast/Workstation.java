package productionbroadcast;

public enum Workstation {
    A("Drehmaschine Mazák"),
    B("Fräsmaschine MCFV1060"),
    C("Fräsmaschine G. Master"),
    D("Fräsmaschine Huron"),
    E("Fräsmaschine DMG DMF260");

    private final String stationName;

    Workstation(String name) {
        this.stationName = name;
    }

    public String getStationname() {
        return stationName;
    }

    @Override
    public String toString() {
        return this.name() + " " + stationName;
    }
}
