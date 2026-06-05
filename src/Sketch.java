import processing.core.PApplet;
import java.util.ArrayList;
import processing.core.PFont;

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
    int gameState = 0;
    boolean displayOneCard;

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

    int targetXPosition;
    int cardSpacing = 200;
    float cardSpeed = (float)0.1;
    
    ArrayList <Float> playerCardXPosition = new ArrayList<Float>();
    ArrayList <Float> dealerCardXPosition = new ArrayList<Float>();

    // Array of card suit symbols
    String[] suits = {"♠", "♥", "♦", "♣"};
    PFont cardFont; // Global font configuration variable

    public static void main(String[] args) {
        PApplet.main("Sketch");
    }

    @Override
    public void settings() {
        size(1200, 1000); 
    }

    @Override
    public void setup() {
        background(0);
        cardFont = createFont("Arial", 64, true);
    }

    @Override
    public void draw() {
        if (gameState == 0){
            welcomeScreen();
        }
        else if (gameState == 1){
            showHandsAndGameMessage();   
        }
    }

    private void welcomeScreen(){
        background(0);
        textFont(cardFont); // Use loaded font asset safely
        textAlign(CENTER, CENTER);
        fill(255);
        textSize(50);
        text("WELCOME TO COLLINS POKER ROOM", 600, 500);
        text("PRESS B TO PLAY: ", 600, 700);        
    }

    private void gameStart(){
        playerTurn = true;

        for (int i = 0; i < 2; i++){                    
            playerHand.add(randomDeckIndex());              // Store the INDEX of card value 
            playerCardXPosition.add((float)-200.0);

            dealerHand.add(randomDeckIndex());    
            dealerCardXPosition.add((float)-200.0);
        }

        playerSum = getSum(playerHand);                     // Updates sum of dealer and player hands    
        dealerSum = getSum(dealerHand);

        if (playerSum >= 21 || dealerSum >= 21){            // Checks for if anyone was sum of 21 
            playerTurn = false;                        
            determineWinner();
        }
    }

    private void showHandsAndGameMessage(){
        background(21, 115, 63);

        playerSum = getSum(playerHand);                           // Updates sum of dealer and player hands
        dealerSum = getSum(dealerHand);
        
        // Draw Player Hand
        cardY = 700;
        cardsVisual(playerHand, playerCardXPosition, false);      // false = don't hide anything

        // Draw Dealer Hand
        cardY = 200;
        cardsVisual(dealerHand, dealerCardXPosition, playerTurn); // hides card faces during player turn

        // Realigns text and displays dealer and player sum
        textAlign(LEFT, TOP);
        fill(255);
        textSize(40);

        text("Your sum: " + playerSum, 20, 10);
        if (playerTurn){
            text("Dealer has " + deck[dealerHand.get(0)] + " and [?]", 20, 60);
        }
        else {
            text("Dealer sum: " + dealerSum, 20, 60);
        }
        text(gameMessage, 20, 110);
    }

    private void cardsVisual(ArrayList<Integer> hand, ArrayList<Float> cardPositionX, boolean hideSecondCard){
        targetXPosition = 100;

        for (int i = 0; i < hand.size(); i++){
            int deckIndex = hand.get(i);
            float currentX = cardPositionX.get(i);

            currentX += (targetXPosition - currentX) * (float)cardSpeed;  // The card moves 10% of the remaining distance from the target every frame. 
            cardPositionX.set(i, currentX);                              

            // Draws card
            stroke(180);
            strokeWeight(1);
            fill(255, 255, 255);
            rect(currentX, cardY, cardWidth, cardHeight, 8);  
            noStroke();

        // If it's the dealer's turn and this isn't the first card, paint a card back design

        if (hideSecondCard && i > 0) {
            fill(40, 90, 180); // Blue card back color
            rect(currentX + 10, cardY + 10, cardWidth - 20, cardHeight - 20, 4);
            
            fill(255, 150); // white accent lines
            textAlign(CENTER, CENTER);
            textSize(30);
            text("?", currentX + (cardWidth / 2f), cardY + (cardHeight / 2f));
        } 

            // Otherwise, draw the normal face card 
        else {
            String suit = suits[deckIndex % 4];
            
            if (suit.equals("♥") || suit.equals("♦")) {
                fill(220, 30, 30); 
            } else {
                fill(0, 0, 0);     
            }

            // Draw Corner Rank & Suit Indicator 
            textFont(cardFont);
            textAlign(LEFT, TOP);
            textSize(18);
            text(cardName[deckIndex] + " " + suit, currentX + 10, cardY + 10);

            // Find center points
            float cardCenterX = currentX + (cardWidth / 2.0f);
            float cardCenterY = cardY + (cardHeight / 2.0f);

            textAlign(CENTER, CENTER);
            textSize(65); 
            text(suit, cardCenterX, cardCenterY + 10);
        }

            targetXPosition += cardSpacing;                         
        }
    }

    public void keyPressed() {
        if (key == 'b' && gameState == 0){
            gameState = 1; 
            gameStart();
        }

        if (key == 'h' && playerTurn) {                 
            playerHand.add(randomDeckIndex());           
            playerCardXPosition.add((float)-200.0);

            playerSum = getSum(playerHand);

             if (playerSum >= 21){                       
                determineWinner();
                playerTurn = false;
            }
        }

        if (key == 's' && playerTurn){                  
            background(21, 115, 63);                    
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
            dealerHand.add(randomDeckIndex());
            dealerCardXPosition.add((float)-200.0);
            dealerSum = getSum(dealerHand);
        } 
        determineWinner();                        
    }

    private void determineWinner(){
        playerSum = getSum(playerHand);                 
        dealerSum = getSum(dealerHand);
        
        if (playerSum > 21){                            
            gameMessage = "HOUSE WINS ";
        }
        else if (playerSum == 21 || dealerSum > 21){      
            gameMessage = "WINNER WINNER WINNER";
        } 
        else if(dealerSum == 21){                       
            gameMessage = "HOUSE WINS ";
        } 
        else if(playerSum == dealerSum){                
            gameMessage = "TIE, Bets have been returned ";
        } 
        else if (playerSum > dealerSum){                
            gameMessage = "WINNER WINNER WINNER";
        }
        else{                                           
            gameMessage = "HOUSE WINS ";
        }
        
        gameMessage += ". Would you like to play again? (y/n)"; 
        wouldYouLikePlayAgain = true;
    }

    private int getSum(ArrayList<Integer> hand){
        int sum = 0; 
        int aceCount = 0;
        for (int deckIndex : hand){
            int cardValue = deck[deckIndex];                        // Translate the index back to its score value
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
        playerCardXPosition.clear();
        dealerCardXPosition.clear(); 
        gameStart();
        gameMessage = "Would you like to hit or stay? (h/s)";
    }
    
    private int randomDeckIndex(){
        return((int)(Math.random() * deck.length));
    }
}