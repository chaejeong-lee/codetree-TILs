import java.io.*;
import java.util.*;

public class Main {

    static class Memeber implements Comparable<Member> {
        int r, c, value;
        Set<Character> hs;

        public Member(int r, int c) {
            this.r = r;
            this.c = c;
            hs = new HashSet<>();
        }

        public int getPower() {
            int power = this.value - 1;
            this.value = 1;
            return power;
        }

        @Override
        public int compareTo(Member m) {
            if(this.value != m.value) {
                return Integer.compare(m.value, this.value);
            }
            if(this.r != m.r) {
                return Integer.compare(this.r, m.r);
            }
            return Integer.compare(this.c, m.c);
        }
    }

    static int N, T;
    static Member[][] map;
    static boolean[][] isGrouped, isDamaged;

    static int[] dr = {0, -1, 0, 1};
    static int[] dc = {1, 0, -1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        map = new Member[N+1][N+1];
        isGrouped = new boolean[N+1][N+1];
        isDamaged = new boolean[N+1][N+1];

        for(int i=1;i<=N;i++) {
            String str = br.readLine();

            for(int j=1;j<=N;j++) {
                map[i][j] = new Member(i, j);
                map[i][j].hs.add(line.charAt(j-1));
            }
        }

        for(int i=1;i<=N;i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=1;j<=N;j++) {
                map[i][j].value = Integer.parseInt(st.nextToken());
            }
        }

    }
}