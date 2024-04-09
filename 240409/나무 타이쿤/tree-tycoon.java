import java.util.*;
import java.io.*;

class Pair {
    int r, c;

    public Pair(int r, int c){
        this.r = r;
        this.c = c;
    }
}

public class Main {
    private static final int DIR_NUM = 8;
    private static final int MAX_N = 15;

    private static int n, m;
    private static int[][] map;
    private static boolean[][] visited = new boolean[MAX_N][MAX_N];
    private static boolean[][] nextVisited = new boolean[MAX_N][MAX_N];

    //  → ↗ ↑ ↖ ← ↙ ↓ ↘
    private static int[] dr = {0, -1, -1, -1, 0, 1, 1, 1};
    private static int[] dc = {1, 1, 0, -1, -1, -1, 0, 1};

    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine()," ");
        n = Integer.parseInt(st.nextToken());   // n: 격자의 크기
        m = Integer.parseInt(st.nextToken());   // m: 리브로수를 키우는 총 년 수

        map = new int[n][n];
        for(int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine()," ");
            for(int j=0;j<n;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 초기 설정
        for(int i=n-2;i<n;i++){
            for(int j=0;j<2;j++){
                visited[i][j] = true;
            }
        }

        // 이동 규칙 출력
        for(int i=0;i<m;i++){
            st = new StringTokenizer(br.readLine());
            int dir = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            simulate(dir-1, p);
        }

        System.out.println(getScore());
    }

    private static void simulate(int dir, int p){
        // 1. 특수 영양제 이동
        move(dir, p);
        // 2. 특수 영양제 위치에 있는 리브로수 성장
        grow();
        // 3. 대각선 방향의 높이가 1 이상인 리브로수 만큼 더 성장
        diagonalGrow();
        // 4. 새로운 특수 영양제를 추가하고, 기존 영양제 없애기
        determineFert();
    }

    // 1. 특수 영양제 이동
    private static void move(int dir, int p){
        // 1. fert위치를 저장할 nextFert 초기화
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                nextVisited[i][j] = false;
            }
        }

        // 2. 영양제 이동
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(visited[i][j]){
                    Pair next = nextPos(i, j, dir, p);
                    int nr = next.r;
                    int nc = next.c;
                    nextVisited[nr][nc] = true;
                }
            }
        }

        // 3. nextvisited -> visited로 이동
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                visited[i][j] = nextVisited[i][j];
            }
        }
    }

    private static Pair nextPos(int r, int c, int d, int p){
        int nr = (r + dr[d]*p + n*p)%n;
        int nc = (c + dc[d]*p + n*p)%n;
        return new Pair(nr, nc);
    }

    // 2. 특수 영양제 위치에 있는 리브로수 성장
    private static void grow() {
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(visited[i][j]) map[i][j]++;
            }
        }
    }

    // 3. 대각선 리브로수 증가
    private static void diagonalGrow(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(visited[i][j]){
                    int cnt = getDiagCnt(i, j);
                    map[i][j] += cnt;
                }
            }
        }
    }

    private static int getDiagCnt(int r, int c){
        int cnt = 0;
        for(int d=1;d<DIR_NUM;d+=2){
            int nr = r + dr[d];
            int nc = c + dc[d];
            if(isRange(nr, nc) && map[nr][nc]>0) cnt++;
        }
        return cnt;
    }

    private static boolean isRange(int r, int c){
        return 0<=r && 0<=c && r<n && c<n;
    }

    // 4. 새로운 특수 영양제 추가 + 기존 영양제 제거
    private static void determineFert(){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                // 기존 영양제 제거
                if(visited[i][j]) visited[i][j] = false;
                // 특수 영양제 추가
                else if(map[i][j]>1){
                    visited[i][j] = true;
                    map[i][j] -= 2;
                }
            }
        }
    }

    private static int getScore(){
        int sum = 0; 
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                sum += map[i][j];
            }
        }
        return sum;
    }
}