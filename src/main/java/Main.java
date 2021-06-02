import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
//import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        //BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(GenerateData.BUCKETS_FILE_PATH)));
        /*
        List<List<String>> countList = new ArrayList<>();
        while (bufferedReader.ready()) {
            String[] str = bufferedReader.readLine().split(" ");
            List<String> lis = Arrays.stream(str).collect(Collectors.toList());

            countList.add(new ArrayList<>(lis));
        }

        MultiStage multiStage = new MultiStage(countList);
        List<String> l = multiStage.analyze();
        l.forEach(System.out::println);

         */
        List<List<String>> countList = new ArrayList<>();
        Map<String, ArrayList<String>> bucket = new HashMap<>();
        CSVReader reader = new CSVReader(new FileReader("/Users/evgeniisamarin/Downloads/transactions_by_dept.csv"), ',', '"', 1);
        List<String[]> allRows = reader.readAll();
        int i = 0;
        for(String[] row : allRows){
            String value = row[1].split(":")[1];
            if (!bucket.containsKey(row[0])) {
                ArrayList<String> list = new ArrayList<>();
                list.add(value);
                bucket.put(row[0], list);
                i++;
            } else {
                bucket.get(row[0]).add(value);
            }

        }
        bucket.forEach((k, v) -> {
            countList.add(v);
            System.out.println("Key: " + k + " Value: " + v.toString());
        });
        System.out.println(i);
        System.out.println("----------------");

        MultiStage multiStage = new MultiStage(countList);
        List<String> list = multiStage.analyze();
        list.forEach(System.out::println);
    }
}
