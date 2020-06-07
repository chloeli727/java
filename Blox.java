/**
** Program Name: Blox
** Author:           Seven (Qi Yue) Li
** Date:              November 18th, 2014
** Course:            CPSC 1150
** Compiler:         JDK 1.7
*/

import java.util.*;

public class Blox {
	/* the next several lines create constants local to THIS java file
	 *		with the same names/values as those present in the BloxLibrary
	 *		file, to make them easier to refer to (so you can do map[Y][X]
	 *		instead of map[BloxLibrary.Y][BloxLibrary.X], for example).
	 */
	public static final int X = BloxLibrary.X, Y = BloxLibrary.Y;
	public static final char BLOCK = BloxLibrary.BLOCK, HOLE = BloxLibrary.HOLE,
			EMPTY = BloxLibrary.EMPTY;
	
	// constants for user input and map output. Make more as needed!
	public static final char PLAYER = 'P', BORDER = 'X', EASY = 'E',
			MEDIUM = 'M', HARD = 'H', QUIT = 'Q', CHALLENGE = 'C', UP = 'W', DOWN = 'S', LEFT = 'A', RIGHT = 'D',
         YES = 'Y';
   
	public static final int BLOCKED = 1, UNBLOCKED = 2, MOVED = 3, TRAPPED = 4;
	
	/* Controls the main logic of this program, calling most of the other
	 *		methods directly to accomplish subtasks of the overall program.
	 *
	 * @param  args  Command-line arguments. Unused by this program.
	 */
	public static void main(String[] args) 
	{
		char[][] map=null;
		boolean run = true;
		char difficulty;
		int gameEnd=0;//store the integer number returned from the playGame method
      
		Scanner input = new Scanner(System.in);
      
		char level;//store the information if the user want to play the next level or not
      
		displayInstructions();//display instructions of the game
      
		difficulty=getDifficulty();
		while(run)
		{
			switch(difficulty)
			{
				case EASY:
					map= BloxLibrary.randomizeMap(1, 5, 4);
					gameEnd=playGame(map, BloxLibrary.findAvailablePosition(map), 1);
				break;
				
				case MEDIUM:
					map= BloxLibrary.randomizeMap(3, 8, 5);
					gameEnd=playGame(map, BloxLibrary.findAvailablePosition(map), 3);
				break;
				
				case HARD:
					map = BloxLibrary.randomizeMap(10, 10, 8);
					gameEnd=playGame(map, BloxLibrary.findAvailablePosition(map), 10);
				break;
				
				case CHALLENGE:
					System.out.println("You only have 60 moves for this level");
					map = BloxLibrary.randomizeMap(12, 12, 10);
					gameEnd=playGame(map, BloxLibrary.findAvailablePosition(map), 12);
				break;
				
				case QUIT:
					run = false;
				return;
			}
		
			
			if(gameEnd==MOVED)
			{
				if(difficulty==CHALLENGE)
				{
					System.out.println("Congradulations! You have COMPLETED all the levels!");//the end of the difficulty levels
					System.exit(0);
				}
				
				System.out.println("Would you like to play the next level? - [Y]es/[N]o");//ask the user if they want to play the next level, only when they win the game
				String increaseLev=input.next().toUpperCase();
				level = increaseLev.charAt(0);
            
				if(level==YES)
				{
					difficulty=increaseDiff(difficulty);
	
				}
			
				else
				{
					difficulty=getDifficulty();
				}
			
			}
			
			//if the user chooses to quit or the user is lost, allow them to choose the level again
			if(gameEnd==QUIT||gameEnd==TRAPPED)
			{
				difficulty=getDifficulty();
			}
			
		}
	}
	
	/* Displays instructions on how to play this game.*/
	
	public static void displayInstructions() 
	{ 
	      System.out.println("GAME RULES:");
	      System.out.println("\nThe player is represented by a 'P'. The player can interact with the game by"
	      +"\nmoving in one of four directions; up, down, left, and right.");
	      System.out.println("\nThe blox are represented by '*' and the holes by 'O'. "
	      +"\nThe goal of the game is to push the blox around until each block has filled" 
	      + "\none of the available holes."
	      +"\nThe player pushes blox by standing beside them and moving towards them."
	      +"\nIf the far side of the block is clear (or a hole), both the player and the block will move.");
			System.out.println("\nIf the player pushes one of the blox into a hole,"
	      +"\nboth the block and the hole disappear (the block filled the hole).");
	      System.out.println("\nThe map is surrounded by a border which is impassable to both the player and"
	      +"\nblox.");
	      System.out.println("\nThe player wins the game once every block has been pushed into a hole.");
	      System.out.println("\nThe player loses the game if they quit or walk into a hole that has not been"
	      +"\nfilled.");
	      
	      System.out.println("\n*The moves in the challenge level is limited."
	      +"\nIf the player did not push the block into the hole by the time the moves"
	      +"\nwere out, the game is automatically failed.");
	      System.out.println();
	}
	
