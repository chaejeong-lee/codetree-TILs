import java.io.*;
import java.util.*;

public class Main {

    static int N, B;
    static int[] bulbs;
        

    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());

        bulbs = new int[N];
        for(int i=0;i<N;i++){ 
            bulbs[i] = Integer.parseInt(br.readLine());
        }

        HashMap<String, Integer> states = new HashMap<>();
        List<int[]> list = new ArrayList<>();
        int cycle = -1;

        for(int step = 0; step < B;step++) {
            String cur = Arrays.toString(bulbs);

            if(states.containsKey(cur)) {
                cycle = states.get(cur);
                break;
            }

            states.put(cur, step);
            list.add(bulbs);

            int[] newBulbs = bulbs.clone();
            for(int i=0;i<N;i++) {
                int leftIdx = (i + N - 1) % N;
                if(bulbs[leftIdx] == 1) {
                    newBulbs[i] = 1-bulbs[i];
                }
            }
            bulbs = newBulbs;
        }

        if(cycle != -1) {
            int cycleLen = states.size() - cycle;
            int finalStateIndex = B%cycleLen;
            bulbs = list.get(finalStateIndex);
        }
        
        for(int bulb: bulbs) {
            System.out.println(bulb);
        }
    }
}