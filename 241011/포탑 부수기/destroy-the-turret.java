import java.io.*;
import java.util.*;

public class Main {
	static class Point{
		int r, c, damage, attackTurn;
		
		public Point(int r, int c) {
			this.r = r;
			this.c = c;
		}

		public Point(int r, int c, int damage, int attackTurn) {
			this.r = r;
			this.c = c;
			this.damage = damage;
			this.attackTurn = attackTurn;
		}

		@Override
		public String toString() {
			return "Point [r=" + r + ", c=" + c + ", damage=" + damage + ", attackTurn=" + attackTurn + "]";
		}
	}
	
	static int N, M, K, curTurn;
	static int[][] map;
	static boolean isCheck = false;
	static Point[] turrets;
	static Point choiceTurret, attackTurret;
	static Queue<Point> lazerTurrets;
	static int size, choiceIdx;
	
	static int[] dr = {0, 1, 0, -1, -1, 1, 1, -1};
	static int[] dc = {1, 0, -1, 0, 1, 1, -1, -1};

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		turrets = new Point[101];
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<M;j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] != 0) {
					turrets[size] = new Point(i, j, map[i][j], 0);
					size++;
				}
			}
		}
		
		while(curTurn++ != K) {
//			System.out.println("----------- "+curTurn+"번째 턴 ----------- ");
			if(!checkTurrets()) break;
//            for(Point p: turrets) {
//            	if(p == null) continue;
//                System.out.println(curTurn+" => "+p.toString());
//            }

			
			// 1. 공격자 포탑 선정
			chooseTurret();
			// 2. 공격 당할 포탑 선정
			chooseAttackTurret();
			
			// 2-1. 공격자의 공격(레이저 공격, 포탑 공격)
			// 공격 받은 애들 queue에 담아두기
			attackLaser();

			boolean[][] isTurretAttack = new boolean[N][M];
			if(!isCheck) {
				attackBomb(isTurretAttack);
			} else {
				// 공격 받은 애들 값 출력
				isTurretAttack[choiceTurret.r][choiceTurret.c] = true;
				for(Point p: lazerTurrets) {
					isTurretAttack[p.r][p.c] = true;
					if(p.r == attackTurret.r && p.c == attackTurret.c) {
						map[p.r][p.c] -= choiceTurret.damage;
					}else {
						map[p.r][p.c] -= choiceTurret.damage/2;
					}
					if(map[p.r][p.c]<0) map[p.r][p.c] = 0; 
					isTurretAttack[p.r][p.c] = true;
				}
			}
			// 3. 포탑 부수기(그냥 0인거 죽이는 거라 무시)
			// 4. 포탑 정비(turrets초기화 하고 맵체크해서 다시 넣기)
			for(int i=0;i<N;i++) {
				for(int j=0;j<M;j++) {
					if(!isTurretAttack[i][j] && map[i][j] != 0) {
						map[i][j]++;
					}
				}
			}
//            printMap(map);

		}
		
		int answer = 0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				answer = Math.max(answer, map[i][j]);
			}
		}
		System.out.println(answer);
	}
	
	public static boolean checkTurrets() {
		int cnt = 0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(map[i][j] != 0) {
					cnt++;
				}
				for(int t=0;t<size;t++) {
		        	if(turrets[t] == null) continue;
					if(turrets[t].r == i && turrets[t].c == j) {
						turrets[t] = new Point(i, j, map[i][j], turrets[t].attackTurn);
					}
				}
			}
		}

        return cnt>=2?true:false;
	}
	
	public static void attackBomb(boolean[][] isTurretAttack) {
//        System.out.println("폭탄으로 공격!");
		int curR = attackTurret.r;
		int curC = attackTurret.c;
		
		
		int attackDamage = choiceTurret.damage;
		map[curR][curC] = (map[curR][curC]-attackDamage<0?0:map[curR][curC]-attackDamage);
		isTurretAttack[curR][curC] = true;
		isTurretAttack[choiceTurret.r][choiceTurret.c] = true;
		
		attackDamage /= 2;
		for(int d = 0; d<8;d++) {
			int nr = (curR + dr[d]<0? N-1:((curR + dr[d])%N));
			int nc = (curC + dc[d]<0? M-1:((curC + dc[d])%M));
			isTurretAttack[nr][nc] = true;
			map[nr][nc] = map[nr][nc]-attackDamage<0?0:map[nr][nc]-attackDamage;
		}
	}
	
	public static void attackLaser() {		
		boolean[][] visited = new boolean[N][M];
		lazerTurrets = new ArrayDeque<>();
		Stack<Point> curQ = new Stack<>();
		isCheck = false;

		visited[choiceTurret.r][choiceTurret.c] = true;
		dfs(choiceTurret.r, choiceTurret.c, visited, curQ, 0);
	}
	
	public static void dfs(int r, int c, boolean[][] visited, Stack<Point> curQ, int depth) {
		if(lazerTurrets.size() != 0 && lazerTurrets.size() < depth) return;
		
		if(r == attackTurret.r && c == attackTurret.c) {
			isCheck = true;

			// 처음 들어왔으면 그냥 배열에 넣어주기
			if(lazerTurrets.size() == 0) {
				lazerTurrets.clear();
				for(Point p: curQ) {
					lazerTurrets.add(new Point(p.r, p.c));
				}
			}
			// 두번째 부터는 들어온 큐의 크기 비교해서 적으면 넣어주기.+ 같으면 변경해줄 필요 없음.
			else {
				if(lazerTurrets.size() > depth) {
					lazerTurrets.clear();
					for(Point p: curQ) {
						lazerTurrets.add(new Point(p.r, p.c));
					}
				}
			}
			return;
		}
		
		for(int d = 0; d<4;d++) {
			int nr = (r + dr[d]<0? N-1:((r + dr[d])%N));
			int nc = (c + dc[d]<0? M-1:((c + dc[d])%M));
			
			if(map[nr][nc] != 0 && !visited[nr][nc] ) {
				curQ.add(new Point(nr, nc));
				visited[nr][nc] = true;
				dfs(nr, nc, visited, curQ, depth+1);
				curQ.pop();
				visited[nr][nc] = false;
			}
		}
	}

	public static void chooseTurret() {
		choiceIdx = 0;
		choiceTurret = new Point(0, 0, 5015, 0);
		
		for(int i=0;i<size;i++) {
			if(turrets[i] == null) continue;
			if(turrets[i].damage == 0) continue;
			// 1. 공격력이 가장 낮은 경우
			if(choiceTurret.damage < turrets[i].damage)continue;
			
			if(choiceTurret.damage > turrets[i].damage) {
//				System.out.println("갱신");
				choiceTurret = turrets[i];
				choiceIdx = i;
				continue;
			}
			
			// 2. 같은 경우 attackTurn이 더 큰 경우
			if(choiceTurret.attackTurn > turrets[i].attackTurn) continue;
			if(choiceTurret.attackTurn < turrets[i].attackTurn) {
				choiceTurret = turrets[i];
				choiceIdx = i;
				continue;
			}
			
			
			// 3. 같은 경우 행 + 열 최대인 경우
			int choiceSum = choiceTurret.r + choiceTurret.c;
			int turretSum = turrets[i].r + turrets[i].c;
			if(choiceSum > turretSum) continue;
			if(choiceSum < turretSum) {
				choiceTurret = turrets[i];
				choiceIdx = i;
				continue;
			}
			
			// 4. 같은 경우 열의 위치가 큰 것
			if(choiceTurret.c > turrets[i].c) continue;
			if(choiceTurret.c < turrets[i].c) {
				choiceTurret = turrets[i];
				choiceIdx = i;
				continue;
			}

		}
	}
	
	public static void chooseAttackTurret() {
		attackTurret = new Point(0, 0, 0, 1001);
		
		for(int i=0;i<size;i++) {
			if(turrets[i] == null) continue;
			if(turrets[i].damage == 0) continue;
			
			// 1. 공격력이 가장 높은 경우
			if(attackTurret.damage > turrets[i].damage) continue;
			
			if(attackTurret.damage < turrets[i].damage) {
//				System.out.println("갱신");
				attackTurret = turrets[i];
				continue;
			}
			
			// 2. 같은 경우 attackTurn이 더 큰 경우
			if(attackTurret.attackTurn < turrets[i].attackTurn) continue;
			if(attackTurret.attackTurn > turrets[i].attackTurn) {
//                System.out.println("변경~");
				attackTurret = turrets[i];
				continue;
			}
			
			
			// 3. 같은 경우 행 + 열 최대인 경우
			int choiceSum = attackTurret.r + attackTurret.c;
			int turretSum = turrets[i].r + turrets[i].c;
			if(choiceSum < turretSum) continue;
			if(choiceSum > turretSum) {
				attackTurret = turrets[i];
				continue;
			}
			
			// 4. 같은 경우 열의 위치가 큰 것
			if(attackTurret.c < turrets[i].c) continue;
			if(attackTurret.c > turrets[i].c) {
				attackTurret = turrets[i];
				continue;
			}

		}
//		System.out.println("공격자 포탑: "+choiceTurret.toString());
		
		choiceTurret.damage += (N+M);
		map[choiceTurret.r][choiceTurret.c] += (N+M); 
		choiceTurret.attackTurn = curTurn;
		turrets[choiceIdx].attackTurn = curTurn;
		
//		System.out.println("공격자 포탑 공격과, 대미지 값 변경: "+choiceTurret.toString());
//		System.out.println("공격 받을 포탑: "+attackTurret.toString());
	}
	
	public static void printMap(int[][] map) {
		System.out.println("-----------------");
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[i].length;j++) {
				System.out.print(map[i][j]+"\t");
			}
			System.out.println();
		}
		System.out.println("-----------------");
	}
}