import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MultiStage {

    private final List<List<Integer>> list;
    private final Map<Integer, Integer> hashMap;
    private final Map<String, Integer> map;
    private final Map<Integer, String> mapString;

    private static final int SUPPORT = 80;

    public MultiStage(List<List<String>> list) {
        hashMap = new HashMap<>();
        map = new HashMap<>();
        mapString = new HashMap<>();
        this.list = listInitialization(list);
    }

    public List<List<Integer>> listInitialization(List<List<String>> list) {
        int k = 1;
        List<List<Integer>> answerList = new ArrayList<>();

        for (List<String> a : list) {
            for (String str : a) {
                if (!map.containsKey(str)) {
                    map.put(str, k);
                    mapString.put(k, str);
                    k++;
                }
                if (!hashMap.containsKey(map.get(str))) {
                    hashMap.put(map.get(str), 0);
                }
                int count = hashMap.get(map.get(str));
                hashMap.remove(map.get(str));
                hashMap.put(map.get(str), count + 1);
            }
        }

        for (List<String> a : list) {
            List<Integer> localList = new ArrayList<>();
            for (String str : a) {
                localList.add(map.get(str));
            }
            answerList.add(localList);
        }
        return answerList;
    }

    public List<String> analyze() {
        List<Pair> doublePairs = multiStage();
        List<String> answerString = new ArrayList<>();
        for (int i = 1; i <= hashMap.size(); i++) {
            if (hashMap.get(i) >= SUPPORT) {
                String str = mapString.get(i);
                answerString.add(str);
            }
        }

        for (Pair pair : doublePairs) {
            String str = mapString.get(pair.getFirst()) + " " + mapString.get(pair.getSecond());
            answerString.add(str);
        }
        return answerString;
    }

    public List<Pair> multiStage() {
        List<Pair> pairList = new ArrayList<>();
        for (List<Integer> list : list) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    pairList.add(new Pair(list.get(i), list.get(j)));
                }
            }
        }

        List<Set<Pair>> list = integerListMap(pairList, hashMap.size(), 1, 1);
        List<Pair> currentParList = getGoodPair(list, pairList);
        list = integerListMap(currentParList, hashMap.size(), 1, 2);
        currentParList = getGoodPair(list, pairList);
        List<Pair> answerList = new ArrayList<>();
        Set<Pair> answerSet = new HashSet<>();

        for (Pair pair : currentParList) {
            int x = hashMap.get(pair.getFirst());
            int y = hashMap.get(pair.getSecond());
            if (!(x < SUPPORT || y < SUPPORT)) {
                if (answerSet.stream().noneMatch(k -> k.getFirst() == pair.getFirst() && k.getSecond() == pair.getSecond())) {
                    answerSet.add(pair);
                    answerList.add(pair);
                }
            }
        }
        return answerList;
    }

    private List<Pair> getGoodPair(List<Set<Pair>> list, List<Pair> pairList) {
        Set<Pair> goodPairs = new HashSet<>();
        List<Pair> currentParList = new ArrayList<>();
        for (Set<Pair> pairs : list) {
            Iterator<?> iterator = pairs.iterator();
            int currentSum = 0;
            while (iterator.hasNext()) {
                Pair pair = (Pair) iterator.next();
                currentSum += pair.getCount();
            }
            if (currentSum >= SUPPORT) {
                goodPairs.addAll(pairs);
            }
        }

        for (Pair pair : pairList) {
            if (goodPairs.stream().anyMatch(k -> k.getFirst() == pair.getFirst() && k.getSecond() == pair.getSecond())) {
                currentParList.add(pair);
            }
        }
        return currentParList;
    }

    public List<Set<Pair>> integerListMap(List<Pair> list, int hash, int cofFirst, int cofSecond) {
        List<Set<Pair>> answerList = new ArrayList<>();
        for (int i = 0; i < hash; i++) {
            answerList.add(new HashSet<>());
        }
        for (Pair pair : list) {
            int hashPair = (cofFirst * pair.getFirst() + cofSecond * pair.getSecond()) % hash;
            Iterator<?> iterator = answerList.get(hashPair).iterator();
            int k = 0;
            while (iterator.hasNext()) {
                Pair pairNext = (Pair) iterator.next();
                if (pairNext.getFirst() == pair.getFirst() && pairNext.getSecond() == pair.getSecond()) {
                    k = pairNext.getCount();
                }
            }
            answerList.get(hashPair).removeIf(key -> key.getFirst() == pair.getFirst() && key.getSecond() == pair.getSecond());
            pair.setCount(k + 1);
            answerList.get(hashPair).add(pair);
        }
        return answerList;
    }


     /*

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("D:\\DataMaining\\DataMining_gvozdev_11-904\\MultiStageAlgorithm\\src\\data\\data.txt"));
        int count = scanner.nextInt();

        int[] arr = new int[count + 1];
        List<List<Integer>> lists = new ArrayList<>();
        lists.add(new ArrayList<>());


        while (scanner.hasNext()){
            String s = scanner.next();
            if (!s.equals(";")){
                arr[Integer.parseInt(s)]++;
                lists.get(lists.size() - 1).add(Integer.parseInt(s));
            } else {
                lists.add(new ArrayList<>());
            }
        }

        int support = 3;

        Set<Integer> set = new HashSet<>();

        for (int i = 1; i < arr.length; i++){
            if (arr[i] >= support){
                set.add(i);
            }
        }

        class Pair{
            int a, b, count1, hash1, hash2;

            public Pair(int a, int b) {
                this.a = a;
                this.b = b;
                count1 = 0;
                hash1 = (a + b) % count;
                hash2 = (a + 2 * b) % count;
            }

            @Override
            public boolean equals(Object obj){
                return this.a == ((Pair)(obj)).a && this.b == ((Pair)(obj)).b ||
                        this.a == ((Pair)(obj)).b && this.b == ((Pair)(obj)).a;
            }

            @Override
            public int hashCode(){
                return Integer.hashCode(a) + Integer.hashCode(b);
            }
        }
        Set<Pair> pairs = new HashSet<>();
        Map<Pair, Integer> pairIntegerMap = new HashMap<>();

        for(int i = 0; i < 8; i++){
            List<Integer> list = lists.get(i);
            for (int j = 0; j < list.size(); j++){
                for (int k = j + 1; k < list.size(); k++){
                    Pair pair = new Pair(list.get(j), list.get(k));
                    pairIntegerMap.put(pair,
                            pairIntegerMap.get(pair) != null ?
                                    pairIntegerMap.get(pair) + 1 : 1);
                }
            }
        }

        List<List<Pair>> buckets1 = new ArrayList();
        int[] pairCount1 = new int[count + 1];
        List<List<Pair>> buckets2 = new ArrayList();
        int[] pairCount2 = new int[count + 1];

        for (int i = 0; i < count; i++){
            List<Pair> pairList = new ArrayList<>();

            for(Pair pair: pairIntegerMap.keySet()){
                if (pair.hash1 == i){
                    pairList.add(pair);
                    pairCount1[i] += pairIntegerMap.get(pair);
                }
            }
            buckets1.add(pairList);
        }

        for (int i = 0; i < count; i++){
            if (pairCount1[i] >= support){
                pairs.addAll(buckets1.get(i));

            }
        }

        for (int i = 0; i < count; i++){
            List<Pair> pairList = new ArrayList<>();

            for(Pair pair: pairs){
                if (pair.hash2 == i){
                    pairList.add(pair);
                    pairCount2[i] += pairIntegerMap.get(pair);
                }
            }
            buckets2.add(pairList);
        }

        pairs.clear();
        for (int i = 0; i < count; i++){
            if (pairCount2[i] >= support){
                for (Pair pair: buckets2.get(i))
                    if (set.contains(pair.a) && set.contains(pair.b)) {
                        pairs.add(pair);
                    }
            }
        }

        System.out.println("count: " + (set.size() + pairs.size()));

        for (Integer integer: set){
            System.out.println("{" + integer + "}");
        }

        for (Pair p: pairs){
            System.out.println("{" + p.a + ", " + p.b + "}");
        }


    }

      */
}
