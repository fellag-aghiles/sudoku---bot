package sudoku;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SolverPanel extends JPanel{
	
	final int tileSize = 40;
	final int panelWidth = tileSize*11;
	final int panelHeight = tileSize*16;
	
	int CurrCol = 1;
	int CurrLine = 1;
	
	ArrayList<ArrayList<Integer>> map;
	ArrayList<ArrayList<ArrayList<Integer>>> sols = new ArrayList<>();
	int totalSols = 0;
	int currSol = 1;
	
	ArrayList<int[]> listStat = new ArrayList<>();
	
	Control control = new Control(this);
	
	MyButton buttonSolve = new MyButton(this, 1, "Solve");
	MyButton buttonReset = new MyButton(this, 2, "Reset");
	MyButton buttonLeft = new MyButton(this, 3, "<<");
	MyButton buttonRight = new MyButton(this, 4, ">>");
	boolean buttonsAdded = false;
	
	boolean solving = false;
	boolean solved = false;
	Messages message;
	
	public SolverPanel() {
		this.setPreferredSize(new Dimension(panelWidth, panelHeight));
		this.setBackground(new Color(39, 186, 181));
		this.setLayout(null);
		this.setDoubleBuffered(true);
		this.addKeyListener(control);
		this.setFocusable(true);
		
		buttonSolve.setBounds(2*tileSize, 13*tileSize, 3*tileSize, tileSize);
		this.add(buttonSolve);
		buttonReset.setBounds(6*tileSize, 13*tileSize, 3*tileSize, tileSize);
		this.add(buttonReset);
		buttonLeft.setBounds(2*tileSize, 14*tileSize+tileSize/2, tileSize, tileSize);
		buttonRight.setBounds(8*tileSize, 14*tileSize+tileSize/2, tileSize, tileSize);
		buttonSolve.addMouseListener(buttonSolve);
		buttonReset.addMouseListener(buttonReset);
		buttonLeft.addMouseListener(buttonLeft);
		buttonRight.addMouseListener(buttonRight);
		
		initMap();
	}
	
	void initMap() {
		map = new ArrayList<>();
		for(int i = 1; i <= 9; i++) {
			ArrayList<Integer> line = new ArrayList<>();
			for(int j = 1; j <= 9; j++) {
				line.add(0);
			}
			map.add(line);
		}
		sols.clear();
		listStat.clear();
		currSol = 1;
	}
	
	int TestPos(int line, int col) {
		for(int i = 0; i < listStat.size(); i++) {
			int[] list = listStat.get(i);
			if(list[0] == line && list[1] == col) {
				return i;
			}
		}
		return -1;
	}
	
	void addButtons() {
		if(!buttonsAdded) {
			this.add(buttonLeft);
			this.add(buttonRight);
			buttonsAdded = true;
		}
	}
	
	void removeButtons() {
		if(buttonsAdded) {
			this.remove(buttonLeft);
			this.remove(buttonRight);
			buttonsAdded = false;
		}
	}
	
	
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.white);
		g2d.fillRoundRect(50, tileSize+10, 8*tileSize+15, tileSize+19, 30, 30);
		
		g2d.setColor(new Color(94, 0, 2));
		g2d.setFont(new Font("Impact", Font.BOLD, 30));
		g2d.drawString("Sudoku Solver", panelWidth/2 - 92, tileSize);
		
		g2d.setColor(Color.darkGray);
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		g2d.drawString("+ Use the keyboard arrows to navigate", 75, tileSize+tileSize/2 + 5);
		g2d.drawString("+ Use number keys to enter, space to delete", 55, tileSize+tileSize/2 + 22);
		g2d.drawString("+ Over 1k solutions: shows first 1k only", 70, tileSize+tileSize/2 + 40);
		
		g2d.setColor(Color.white);
		g2d.fillRoundRect(tileSize, 3*tileSize, tileSize*9, tileSize*9, 30, 30);
		g2d.setColor(Color.DARK_GRAY);
		g2d.setStroke(new BasicStroke(4));
		g2d.drawRoundRect(tileSize, 3*tileSize, tileSize*9, tileSize*9, 30, 30);
		for(int i = 1; i < 9; i++) {
			if(i == 3 || i == 6) {
				g2d.setStroke(new BasicStroke(4));
			}else {
				g2d.setStroke(new BasicStroke(2));
			}
			g2d.drawLine(tileSize + i*tileSize, 3*tileSize, tileSize + i*tileSize, 12*tileSize);
		}
		for(int i = 1; i < 9; i++) {
			if(i == 3 || i == 6) {
				g2d.setStroke(new BasicStroke(4));
			}else {
				g2d.setStroke(new BasicStroke(2));
			}
			g2d.drawLine(tileSize, 3*tileSize + i*tileSize, 10*tileSize, 3*tileSize + i*tileSize);
		}
		g2d.setFont(new Font("Arial", Font.BOLD, 25));
		for(int i = 1; i <= 9; i++) {
			ArrayList<Integer> line = map.get(i-1);
			for(int j = 1; j <= 9; j++) {
				boolean draw = true;
				if(TestPos(i, j) != -1) {
					g2d.setColor(Color.DARK_GRAY);
				}else {
					g2d.setColor(Color.blue);
					if(solving) {
						draw = false;
					}
				}
				if(line.get(j-1) != 0 && draw) {
					g2d.drawString(Integer.toString(line.get(j-1)), j*tileSize + tileSize/3 , 3*tileSize + i*tileSize - tileSize/4);
				}
			}
		}
		if(!solving && !solved) {
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(Color.blue);
			g2d.drawRoundRect(CurrCol*tileSize, tileSize*2 + CurrLine*tileSize, tileSize, tileSize, 30, 30);
		}
		buttonSolve.paintButton(g2d, 2*tileSize, 13*tileSize, 3*tileSize, tileSize);
		buttonReset.paintButton(g2d, 6*tileSize, 13*tileSize, 3*tileSize, tileSize);
		if(solved) {
			if(message == Messages.solutionExists) {
				addButtons();
				buttonLeft.paintButton(g2d, 2*tileSize, 14*tileSize+tileSize/2, tileSize, tileSize);
				buttonRight.paintButton(g2d, 8*tileSize, 14*tileSize+tileSize/2, tileSize, tileSize);
				String text = "("+currSol+"/"+totalSols+")";
				Font textFont = new Font("Impact", Font.BOLD, 20);
				int textWidth = getFontMetrics(textFont).stringWidth(text);
				g2d.drawString(text, panelWidth/2 - textWidth/2 - 5, 15*tileSize + 10);
			}else if(message == Messages.noSolution) {
				g2d.setColor(Color.red);
				g2d.drawString("No Solution for this Grid !!", panelWidth/2 - 128, 15*tileSize);
			}else if(message == Messages.error){
				g2d.setColor(Color.red);
				g2d.drawString("Can't Solve this Grid !!", panelWidth/2 - 115, 15*tileSize);
			}
		}
		g2d.dispose();
		
	}
	

}