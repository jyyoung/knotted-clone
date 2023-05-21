//import java.io.*;
//import java.util.StringTokenizer;
//
//// 연습용
//public class Main {
//    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
//
//        String input = br.readLine();
//
//        StringTokenizer st = new StringTokenizer(input);
//
//        int n = Integer.parseInt(st.nextToken()); // 임무 총 일수
//        int m = Integer.parseInt(st.nextToken()); // 얻고자 하는 최소 기여도
//
//        // 기여도를 m 이상 얻을 수 있는 방법의 수
//
//        // 전날 선택한 장소와 같은 장소에서 근무 시 기여도는 원래의 절반(이전의 절반이 아님)만 얻을 수 있음
//        // 그냥 브루트포스로 조져보자
//
//        int count = 0; // 가능한 경우
//
//        // 어차피 최대 8일, 최대 6가지 중 고르는 거기 때문에, 160만 번밖에 안 된다.
//        // 또한 중간에 이미 m 이상이 되면 빠져나갈 수 있음
//
//        // 0, 1, 2, 3, 4, 5번으로 인덱스를 넣고,
//
//        for (int i = 0; i < n; i++) {
//
//        }
//
//
//
//    }
//}
