import processing.core.PApplet;
import java.util.ArrayList;

/**
 * Template for programs with Processing graphics output.
 * @author Collin Jin
 */
public class Sketch extends PApplet {
    ArrayList <Integer> playerHand = new ArrayList<Integer>(); // Special kind of array which grows/shrinks based on # of values
    ArrayList <Integer> dealerHand = new ArrayList<Integer>();

    int randomIndex;
    boolean playerTurn = true; 
    boolean wouldYouLikePlayAgain = false;

    int [] deck = {11,2,3,4,5,6,7,8,9,10,10,10,10};
    String [] cardName = {"Ace", "2","3", "4","5","6","7","8","9","10", "Jack", "Queen", "King"};
    int card; 
    int aceCount = 0;


    public static void main(String[] args) {
        PApplet.main("Sketch");
    }

    private int randomDeckIndex(){
        return((int)(Math.random() * deck.length));
    }

    @Override
    public void settings() {
        size(1200, 1000); 
    }

    @Override
    public void setup() {
        gameStart();
    }

    @Override
    public void draw() {
        playerAndDealerTurn();
    }

    private void gameStart(){
        background(0);  //Black 
        fill(255);
        textSize(20);
        println();
        println("=========Black Jack=========");
        println();
        for (int i = 0; i < 2; i++){
            playerHand.add(deck[randomDeckIndex()]); // Adds random card
            dealerHand.add(deck[randomDeckIndex()]);
        }
    }

    private void playerAndDealerTurn(){
        if (playerTurn){
            playerTurn();
        }
        else {
            while (getSum(dealerHand)<17){
               dealerTurn(); 
            }
            determineWinner();
        }
    }

    public void keyPressed() {
        // Note: It's safer to use the logical AND (&&) instead of bitwise AND (&)
        if (key == 'h' && playerTurn) {
            playerHand.add(deck[randomDeckIndex()]); 
        }
        if (key == 's' && playerTurn){
            playerTurn = false;
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

    private void playerTurn(){
        showHands();
        text("Would you like to hit or stay? (h/s)", 20, 90);
        playerTurn = true;
        if (getSum(playerHand) >= 21){
            determineWinner();
        }
    }

    private void dealerTurn(){
        showHands();
        dealerHand.add(deck[randomDeckIndex()]);
    }

    private void determineWinner(){
        showHands();
        boolean playerWon;
        
        if (getSum(playerHand)==21||getSum(dealerHand)>21){
            playerWon = true;
            text("WINNER WINNER WINNER", 20, 90);
        } 
        else if(getSum(playerHand)>21||getSum(dealerHand)==21){
            playerWon = false;
            text("HOUSE WINS " , 20, 90);
        } 
        else if (getSum(playerHand)>getSum(dealerHand)){
            playerWon = true;
            text("WINNER WINNER WINNER", 20, 90);
        }
        else{
            playerWon = false;
            text("HOUSE WINS " , 20, 90);
        }

        wouldYouLikePlayAgain = true; 
        text("Would You like to Play Again? (y/n)" , 20, 120);
    }

    private int getSum(ArrayList <Integer> hand){
        int sum = 0; 
        for (int card : hand){
            sum += card;
            if (card == 11){
                aceCount++;
            }

            while (sum > 21 && aceCount > 0){
                sum -= 10;
            }
        }
        return sum;
    }

    private void showHands(){
        background(0); 
        fill(255);
        textSize(20);
        text("You have: " + playerHand + ", sum: " + getSum(playerHand), 20, 30);
        text("The dealer reveals " + dealerHand + getSum(dealerHand), 20, 60);
    }

    private void wouldYouLikePlayAgain(){       
        playerHand.clear();
        dealerHand.clear();
        gameStart();
        playerAndDealerTurn();
        playerTurn = true;
    }

}