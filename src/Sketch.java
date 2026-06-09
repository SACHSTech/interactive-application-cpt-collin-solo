import processing.core.PApplet;
import java.util.ArrayList;
import processing.core.PFont;

/**
 * Template for programs with Processing graphics output.
 * @author Collin Jin
 */
public class Sketch extends PApplet {

    // Window & Layout Settings 

    int windowWidth = 1200;
    int windowHeight = 1000;
    int welcomeTextX = 600;
    int welcomeTitleY = 500;
    int welcomePromptY = 700;
    int hudTextX = 20;
    int hudLine1Y = 10;
    int hudLine2Y = 60;
    int hudLine3Y = 110;

    // Game Rules & Engine States 

    int waitingPhase = 0;
    int playPhase = 1;
    int endPhase = 2;
    int gameState;
    int blackJack = 21;
    int dealerStayThreshold = 17;
    int aceReductionValue = 10;
    boolean playerTurn = true; 
    boolean wouldYouLikePlayAgain = false;
    boolean displayOneCard;
    String gameMessage = "Would you like to hit or stay? (h/s)";

    // Card Asset Configurations

    int cardWidth = 150;
    int cardHeight = 200;
    float halfCardWidth = cardWidth / 2;
    float halfCardHeight = cardHeight / 2;
    int cardX = 100;
    int cardY = 700;
    int targetXPosition;
    int cardSpacing = 200;
    float cardSpeed = (float)0.05;
    float cardSpawnX = -200.0f;
    int cardCornerRounding = 8;
    int cardBackInnerOffset = 10;
    int cardBackCornerRounding = 4;

    // Font & Render Settings 

    PFont cardFont;                                         // Global font configuration variable
    int fontSizeLarge = 64;
    int fontSizeWelcome = 50;
    int fontSizeHud = 40;
    int fontSizeCardBack = 30;
    int fontSizeCardRank = 18;
    int fontSizeCardSuit = 65;

    // Deck & Tracking 

