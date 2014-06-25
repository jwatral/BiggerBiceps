package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");
        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

//        Schema schemaExercises = new Schema(1, "exercises");
        Entity exerciseSet = schema.addEntity("ExerciseSet");
        exerciseSet.addIdProperty();
        exerciseSet.addIntProperty("duration");
        exerciseSet.addDateProperty("date");
        exerciseSet.addStringProperty("exercise");
        exerciseSet.addStringProperty("muscle");
        exerciseSet.addDoubleProperty("weight");
        exerciseSet.addIntProperty("reps");
        new DaoGenerator().generateAll(schema, args[0]);
//        new DaoGenerator().generateAll(schemaExercises, args[0]);


    }
}
