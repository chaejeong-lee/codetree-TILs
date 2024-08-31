import java.io.*;
import java.util.*;

public class Main {

    static int N;
    static HashSet<String> name;
    static int answer = 1;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        int N = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        name = new HashSet<>();
        for(int i=0; i<3;i++) {
            name.add(st.nextToken());
        }

        for(int i=1;i<N;i++) {
            st = new StringTokenizer(br.readLine());
            int cnt = 0;
            for(int j=0; j<3;j++) {
                if(name.contains(st.nextToken())) cnt++;
            }
            if(cnt == 3) answer++;

        }
        System.out.println(answer);
    }
}