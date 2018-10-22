package in.example.attendance;

/**
 * Created by admin on 30-03-17.
 */
public class students_report_list {
    private String name;
    private String pname;
    private String regno;
    private String rollno;
    private String phno;
    private int id;
    private String admno;


    public students_report_list(String name, String pname,String regno, String rollno,String phno,String admno, int id) {
        super();
        this.name=name;
        this.pname=pname;
        this.regno=regno;
        this.rollno=rollno;
        this.phno=phno;
        this.admno=admno;
        this.id = id;




    }

    public String getName() {
        return name;
    }
    public void setName(String nameText) {
        name = nameText;
    }

    public String getPhno() {return phno;}
    public void setPhno(String nameText) {phno = nameText;}

    public String getPname() {return pname;}
    public void setPname(String nameText) {pname = nameText;}

    public String getAdmno() {return admno;}
    public void setAdmno(String nameText) {admno = nameText;}

    public String getRollno() {return rollno;}
    public void setRollno(String nameText) {rollno = nameText;}

    public String getRegno() {return regno;}
    public void setRegno(String nameText) {regno = nameText;}


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
