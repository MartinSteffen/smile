package gui;

import java.awt.Point;

/**
 * Verwaltung der Belegung Rasters
 * 
 * @author sek
 *
 */

public class Grid {
	private boolean[][] grid;
	private int rows, cols;
	private int gridsize;
	
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;
	
	// TODO Array Grenzen beachten - ArrayIndexOutOfBounds etc.
	
	
	public Grid(int rows, int cols, int gridsize) {
		this.rows = rows;
		this.cols = cols;
		this.gridsize = gridsize;
		this.grid = new boolean[rows][cols];
	}
	
	/**
	 * Einfuegen eines Objects an der bestimmten Position oder,
	 * falls dieses besetzt ist, im naehesten freien Feld des
	 * Rasters.
	 * 
	 * @param row - Zeile in der eingefuegt werden soll
	 * @param col - Spalte in der eingefuegt werden soll
	 * @return: Punkt, der als x-Koordinate die Zeile und als
	 * 			y-Koordinate die Spalte des Feldes hat, an dem
	 * 			das Object tatsaechlich eingefuegt wurde.
	 * 			Null, falls einfuegen nicht moeglich. 
	 */
	public void insert(int row, int col) {
		
		if(checkBounds(row,col,null)) {
			// falls Feld frei, hier einfuegen
			if(!grid[row][col])
				grid[row][col] = true;
		}

	}
	
	public void insert(Point position) {
		
		if(position != null) {
			int col = position.x / gridsize;
			int row = position.y / gridsize;
			
			this.insert(row, col);
		}
	}
	
	public void delete(int row, int col) {
		grid[row][col] = false;
	}
	
	public void delete(Point position) {
		
		if(position != null) {
			int col = position.x / gridsize;
			int row = position.y / gridsize;
			
			this.delete(row, col);
		}
	}
	
	
	/**
	 * Zeigt an, ob angegebenes Feld frei ist.
	 * 
	 * @param row - Zeile des angegebenen Feldes
	 * @param col - Spalte des angegebenen Feldes
	 * @return: true = frei, fals = belegt.
	 */
	public boolean isFree(int row, int col) {
		return !grid[row][col];
	}
	
	public boolean isFree(int startrow, int startcol, int endrow, int endcol, Point bounds) {
		for(int i = startrow; i <= endrow; i++) {
			for(int j = startcol; j <= endcol; j++) {
				if(!checkBounds(i,j,bounds))
					return false;
				else if(grid[i][j])
					return false;
			}
		}
		return true;
	}
	
	public boolean isFree(Point startposition, Point endposition) {
		int startrow = startposition.y / gridsize;
		int startcol = startposition.x / gridsize;
		int endrow = endposition.y / gridsize;
		int endcol = endposition.x / gridsize;
		
		return this.isFree(startrow, startcol, endrow, endcol, null);
	}
	
