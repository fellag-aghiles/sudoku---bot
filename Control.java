package sudoku;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Control implements KeyListener{
	
	SolverPanel panel;
	public Control(SolverPanel p) {
		panel = p;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!panel.solved && !panel.solved) {
			int[] numCodes = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, 
					  KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9};
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_UP) {
				panel.CurrLine--;
				if(panel.CurrLine == 0) {
					panel.CurrLine = 9;
				}
			}
			if(code == KeyEvent.VK_DOWN) {
				panel.CurrLine++;
				if(panel.CurrLine == 10) {
					panel.CurrLine = 1;
				}
			}
			if(code == KeyEvent.VK_LEFT) {
				panel.CurrCol--;
				if(panel.CurrCol == 0) {
					panel.CurrCol = 9;
				}
			}
			if(code == KeyEvent.VK_RIGHT) {
				panel.CurrCol++;
				if(panel.CurrCol == 10) {
					panel.CurrCol = 1;
				}
			}
			if(code == KeyEvent.VK_SPACE) {
				panel.map.get(panel.CurrLine-1).set(panel.CurrCol-1, 0);
				panel.listStat.remove(panel.TestPos(panel.CurrLine, panel.CurrCol));
			}
			for(int i = 0; i < numCodes.length; i++) {
				if(code == numCodes[i]) {
					panel.map.get(panel.CurrLine-1).set(panel.CurrCol-1, i+1);
					int[] pos = {panel.CurrLine, panel.CurrCol};
					panel.listStat.add(pos);
					break;
				}
			}
			panel.repaint();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
