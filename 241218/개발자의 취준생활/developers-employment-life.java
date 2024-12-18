import java.io.*;
import java.util.*;

/*
    N명의 개발자 - N개의 회사 지원
    각 개발자들이 원하는 연봉과 회사에서 맞춰줄 수 있는 가짓수

    1 <= N <= 20
    1 <= 주어지는 연봉 <= 1_000_000_000

    입력:
    N
    개발자들이 원하는 연봉 값
    회사에서 줄 수 있는 최대 연봉 값
    
    ex)
    4
    1 2 3 4
    2 4 3 4


*/

public class Main {

    static int N, answer = 0;
    static int[] dev, company;
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        dev = new int[N];
        company = new int[N];
        visited = new boolean[N];

        st = new StringTokenizer(br.readLine());
        for(int i=0;i<N;i++) {
            dev[i] = Integer.parseInt(st.nextToken());
        }
        
        st = new StringTokenizer(br.readLine());
        for(int i=0;i<N;i++) {
            company[i] = Integer.parseInt(st.nextToken());
        }

        backtracking(0);
        System.out.println(answer);
    }

    public static void backtracking(int idx) {
        if(idx == N) {
            answer++;
            return;
        }

        for(int i=0;i<N;i++) {
            if(!visited[i] && dev[idx] <= company[i]) {
                visited[i] = true;
                backtracking(idx+1);
                visited[i] = false;
            }
        }
    }
}