import processing.core.PApplet;
import java.util.ArrayList;

/**
 * Template for programs with Processing graphics output.
 * @author Collin Jin
 */
public class Sketch extends PApplet {
    ArrayList <Integer> playerHand = new ArrayList<Integer>(); 
    ArrayList <Integer> dealerHand = new ArrayList<Integer>();

    boolean playerTurn = true; 
    boolean wouldYouLikePlayAgain = false;
    String gameMessage = "Would you like to hit or stay? (h/s)";
    int playerSum;
    int dealerSum;

    int [] deck = {11,2,3,4,5,6,7,8,9,10,10,10,10};
    String [] cardName = {"Ace", "2","3", "4","5","6","7","8","9","10", "Jack", "Queen", "King"};
    int card; 


    public static void main(String[] args) {
        PApplet.main("Sketch");
    }

    @Override
    public void settings() {
        size(1200, 1000); 
    }

    @Override
    public void setup() {
        background(0);  //Black 
        gameStart();
    }

    @Override
    public void draw() {
        showHands();
    }

    private void gameStart(){
        playerTurn = true;

        for (int i = 0; i < 2; i++){                    // Draws 2 random cards for dealer and player
            playerHand.add(deck[randomDeckIndex()]); 
            dealerHand.add(deck[randomDeckIndex()]);
        }

        playerSum = getSum(playerHand);                 // Updates hand sums
        dealerSum = getSum(dealerHand);

        if (playerSum >= 21 || dealerSum >= 21){        // Checks for if anyone was dealt 21 
            playerTurn = false;
            determineWinner();
        }
    }

    private void showHands(){
        background(0);
        playerSum = getSum(playerHand);
        dealerSum = getSum(dealerHand);

        fill(255);
        textSize(20);
        text("You have: " + playerHand + ", sum: " + playerSum, 20, 30);

        if (playerTurn){
            text("The dealer reveals " + dealerHand.get(0) + " and [?] ", 20, 60);
        }
        else {
            text("The dealer reveals " + dealerHand + ", sum: " + dealerSum, 20, 60);
        }

        text(gameMessage, 20, 90);
    }

    public void keyPressed() {
        if (key == 'h' && playerTurn) {
            playerHand.add(deck[randomDeckIndex()]); 
            playerSum = getSum(playerHand);

            if (playerSum >= 21){
                dealerTurn();
            }
        }
        if (key == 's' && playerTurn){
            background(0);
            playerTurn = false;
            dealerTurn();
        }
        if (key == 'y' && wouldYouLikePlayAgain){
            wouldYouLikePlayAgain();
            wouldYouLikePlayAgain = false;
        }
        if (key == 'n' && wouldYouLikePlayAgain){
            text("THANKS FOR PLAYING", 20, 150);
            wouldYouLikePlayAgain = false;
        }
    }

    private void dealerTurn(){
        dealerSum = getSum(dealerHand);
        while (dealerSum < 17){
            dealerHand.add(deck[randomDeckIndex()]);
            dealerSum = getSum(dealerHand);
        } 
        determineWinner();
    }

    private void determineWinner(){
        boolean playerWon;
        playerSum = getSum(playerHand);
        dealerSum = getSum(dealerHand);
        
        if (playerSum > 21){
            playerWon = false;
            gameMessage = "HOUSE WINS ";
        }
        else if (playerSum == 21||dealerSum > 21){
            playerWon = true;
            gameMessage = "WINNER WINNER WINNER";
        } 
        else if(playerSum > 21|| dealerSum == 21){
            playerWon = false;
            gameMessage = "HOUSE WINS ";
        } 
        else if(playerSum == dealerSum){
            playerWon = false;
            gameMessage = "TIE, Bets have been returned ";
        } 
        else if (playerSum > dealerSum){
            playerWon = true;
            gameMessage = "WINNER WINNER WINNER";
        }
        else{
            playerWon = false;
            gameMessage = "HOUSE WINS ";
        }
        
        gameMessage += ". Would you like to play again? (y/n)"; 
        wouldYouLikePlayAgain = true;
    }

    private int getSum(ArrayList <Integer> hand){
        int sum = 0; 
        int aceCount = 0;
        for (int card : hand){
            sum += card;
            if (card == 11){
                aceCount++;
            }

            while (sum > 21 && aceCount > 0){
                sum -= 10;
                aceCount--;
            }
        }
        return sum;
    }

    private void wouldYouLikePlayAgain(){
        playerHand.clear();
        dealerHand.clear();
        gameStart();
        gameMessage = "Would you like to hit or stay? (h/s)";
    }
    
    private int randomDeckIndex(){
        return((int)(Math.random() * deck.length));
    }

}