	public boolean isFree(Point startposition, Point endposition, Point bounds) {
		
		
		return false;
	}
	
	
	/**
	 * Zeigt an, ob angegebenes Feld belegt ist.
	 * 
	 * @param row - Zeile des angegebenen Feldes
	 * @param col - Spalte des angegebenen Feldes
	 * @return: true = belegt, false = frei.
	 */
	public boolean isUsed(int row, int col) {
		return grid[row][col];
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	
	public void extendSize(int addition, int direction) {
		
		/* falls direction nicht aus {LEFT, RIGHT, TOP, BOTTOM}
		 * oder addition negativ, tue nichts (oder benachrichtige
		 * User) 
		 */
		if(addition < 0 || direction < 1 || direction > 4)
			return;
		
		
		else {
			boolean[][] original = grid.clone();
			
			if(direction == LEFT) {
				this.grid = new boolean[this.rows][this.cols += addition];
				for(int i = 0; i < this.rows; i++) {
					for(int j = addition; j < this.cols; j++) {
						grid[i][j] = original[i][j-addition];
					}
				}
			}
			if(direction == RIGHT) {
				this.grid = new boolean[this.rows][this.cols += addition];
				for(int i = 0; i < this.rows; i++) {
					for(int j = 0; j < this.cols - addition; j++) {
						grid[i][j] = original[i][j];
					}
				}
			}
			if(direction == TOP) {
				this.grid = new boolean[this.rows += addition][this.cols];
				for(int i = addition; i < this.rows; i++) {
					for(int j = 0; j < this.cols; j++) {
						grid[i][j] = original[i-addition][j];
					}
				}
			}
			if(direction == BOTTOM) {
				this.grid = new boolean[this.rows += addition][this.cols];
				for(int i = 0; i < this.rows - addition; i++) {
					for(int j = 0; j < this.cols; j++) {
						grid[i][j] = original[i][j];
					}
				}
			}
		}

	}
	
	
	public void reduceSize(int reduction, int direction) {
		
		/* falls direction nicht aus {LEFT, RIGHT, TOP, BOTTOM}
		 * oder addition negativ, tue nichts (oder benachrichtige
		 * User) 
		 */
		if(reduction < 0 || direction < 1 || direction > 4)
			return;
		
		
		else {
			boolean[][] original = grid.clone();
			if(direction == LEFT) {
				this.grid = new boolean[this.rows][this.cols -= reduction];
				for(int i = 0; i < this.rows; i++) {
					for(int j = 0; j < this.cols; j++) {
						grid[i][j] = original[i][j+reduction];
					}
				}
			}
			if(direction == RIGHT) {
				this.grid = new boolean[this.rows][this.cols -= reduction];
				for(int i = 0; i < this.rows; i++) {
					for(int j = 0; j < this.cols; j++) {
						grid[i][j] = original[i][j];
					}
				}
			}
			if(direction == TOP) {
				this.grid = new boolean[this.rows -= reduction][this.cols];
				for(int i = 0; i < this.rows; i++) {
					for(int j = 0; j < this.cols; j++) {
						grid[i][j] = original[i+reduction][j];
					}
				}
			}
			if(direction == BOTTOM) {
				this.grid = new boolean[this.rows -= reduction][this.cols];
				for(int i = 0; i < this.rows; i++) {
					for(int j = 0; j < this.cols; j++) {
						grid[i][j] = original[i][j];
					}
				}
			}
		}

	}
	
	public Point getFreePosition(Point position) {
		return this.getFreePosition(position, null);
	}
	
	
	public Point getFreePosition(Point position, Point bounds) {
		
		int row_pointer, col_pointer;
		
		int row = position.y / gridsize;
		int col = position.x / gridsize;
		
		if(checkBounds(row, col, bounds))
			if(!grid[row][col])
				return new Point((col * gridsize) + (gridsize / 2),
						(row * gridsize) + (gridsize / 2));
		
		for(int n = 3; n <= Math.max(rows,cols) + 2; n = n + 2) {
			row_pointer = row - (n - 1) / 2;
			col_pointer = col - (n - 1) / 2;
			if(checkBounds(row_pointer, col_pointer, bounds)) {
				if(!grid[row_pointer][col_pointer])
					return new Point((col_pointer * gridsize) + (gridsize / 2),
							(row_pointer * gridsize) + (gridsize / 2));
			}
			
			// oben von links nach rechts
			for(int i = 1; n - i > 0; i++) {
				if(checkBounds(row_pointer, col_pointer+i, bounds)) {
					if(!grid[row_pointer][col_pointer + i])
						return new Point(((col_pointer + i) * gridsize) + (gridsize / 2),
								(row_pointer * gridsize) + (gridsize / 2));
				}
			}
			//rechts von oben nach unten
			for(int i = 1; n - i > 0; i++) {
				if(checkBounds(row_pointer+i, col_pointer+(n-1), bounds)) {
					if(!grid[row_pointer + i][col_pointer + (n - 1)])
						return new Point(((col_pointer + (n - 1)) * gridsize) + (gridsize / 2),
								((row_pointer + i) * gridsize) + (gridsize / 2));
				}
			}
			// unten von rechts nach links
			for(int i = 2; n - i >= 0; i++) {
				if(checkBounds(row_pointer+(n-1), col_pointer+(n-i), bounds)) {
					if(!grid[row_pointer + (n - 1)][col_pointer + (n - i)])
						return new Point(((col_pointer + (n - i)) * gridsize) + (gridsize / 2),
								((row_pointer + (n - 1)) * gridsize) + (gridsize / 2));
				}
			}
			// links von unten nach oben (Besonderheit: letztes Feld = erstes Feld)
			for(int i = 2; n - i > 0; i++) {
				if(checkBounds(row_pointer+(n-i), col_pointer, bounds)) {
					if(!grid[row_pointer + (n - i)][col_pointer])
						return new Point(((col_pointer) * gridsize) + (gridsize / 2),
								((row_pointer + (n - i)) * gridsize) + (gridsize / 2));
				}
			}
			
			
		}		
		return null;
	}
	
	public boolean acquireSpace(Point position, int rowspan, int colspan) {
		int row = position.y / gridsize;
		int col = position.x / gridsize;
		
		return this.acquireSpace(row, col, rowspan, colspan, null);
	}
	
	
	/**
	 * 
	 * @param row - Startzeile
	 * @param col - Startspalte
	 * @param rowspan - Anzahl zusätlicher Zeilen (positiv: rechts, negativ: links)
	 * @param colspan - Anzahl zusätzlicher Spalten (positiv: unten, negativ: oben)
	 * @return: true, wenn Platz komplett frei, false sonst
	 */
	public boolean acquireSpace(int row, int col, int rowspan, int colspan) {
		return this.acquireSpace(row, col, rowspan, colspan, null);
	}
	
	
	/**
	 * 
	 * @param row - Startzeile
	 * @param col - Startspalte
	 * @param rowspan - Anzahl zusätlicher Zeilen (positiv: rechts, negativ: links)
	 * @param colspan - Anzahl zusätzlicher Spalten (positiv: unten, negativ: oben)
	 * @param bounds - bounds.x = linke Grenze, bound.y = rechte Grenze, nur relevant bei SubRegions
	 * @return: true, wenn Platz komplett frei, false sonst
	 */
	public boolean acquireSpace(int row, int col, int rowspan, int colspan, Point bounds) {		
		
		if(rowspan > 0) {
			if(colspan > 0) {
				for(int i = row; i < row + rowspan; i++) {
					for(int j = col; j < col + colspan; j++) {
						if(checkBounds(i,j,bounds)) {
							if(grid[i][j])
								return false;
						}
						else
							return false;
					}
				}
			}
			else if(colspan < 0) {
				for(int i = row; i < row + rowspan; i++) {
					for(int j = col; j > col + colspan; j--) {
						if(checkBounds(i,j,bounds)) {
							if(grid[i][j])
								return false;
						}
						else
							return false;
					}
				}				
			}
			else { // colspan == 0
				for(int i = row; i < row + rowspan; i++) {
					if(checkBounds(i,col,bounds)) {
						if(grid[i][col])
							return false;
					}
					else
						return false;
				}	
			}	
		}
		else if(rowspan < 0) {
			if(colspan > 0) {
				for(int i = row; i > row + rowspan; i--) {
					for(int j = col; j < col + colspan; j++) {
						if(checkBounds(i,j,bounds)) {
							if(grid[i][j])
								return false;
						}
						else
							return false;
					}
				}
			}
			else if(colspan < 0) {
				for(int i = row; i > row + rowspan; i--) {
					for(int j = col; j > col + colspan; j--) {
						if(checkBounds(i,j,bounds)) {
							if(grid[i][j])
								return false;
						}
						else
							return false;
					}
				}				
			}
			else { // colspan == 0
				for(int i = row; i > row + rowspan; i--) {
					if(checkBounds(i,col,bounds)) {
						if(grid[i][col])
							return false;
					}
					else
						return false;
				}	
			}
		}
		else { // rowspan == 0
			if(colspan > 0) {
				for(int j = col; j < col + colspan; j++) {
					if(checkBounds(row,j,bounds)) {
						if(grid[row][j])
							return false;
					}
					else
						return false;
				}
			}
			else if(colspan < 0) {
				for(int j = col; j > col + colspan; j--) {
					if(checkBounds(row,j,bounds)) {
						if(grid[row][j])
							return false;
					}
					else
						return false;
				}			
			}
			else { // colspan == 0
				if(checkBounds(row,col,bounds)) {
					if(grid[row][col])
						return false;
				}
				else
					return false;
			}
		}	
		
		return true;
	}
	
	public void releaseBlock(int startrow, int startcol, int endrow, int endcol) {
		for(int i = startrow; i <= endrow; i++) {
			for(int j = startcol; j <= endcol; j++) {
				if(checkBounds(i,j,null))
					grid[i][j] = false;
			}
		}
	}
	
	
	public void releaseBlock(Point startposition, Point endposition) {
		int startrow = startposition.y / gridsize;
		int startcol = startposition.x / gridsize;
		int endrow = endposition.y / gridsize;
		int endcol = endposition.x / gridsize;
		
		this.releaseBlock(startrow, startcol, endrow, endcol);
	}
	
	
	public void allocateBlock(Point startposition, Point endposition) {
		int startrow = startposition.y / gridsize;
		int startcol = startposition.x / gridsize;
		int endrow = endposition.y / gridsize;
		int endcol = endposition.x / gridsize;
		
		this.allocateBlock(startrow, startcol, endrow, endcol);
	}
	
	public void allocateBlock(int startrow, int startcol, int endrow, int endcol) {
		for(int i = startrow; i <= endrow; i++) {
			for(int j = startcol; j <= endcol; j++) {
				if(checkBounds(i,j,null))
					grid[i][j] = true;
			}
		}		
	}
	
	
	public void moveBlock(Point start_upperleft, Point start_lowerright, Point end_upperleft, Point end_lowerright) {
		releaseBlock(start_upperleft, start_lowerright);
		allocateBlock(end_upperleft, end_lowerright);
	}
	
	
	
	private boolean checkBounds(int row, int col, Point bounds) {
		if (bounds != null) {
			int left = bounds.x;
			int right = bounds.y;
			if(row < 0 || col < left || row >= this.rows || col > right)
				return false;
			return true;
		}
		else {
			if(row < 0 || col < 0 || row >= this.rows || col >= this.cols)
				return false;
			return true;
		}
	}
	
/*	private boolean checkBounds(int row, int col, Point bounds) {
		if(row < 0 || col < 0 || row >= this.rows || col >= this.cols)
			return false;
		return true;
	}*/

	
	
/*	*//**
	 * Im Uhrzeigersinn um aktuelles Feld umhergehen, um freies
	 * Feld zu finden. Sukzessive immer weiter vom aktuellen
	 * Feld entfernt.
	 * 
	 * @param row - Zeile des aktuellen Feldes
	 * @param col - Spalte des aktuellen Feldes
	 * @return: Punkt, der als x-Koordinate die Zeile und als
	 * 			y-Koordinate die Spalte des freien Feldes hat
	 *//*
	private Point getFreeField(int row, int col) {
		
		int row_pointer, col_pointer;
		
		for(int n = 3; n <= Math.min(rows,cols) + 2; n = n + 2) {
			row_pointer = row - (n - 1) / 2;
			col_pointer = col - (n - 1) / 2;
			if(!grid[row_pointer][col_pointer])
				return new Point(row_pointer,col_pointer);
			
			// oben von links nach rechts
			for(int i = 1; n - i > 0; i++) {
				if(!grid[row_pointer][col_pointer + i])
					return new Point(row_pointer, col_pointer + i);				
			}
			//rechts von oben nach unten
			for(int i = 1; n - i > 0; i++) {
				if(!grid[row_pointer + i][col_pointer + n])
					return new Point(row_pointer, col_pointer + i);				
			}
			// unten von rechts nach links
			for(int i = 1; n - i > 0; i++) {
				if(!grid[row_pointer + n][col_pointer + (n - i)])
					return new Point(row_pointer, col_pointer + i);				
			}
			// links von unten nach oben (Besonderheit: letztes Feld = erstes Feld)
			for(int i = 1; n - i > 1; i++) {
				if(!grid[row_pointer + (n - i)][col_pointer])
					return new Point(row_pointer, col_pointer + i);				
			}
			
			
		}		
		return null;
	}*/
	
/*	*//**
	 * Einfuegen eines Objects an der bestimmten Position oder,
	 * falls dieses besetzt ist, im naehesten freien Feld des
	 * Rasters.
	 * 
	 * @param row - Zeile in der eingefuegt werden soll
	 * @param col - Spalte in der eingefuegt werden soll
	 * @return: Punkt, der als x-Koordinate die Zeile und als
	 * 			y-Koordinate die Spalte des Feldes hat, an dem
	 * 			das Object tatsaechlich eingefuegt wurde.
	 * 			Null, falls einfuegen nicht moeglich. 
	 *//*
	public Point insert(int row, int col) {
		
		// falls Feld frei, hier einfuegen
		if(!grid[row][col]) {
			grid[row][col] = true;
			System.out.println("frei");
			return new Point(row,col);
		}		
		// falls gewuenschtes Feld nicht frei
		else {
			Point freeField;
			System.out.println("besetzt");
			// nach freiem Feld suchen und dort einfuegen
			if((freeField = getFreeField(row, col)) != null) {
				grid[freeField.x][freeField.y] = true;
				return freeField;
			}
			// falls nicht moeglich, Rueckgabe "null"
			else
				return null;
		}
	}*/

	


}
