import java.io.*;
import java.util.*;

public class Main {

    static class Info implements Comparable<Info> {
        int loc;
        boolean infection;

        public Info(int loc, boolean infection) {
            this.loc = loc;
            this.infection = infection;
        }

        @Override
        public int compareTo(Info o) {
            return this.loc - o.loc;
        }
    }
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        int N = Integer.parseInt(br.readLine());
        Info[] info = new Info[N];

        for(int i=0;i<N;i++) {
            st = new StringTokenizer(br.readLine());
            int loc = Integer.parseInt(st.nextToken());
            int infec = Integer.parseInt(st.nextToken());
            info[i] = new Info(loc, infec == 1?true:false);
        }

        Arrays.sort(info);

        int cnt = 0;
        int curCnt = 0;
        for(int i=0;i<N;i++) {
            if(info[i].infection) {
                cnt++;
            }
            else {
                if(cnt == 1) {
                    curCnt++;
                }
                else {
                    curCnt += cnt/2;
                }
                cnt = 0;
            }
        }

        if(cnt == 1) {
            curCnt++;
        } 
        else {
            curCnt += cnt/2;
        }
        System.out.println(curCnt);
    }
}