package in.example.attendance;

/**
 * Created by admin on 30-03-17.
 */
public class att_day_list {
    private String subject;
    private String hour;
    private String status;

    private int id;



    public att_day_list(String subject, String hour,String status) {
        super();
        this.subject=subject;
        this.hour=hour;
        this.status=status;





    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String nameText) {
        subject = nameText;
    }

    public String getStatus() {return status;}
    public void setStatus(String nameText) {status = nameText;}

    public String getHour() {return hour;}
    public void setHour(String nameText) {hour = nameText;}



}
