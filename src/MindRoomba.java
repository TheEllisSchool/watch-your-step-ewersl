import java.awt.BorderLayout;
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.EventQueue; 
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import javax.swing.UIManager;


public class MindRoomba extends JFrame {
	
	private static final int GRIDSIZE = 10; 
	private static final int NUMBEROFHOLES = 10; 
	
	private TerrainButton [] [] terrain = new TerrainButton[GRIDSIZE]
[GRIDSIZE]; 
	private int totalRevealed = 0; 
	
	
	public MindRoomba () {
		initGUI (); 
		
		setTitle ("Mind Roomba"); 
		setSize(200, 100); 
		setResizable(false); 
		pack(); 
		setLocationRelativeTo (null); 
		
		setVisible(true); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
	}

	private void initGUI() {
		JPanel titlePanel = new JPanel(); 
		add(titlePanel, BorderLayout.PAGE_START);
		titlePanel.setBackground(Color.WHITE);
		JLabel titleLabel = new JLabel("Mindroomba game");
			titlePanel.add(titleLabel); 
			titleLabel.setHorizontalAlignment(JLabel.CENTER); //left or right
			Font titleFont = new Font ("Georgia", Font.BOLD, 50);
			titleLabel.setForeground (Color.MAGENTA);
			titleLabel.setFont(titleFont);
			
			//center panel 
			JPanel centerPanel = new JPanel (); 
			centerPanel.setLayout (new GridLayout (GRIDSIZE, GRIDSIZE)) ; 
			add (centerPanel, BorderLayout.CENTER) ; 
			for (int r=0; r < GRIDSIZE; r++) {
				for (int c= 0; c < GRIDSIZE; c++) {
					terrain[r][c] = new TerrainButton (r, c);
					terrain[r][c].addActionListener(new ActionListener () {
						@Override
						public void actionPerformed (ActionEvent e) {
							TerrainButton button = (TerrainButton) e.getSource (); 
							int row = button.getRow (); 
							int col = button.getCol (); 
							clickedTerrain (row, col);
						}
						
					} ); 
					centerPanel.add(terrain[r][c]); 
				}
			}
			setHoles (); 
		}

	private void setHoles () {
		Random rand = new Random () ; 
		int pickRow;
		int pickCol; 
		for (int i = 0; i < NUMBEROFHOLES; i++) {
			do {
				pickRow = rand.nextInt (GRIDSIZE); 
				pickCol = rand.nextInt (GRIDSIZE); 
			} while (terrain [pickRow] [pickCol].hasHole ());
			terrain [pickRow] [pickCol].setHole (true);
			addToNeighborsHoleCount(pickRow, pickCol); 
			// terrain[pickRow] [pickCol].reveal (true); 
		}
	
	}

	private void addToNeighborsHoleCount(int row, int col) {
		addToHoleCount(row+ 1, col-1 );
		addToHoleCount(row+ 1, col );
		addToHoleCount(row , col- 1 );
		addToHoleCount(row-1 , col-1 );
		addToHoleCount(row -1, col );
		addToHoleCount(row +1, col +1);
		addToHoleCount(row , col +1);
		addToHoleCount(row -1 , col+1 );
	} 
	
	private void addToHoleCount(int row, int col) {
		if (row > -1 && row < GRIDSIZE && col < -1 && col< GRIDSIZE ) {
			terrain [row] [col].increaseHoleCount (); 
			// terrain [row] [col].reveal(true);
		}	
	}
	
	private void clickedTerrain(int r, int c) {
		if (terrain [r][c].hasHole()) {
			String message= "u stepped on a hole! u lose. try again!!"; 
			promptForNewGame (message); 
		} else {
			check (r, c); 
			checkNeighbors(r, c); 
			if (GRIDSIZE * GRIDSIZE - NUMBEROFHOLES == totalRevealed) {
				String message = "u won! congrats! give it another go! "; 
				promptForNewGame (message); 
			}
		}
	}
	
	private void check (int r, int c) {
		if (r > -1 && r < GRIDSIZE && c > -1 && c < GRIDSIZE
			&& !terrain [r][c].hasHole() && !terrain [r][c].isRevealed()) {
			terrain [r][c].reveal(true);
			totalRevealed ++;
			System.out.println(totalRevealed);
			if (!terrain[r][c].isNextToHoles()) {
				checkNeighbors(r, c); 
			}
		}
		
	}
	
	private void checkNeighbors(int r, int c) {
		check(r-1, c-1);
		check(r-1, c);
		check(r-1, c+1);
		check(r, c-1);
		check(r, c+1);
		check(r+1, c-1);
		check(r+1, c);
		check(r+1, c+1);
	}
	
	private void promptForNewGame(String message) {
		showHoles();
		int option = JOptionPane.showConfirmDialog(this, message, "Play Again?", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			newGame();
		} else {
			System.exit(0);
		}
	}
	
	private void newGame() {
		for (int r = 0; r < GRIDSIZE; r++) {
			for (int c = 0; c < GRIDSIZE; c++) {
				terrain[r][c].reset();
			}
		}
		setHoles();
		totalRevealed = 0;
	}
	
	private void showHoles() {
		for (int r = 0; r < GRIDSIZE; r++) {
			for (int c = 0; c < GRIDSIZE; c++) {
				if (terrain[r][c].hasHole()) {
					terrain[r][c].reveal(true);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
            String className = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch ( Exception e) {}
        
        EventQueue.invokeLater(new Runnable (){
            @Override
            public void run() {
            		new MindRoomba();
            }   
        });

	}
	
	
	
}



