package in.example.attendance;

/**
 * Created by admin on 30-03-17.
 */
public class class_list {
    private String classes;
    private String section;
    private String image;
    private int classesID;
    private int sectionID;


    public class_list(String classes,String section, int classesID,int sectionID) {
        super();
        this.classes=classes;
        this.classesID = classesID;
        this.section = section;
        this.sectionID = sectionID;


    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String nameText) {
        classes = nameText;
    }


    public int getId() {
        return sectionID;
    }

    public void setId(int id) {
        this.sectionID = id;
    }


    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int id) {
        this.sectionID = id;
    }



    public String getSection() {
        return section;
    }

    public void setSection(String id) {
        this.section = id;
    }


}
