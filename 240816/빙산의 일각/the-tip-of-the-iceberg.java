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
            return this.h - o.h;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        // int[] H = new int[N];
        PriorityQueue<Point> pq = new PriorityQueue<>();
        int maxHeight = 0;
        int minHeight = Integer.MAX_VALUE;
        int result = 0;

        for(int i=0;i<N;i++) {
            int h = Integer.parseInt(br.readLine());
            pq.add(new Point(i, h));
            maxHeight = Math.max(maxHeight, h);
            minHeight = Math.min(minHeight, h);
        }

        for(int i=minHeight; i<=maxHeight; i++) {
            int cnt = 1;

            // System.out.println("--------- " +i + " ------------");
            while(!pq.isEmpty()) {
                if(pq.peek().h == i) {
                    pq.poll();
                    cnt++;
                }
                else {
                    break;
                }
            }
            result = Math.max(result, cnt);
        }
        System.out.println(result);
    }
}