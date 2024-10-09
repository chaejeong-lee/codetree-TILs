import java.io.*;
import java.util.*;

public class Main {
    static class Knight {
        int r, c, h, w, k;
        public Knight(int r, int c, int h, int w, int k) {
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;
        }

        public Knight(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static int L, N, Q;
    static int[][] mapInfo;
    static Knight[] knights, nextRC;
    static int[] damage, originK;
    static boolean[] isVisited;

    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        mapInfo = new int[L][L];
        for(int i=0;i<L;i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<L;j++) {
                mapInfo[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        knights = new Knight[N+1];
        nextRC = new Knight[N+1];
        damage = new int[N+1];
        originK = new int[N+1];
        isVisited = new boolean[N+1];

        for(int i=1;i<=N;i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken())-1;
            int c = Integer.parseInt(st.nextToken())-1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            knights[i] = new Knight(r, c, h, w, k);
            originK[i] = k;
        }

        while(Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            int knightIdx = Integer.parseInt(st.nextToken());
            int knightDir = Integer.parseInt(st.nextToken());

            if(knights[knightIdx].k <= 0) continue;

            if(canMove(knightIdx, knightDir)) {
                // 이동 가능한 경우,실제 위치와 체력을 업데이트
                for(int i=1;i<=N;i++) {
                    knights[i].r = nextRC[i].r;
                    knights[i].c = nextRC[i].c;
                    knights[i].k -= damage[i];
                }
            }
        }

        int answer = 0;
        for(int i=1; i<=N;i++) {
            if(knights[i].k>0) {
                answer += (originK[i]-knights[i].k);
            }
        }
        System.out.println(answer);
    }

    public static boolean canMove(int idx, int dir) {
        Queue<Integer> q = new ArrayDeque<>();
        boolean check = true;

        for(int i=1;i<=N;i++) {
            damage[i] = 0;
            isVisited[i] = false;
            nextRC[i] = new Knight(knights[i].r, knights[i].c);
        }

        q.add(idx);
        isVisited[idx] = true;

        while(!q.isEmpty()) {
            int cur = q.poll();

            nextRC[cur].r += dr[dir];
            nextRC[cur].c += dc[dir];

            if(nextRC[cur].r < 0 || nextRC[cur].c < 0 || nextRC[cur].r + knights[cur].h - 1 >= L || nextRC[cur].c + knights[cur].w -1 >= L ) continue;

            // 대상 조각들이 벽과 충돌 or 장애물과 충돌
            for(int i=nextRC[cur].r; i<nextRC[cur].r + knights[cur].h;i++) {
                for(int j=nextRC[cur].c;j<nextRC[cur].c + knights[cur].w;j++) {
                    if(mapInfo[i][j] == 1) damage[cur]++;
                    if(mapInfo[i][j] == 2) return false; 
                }
            }

            // 대상 조각들이 다른 조각들과 충돌
            for(int i=1;i<=N;i++) {
                if(isVisited[i] || knights[i].k <= 0) continue;
                if(knights[i].r > nextRC[cur].r + knights[cur].h - 1 || nextRC[cur].r > knights[i].r + knights[i].h - 1) continue;
                if(knights[i].c > nextRC[cur].c + knights[cur].w - 1 || nextRC[cur].c > knights[i].c + knights[i].c - 1) continue;
                
                isVisited[i] = true;
                q.add(i);
            }
        }
        damage[idx] = 0;
        return true;
    }
}