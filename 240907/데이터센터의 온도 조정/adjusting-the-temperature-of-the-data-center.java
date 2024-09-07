import java.io.*;
import java.util.*;

public class Main {

    static int N, C, G, H;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        G = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        HashMap<Integer, Integer> hm = new HashMap<>();
        int[][] arr = new int[N][2];
        for(int i=0;i<N;i++) {
            st = new StringTokenizer(br.readLine());
            arr[i][0] = Integer.parseInt(st.nextToken());
            arr[i][1] = Integer.parseInt(st.nextToken());
            for(int j = arr[i][0]; j<=arr[i][1];j++) {
                if(hm.containsKey(j)) {
                    hm.put(j, hm.get(j) + 1);
                }
                else {
                    hm.put(j, 1);
                }
            }
        }

        int temp = 0, maxCnt = 0;
        for (Integer key: hm.keySet()) {
			int value = hm.get(key);
            if(value > maxCnt) {
                temp = key;
                maxCnt = value;
            }
		}

        int sum = 0;
        for(int i=0;i<N;i++) {
            if(arr[i][0] > temp) {
                sum += C;
            }else if(temp <=arr[i][1]) {
                sum += G;
            }else {
                sum += H;
            }
        }

        System.out.println(sum);
    }
}