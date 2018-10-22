package in.example.attendance;

/**
 * Created by admin on 30-03-17.
 */
public class students_info_list {
    private String c_name;
    private String phno;
    private String location1;
    private String location2;
    private String image;
    private int id;
    private boolean selected;


    public students_info_list(String c_name, String phno,int id) {
        super();
        this.c_name=c_name;
        this.id = id;


        this.phno = phno;

    }

    public String getC_name() {
        return c_name;
    }
    public void setC_name(String nameText) {
        c_name = nameText;
    }

    public String getPhno() {return phno;}
    public void setPhno(String nameText) {phno = nameText;}



    public boolean getSelected() {
        return selected;
    }
    public void setSelected(boolean id) {
        this.selected = id;
    }
    public boolean isSelected() {
        return selected;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
