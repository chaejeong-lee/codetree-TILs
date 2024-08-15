import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[] H = new int[N];
        int maxHeight = 0;
        int result = 0;

        for(int i=0;i<N;i++) {
            H[i] = Integer.parseInt(br.readLine());
            maxHeight = Math.max(maxHeight, H[i]);
        }

        for(int i=1; i<maxHeight-1; i++) {
            int cnt = 0;
            boolean check = false;
            for(int k: H) {
                if(!check) {
                    if(k > i) {
                        check = true;
                        cnt++;
                    }
                }
                else {
                    check = k > i;
                }
            }
            
            result = Math.max(result, cnt);
        }
        System.out.println(result);
    }
}