    int card; 
    int numberOfSuits = 4;
    int [] deck = {11,2,3,4,5,6,7,8,9,10,10,10,10};
    String [] cardName = {"Ace", "2","3", "4","5","6","7","8","9","10", "Jack", "Queen", "King"};
    String[] suits = {"♠", "♥", "♦", "♣"};
    int playerSum;
    int dealerSum;
    ArrayList <Integer> playerHand = new ArrayList<Integer>(); 
    ArrayList <Integer> dealerHand = new ArrayList<Integer>();
    ArrayList <Float> playerCardXPosition = new ArrayList<Float>();
    ArrayList <Float> dealerCardXPosition = new ArrayList<Float>();

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
        if (gameState == waitingPhase){
            welcomeScreen();
        }
        else if (gameState == playPhase || gameState == endPhase){
            showHandsAndGameMessage();   
        }
    }

    private void welcomeScreen(){
        background(0);                                      // Black background
        textFont(cardFont);                                 // Use our font asset 
        textAlign(CENTER, CENTER);                          // Changes alignment of text for cards 
        fill(255);                                          // White text                   
        textSize(50);
        text("WELCOME TO COLLINS BLACK JACK", welcomeTextX, welcomeTitleY);
        text("PRESS B TO PLAY: ", welcomeTextX, welcomePromptY);        
    }

    private void gameStart(){
        playerTurn = true;
        wouldYouLikePlayAgain = false; 

        for (int i = 0; i < 2; i++){                    
            playerHand.add(randomDeckIndex());              // "Draws" two cards by storing their index values in the ArrayList
            playerCardXPosition.add((float)-200.0);

            dealerHand.add(randomDeckIndex());    
            dealerCardXPosition.add((float)-200.0);
        }

        playerSum = getSum(playerHand);                     // Updates sum of dealer and player hands    
        dealerSum = getSum(dealerHand);

        if (playerSum >= blackJack || dealerSum >= blackJack){            // If player or dealer is dealt 21, skip turns and determine the winner
            playerTurn = false;
            determineWinner();
            return;
        }
    }

    private void showHandsAndGameMessage(){
        background(21, 115, 63);

        playerSum = getSum(playerHand);                           // Updates sum of dealer and player hands
        dealerSum = getSum(dealerHand);
        
        // Draw Player Hand
        cardY = 700;
        cardsVisual(playerHand, playerCardXPosition, false);     

        // Draw Dealer Hand
        cardY = 200;
        cardsVisual(dealerHand, dealerCardXPosition, playerTurn); 

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
        /** This method is seperated into three sections: 
         *  1. Drawing each card + making sure they stop at the right spot.
         *  2. Checking if the card should be shown face up or down + drawing face down if needed.
         *  3. Drawing card suit at card center. 
        **/
    
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
            rect(currentX + cardBackInnerOffset, cardY + cardBackInnerOffset, cardWidth - (cardBackInnerOffset * 2), cardHeight - (cardBackInnerOffset * 2), 4);
            
            fill(255, 150); // white accent lines
            textAlign(CENTER, CENTER);
            textSize(30);
            text("?", currentX + (halfCardWidth), cardY + (halfCardHeight));
        } 

        // Otherwise, draw the normal face card 
        else {
            String suit = suits[deckIndex % numberOfSuits];
            
            if (suit.equals("♥") || suit.equals("♦")) {
                fill(220, 30, 30); 
            } else {
                fill(0, 0, 0);     
            }

            // Draw Corner Rank & Suit Indicator 
            textFont(cardFont);
            textAlign(LEFT, TOP);
            textSize(18);
            text(cardName[deckIndex] + " " + suit, currentX + cardBackInnerOffset, cardY + cardBackInnerOffset);

            // Find center points
            float cardCenterX = currentX + (halfCardWidth);
            float cardCenterY = cardY + (halfCardHeight);

            textAlign(CENTER, CENTER);
            textSize(65); 
            text(suit, cardCenterX, cardCenterY + cardBackInnerOffset);
        }

            targetXPosition += cardSpacing;                         
        }
    }

    public void keyPressed() {
        if (key == 'b' && gameState == waitingPhase){
            gameState = playPhase; 
            gameStart();
            return;
        }

        if(gameState == playPhase){
            if (key == 'h' && playerTurn) {                 
                playerHand.add(randomDeckIndex());           
                playerCardXPosition.add((float)-200.0);

                playerSum = getSum(playerHand);

                if (playerSum >= 21){                       
                    determineWinner();
                    playerTurn = false;
                }
                return;
            }
            if (key == 's' && playerTurn){                  
                background(21, 115, 63);                    
                playerTurn = false;
                dealerTurn();
                return;
            }
        }

        if (gameState == endPhase) {

            if (key == 'y') {
                wouldYouLikePlayAgain();
                return;
            }

            if (key == 'n') {
                gameMessage = "THANKS FOR PLAYING";
                noLoop();
                return;
            }
        }
    }

    private void dealerTurn(){
        dealerSum = getSum(dealerHand);     
        
        cardY = 200;
        cardsVisual(dealerHand, dealerCardXPosition, playerTurn);       

        while (dealerSum < dealerStayThreshold){                         
            dealerHand.add(randomDeckIndex());
            dealerCardXPosition.add((float)-200.0);
            dealerSum = getSum(dealerHand);
        } 

        determineWinner();                        
    }

    private void determineWinner(){
        playerSum = getSum(playerHand);                 
        dealerSum = getSum(dealerHand);
        
        if (playerSum > blackJack){                                        // Player Busts = House win
            gameMessage = "HOUSE WINS ";
        }
        else if (playerSum == blackJack || dealerSum > blackJack){         // Player has 21 or dealer bust = Player win
            gameMessage = "WINNER WINNER WINNER";
        } 
        else if(dealerSum == blackJack){                                   // Dealer has 21 = House win
            gameMessage = "HOUSE WINS ";
        } 
        else if(playerSum == dealerSum){                                   // If dealer is equal to player == push 
            gameMessage = "TIE, Bets have been returned ";
        } 
        else if (playerSum > dealerSum){                                   // If player > dealer = Player win
            gameMessage = "WINNER WINNER WINNER";
        }
        else{                                                        
            gameMessage = "HOUSE WINS ";
        }
        
        gameMessage += ". Would you like to play again? (y/n)"; 
        wouldYouLikePlayAgain = true;
        gameState = endPhase;                                              // Swap game state
    }

    private int getSum(ArrayList<Integer> hand){
        int sum = 0; 
        int aceCount = 0;
        for (int deckIndex : hand){
            int cardValue = deck[deckIndex];                              // Translate the index back to its score value
            sum += cardValue;
            if (cardValue == 11){                            
                aceCount++;
            }

            while (sum > blackJack && aceCount > 0){           
                sum -= aceReductionValue;
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
        gameMessage = "Would you like to hit or stay? (h/s)";
        gameState = playPhase;                                           // Reset State Machine
        gameStart();
    }
    
    private int randomDeckIndex(){
        return((int)(Math.random() * deck.length));
    }
}