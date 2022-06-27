package algorithms;

public class AnomalyReport {
    public final String description;
    public final  long timeStep;
    public Point point;
    public AnomalyReport(String description, long timeStep,Point p){
        this.description=description;
        this.timeStep=timeStep;
        this.point=p;
    }
    public AnomalyReport(String description, long timeStep){
        this.description=description;
        this.timeStep=timeStep;
        this.point=null;
    }
}
