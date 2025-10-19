package sudoku;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		JFrame f = new JFrame("Sudoku Solver");
		
		SolverPanel panel = new SolverPanel();
		
		f.add(panel);
		
		f.pack();
		
		f.setResizable(false);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.setLocationRelativeTo(null);
		
		f.setVisible(true);

	}

}
