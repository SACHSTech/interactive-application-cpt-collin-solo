import processing.core.PApplet;
import java.util.ArrayList;

/**
 * Template for programs with Processing graphics output.
 * @author Collin Jin
 */
public class Sketch extends PApplet {

    // Game Setup
    boolean playerTurn = true; 
    boolean wouldYouLikePlayAgain = false;
    String gameMessage = "Would you like to hit or stay? (h/s)";
    int playerSum;
    int dealerSum;

    // Cards and Deck
    int [] deck = {11,2,3,4,5,6,7,8,9,10,10,10,10};
    String [] cardName = {"Ace", "2","3", "4","5","6","7","8","9","10", "Jack", "Queen", "King"};
    int card; 

    ArrayList <Integer> playerHand = new ArrayList<Integer>(); 
    ArrayList <Integer> dealerHand = new ArrayList<Integer>();

    //Card Visuals
    int cardX = 100;
    int cardY = 700;
    int cardWidth = 150;
    int cardHeight = 200;


    public static void main(String[] args) {
        PApplet.main("Sketch");
    }

    @Override
    public void settings() {
        size(1200, 1000); 
    }

    @Override
    public void setup() {
        background(21, 115, 63);  //Green felt colour
        gameStart();
    }

    @Override
    public void draw() {
        showHandsAndGameMessage();                      
    }

    private void gameStart(){
        playerTurn = true;

        // INSIDE gameStart()
        for (int i = 0; i < 2; i++){                    
            playerHand.add(randomDeckIndex());          // Store the INDEX instead of card value
            dealerHand.add(randomDeckIndex());    
        }

        playerSum = getSum(playerHand);                 // Updates sum of dealer and player hands 
        dealerSum = getSum(dealerHand);

        if (playerSum >= 21 || dealerSum >= 21){        // Checks for if anyone was sum of 21 
            playerTurn = false;                        
            determineWinner();
        }
    }

    private void showHandsAndGameMessage(){

        background(21, 115, 63);

        playerSum = getSum(playerHand);                 // Updates sum of dealer and player hands
        dealerSum = getSum(dealerHand);
        
        cardsVisual(playerHand);
        cardY = 200;
        cardsVisual(dealerHand);
        cardY = 700;

        textAlign(LEFT, TOP);

        fill(255);
        textSize(20);
        text("You have: " + playerHand + ", sum: " + playerSum, 20, 30);

        if (playerTurn){                                                                // On the player's turn the dealer only reveals on card
            text("The dealer reveals " + dealerHand.get(0) + " and [?] ", 20, 60);
        }
        else {
            text("The dealer reveals " + dealerHand + ", sum: " + dealerSum, 20, 60);
        }

        text(gameMessage, 20, 90);
    }

    private void cardsVisual(ArrayList<Integer> hand){
        cardX = 100;
        for (int deckIndex : hand){
            fill(255, 255, 255);
            rect(cardX, cardY, cardWidth, cardHeight);  

            int cardCenterX = cardX + (cardWidth / 2);
            int cardCenterY = cardY + (cardHeight / 2);

            // Uses the index of stored in hand to find card name
            fill(0);
            textAlign(CENTER, CENTER);                              // Centers the text on card
            text(cardName[deckIndex], cardCenterX, cardCenterY);

            cardX += 200;
        }
    }

    public void keyPressed() {

        if (key == 'h' && playerTurn) {                 
            playerHand.add(randomDeckIndex());           // Player presses H to hit
            playerSum = getSum(playerHand);

             if (playerSum >= 21){                       // Skips player next turn if their sum is 21 or greater
                dealerTurn();
            }
        }

        if (key == 's' && playerTurn){                  // Player presses S to stay
            background(21, 115, 63);
            playerTurn = false;
            dealerTurn();
        }

        if (key == 'y' && wouldYouLikePlayAgain){       // Player presses Y to play again 
            wouldYouLikePlayAgain();
            wouldYouLikePlayAgain = false;
        }

        if (key == 'n' && wouldYouLikePlayAgain){       // Player presses N to exit game 
            text("THANKS FOR PLAYING", 20, 150);
            wouldYouLikePlayAgain = false;
        }
    }

    private void dealerTurn(){
        dealerSum = getSum(dealerHand);                 // Update dealer hand sum
        while (dealerSum < 17){                         // Hits while the dealer's sum is less than 17
            dealerHand.add(deck[randomDeckIndex()]);
            dealerSum = getSum(dealerHand);
        } 
        determineWinner();                        
    }

    private void determineWinner(){
        boolean playerWon;
        playerSum = getSum(playerHand);                 // Updates sum of dealer & player hands
        dealerSum = getSum(dealerHand);
        
        if (playerSum > 21){                            // Player busts = Loss
            playerWon = false;
            gameMessage = "HOUSE WINS ";
        }
        else if (playerSum == 21||dealerSum > 21){      // Dealer bust/Player has 21 = Win 
            playerWon = true;
            gameMessage = "WINNER WINNER WINNER";
        } 
        else if(dealerSum == 21){                       // Dealer has 21 = Loss
            playerWon = false;
            gameMessage = "HOUSE WINS ";
        } 
        else if(playerSum == dealerSum){                // Dealer has same as player = Push
            playerWon = false;
            gameMessage = "TIE, Bets have been returned ";
        } 
        else if (playerSum > dealerSum){                // If no bust and player > dealer = Win
            playerWon = true;
            gameMessage = "WINNER WINNER WINNER";
        }
        else{                                           // If no bust and dealer > player = Loss
            playerWon = false;
            gameMessage = "HOUSE WINS ";
        }
        
        gameMessage += ". Would you like to play again? (y/n)"; 
        wouldYouLikePlayAgain = true;
    }

    private int getSum(ArrayList<Integer> hand){
        int sum = 0; 
        int aceCount = 0;
        for (int deckIndex : hand){
            int cardValue = deck[deckIndex];            // Translate the index back to its score value
            sum += cardValue;
            if (cardValue == 11){                            
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