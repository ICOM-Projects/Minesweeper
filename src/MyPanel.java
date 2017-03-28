import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 60;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 10;   //Last row has only one cell
	private int minesWanted = 10;
	private int maxFlags = minesWanted;
	public Color[][] mineField = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] minas = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public String [][] gridAmount = new String [TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean minesOnField[][] = new boolean [TOTAL_COLUMNS][TOTAL_ROWS];
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public int difficult = 0;


	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}

		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			mineField[x][0] = Color.WHITE;
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			mineField[0][y] = Color.WHITE;
		}

		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				mineField[x][y] = Color.WHITE;
			}
		}
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.PINK);
		g.fillRect(x1, y1, width + 1, height+1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Draw an additional cell at the bottom left
		//g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS - 1; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = mineField[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1))+1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}	
	}

	public int getColumns(){
		return TOTAL_COLUMNS;
	}
	public int getROWS(){
		return TOTAL_ROWS;
	}

	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		/*if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}*/
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {

		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		/*if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}*/
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}

	public boolean isMine(int x, int y) {
		if (minas[x][y] == -1){
			return true;
		}
		return false;
	}
	public void genField(){
		for(int y = 0; y<this.getROWS() - 1; y++){
			for(int x = 0; x<this.getColumns(); x++){
				minas[x][y] = 0;
			}
		}
	}
	//By Lemanuel Colon
	public void genMines(){
		int minesPlaced = 0;
		while(minesPlaced < minesWanted){
			int x = new Random().nextInt(TOTAL_COLUMNS);
			int y = new Random().nextInt(TOTAL_ROWS-1);
			if(minas[x][y] == -1){
				x = new Random().nextInt(TOTAL_COLUMNS);
				y = new Random().nextInt(TOTAL_ROWS-1); 
			}
			minas[x][y] = -1;
			minesPlaced++;
		}
	}

	//By Lemanuel Colon
	public void readMines(int x, int y){
		if(minas[x][y] == -1){
			mineField[x][y] = Color.BLACK;
			this.repaint();

			for(int i = 0; i < this.getROWS() - 1 ; i++){
				for(int j = 0 ; j < this.getColumns() ; j++){
					if(minas[i][j] == -1){
						mineField[i][j] = Color.BLACK;
					}
				}
			}

		} else if(minas[x][y] == 0 ){
			mineField[x][y] = Color.LIGHT_GRAY;
			this.checkField(x, y);
			this.repaint();	
		}
	}

	//By Lemanue Colon
	public void placeFlag(int x, int y){
		if(mineField[x][y] == Color.RED){
			mineField[x][y] = Color.WHITE;
			this.repaint();
		} else{
		mineField[x][y] = Color.RED;
		this.repaint();
		}
	}

	//By Lemanuel Colon
	public void checkField(int x, int y){

		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++ ){
				
				if((x-i) >= 0 && (y-j) >=0){
					if(minas[x-i][y - j] == 0 && mineField[x-i][y-j] != Color.RED){
						mineField[x-i][y-j] = Color.LIGHT_GRAY;

					}
				}
				if((x-i)>=0){

					if(minas[x-i][y] == 0 && mineField[x-i][y] != Color.RED){
						mineField[x-i][y] = Color.LIGHT_GRAY;
					}
				}
				if((x-i) >= 0 && (y+j) < this.getROWS() - 1){
					if(minas[x-i][y+j] == 0 && mineField[x-i][y+j] != Color.RED) {
						mineField[x-i][y+j] = Color.LIGHT_GRAY;

					}
				}
				if((x+1) < this.getColumns() && (y+j) < this.getROWS() - 1){
					if(minas[x+i][y+j] == 0 && mineField[x+i][y+j] != Color.RED){
						mineField[x+i][y+j] = Color.LIGHT_GRAY;

					}
				}
				if((x+1) < this.getColumns()){
					if(minas[x+i][y] == 0 && mineField[x+i][y] != Color.RED){
						mineField[x+i][y] = Color.LIGHT_GRAY;

					}
				}
				if((x+i) < this.getColumns() && (y-j) >=0){
					if(minas[x+i][y-j] == 0 && mineField[x+i][y-j] != Color.RED){
						mineField[x+i][y-j] = Color.LIGHT_GRAY;

					}
				}
				if(x >= 0 && (y-j) >=0){
					if(minas[x][y-j] == 0 && mineField[x][y-j] != Color.RED){
						mineField[x][y-j] = Color.LIGHT_GRAY;
					}
				}
				if(x >= 0 && (y+j) < this.getROWS()-1){
					if(minas[x][y+j] == 0 && mineField[x][y+j] != Color.RED){
						mineField[x][y+j] = Color.LIGHT_GRAY;	
					}
				}
			}
		}
	}
}

