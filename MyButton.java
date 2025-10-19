package sudoku;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class MyButton extends JComponent implements MouseListener{
	
	SolverPanel panel;
	
	int ID;
	
	boolean inside = false;
	
	public MyButton(SolverPanel p, int id, String text) {
		panel = p;
		ID = id;
		this.text = text;
	}
	
	Color color = Color.green;
	String text;
	
	void paintButton(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.setColor(color);
		g2d.fillRoundRect(x, y, width, height, 30, 30);
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRoundRect(x, y, width, height, 30, 30);
		Font textFont = new Font("Impact", 0, 25);
		g2d.setFont(textFont);
		int textWidth = getFontMetrics(textFont).stringWidth(text);
		g2d.drawString(text, x + width/2 - textWidth/2, y + height*5/7+1);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		color = Color.cyan;
		panel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(inside) {
			color = Color.yellow;
			if(panel.solving == false) {
				if(ID == 1) {
					if(panel.solved && panel.message == Messages.error) {
						panel.solved = false;
						text = "Solve";
					}else if(!panel.solved) {
						text = "solving...";
						panel.solving = true;
						new Thread(new Solver(panel.sols, panel.map, panel)).start();
					}
				}else if(ID == 2) {
					panel.solved = false;
					panel.buttonSolve.text = "Solve";
					panel.initMap();
					panel.solved = false;
					panel.removeButtons();
				}else if(ID == 3) {
					if(panel.currSol > 1) {
						panel.currSol--;
						panel.map = panel.sols.get(panel.currSol-1);
					}
				}else if(ID == 4) {
					if(panel.currSol < panel.totalSols) {
						panel.currSol++;
						panel.map = panel.sols.get(panel.currSol-1);
					}
				}
			}
			panel.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		color = Color.yellow;
		panel.repaint();
		inside = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		color = Color.green;
		panel.repaint();
		inside = false;
	}

}
