package model.entities;

public class Gate {

    private Integer id;
    private Integer entranceGate;
    private Integer exitGate;

    public Gate() {
    }

    public Gate(Integer id, Integer entranceGate, Integer exitGate) {
        this.id = id;
        this.entranceGate = entranceGate;
        this.exitGate = exitGate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntranceGate() {
        return entranceGate;
    }

    public void setEntranceGate(Integer entranceGate) {
        this.entranceGate = entranceGate;
    }

    public Integer getExitGate() {
        return exitGate;
    }

    public void setExitGate(Integer exitGate) {
        this.exitGate = exitGate;
    }
}
