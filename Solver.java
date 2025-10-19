package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solver implements Runnable{
	
	ArrayList<ArrayList<ArrayList<Integer>>> sols;
	ArrayList<ArrayList<Integer>> map;
	SolverPanel panel;
	
	boolean[][] rowBitMask = new boolean[9][9];  //[number][value]
	boolean[][] colBitMask = new boolean[9][9];  //[number][value]
	boolean[][][] recBitMask = new boolean[3][3][9];  //[line][col][value]
	
	public Solver(ArrayList<ArrayList<ArrayList<Integer>>> sols, ArrayList<ArrayList<Integer>> map, SolverPanel p) {
		this.sols = sols;
		this.map = map;
		panel = p;
	}
	
	
	void initBitMask(ArrayList<ArrayList<Integer>> map) {
		
		for(int val = 1; val <= 9; val++) {
			for(int i = 0; i < map.size(); i++) {
				ArrayList<Integer> line = map.get(i);
				if(line.contains(val)) {
					rowBitMask[i][val-1] = true;
				}
			}
			
			for(int i = 0; i < 9; i++) {
				ArrayList<Integer> col = new ArrayList<>();
				for(int j = 0; j < map.size(); j++) {
					ArrayList<Integer> line = map.get(j);
					col.add(line.get(i));
				}
				if(col.contains(val)) {
					colBitMask[i][val-1] = true;
				}
			}
			
			for(int i = 0; i < 3; i++) {
				ArrayList<Integer> line1 = map.get(3*i);
				ArrayList<Integer> line2 = map.get(3*i+1);
				ArrayList<Integer> line3 = map.get(3*i+2);
				for(int j = 0; j < 3; j++) {
					ArrayList<Integer> rec = new ArrayList<>();
					for(int h = 0; h < 3; h++) {
						int num = line1.get(3*j + h);
						rec.add(num);
					}
					for(int h = 0; h < 3; h++) {
						int num = line2.get(3*j + h);
						rec.add(num);
					}
					for(int h = 0; h < 3; h++) {
						int num = line3.get(3*j + h);
						rec.add(num);
					}
					if(rec.contains(val)) {
						recBitMask[i][j][val-1] = true;
					}
				}
			}
			
			
		}
		
	}
	
	
	ArrayList<ArrayList<Integer>> copy(ArrayList<ArrayList<Integer>> map){
		ArrayList<ArrayList<Integer>> mapCopy = new ArrayList<>();
		for(int i = 0; i < map.size(); i++) {
			ArrayList<Integer> line = map.get(i);
			ArrayList<Integer> lineCopy = new ArrayList<>();
			for(int j = 0; j < line.size(); j++) {
				int val = line.get(j);
				lineCopy.add(val);
			}
			mapCopy.add(lineCopy);
		}
		
		return mapCopy;
	}
	
	
	boolean verifyInput(ArrayList<ArrayList<Integer>> map) {
		
		for(int i = 0; i < map.size(); i++) {
			ArrayList<Integer> line = map.get(i);
			ArrayList<Integer> pureLine = new ArrayList<>();
			for(int j = 0; j < line.size(); j++) {
				int num = line.get(j);
				if(num != 0) {
					pureLine.add(num);
				}
			}
			Set<Integer> lineSet = new HashSet<>(pureLine);
			if(pureLine.size() - lineSet.size() != 0) {
				return false;
			}
		}
		
		for(int i = 0; i < 9; i++) {
			ArrayList<Integer> pureCol = new ArrayList<>();
			for(int j = 0; j < map.size(); j++) {
				int num = map.get(j).get(i);
				if(num != 0) {
					pureCol.add(num);
				}
			}
			Set<Integer> colSet = new HashSet<>(pureCol);
			if(pureCol.size() - colSet.size() != 0) {
				return false;
			}
		}
		
		for(int i = 0; i < 3; i++) {
			ArrayList<Integer> line1 = map.get(3*i);
			ArrayList<Integer> line2 = map.get(3*i+1);
			ArrayList<Integer> line3 = map.get(3*i+2);
			for(int j = 0; j < 3; j++) {
				ArrayList<Integer> pureRec = new ArrayList<>();
				for(int h = 0; h < 3; h++) {
					int num = line1.get(3*j + h);
					if(num != 0) {
						pureRec.add(num);
					}
				}
				for(int h = 0; h < 3; h++) {
					int num = line2.get(3*j + h);
					if(num != 0) {
						pureRec.add(num);
					}
				}
				for(int h = 0; h < 3; h++) {
					int num = line3.get(3*j + h);
					if(num != 0) {
						pureRec.add(num);
					}
				}
				Set<Integer> recSet = new HashSet<>(pureRec);
				if(pureRec.size() - recSet.size() != 0) {
					return false;
				}
			}
		}
		return true;
		
		
	}
	
	int[] searchBestStart(ArrayList<ArrayList<Integer>> map) {
		int min = 10;
		int[] cor = {0, 0};
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				int nums = 0;
				for(int h = 1; h <= 9; h++) {
					if(valPoss(h, i, j, map)) {
						nums++;
					}
				}
				if(nums < min) {
					min = nums;
					cor[0] = i;
					cor[1] = j;
				}
			}
		}
		return cor;
	}
	
	
	boolean valPoss(int val, int line, int col, ArrayList<ArrayList<Integer>> map) {
		
		int recLine = line/3;
		int recCol = col/3;
		
		return !rowBitMask[line][val-1] && !colBitMask[col][val-1] && !recBitMask[recLine][recCol][val-1];
		
	}
	
	void updateBitMask(boolean adding, int val, int line, int col) {
		
		if(val != 0) {
			int recLine = line/3;
			int recCol = col/3;
			
			if(adding) {
				rowBitMask[line][val-1] = true;
				colBitMask[col][val-1] = true;
				recBitMask[recLine][recCol][val-1] = true;
			}else {
				rowBitMask[line][val-1] = false;
				colBitMask[col][val-1] = false;
				recBitMask[recLine][recCol][val-1] = false;
			}
		}
		
	}
	
	
	void search(ArrayList<ArrayList<ArrayList<Integer>>> sols, ArrayList<ArrayList<Integer>> map, int line, int col, int initLine, int initCol){
		
		if(map.get(line).get(col) == 0) {
			
			for(int val = 1; val <= 9; val++) {
				if(sols.size() == 1000) {
					break;
				}
				if(valPoss(val, line, col, map)) {
					updateBitMask(false, map.get(line).get(col), line, col);
					map.get(line).set(col, val);
					updateBitMask(true, val, line, col);
					int nextLine = line;
					int nextCol = col+1;
					if(nextCol == 9) {
						nextLine++;
						nextCol = 0;
					}
					if(nextLine == 9) {
						nextLine = 0;
					}
					if(nextLine == initLine && nextCol == initCol) {
						sols.add(copy(map));
					}else {
						search(sols, map, nextLine, nextCol, initLine, initCol);
					}
				}
			}
			updateBitMask(false, map.get(line).get(col), line, col);
			map.get(line).set(col, 0);
			
		}else {
			
			int nextLine = line;
			int nextCol = col+1;
			if(nextCol == 9) {
				nextLine++;
				nextCol = 0;
			}
			if(nextLine == 9) {
				nextLine = 0;
			}
			if(nextLine == initLine && nextCol == initCol) {
				sols.add(copy(map));
			}else {
				search(sols, map, nextLine, nextCol, initLine, initCol);
			}
			
		}
		
	}
	


	@Override
	public void run() {
		
		boolean solution = verifyInput(map);
		
		if(solution) {
			initBitMask(map);
			int[] bestCor = searchBestStart(map);
			search(sols, map, bestCor[0], bestCor[1], bestCor[0], bestCor[1]);
			if(sols.size() >= 1) {
				panel.map = sols.get(0);
				panel.message = Messages.solutionExists;
			}else {
				panel.message = Messages.noSolution;
			}
		}else {
			panel.message = Messages.error;
		}
		
		panel.solving = false;
		panel.solved = true;
		if(panel.message == Messages.solutionExists) {
			panel.buttonSolve.text = "Solved!!";
			panel.totalSols = sols.size();
		}else if(panel.message == Messages.noSolution) {
			panel.buttonSolve.text = "Finish";
		}else if(panel.message == Messages.error){
			panel.buttonSolve.text = "Continue";
		}
		panel.repaint();
		
	}
	

}
