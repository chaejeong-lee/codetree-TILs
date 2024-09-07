import java.io.*;
import java.util.*;

public class Main {

    static int N, C, G, H;
    static int min, max;
    static int[][] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        G = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        arr = new int[N][2];
        for(int i=0;i<N;i++) {
            st = new StringTokenizer(br.readLine());
            arr[i][0] = Integer.parseInt(st.nextToken());
            arr[i][1] = Integer.parseInt(st.nextToken());
            min = Math.min(min, arr[i][0]);
            max = Math.max(max, arr[i][1]);
        }

        int result = 0;
        for(int i=min; i<=max;i++) {
            int sum = 0;

            for(int j=0;j<N;j++) {
                if(i < arr[j][0]) {
                    sum += C;
                }
                else if(arr[j][0] <= i && i <= arr[j][1]) {
                    sum += G;
                }
                else {
                    sum += H;
                }
            }
            result = Math.max(sum, result);
        }
        System.out.println(result);
    }
}