	/* Prints the 2D character map passed in as a parameter, including the player
	 *		whose position is specified by the int array parameter. The player
	 *		position array should be in the same format as that returned by the
	 *		findAvailablePosition method provided in the BloxLibrary file.
	 *
	 * @param  map  The 2D character array map of the current game.
	 * @param  player  An array containing the x and y coordinates of the player.
	 */
	public static void printMap(char[][] map, int[] player) 
	{
		int row;
		int col;
		int height=map.length;//height of the map
		int width=map[0].length;//width of the map
		
		//upper boarder
		for(int i=0; i< width+2; i++)
		{
			System.out.print(BORDER);  
		}
		System.out.println();
		
		//print map
		for(row=0; row< height; row++)
		{
			System.out.print(BORDER);
		
			for(col=0; col< width; col++)
			{
				
				if(row == player[Y] && col == player[X])
				{
					System.out.print(PLAYER);
				}
				
				else
				System.out.print(map[row][col]);
			}
			System.out.print(BORDER);
			System.out.println();
		}
      
		
		//bottom boarder
		for(int i=0; i< width+2; i++)
		{
			System.out.print(BORDER);  
		}
		System.out.println();
		 
	}
	
	/* Prompts the user to select a difficulty from the available options.
	 *		Continues to prompt while the selection made by the user does
	 *		not correspond to any of the available options, ensuring that
	 *		a valid difficulty (or quit) is returned.
	 *
	 * @return  The validated difficulty selected by the user.
	 */
	public static char getDifficulty()	
	{
		String answer;
		Scanner input = new Scanner(System.in);
		char choice;
		boolean keepGoing = true;
		
		while(keepGoing)
		{
			System.out.println("Please select a difficulty - [E]asy,[M]edium, [H]ard, *[C]hallenge, or [Q]uit: ");
			answer = input.next().toUpperCase();
			choice = answer.charAt(0);
		
			switch(choice)
			{
				case EASY:
				
				case MEDIUM:
				
				case HARD:
					
				case CHALLENGE:
					
				case QUIT:
					return choice;
			}
			System.out.println("This is not an option! Please select an option listed above.");
		}
		return ' ';
	}
	
	/* Prompts the user to select a move from the available options.
	 *		Continues to prompt while the selection made by the user does
	 *		not correspond to any of the available options, ensuring that
	 *		a valid move (or quit) is returned.
	 *
	 * @return  The validated move selected by the user.
	 */
	public static char getMove()
	{
	      String answer;
	      int countStep=15;//variable use for counting moves (only for the challenge level)
	      char choice = ' ';
	      Scanner input = new Scanner(System.in);
	      
	      
	      System.out.println("Please enter a move - UP(W), DOWN(S), LEFT(A), RIGHT(D), or QUIT(Q):");
	      answer = input.next().toUpperCase();
	      
	      switch(answer)
	      {
			 case "UP":
			 case "W": 
				choice = 'W';
			 break;
			 
			 case "DOWN":
			 case "S":
			    choice = 'S';
			 break;
			 
			 case "LEFT":
			 case "A":
			    choice = 'A';
			 break;
			 
			 case "RIGHT":
			 case "D":
			    choice = 'D';
			 break;
			 
			 case "QUIT":
			 case "Q":
			    choice = 'Q';
			 break;
			 
			 default:
			    System.out.println("This is not an option! Please select an option listed above.");
	      }
	      return choice;
	}
	
	/* This method indicates if the coordinate specified by the integer parameters
	 *		is a valid location within the 2D character array map specified by the
	 *		remaining parameter.
	 *
	 * @param  map  The 2D character array map of the current game.
	 * @param  x  The horizontal coordinate for the location being tested.
	 * @param  y  The vertical coordinate for the location being tested.
	 * @return  True if the coordinate is within the range of the map,
	 *		false otherwise.
	 */
	public static boolean isOnMap(char[][] map, int x, int y) 
	{
		int height=map.length;//height of the map
		int width=map[0].length;//width of the map
		
		if (x >= width || x<0)
		{
			System.out.println("You are blocked! You can't move further in this direction.");
			return false;
		}
		
		if(y>= height|| y<0)
		{
			System.out.println("You are blocked! You can't move further in this direction.");
			return false;
		}
		return true;
	}
	
