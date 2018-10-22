package in.example.attendance;

/**
 * Created by admin on 30-03-17.
 */
public class students_att_report_list {
    private String date;
    private String h1;
    private String h2;
    private String h3;
    private String h4;
    private int id;
    private String h5;


    public students_att_report_list(String date, String h1, String h2, String h3, String h4, String h5, int id) {
        super();
        this.date=date;
        this.h1=h1;
        this.h2=h2;
        this.h3=h3;
        this.h4=h4;
        this.h5=h5;
        this.id = id;




    }

    public String getDate() {
        return date;
    }
    public void setDate(String nameText) {
        date = nameText;
    }

    public String getH1() {return h1;}
    public void setH1(String nameText) {h1 = nameText;}

    public String getH2() {return h2;}
    public void setH2(String nameText) {h2 = nameText;}

    public String getH3() {return h3;}
    public void setH3(String nameText) {h3 = nameText;}

    public String getH4() {return h4;}
    public void setH4(String nameText) {h4 = nameText;}

    public String getH5() {return h5;}
    public void setH5(String nameText) {h5 = nameText;}


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
