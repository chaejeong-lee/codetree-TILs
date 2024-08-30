import java.io.*;
import java.util.*;

/*
    n개의 숫자 -> 각 숫자: 각 번호에서 이동 가능한 번호
    어떻게 이동해도 loop에 빠지지 않는 서로 다른 번호의 수를 구해라
    (0: 이동할 곳 X)
*/

public class Main {

    static int N;
    static int[] arr;
    static boolean[] visited, isDone;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        arr = new int[N+1];
        visited = new boolean[N+1];
        isDone = new boolean[N+1];
        for(int i=1;i<=N;i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        for(int i=1; i<=N;i++) {
            if(arr[i] != 0 && !isDone[i]) {
                dfs(i);
            }
        }

        int cnt = 0;
        for(int i=1; i<=N;i++) {
            if(!isDone[i]) cnt++;
        }

        System.out.println(cnt);
    }

    public static void dfs(int num) {
        if(arr[num] != 0 && visited[num]) {
            for(int i=1; i<=N; i++) {
                if(visited[i]) {
                    isDone[i] = true;
                }
            }
        }
        visited[num] = true;

        if(arr[num] != 0 && !isDone[arr[num]]) {
            dfs(arr[num]);
        }
        visited[num] = false;
    }
}