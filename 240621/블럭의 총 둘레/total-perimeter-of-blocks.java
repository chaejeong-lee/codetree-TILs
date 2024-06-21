import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        int N = Integer.parseInt(br.readLine());

        int minX = 100, minY = 100;
        int maxX = 0, maxY = 0;
        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine()," ");
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            minX = minX>x?x:minX;
            minY = minY>y?y:minY;
            maxX = maxX<x?x:maxX;
            maxY = maxY<y?y:maxY;
        }
        System.out.println(((maxX - minX + 1) + (maxY - minY + 1))*2);

    }
}