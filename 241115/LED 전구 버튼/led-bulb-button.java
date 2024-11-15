import java.io.*;
import java.util.*;

public class Main {

    static int N, B;
    static boolean[] lights;

    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());

        lights = new boolean[N];
        for(int i=0;i<N;i++){ 
            lights[i] = Integer.parseInt(br.readLine())==1?true: false;
        }

        // 처음과 동일하게 나오는지 체크
        int turn = sameTurnCheck();
        if(turn == B) {
            for(boolean cur: lights) {
                System.out.println(cur?1:0);
            }
        }
        else {
            // turn 횟수 만큼 나눠서 계산
            B %= turn;

            while(B-- > 0) {
                boolean[] curLights = lights.clone();
                // 왼쪽이 1이면 켜주기
                for(int i=0;i<N;i++) {
                    curLights[i] = lights[(i + N-1)%N]? !lights[i]:lights[i];
                }
                lights = curLights.clone();
            }

            for(boolean cur: lights) {
                System.out.println(cur?1:0);
            }
        }
    }

    public static int sameTurnCheck() {
        boolean[] copyLights = lights.clone();

        int turn = 0;
        while(true) {
            boolean[] curLights = copyLights.clone();
            // 왼쪽이 1이면 켜주기
            for(int i=0;i<N;i++) {
                curLights[i] = copyLights[(i + N-1)%N]? !copyLights[i]:copyLights[i];
            }

            copyLights = curLights.clone();

            turn++;
            if(turn == B) {
                lights = curLights.clone();
                return turn;
            }
            if(Arrays.equals(copyLights, lights)) break;
        }
        
        return turn;
    }
}