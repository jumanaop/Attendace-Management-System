package in.example.attendance;

public class Spn_Adapter_ID_NAME {

    private int classesID;
    private String classes;

    public Spn_Adapter_ID_NAME(){}

    public Spn_Adapter_ID_NAME(int classesID, String classes){
        this.classesID = classesID;
        this.classes = classes;
    }

    public void setId(int classesID){
        this.classesID = classesID;
    }

    public void setName(String classes){
        this.classes = classes;
    }

    public int getId(){
        return this.classesID;
    }

    public String getName(){
        return this.classes;
    }

}
