/* Provided framework code for the initialization of the map for the 2D Blox
 *		game. This file must not be edited in any way, as the marker will be
 *		using a seperate copy of the original file, not the copy submitted
 *		by any individual student.
 *
 * @author  Nicholas Verrinder
 * @date  November 1st, 2014
 */

public class BloxLibrary {
	public static final int EMPTY_LINES = 2, MARGIN = 1, X = 0, Y = 1, MAX_ATTEMPTS = 100;
	public static final char BLOCK = '*', HOLE = 'O', EMPTY = ' ';
	
	/* Generates a 2D character array corresponding to the size dimensions
	 *		passed in as parameters and then populates this map with the
	 *		specified number of block/hole pairs. Note that this method
	 *		does not place the player, as that should be handled
	 *		seperately within the game logic.
	 *
	 * @param  blox  the number of block/hole pairs to be added to the map
	 *		being generated
	 * @param  width  the horizontal extension of the map to be generated
	 * @param  height  the vertical extension of the map to be generated
	 * @return  the populated 2D array map, or null if the parameters
	 *		were invalid in some way
	 */
	public static char[][] randomizeMap(int blox, int width, int height) {
		if(blox < 1 || width <= EMPTY_LINES || height <= EMPTY_LINES) {
			return null; // if map has no blox or is too small for an empty border
		}
		else if((blox + blox + 1) >= (width - EMPTY_LINES) * (height - EMPTY_LINES)) {
			return null; // if the map is too small (with margin) to fit the stuff
		}
		
		char[][] map = new char[height][width];
		
		for(int row = 0; row < map.length; ++row) { // for every row in the entire map
			for(int col = 0; col < map[row].length; ++col) { // and every column in that row
				map[row][col] = EMPTY; // make it an empty square
			}
		}
		
		for(int block = 0; block < blox; ++block) {
			int[] position = findAvailablePosition(map);
			map[position[Y]][position[X]] = BLOCK; // place the right number of blox
		}
		
		for(int hole = 0; hole < blox; ++hole) {
			int[] position = findAvailablePosition(map);
			map[position[Y]][position[X]] = HOLE; // place the same number of holes
		}
		
		return map;
	}
	
	/* Randomly generates coordinate pairs until the corresponding location
	 *		on the provided map is an empty square, then returns the
	 *		coordinates so generated. If an excessive number of attempts
	 *		are made before success, then the range is increased to include
	 *		the border which should otherwise be empty. This SHOULD only
	 *		happen in VERY large maps that are almost completely full. This
	 *		is mostly included as an example of a comprehensive understanding
	 *		of inputs/outputs and the possible problems that you need to
	 *		anticipate as programs become more complicated.
	 *
	 * @param  map  The 2D array map within which an available space must
	 *		be found.
	 * @return  an array of coordinates corresponding to a randomly selected
	 *		free space within the map.
	 */
	public static int[] findAvailablePosition(char[][] map) {
		int[] position = new int[2];
		int attempts = 0;
		
		do {
			if(attempts < MAX_ATTEMPTS) {
				// randomize an X coordinate, excluding the border
				position[X] = randomBetween(MARGIN, map[0].length - MARGIN - 1);
				
				// randomize a Y coordinate, excluding the border
				position[Y] = randomBetween(MARGIN, map.length - MARGIN - 1);
				
				++attempts; // record an attempt
			}
			else { // if this method call it taking too long, use the "margin"
				if(Math.random() >= 0.5) { // randomly choose between X and Y
					// randomize an X coordinate, INCLUDING the border
					position[X] = randomBetween(0, map[0].length - 1);
					
					// choose the max or min Y coordinate
					position[Y] = randomBetween(0, 1) * (map.length - 1);
				}
				else {
					// randomize a Y coordinate, INCLUDING the border
					position[Y] = randomBetween(0, map.length - 1);
					
					// choose the max or min X coordinate
					position[X] = randomBetween(0, 1) * (map[position[Y]].length - 1);
				}
			}
		} while(map[position[Y]][position[X]] != EMPTY); // while the space isn't empty
		
		return position;
	}
	
	/* Generates a random integer between min and max, inclusive. Swaps these
	 *		two parameters if they are out of order.
	 *
	 * @param  min  the smallest acceptable integer value.
	 * @param  max  the largest acceptable integer value.
	 * @return  the randomly generated integer value between the two parameters.
	 */
	public static int randomBetween(int min, int max) {
		if(max < min) {
			int temp = min;
			min = max;
			max = temp;
		}
		
		return min + (int)(Math.random() * (max - min + 1));
	}
}
