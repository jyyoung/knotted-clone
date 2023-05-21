//package com.knotted.service;
//
//import java.io.*;
//import java.util.*;
//
//// 연습용
//public class Main {
//    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
//
//        int[] t1 = new int[3];
//        HashMap<String, Integer> t2 = new HashMap<>(); // 해시맵을 사용해야 문자열형, 정수형 같은 형태로 저장이 가능
//
//        for (int i = 0; i < 3; i++) {
//            String input = br.readLine();
//
//            StringTokenizer st = new StringTokenizer(input);
//
//            int p = Integer.parseInt(st.nextToken());
//            int y = Integer.parseInt(st.nextToken());
//            String s = st.nextToken();
//
//            t1[i] = y % 100;
//            t2.put(s, p);
//        }
//
//        Arrays.sort(t1); // 오름차순 정렬
//        String t1_r = "";
//        for(int v : t1){
//            t1_r += + v;
//        }
//
//        List<Map.Entry<String, Integer>> listEntries = new ArrayList<>(t2.entrySet());
//
//        Collections.sort(listEntries, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o2.getValue().compareTo(o1.getValue()); // 내림차순 정렬
//            }
//        });
//        String t2_r = "";
//        for(Map.Entry<String, Integer> entry : listEntries){
//            t2_r += entry.getKey().charAt(0);
//        }
//
//        bw.write(t1_r + "\n");
//        bw.write(t2_r);
//
//        bw.flush();
//        bw.close();
//
//    }
//}
