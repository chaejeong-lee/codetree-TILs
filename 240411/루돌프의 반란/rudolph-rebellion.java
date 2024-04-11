import java.util.*;
import java.io.*;

public class Main {

    static class Point implements Comparable<Point> {
        int r, c;

        public Point (int r, int c){
            this.r = r;
            this.c = c;
        }

        @Override
        public int compareTo(Point o){
            if(o.r == r) return o.c - c;
            return o.r - r;
        }
    }

    static class Santa implements Comparable<Santa> {
        int num, r, c, d;

        public Santa(int r, int c){
            this.r = r;
            this.c = c;
        }
        
        public Santa (int num, int r, int c, int d){
            this.num = num;
            this.r = r;
            this.c = c;
            this.d = d;
        }

        @Override
        public int compareTo(Santa o){
            if(this.d == o.d){
                if(this.r == r){
                    return this.c - o.c;
                }
                return this.r - o.r;
            }
            return this.d - o.d;
        }
    }

    private static int N, M, P, C, D;
    private static Point rudolfPoint;
    private static int[][] map;
    private static Santa[] santa;
    private static int[] score;
    private static boolean[] santaDead;
    private static int[] santaStun;
    
    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine()," ");
        N = Integer.parseInt(st.nextToken());   // N : 게임판의 크기
        M = Integer.parseInt(st.nextToken());   // M : 게임 턴 수
        P = Integer.parseInt(st.nextToken());   // P : 산타의 수
        C = Integer.parseInt(st.nextToken());   // C : 루돌프의 힘
        D = Integer.parseInt(st.nextToken());   // D : 산타의 힘

        map = new int[N][N];

        st = new StringTokenizer(br.readLine());
        rudolfPoint = new Point(Integer.parseInt(st.nextToken())-1,Integer.parseInt(st.nextToken())-1);  // startPoint: 루돌프의 시작점
        map[rudolfPoint.r][rudolfPoint.c] = -1;   // 루돌프의 위치

        santa = new Santa[P + 1];   // santa: 산타의 위치 등 정보
        santaDead = new boolean[P + 1]; // santaDead: 산타 죽었는지 유무
        score = new int[P+1];           // score: 산타 점수
        santaStun = new int[P + 1];     // santaStun: 부딪혀서 스턴 걸렸느지..

        for(int p=0;p<P;p++){
            st = new StringTokenizer(br.readLine());
            int santaNum = Integer.parseInt(st.nextToken());
            int santaR = Integer.parseInt(st.nextToken())-1;
            int santaC = Integer.parseInt(st.nextToken())-1;
            santa[santaNum] = new Santa(santaR, santaC);
            map[santaR][santaC] = santaNum;
        }

        // 게임 시작
        for(int i=0;i<M;i++){
            // 0. 끝났는지 조건 확인
            if(isFinished()){
                break;
            }
            // 1. 루돌프의 움직
            rudolfMove();
            // 2. 산타의 움직임
            santaMove();
            // 3. 탈락하지 않은 산타 점수 더해주기
            addScore();
            // 4. 스턴 줄이기
            decreaseStun();
        }

        // 점수 출력
        for(int i=1;i<=P;i++){
            System.out.print(score[i]+ " ");
        }
    }

    // 0. 끝났는지 조건 확인
    private static boolean isFinished(){
        for(int i=1;i<=P;i++){
            if(!santaDead[i]){
                return false;
            }
        }
        return true;
    }

    // 1. 루돌프의 움직임
    private static void rudolfMove() {
        // 가장 근접한 산타 찾기
        Santa nearSanta = nearBySanta();

        // 루돌프가 원래 있던 자리는 0으로
        map[rudolfPoint.r][rudolfPoint.c] = 0;

        // 루돌프 이동할 방향
        int moveRudolfR = 0;
        int moveRudolfC = 0;

        // 루돌프와 산타 r값 비교 후 방향 수정
        if(rudolfPoint.r > nearSanta.r) moveRudolfR = -1;
        else if(rudolfPoint.r < nearSanta.r) moveRudolfR = 1;
        // 루돌프와 산타 c값 비교 후 방향 수정
        if(rudolfPoint.c > nearSanta.c) moveRudolfC = -1;
        else if(rudolfPoint.c < nearSanta.c) moveRudolfC = 1;

        // 루돌프가 이동
        rudolfPoint.r += moveRudolfR;
        rudolfPoint.c += moveRudolfC;

        map[rudolfPoint.r][rudolfPoint.c] = -1;

        // 이동한 루돌프의 좌표가 산타와 같으면 충돌 + 상호작용
        if(rudolfPoint.r == nearSanta.r && rudolfPoint.c == nearSanta.c){
            score[nearSanta.num] += C;
            santaStun[nearSanta.num] = 2;

            int nr = nearSanta.r + moveRudolfR*C;
            int nc = nearSanta.c + moveRudolfC*C;

            // 상호작용(이동한 곳에 산타가 있는 경우 한 칸 뒤로 밀려남)
            interaction(nearSanta.num, nr, nc, moveRudolfR, moveRudolfC);
        }
    }

    // 1-1. 가장 근접한 산타 찾기
    private static Santa nearBySanta() {
        ArrayList<Santa> list = new ArrayList<>();

        for(int i=1;i<=P;i++){
            if(santaDead[i]) continue;

            Santa s = santa[i];

            int dis = (int)(Math.pow(rudolfPoint.r-s.r, 2) + Math.pow(rudolfPoint.c - s.c, 2));
            list.add(new Santa(i, s.r, s.c, dis));
        }
        Collections.sort(list);

        return list.get(0);
    }

    // 1-2. 밀려난 위치에 산타가 잇는 경우 뒤로 밀어냄
    private static void interaction(int num, int r, int c, int moveR, int moveC){
        if(isRange(r, c)){
            if(map[r][c]>0){
                interaction(map[r][c], r+ moveR, c+moveC, moveR, moveC);
            }

            map[r][c] = num;
            santa[num] = new Santa(r, c);
        }else{
            santaDead[num] = true;
        }
    }

    // 2. 산타의 움직임
    private static void santaMove() {
        for(int i=1;i<=P;i++){
            // 기절 or 스턴 => 움직이지X
            if(santaDead[i] || santaStun[i] != 0) continue;

            Santa cur = santa[i];

            // 현재 위치에서 루돌프까지의 최소 거리
            int min = (int)(Math.pow(rudolfPoint.r - cur.r, 2) + Math.pow(rudolfPoint.c - cur.c, 2));
            int dir = -1;

            for(int d = 0; d<4;d++){
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                // 범위를 벗어나거나 산타가 잇거나
                if(!isRange(nr,nc) || map[nr][nc] > 0) continue;

                int dis = (int)(Math.pow(rudolfPoint.r - nr, 2) + Math.pow(rudolfPoint.c - nc, 2));

                if(min > dis){
                    min = dis;
                    dir = d;
                }
            }

            // 원래 산타가 있던 곳은 빈칸으로 변경
            map[cur.r][cur.c] = 0;

            cur.r += dr[dir];
            cur.c += dc[dir];

            // 이동한 칸에 루돌프가있는 경우
            if(cur.r == rudolfPoint.r && cur.c == rudolfPoint.c){
                // 충돌한 산타 점수 D점 증가 + stun 값 증가
                score[i] += D;
                santaStun[i] = 2;

                // 산타가 밀려날 위치
                int nr = cur.r + (-dr[dir]*D);
                int nc = cur.c + (-dc[dir]*D);

                // 밀려난 자리에 산타가 있을 경우 상호작용 필요
                interaction(i, nr, nc, -dr[dir], -dc[dir]);
            } else {
                map[cur.r][cur.c] = i;
            }
        }
    }

    // 3. 탈락하지 않은 산타 점수 더해주기
    private static void addScore() {
        for(int i=1;i<=P;i++){
            if(!santaDead[i]) {
                score[i]++;
            }
        }
    }

    // 4. 스턴 계산하기
    private static void decreaseStun() {
        for(int i=1;i<=P;i++){
            if(santaStun[i] > 0){
                santaStun[i]--;
            }
        }
    }

    // 범위를 벗어났는지
    private static boolean isRange(int r, int c){
        return r >= 0 && r<N && c>=0 && c<N;
    }

    // private static void printMap() {
    //     System.out.println("*******************************");
    //     for(int i=0;i<N;i++){
    //         for(int j=0;j<N;j++){
    //             System.out.print(map[i][j]+" ");
    //         }
    //         System.out.println();
    //     }
    //     System.out.println("*******************************");
    // }
}