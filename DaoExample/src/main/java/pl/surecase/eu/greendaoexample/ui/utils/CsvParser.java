package pl.surecase.eu.greendaoexample.ui.utils;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
//import com.googlecode.jmapper.JMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import greendao.ExerciseSet;
import pl.surecase.eu.greendaoexample.backend.csv.CsvBean;
import pl.surecase.eu.greendaoexample.backend.repositories.ExerciseSetRepository;

/**
 * Created by jwatral on 24.06.2014.
 */
public class CsvParser {
    public static List<ExerciseSet> importFromCsvFile(File f) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Reader reader = new FileReader("persons.csv");
        List<ExerciseSet> result = new ArrayList<ExerciseSet>();

        ValueProcessorProvider provider = new ValueProcessorProvider();
        CSVEntryParser<CsvBean> entryParser = new AnnotationEntryParser<CsvBean>(CsvBean.class, provider);
        CSVStrategy strategy = new CSVStrategy(';', '"', '#', true, true);
        CSVReader<CsvBean> csvPersonReader = new CSVReaderBuilder<CsvBean>(reader).strategy(strategy).entryParser(entryParser).build();


        Iterator<CsvBean> it = csvPersonReader.iterator();
        CsvBean previousBean = new CsvBean();
//        JMapper mapper = new JMapper(CsvBean.class, ExerciseSet.class);
        while (it.hasNext()) {
            CsvBean bean = it.next();
            merge(previousBean, bean);

//            result.add((ExerciseSet) mapper.getDestination(previousBean));

            // ...
        }



        return result;
    }

    public static void merge(Object obj, Object update){
        if(!obj.getClass().isAssignableFrom(update.getClass())){
            return;
        }

        Method[] methods = obj.getClass().getMethods();

        for(Method fromMethod: methods){
            if(fromMethod.getDeclaringClass().equals(obj.getClass())
                    && fromMethod.getName().startsWith("get")){

                String fromName = fromMethod.getName();
                String toName = fromName.replace("get", "set");

                try {
                    Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[])null);
                    if(value != null){
                        toMetod.invoke(obj, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
