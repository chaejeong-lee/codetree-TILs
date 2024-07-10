import java.io.*;
import java.util.*;

public class Main {

    public static int N;
    public static int[] bomb;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        bomb = new int[N];
        for(int i=0;i<N;i++) {
            bomb[i] = Integer.parseInt(br.readLine());
        }
        Arrays.sort(bomb);
        
        int answer = 0;
        for(int i=0;i<N;i++) {
            int cnt = solution(i);
            answer = Math.max(answer, cnt);
        }
        System.out.println(answer);
    }

    public static int solution(int idx) {
        int bombSize = 1;
        int answer = 1;

        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[N];
        q.add(bomb[idx]);
        visited[idx]= true;

        while(!q.isEmpty()) {
            int cur = q.poll();
            int bombBefore = cur - bombSize;
            int bombAfter = cur + bombSize;

            // 1. 범위 idx 안에 속해야 함
            // 2. 방문한 적이 없어야 한다.
            // 3. 범위 안에 속한 폭탄이 있는 경우 q에 넣기
            for(int i=0;i<N;i++) {
                if(0 <= bombBefore && bombAfter < N) {
                    // System.out.println("cur: " + cur +" / "+bombBefore+" / "+bombAfter);
                    if(!visited[i] && bombBefore <= bomb[i] && bomb[i] <= bombAfter){
                        q.add(bomb[i]);
                        answer++;
                    }
                }
            }
            bombSize++;
        }

        return answer;
    }
}