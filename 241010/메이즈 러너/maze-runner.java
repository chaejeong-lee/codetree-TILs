import java.io.*;
import java.util.*;

public class Main {
    static class Point implements Comparable<Point> {
        int r, c, len;
        
        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public Point(int r, int c, int len) {
            this.r = r;
            this.c = c;
            this.len = len;
        }

        @Override
        public int compareTo(Point o) {
            if(this.len == o.len) {
                if(this.r == o.r) {
                    return this.c - o.c;
                }
                return this.r - o.r;
            }
            return this.len - o.len;
        }
    }

    static int N, M, K;
    static int[][] info;
    static int moveCnt = 0;
    static Point exit;
    static Point[] participants;
    static boolean[] isExit;

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static int[] rotDr = {-1, -1, 1, 1};
    static int[] rotDc = {-1, 1, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        // 초기화
        info = new int[N][N];
        participants = new Point[N];
        isExit = new boolean[N];

        // maze의 벽 내구성 입력
        for(int i=0;i<N;i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++) {
                info[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 참가자 좌표 입력
        for(int i=0;i<M;i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken())-1;
            int c = Integer.parseInt(st.nextToken())-1;
            participants[i] = new Point(r, c);
        }

        // 출구 좌표 입력
        st = new StringTokenizer(br.readLine());
        int r = Integer.parseInt(st.nextToken())-1;
        int c = Integer.parseInt(st.nextToken())-1;
        exit = new Point(r, c);

        // 게임 시작
        while(K-- > 0) {
            // System.out.println(8-K+"번째 ------------ ");
            // 1. 1초마다 사용자 동시에 움직임 시작.
            moveParticipants();
            // 2. 맵 돌리기
            rotateMaze();
            // printMap(info);
        }
        System.out.println(moveCnt);
        System.out.println((exit.r+1)+" "+(exit.c+1));
    }

    // 맵 돌리기
    public static void rotateMaze() {
        // printMap(info);
        int nearIdx = 0;
        int minDist = Integer.MAX_VALUE;

        for(int i=0;i<M;i++) { 
            int dist = Math.abs(participants[i].r - exit.r) + Math.abs(participants[i].c - exit.c);
            if(dist != 0 && minDist > dist && !isExit[i]) {
                nearIdx = i;
                minDist= dist;
            }
        }

        int maxR = Math.max(participants[nearIdx].r, exit.r);
        int minR = Math.min(participants[nearIdx].r, exit.r);
        int maxC = Math.max(participants[nearIdx].c, exit.c);
        int minC = Math.min(participants[nearIdx].c, exit.c);
        // System.out.println("min, max => minDist : "+minDist);
        // System.out.println(minR+" "+minC+" , "+maxR+" "+maxC + " => "+(maxR - minDist) +" "+(maxC - minDist));

        int curR = (maxR-minDist) < 0? 0: maxR - minDist;
        int curC = (maxC - minDist) < 0 ? 0 : maxC - minDist;

        // System.out.println(curR+" / "+curC+" "+(curR+minDist)+" / "+(curC+minDist));
        rotatedMaze(curR, curC, curR+minDist, curC+minDist, minDist+1);

        // Point start = new Point(participants[nearIdx].r, participants[nearIdx].c);
        // int dir = 0;
        // for(int d = 0; d<4;d++) {
        //     int nr = exit.r + rotDr[d]*minDist;
        //     int nc = exit.c + rotDc[d]*minDist;

        //     if(!isRange(nr, nc)) continue;
        //     else {
        //         start = new Point(nr, nc);
        //         dir = d;
        //         break;
        //     }
        // }

        // int startR = Math.min(start.r, exit.r);
        // int startC = Math.min(start.c, exit.c);
        // int endR = startR + minDist;
        // int endC = startC + minDist;
        // System.out.println(startR+" " + startC + " "+ endR+" "+endC);
        // rotatedMaze(startR, startC, endR, endC, minDist+1);
    }

    // 맵 실제로 돌리기
    public static void rotatedMaze(int startR, int startC, int endR, int endC, int len) {
        // System.out.println("시작점: "+startR +" "+startC + " / 출구점: "+endR+" "+endC+" / len: "+len);
        // System.out.println("출구: "+exit.r+" "+exit.c);
        int[][] copy = new int[len][len];
        
        for(int i=0;i<len;i++) {
            for(int j=0;j<len;j++) {
                copy[i][j] = info[i+startR][j+startC];
            }
        }
        
        // 참가자 index에 10 더해서 삽입해주기.
        for(int i=0;i<M;i++) {
            if(isExit[i]) continue;
            if(startR <= participants[i].r && startC <= participants[i].c && participants[i].r <=endR && participants[i].c <= endC) {
                copy[Math.abs(participants[i].r-startR)][Math.abs(participants[i].c-startC)] = i+10;
            }
        }

        //출구 삽입
        copy[Math.abs(exit.r - startR)][Math.abs(exit.c - startC)] = 999;

        // 맵 돌리고 1이상 9이하면 -1 해주기
        int[][] rotateMap = new int[len][len];
        for(int i=0;i<len;i++) {
            for(int j=0;j<len;j++) {
                if(1<=copy[i][j] && copy[i][j]<=9) rotateMap[j][len-1-i] = copy[i][j]-1;
                else rotateMap[j][len-1-i] = copy[i][j];
            }
        }

        for(int i=0;i<len;i++) {
            for(int j=0;j<len;j++) {
                int mapR = i + startR;
                int mapC = j + startC;
                if(0<=rotateMap[i][j] && rotateMap[i][j]<=9){
                    info[mapR][mapC] = rotateMap[i][j];
                }
                if(rotateMap[i][j] == 999) {
                    exit = new Point(mapR, mapC);
                    info[mapR][mapC] = 0;
                }
                if(10<=rotateMap[i][j] && rotateMap[i][j]<=20){
                    participants[rotateMap[i][j]-10] = new Point(mapR, mapC);
                    info[mapR][mapC] = 0;
                }
                
            }
        }
        // printMap(rotateMap);
    }

    // 참가자들 움직이기
    public static void moveParticipants() {
        boolean[] visited = new boolean[M];
        
        for(int i=0;i<M;i++) {
            if(isExit[i]) continue;

            int curDist = Math.abs(exit.r - participants[i].r) + Math.abs(exit.c - participants[i].c);
            for(int d = 0; d<4;d++) {

                int nr = participants[i].r + dr[d];
                int nc = participants[i].c + dc[d];

                int nDist = Math.abs(exit.r - nr) + Math.abs(exit.c - nc);

                // 범위를 벗어났거나 움직였을 때 
                if(!isRange(nr,nc)) continue;
                if(info[nr][nc] != 0) continue;
                if(curDist <= nDist) continue;
                participants[i] = new Point(nr, nc);
                // System.out.println(i+". "+participants[i].r+", "+participants[i].c+" -> "+nr+", "+nc);
                if(!visited[i]) {
                    // System.out.println("변경");
                    visited[i] = true;
                    moveCnt++;
                }

                if(nr == exit.r && nc == exit.c ) {
                    // System.out.println(i+ " 탈출");
                    isExit[i] = true;
                }
            }
        }
    }

    public static boolean isRange(int r, int c) {
        return 0<=r && r<N && 0<=c && c<N;
    }

    // 출력하기
    public static void printMap(int[][] map) {
        System.out.println("**********************");
        for(int i=0;i<map.length;i++) {
            for(int j=0;j<map[i].length;j++) {
                System.out.print(map[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println("**********************");
    }
}