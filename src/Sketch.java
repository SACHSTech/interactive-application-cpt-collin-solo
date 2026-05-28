import processing.core.PApplet;
import java.util.ArrayList;

/**
 * Template for programs with Processing graphics output.
 * @author Your Name
 */
public class Sketch extends PApplet {
    ArrayList <Integer> playerHand = new ArrayList<Integer>(); // Special kind of array which grows/shrinks based on # of values
    ArrayList <Integer> dealerHand = new ArrayList<Integer>();

    int randomIndex;

    int [] deck = {1,2,3,4,5,6,7,8,9,10,10,10,10};
    String [] cardName = {"1", "2","3", "4","5","6","7","8","9","10", "Jack", "Queen", "King"};
    int card; 


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
        randomIndex = randomDeckIndex();
        for (int i = 0; i < 2; i++){
            playerHand.add(deck[randomDeckIndex()]); // Adds random card
            dealerHand.add(deck[randomDeckIndex()]);
        }
    }


    @Override
    public void draw() {
        background(0);  //Black 
        fill(255);
        textSize(20);
        text(playerHand + " " + dealerHand, 20, 30);
    }

    // public void keyPressed() {
    //     if (key == '1') {
    //         verticalSpeed1 -= 10; // Gives ball upward boost
    //     }
    //     if (key == '2'){
    //         verticalSpeed2 -= 10; 
    //     }
    // }

}
