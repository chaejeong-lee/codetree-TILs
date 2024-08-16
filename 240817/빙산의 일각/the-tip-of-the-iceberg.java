import java.io.*;
import java.util.*;

public class Main {
    static class Point implements Comparable<Point> {
        int idx, h;

        public Point(int idx, int h) {
            this.idx = idx;
            this.h = h;
        }

        @Override
        public int compareTo(Point o) {
            if(this.h == o.h) {
                return this.idx - o.idx;
            }
            return o.h - this.h;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        List<Point> list = new ArrayList<>();
        boolean[] visited = new boolean[N];
        int result = 0;

        for(int i=0;i<N;i++) {
            int h = Integer.parseInt(br.readLine());
            list.add(new Point(i, h));
        }

        Collections.sort(list);

        int curH = list.get(0).h-1;
        int cnt = 0;

        for(Point p: list) {
            visited[p.idx] = true;

            if(curH != (p.h-1)) {
                result = Math.max(result, cnt);
                curH = p.h - 1;
            }
            
            if(p.idx == 0) {
                if(!visited[p.idx+1]){
                    cnt++;
                }
            }
            else if(p.idx == N-1) {
                if(!visited[p.idx-1]){
                    cnt++;
                }
            }
            else {
                // 양쪽에 빙산이 다 있는 경우
                if(visited[p.idx - 1] && visited[p.idx+1]) {
                    cnt--;
                }
                // 양쪽에 빙산이 모두 없는 경우
                else if(!visited[p.idx-1] && !visited[p.idx+1]) {
                    cnt++;
                }
            }
        }

        System.out.println(result);
    }
}