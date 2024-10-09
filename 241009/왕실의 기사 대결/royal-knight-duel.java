import java.io.*;
import java.util.*;

public class Main {

    static class Knight {
        int r, c, h, w, k, damage;

        public Knight(int r, int c, int h, int w, int k, int damage) {
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;
            this.damage = damage;
        }

        public void changeDamage(int damage) {
            this.damage = damage;
        }
    }

    static int L, N, Q;
    static int[][] map;
    static Knight[] knights;
    static boolean[] notAlive;
    
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());       // L: 맵 크기
        N = Integer.parseInt(st.nextToken());       // N: 기사의 수
        Q = Integer.parseInt(st.nextToken());       // Q: 턴 수

        map = new int[L][L];

        for(int i=0;i<L;i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<L;j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        knights = new Knight[N+1];
        notAlive = new boolean[N+1];

        for(int i=1; i<=N;i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken())-1;
            int c = Integer.parseInt(st.nextToken())-1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            knights[i] = new Knight(r, c, h, w, k, 0);
        }

        while(Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            int knightNum = Integer.parseInt(st.nextToken());
            int knightDir = Integer.parseInt(st.nextToken());

            // 기사 이동 -> 단 기사가 움직이지 못한 경우....
            boolean[] visited = new boolean[N+1];
            int[][] knightsMap = new int[L][L];
            if(!moveKnight(knightsMap, knightNum, knightDir, visited)) continue;

            // 함정 안에 있는 기사들의 체력 데미지!
            damageKnight(knightsMap, visited);
        }

        int answer = 0;
        for(int i=1;i<=N;i++) {
            if(!notAlive[i]) {
                // 살아있으면~
                // System.out.println(i+" : "+knights[i].damage);
                answer += knights[i].damage;
            }
        }
        System.out.println(answer);
    }

    // 기사 데미지
    public static void damageKnight(int[][] knightsMap, boolean[] visited) {
        // printMap(knightsMap);
        for(int i=0;i<L;i++) {
            for(int j=0;j<L;j++) {
                int curNum = knightsMap[i][j];
                if(curNum == 0 || map[i][j] != 1) continue;
                if(notAlive[curNum]) continue;

                // System.out.println(i+" "+j);
                knights[curNum].k--;
                knights[curNum].damage++;
                if(knights[curNum].k == 0) notAlive[curNum] = true;
            }
        }
    }

    // 기사 움직이기
    public static boolean moveKnight(int[][] knightsMap, int knightNum,int knightDir, boolean[] visited) {
        int[][] newKnightsMap = new int[L][L];

        Knight[] newKnights = new Knight[N+1];

        Queue<Integer> moveNums = new LinkedList<>();
        int cnt = 0;

        for(int i=1;i<=N;i++) {
            Knight cur = knights[i];
            int r = knights[i].r;
            int c = knights[i].c;

            for(int j=0;j<knights[i].h;j++) {
                for(int k=0;k<knights[i].w;k++) {
                    knightsMap[r+j][c+k] = i;
                }
            }
        }

        // 밀었을 때 해당 칸에 새로운 번호가 잇는 경우 -> 해당 번호에 해당하는 값 아래로 밀어버리기
        // 밀다가 벽이 있는 경우 지우기 or 범위를 벗어나는 경우 못 밀어서 return 해주기
        // 밀어낸 애들 visited로 체크해주기
        int moveR = knights[knightNum].r + dr[knightDir];
        int moveC = knights[knightNum].c + dc[knightDir];
        newKnights[knightNum] = new Knight(moveR, moveC, knights[knightNum].h, knights[knightNum].w, knights[knightNum].k, knights[knightNum].damage);

        for(int i=0;i<knights[knightNum].h;i++) {
            for(int j=0;j<knights[knightNum].w;j++) {
                if(!isRange(moveR+i, moveC+j)) continue;

                newKnightsMap[moveR+i][moveC+j] = knightNum;

                if(knightsMap[moveR+i][moveC+j] == 0 || knightsMap[moveR+i][moveC+j] == knightNum) continue;
                else {
                    visited[knightsMap[moveR+i][moveC+j]] = true;
                    moveNums.add(knightsMap[moveR+i][moveC+j]);
                    cnt++;
                }
            }
        }

        if(cnt == 0) return false;

        // 밀어내서 map에 표시하다가 벽을 만나거나
        while(!moveNums.isEmpty()){
            int curNum = moveNums.poll();
            visited[curNum] = true;

            int curR = knights[curNum].r + dr[knightDir];
            int curC = knights[curNum].c + dc[knightDir];
            if(visited[curNum]) {
                for(int j=0;j<knights[curNum].h;j++) {
                    for(int k=0;k<knights[curNum].w;k++) {
                        int nextR = curR + j;
                        int nextC = curC + k;

                        if(!isRange(nextR, nextC)) return false;
                        if(map[nextR][nextC] == 2) return false;

                        newKnightsMap[nextR][nextC] = curNum;

                        if(!visited[knightsMap[nextR][nextC]] && knightsMap[nextR][nextC] != knightNum && knightsMap[nextR][nextC] != 0) {
                            moveNums.add(knightsMap[nextR][nextC]);
                        }
                    }
                }
                
                newKnights[curNum] = new Knight(curR, curC, knights[curNum].h, knights[curNum].w, knights[curNum].k, knights[curNum].damage);
            }
        }

        for(int i=1;i<=N;i++) {
            if(!visited[i] && i != knightNum) {
                for(int j=0;j<knights[i].h;j++) {
                    for(int k=0;k<knights[i].w;k++) {
                        int nextR = knights[i].r + j;
                        int nextC = knights[i].c + k;

                        newKnightsMap[nextR][nextC] = i;
                    }
                }
            }
        }

        for (int i = 1; i <= N; i++) {
            if (knights[i] == null && newKnights[i] != null) {
                knights[i] = new Knight(newKnights[i].r, newKnights[i].c, newKnights[i].h, newKnights[i].w, newKnights[i].k, newKnights[i].damage);
            }
        }

        copyMap(knightsMap, newKnightsMap);

        return true;
    }

    public static void copyMap(int[][] copy, int[][] originMap) {
        for(int i=0;i<L;i++) {
            for(int j=0;j<L;j++) {
                copy[i][j] = originMap[i][j];
            }
        }
    }

    public static boolean isRange(int r, int c) {
        return 0<=r && r<L && 0<=c && c<L;
    }

    public static void printMap(int[][] map) {
        System.out.println("========================");
        for(int i=0;i<map.length;i++) {
            for(int j=0;j<map[i].length;j++) {
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("========================");
    }
}