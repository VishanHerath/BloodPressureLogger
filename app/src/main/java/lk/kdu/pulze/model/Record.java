package lk.kdu.pulze.model;

import androidx.annotation.Nullable;

public class Record {
    private long id;
    private String systole;
    private String diastole;
    private String pulse;
    private String dateAndTime;

    public Record() {

    }

    public Record(@Nullable String systole, @Nullable String diastole, @Nullable String pulse, @Nullable String dateAndTime) {
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.dateAndTime = dateAndTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDiastole() {
        return diastole;
    }

    public void setDiastole(String diastole) {
        this.diastole = diastole;
    }

    public String getSystole() {
        return systole;
    }

    public void setSystole(String systole) {
        this.systole = systole;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getPulse() {
        return pulse;
    }

    @Override
    public String toString() {
        return systole;
    }
}
