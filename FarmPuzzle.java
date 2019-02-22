
package Farmers;

import java.util.*;

/**
 *
 * @author Ethan
 */
public class FarmPuzzle{

    public static int numRows, numCols;
    public static int[] solvedRows, solvedCols;
    public static int UNFIT = 0, EMPTY = -1, USED = 1;
    public static int[][] boardValues;
    public static int maxReap, maxSow;
      
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        
        //Storing the number of Sowers(Rows) & Reapers(Columns)
        numRows = input.nextInt();
        numCols = input.nextInt();
        
        //Doing some pruning on the input
        if(numRows < 1 || numCols < 1){
            System.out.print("Incorrect input\n");
            System.exit(0);
        }
        
        //Max number of spots reapers should be working
        maxReap = numRows/2;
        //Max number of spots sowers should be working
        maxSow = numCols/2;
        
        //Instantiating
        solvedRows = new int[numRows];
        solvedCols = new int[numCols];
        boardValues = new int[numRows][numCols];
       
        //Storing initial board values
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                boardValues[i][j] = input.nextInt();
            }          
        }
        
        long startTime = System.currentTimeMillis();
        
        if(solve(0,0))
            printSolution();
        else
            System.out.println("Impossible");    
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Execution time: " + (endTime-startTime) + " milliseconds");
                     
    }
    
    public static boolean solve(int row, int col){
        
        /*Base cases*/
        
        //Since we count from 0, if col == numCols then we're past the final column of this row and need to wrap to the next row
        if(col == numCols)
            return solve(row+1,0);
        
        //Since we count from 0, if row = numRows then we're past the final row and have thus reached a solution
        if(row == numRows)
            return true;
        
        
        //If we encounter an unusable plot or one that is already used, we move on to the next spot
        if(boardValues[row][col] != EMPTY){
            if(validState(row,col))
                return solve(row,col+1);
            else
                return false;
        }
        
         //First try a value of 1
        boardValues[row][col] = USED;
        //Check if this is a valid move, and if so continue
        if(validState(row,col)){
            //If this move carries us to a solution we return true
            if(solve(row,col+1))
                return true;
        }
        //Otherwise, reset the value to empty
        boardValues[row][col] = EMPTY;
        
        //Next try a value of 0
        boardValues[row][col] = UNFIT;
        //Check if this is a valid move, and if so continue
        if(validState(row,col)){
            //If this move carries us to a solution we return true
            if(solve(row,col+1))
                return true;
        }
        //Otherwise, reset the value to empty
        boardValues[row][col] = EMPTY;
                
        //No solution found with either value, so return false so other options can be tried prior to this
        return false;
    }
    
    //Checks validity of current state
    public static boolean validState(int row, int col){      
            
        if(col >= 2){
            if(boardValues[row][col] == boardValues[row][col-1] && boardValues[row][col] == boardValues[row][col-2])
                return false;
        }
        
        if(row >= 2){
            if(boardValues[row][col] == boardValues[row-1][col] && boardValues[row][col] == boardValues[row-2][col])
                return false;
        }
        //Mean we're at the end of a row
        
        if(!workQuota(row,col))
            return false;
               
        return true;
    }
    
    //Ensures that all reapers work the same number of spots as other reapers
    //and all sowers work the same number of spots as other sowers
    //Then if that requirement is met, we call the addSolution function to make sure the row or pattern we're looking
    //at is unique
    public static boolean workQuota(int row, int col){
                   
        //Row check
        int count = 0;
        
        for(int i = 0; i < numCols; i++){
            if(boardValues[row][i] == USED)
                count++;
            }
        
        if(count > maxSow)
            return false;
        
        if(col == numCols - 1){  
            
            if(count < maxSow)
                return false;
            
            if(!addSolution(0,row))
                return false;
        }
        
        //Column check
        count = 0;
        
        for(int i = 0; i < numRows; i++){
                if(boardValues[i][col] == USED)
                    count++;
        }
        
        if(count > maxReap)
            return false;
        
        if(row == numRows - 1){
            
            if(count < maxReap)
                return false;
            
            if(!addSolution(1,col))
                return false;
        }
        
        return true;
    }
    
    //Returns false if any previous rows/columns share a pattern with the row or column that we're inspecting
    public static boolean addSolution(int flag, int index){
        
        int total = 0;
        
        //Checking if any rows have this solution
        if(flag == 0){
            
            for(int i = 0; i < numCols; i++){
                if(boardValues[index][i] == USED){
                    total += Math.pow(2, (numCols -1) - i);
                }
            }
            
            if(index > 0){
            //Check if this solution has already been used for a previous row
                for(int i = 0; i < index; i++){
                    if(total == solvedRows[i])
                        return false;
                }
            }
            
            solvedRows[index] = total;
        }
        
        //Checking if any columns have this solution
        else if(flag == 1){
            
            for(int i = 0; i < numRows; i++){
                if(boardValues[i][index] == USED){
                    total += Math.pow(2, (numRows -1) - i);
                }
            }
            
            Math.pow(flag, flag);
            
            if(index > 0){
            //Check if this solution has already been used for a previous column
                for(int i = 0; i < index; i++){
                    if(total == solvedCols[i])
                        return false;
                }
            }
            
            solvedCols[index] = total;
        }
        
        //We have found a unique row or column solution
        return true;
    }
    
     //Simple function to print the array representing the solution
    public static void printSolution(){
        
        System.out.print("Solution Found\n");
        
        printLine();
        
        for(int i = 0; i < numRows; i++){
            
            for(int j = 0; j < numCols; j++){  
                
                System.out.print("| ");
                System.out.print(boardValues[i][j] + " ");
            }
            
            System.out.println("|");
            printLine();
        }  
        
        System.out.print("\n");
    }
    
    //Prints a border (Taken from class notes by Dr. Meade)
    public static void printLine(){
        
        for(int i = 0; i < numCols; i++){ 
            System.out.print("+---");    
        }
        
        System.out.println("+");
    }
}