	/* This method is called within the main for users to play the game.
	 *
	 * @param  map  The 2D character array map of the current game.
	 * @param  player  An array containing the x and y coordinates of the player.
	 * @param  blox  The number of blocks and holes in the map
	 * @return  The condition of the game (user win, lose, or quit the game)
	 */
	public static int playGame (char[][] map, int[] player, int blox)
	{
		char move = ' ';
		int result;//result after the player is moved
		printMap(map, player);
		boolean challenge = false;
		int countStep = 60;//limited moves for challenge level
     
      
		Scanner input = new Scanner(System.in);
		
		if(blox==12)
		{
			challenge = true;//turn on the challenge level
		}
		
		move = getMove();

		if(move == QUIT)
			return QUIT;
		
		while(move!=QUIT)
		{
			result=movePlayer(map, player, move);
			if(result==MOVED)
			{
				blox--;//whenever the block is pushed into the hole, the number of blox is decreased
				if(blox==0)
				{
					printMap(map,player);
					System.out.println("You WIN!");//when the hole is cleared, the user win the game
					return MOVED;
				}
			}
			
			if(result==TRAPPED)//if the player fall into the hole
			{
				System.out.println("You LOST!");
				return TRAPPED;
			}
			
			if(challenge)//for the challenge level only
			{
				countStep --;
				System.out.println(countStep + " move(s) left");
				
				if(countStep==0)
				{
					System.out.println("You are out of moves");
					System.out.println("You LOST!");
					return 0;
				}
		
			}
			printMap(map, player);
			move = getMove();
			
			if(move==QUIT)
			{
				return QUIT;
			}
		}
		return -1;
	}
	
	/* This method updates the map whenever a move is made by the user
	 *
	 * @param  map  The 2D character array map of the current game.
	 * @param  player  An array containing the x and y coordinates of the player.
	 * @param  move  The move made by user
	 * @return  The condition of the game (1.the move is blocked, 2.the move is not blocked, 3.block is moved into the hole,
	 *                   4. if the player is fall into the hole)
	 */
	public static int movePlayer (char[][] map, int[] player, char move)
	{
		int colPrime = player[X];
		int rowPrime = player[Y];
		int blockCol = colPrime;
		int blockRow = rowPrime;
		int directionX = 0;
		int directionY = 0;
		
		
		if(move == RIGHT)
		{
			directionX = 1;
			colPrime++;
		}
			
		if(move == LEFT)
		{
			directionX = -1;
			colPrime--;
		}


		if(move == DOWN)
		{
			directionY = 1;
			rowPrime++;
		}
		
		if(move == UP)
		{
			directionY = -1;
			rowPrime--;
		}
		
		if(isOnMap(map, colPrime, rowPrime)==false)
		{
			return BLOCKED;
		}
		
		
			if(map[rowPrime][colPrime] == EMPTY)
			{
				if(move==LEFT||move==RIGHT)
				{
					player[X]=colPrime;
				}
				
				else
				{
					player[Y]=rowPrime;
				}
				
				return UNBLOCKED;
			}
			
			if(map[rowPrime][colPrime] == HOLE)
			{
				if(move==LEFT||move==RIGHT)
				{
					player[X] = colPrime;
				}
				
				else
				{
					player[Y] = rowPrime;
				}
				map[blockRow][blockCol]= EMPTY;
				printMap(map,player);
      
				return TRAPPED;
			}
			
			if(map[rowPrime][colPrime] == BLOCK)
			{
				if(move==LEFT||move==RIGHT)
				{
					blockCol = colPrime +directionX;
				}
				
				else
				{
					blockRow = rowPrime + directionY;
				}
				
				if(isOnMap(map, blockCol, blockRow)==false)
				{
					return BLOCKED;
				}
				
				if(map[blockRow][blockCol] == EMPTY)
				{
					map[blockRow][blockCol] = BLOCK;
					map[rowPrime][colPrime] = EMPTY;
					
					if(move==LEFT||move==RIGHT)
					{
						player[X] = colPrime;
					}
					
					else
					{
						player[Y]=rowPrime;
					}
				
					return UNBLOCKED;
				}
				
				if(map[blockRow][blockCol] == HOLE)
				{
					map[blockRow][blockCol]= EMPTY;
					map[rowPrime][colPrime] = EMPTY;
					
					return MOVED;
				}
			}
			return UNBLOCKED;
	}
	/* This method increases the level of difficulty of the game
	 *  If the player win the game, the level is increased to the next level
	 *
	 * @param  difficulty  The level of difficulty selected by user
	 * @return  The difficulty of next level
	 */
	public static char increaseDiff (char difficulty)
	{
		if(difficulty==EASY)
			return MEDIUM;
		
		if(difficulty==MEDIUM)
			return HARD;
		
		if(difficulty==HARD)
			return CHALLENGE;
      
		if(difficulty==CHALLENGE)
			return CHALLENGE;
		
		return EASY;
	}
	
	
}
