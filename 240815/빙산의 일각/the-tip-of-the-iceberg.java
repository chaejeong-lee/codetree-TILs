import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[] H = new int[N];
        int maxHeight = 0;
        int minHeight = Integer.MAX_VALUE;
        int result = 0;

        for(int i=0;i<N;i++) {
            H[i] = Integer.parseInt(br.readLine());
            maxHeight = Math.max(maxHeight, H[i]);
            minHeight = Math.min(minHeight, H[i]);
        }

        for(int i=minHeight; i<maxHeight-1; i++) {
            int cnt = 0;
            boolean check = false;
            for(int j=0;j<N;j++) {
                if(H[j] > i ) {
                    if(!check) {
                        check = true;
                        cnt++;
                    }
                }
                else {
                    check = false;
                }
            }
            result = Math.max(result, cnt);
        }
        System.out.println(result);
    }
}