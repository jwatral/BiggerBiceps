package pl.surecase.eu.greendaoexample.backend.csv;

import com.googlecode.jcsv.annotations.MapToColumn;

import java.util.Date;

/**
 * Created by jwatral on 24.06.2014.
 */
public class CsvBean {
    @MapToColumn(column=0)
    private Integer id;

    @MapToColumn(column=1)
    private Integer duration;

    @MapToColumn(column=2)
    private Date date;

    @MapToColumn(column=3)
    private String exercise;

    @MapToColumn(column=4)
    private String muscle;

    @MapToColumn(column=5)
    private Double weight;

    @MapToColumn(column=6)
    private Integer reps;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CsvBean csvBean = (CsvBean) o;

        if (date != null ? !date.equals(csvBean.date) : csvBean.date != null) return false;
        if (duration != null ? !duration.equals(csvBean.duration) : csvBean.duration != null)
            return false;
        if (exercise != null ? !exercise.equals(csvBean.exercise) : csvBean.exercise != null)
            return false;
        if (id != null ? !id.equals(csvBean.id) : csvBean.id != null) return false;
        if (muscle != null ? !muscle.equals(csvBean.muscle) : csvBean.muscle != null) return false;
        if (reps != null ? !reps.equals(csvBean.reps) : csvBean.reps != null) return false;
        if (weight != null ? !weight.equals(csvBean.weight) : csvBean.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (exercise != null ? exercise.hashCode() : 0);
        result = 31 * result + (muscle != null ? muscle.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (reps != null ? reps.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public String getExercise() {
        return exercise;
    }

    public String getMuscle() {
        return muscle;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }
}
