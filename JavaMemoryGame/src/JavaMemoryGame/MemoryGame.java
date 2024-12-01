package JavaMemoryGame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.util.*;
import java.util.Collections;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.*;

//Class that stores card info
class CardValue{
	private ImageIcon cardImage;
	private String cardIdx;
	
	//getter and setter
	public void setCardIdx(String idx) {
		this.cardIdx = idx;
	}
	public void setCardImage(ImageIcon icon) {
		this.cardImage = icon;
	}
	public String getCardIdx() {
		return this.cardIdx;
	}
	public ImageIcon getCardImage() {
		return this.cardImage;
	}
	
}


public class MemoryGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel primary;
	
	//Number of attempts, best record, and factors that determine the end of the game
	private int tryCount;
	private int isEnd;
	private int maxScore = Integer.MAX_VALUE;
	
	//List to store card information to be used in the game
	private ArrayList<JButton> buttons;
	private ArrayList<CardValue> cardInfo;
	
	//Button object to store information about cards selected by the player.
	private JButton firstCard, secondCard;
	
	private boolean checkingPair = false;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					MemoryGame frame = new MemoryGame();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MemoryGame() {
		//GUI created with windowBuilder
		setBackground(new Color(255, 255, 255));
		setResizable(false);
		setTitle("Memory Game!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		primary = new JPanel();
		primary.setBackground(new Color(128, 128, 128));
		primary.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(primary);
		primary.setLayout(new GridLayout(4, 4, 2, 2));
		
		
		//A function that initializes the information of each card.
		initFrame();
		
		//The process of placing buttons on a panel and assigning events
		buttons = new ArrayList<>();
		for(int i = 0; i < 16; i++) {
			//Initialize the text of each button to ?
			JButton button = new JButton("?");
			button.setBackground(new Color(205, 180, 219));
			int index = i;
			
			//Assign an event listener to the button
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ButtonEvent(button, index);
				}
			});
			
			//Insert buttons into lists and panels
			buttons.add(button);
			primary.add(button);
		}
	}
	
	//A Timer is used to implement the card flip effect
	//The timer will start running after 1 second
	Timer timer = new Timer(1000, new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	    	//If the images on the two cards are different, the button color and text are initialized
	        if (!firstCard.getIcon().equals(secondCard.getIcon())) {
	            firstCard.setIcon(null);
	            firstCard.setText("?");
	            firstCard.setBackground(new Color(205, 180, 219));
	            secondCard.setIcon(null);
	            secondCard.setText("?");
	            secondCard.setBackground(new Color(205, 180, 219));
	        }
	        
	        //Resets the comparison card
	        firstCard = null;
	        secondCard = null;
	        checkingPair = false;
	        
	        //If all the cards are turned over
	        if(isEnd == 8) {
	        	//It tells you that the game is over
	        	JOptionPane.showMessageDialog(null, "Game over!\nYou try " + tryCount + " times!");
	        	
	        	//If a record is broken, the user is notified of the update
	        	if(maxScore > tryCount) {
	        		maxScore = tryCount;
	        		JOptionPane.showMessageDialog(null, "new record!\n My best record : " + maxScore);
	        	}
	        	
	        	//And give the user the choice of whether to restart the game or not
	        	int choice = JOptionPane.showConfirmDialog(null, "Try Again?", "Game Over", JOptionPane.YES_NO_OPTION);
	        	//If you choose to restart the game, your game information will be reset
	        	if(choice == JOptionPane.YES_OPTION) {
	        		restartGame();
	        	}
	        }

	        ((Timer) e.getSource()).stop(); // Stop Timer
	    }
	});

    
	//A function that initializes the information of each card.
	public void initFrame() {
		//Reset number of attempts and end point
		tryCount = 0;
		isEnd = 0;
		
		//For each card, insert them into the list in pairs
		cardInfo = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			CardValue cardx = new CardValue();
			CardValue cardy = new CardValue();
			
			cardx.setCardIdx(String.valueOf(i));
			cardy.setCardIdx(String.valueOf(i));
			
			ImageIcon icon = new ImageIcon("imageFile/"+ (i+1) +".png");
			Image origImage = icon.getImage();
			Image resizeImage = origImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			ImageIcon resizeIcon = new ImageIcon(resizeImage);
			
			cardx.setCardImage(resizeIcon);
			cardy.setCardImage(resizeIcon);
			
			cardInfo.add(cardx);
			cardInfo.add(cardy);
		}
		//Shuffle the list
		Collections.shuffle(cardInfo);
	}
	
	//Function to initialize game information
	public void restartGame() {
		//Initializes the list that stores each card's information and the information for the inserted buttons
		
		initFrame();
		Collections.shuffle(cardInfo);
		
		for(int i = 0; i < buttons.size(); i++) {
			JButton button = buttons.get(i);
			button.setText("?");
			button.setIcon(null);
			button.setBackground(new Color(205, 180, 219));
		}
		
		firstCard = null;
		secondCard = null;
		checkingPair = false;
	}
	
	
	
	public void ButtonEvent(JButton button, int idx) {
		// If the card is already flipped or being checked, do not process the event.
		if(checkingPair || button.getText() != "?") return;
		
		//Get information from the card list.
		CardValue card = cardInfo.get(idx);
		ImageIcon icon = card.getCardImage();
		button.setIcon(icon);
		button.setText(null);
		
		//If this is the first card selection, enter the button information in firstCard.
		if(firstCard == null) {
			firstCard = button;
			firstCard.setBackground(new Color(189, 224, 254));
		}
		//If the first card has already been selected, the second card is selected,
		//so information is entered into secondCard and comparison is performed.
		else {
			secondCard = button;
			checkingPair = true;
			//The colors of each card are changed based on the card comparison information, and the number of attempts is also updated.
			 if (firstCard.getIcon().equals(secondCard.getIcon())) {
				 firstCard.setBackground(new Color(175, 252, 65));
				 secondCard.setBackground(new Color(175, 252, 65));
				 isEnd++;
				 tryCount++;
			}
			 else {
				 firstCard.setBackground(new Color(255, 175, 204));
				 secondCard.setBackground(new Color(255, 175, 204));
				 tryCount++;
			 }
			 
			//Call the timer.
			timer.setRepeats(false);
			timer.start();
		}
	}
	
}
