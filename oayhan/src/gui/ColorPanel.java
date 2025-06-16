// Name: Okan Ayhan
// Matrikelnummer:7380423

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;

import javax.swing.*;
import logic.Board;
import logic.Field;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ColorPanel extends JPanel {
	
	private Color[] colors;
	private BoardPanel boardPanel;
	
	private boolean isStarted = false;
	private MouseListener customMouseListener;
	private KeyListener customKeyListener;
	private Timer timer;
	
	private int offsetX;
	private int offsetY;

	public ColorPanel(Color[] colors, BoardPanel boardPanel) {
		this.boardPanel = boardPanel; // boardPanel-Objekt mit dem Attribut menuPanel (mit Anzahl ausgewählter Farben)
									// wird hier dem boardPanel-Attribut des ColorPanel-Objekts übergeben
		this.colors = colors;

	}

	public void addCustomMouseListener(Board board) {
		customMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int mouseX = e.getX();
				int mouseY = e.getY();
                
				int numColors = (int) boardPanel.getMenuPanel().getNumberFarbenBox().getSelectedItem();
				int squareSize = Math.min(getWidth() / numColors, getHeight());
				
				int boardWidth = squareSize*board.getCols();
                
                 if (mouseX > offsetX && mouseX <= offsetX + boardWidth && mouseY > offsetY && mouseY <= offsetY + squareSize) {
         
        		int clickedColorIndex = (e.getX() - offsetX) / squareSize;

				// Überprüfe, ob der Index im gültigen Bereich liegt
				if (clickedColorIndex >= 0 && clickedColorIndex < numColors) {
					// Jetzt bezieht sich clickedColorIndex auf die Position der Farbe in Ihrem
					// colors-Array
					// Was bedeutet, dass wenn man auf das rote Rechteck im ColorPanel klickt,
					// die Zahl 0 erhält, wenn man auf das grüne Rechteck klickt, die Zahl 1 usw.

					boolean colorExists = false;
					for (Field field : board.getKomponentenSpieler1()) {
						if (field.getColor() == clickedColorIndex) {
							JOptionPane.showMessageDialog(null,
									"Du kannst nicht die aktuelle Farbe deiner eigenen Komponente wählen");
							colorExists = true;
							break;
						}
					}

					// Check whether the clicked color is not the same as the color in the position
					// [0][cols-1]
					if (board.getField(0, board.getCols() - 1).getColor() == clickedColorIndex) {
						JOptionPane.showMessageDialog(null,
								"Du kannst nicht die aktuelle Farbe der Komponente des Computers wählen.");
						colorExists = true;
					}

					// Die Operation wird nur ausgeführt, wenn die Farbe nicht bereits in der
					// ArrayList vorhanden ist
					if (!colorExists && board.isSpieler1AmZug()) {

						board.setKomponentenSpieler1Alt(new ArrayList<>(board.getKomponentenSpieler1()));

						ArrayList<Field> newFields = board.selectSameColorNeighbors(board.getKomponentenSpieler1(),clickedColorIndex);
                        	board.getKomponentenSpieler1().addAll(newFields);
							board.floodField(clickedColorIndex, board.getKomponentenSpieler1());
							boardPanel.getGameBoardPanel().repaint();
							boardPanel.getMenuPanel().updatePlayer1Score(board.getKomponentenSpieler1().size());
							
							
                        
                        	boolean keineVeraenderung = board.pruefeObKeineVeraenderung(); 
                        	
                        	if (keineVeraenderung){
                            	board.incrementCounterPruefung();
                            
                            
                            	if (board.getCounterPruefung() == 4){
                                	board.vorzeitigerAbbruch();
									board.resetGame();
									return;
                            		}
                        	}
                    
                        	if (!keineVeraenderung && board.getCounterPruefung()>=1) { //Feld wurde größer von S1
                    
                            	board.setCounterPruefung(0);
                        	}

							if (board.isEndkonfiguration()){
                            	board.pruefeGewinner();;
								board.resetGame();
								return;
                        	}


							board.setSpieler1AmZug(false);
							timer = new Timer(1000, m -> {
								boardPanel.getGameBoardPanel().getBoard().fuehreComputerzugAus();
							});
							timer.setRepeats(false);
							timer.start();
							
					}
				}
			}
		}};

		// Füge den MouseListener hinzu
		this.addMouseListener(customMouseListener);
	}

	public void addCustomKeyListener(Board board) {
		this.setFocusable(true);
		this.requestFocusInWindow();
		customKeyListener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char keyChar = e.getKeyChar();
				if (Character.isDigit(keyChar)) {
					int clickedColorIndex = Character.getNumericValue(keyChar) - 1; // -1 rechnen da wir in
																					// paint-Methode ja noch +1 addieren

					// Überprüfe, ob der Index im gültigen Bereich liegt
					if (clickedColorIndex >= 0
							&& clickedColorIndex < (int) boardPanel.getMenuPanel().getNumberFarbenBox().getSelectedItem()) {

						boolean colorExists = false;
						for (Field field : board.getKomponentenSpieler1()) {
							if (field.getColor() == clickedColorIndex) {
								JOptionPane.showMessageDialog(null,
										"Du kannst nicht die aktuelle Farbe deiner eigenen Komponente wählen");
								colorExists = true;
								break;
							}
						}

						// Check whether the clicked color is not the same as the color in the position
						// [0][cols-1]
						if (board.getField(0, board.getCols() - 1).getColor() == clickedColorIndex) {
							JOptionPane.showMessageDialog(null,
									"Du kannst nicht die aktuelle Farbe der Komponente des Computers wählen.");
							colorExists = true;

						}

						// Die Operation wird nur ausgeführt, wenn die Farbe nicht bereits in der
						// ArrayList vorhanden ist
						if (!colorExists && board.isSpieler1AmZug()) {

						board.setKomponentenSpieler1Alt(new ArrayList<>(board.getKomponentenSpieler1()));

						ArrayList<Field> newFields = board.selectSameColorNeighbors(board.getKomponentenSpieler1(),clickedColorIndex);
                        	board.getKomponentenSpieler1().addAll(newFields);
							board.floodField(clickedColorIndex, board.getKomponentenSpieler1());
							boardPanel.getGameBoardPanel().repaint();
							boardPanel.getMenuPanel().updatePlayer1Score(board.getKomponentenSpieler1().size());
							
							
                        
                        	boolean keineVeraenderung = board.pruefeObKeineVeraenderung(); 
                        	if (keineVeraenderung){
                            	board.incrementCounterPruefung();
                
                            
                            	if (board.getCounterPruefung() == 4){
                                	board.vorzeitigerAbbruch();
									board.resetGame();
									return;
                            		}
                        	}
                        	
                        	if (!keineVeraenderung && board.getCounterPruefung()>=1) { //Feld wurde größer von S1
                            	
                            	board.setCounterPruefung(0);
                        	}



							if (board.isEndkonfiguration()){
                            	board.pruefeGewinner();
								board.resetGame();
								return;
                        	}


							board.setSpieler1AmZug(false);
							timer = new Timer(1000, m -> {
								board.fuehreComputerzugAus();
							});
							timer.setRepeats(false);
							timer.start();

						}
					}
				}
			}
		};

		// Hier fügen wir den KeyListener hinzu
		this.addKeyListener(customKeyListener);
	}

	public void removeCustomMouseListener() {
		if (customMouseListener != null) {
			removeMouseListener(customMouseListener);
			customMouseListener = null;
		}
	}

	public void disableMouseListener() {
		if (customMouseListener != null) {
			removeMouseListener(customMouseListener);
			customMouseListener = null;
		}
	}

	public void enableMouseListener() {
		if (customMouseListener != null) {
			this.addMouseListener(customMouseListener);
		}
	}

	public void removeCustomKeyListener() {
		if (customKeyListener != null) {
			removeKeyListener(customKeyListener);
			customKeyListener = null;
		}
	}

	public void disableKeyListener() {
		if (customKeyListener != null) {
			removeKeyListener(customKeyListener);
			customKeyListener = null;
		}
	}

	public void enableKeyListener() {
		if (customKeyListener != null) {
			this.addKeyListener(customKeyListener);
		}
	}

	public void start() {
		this.isStarted = true;
		repaint();
	}

	public void stop() {
		this.isStarted = false;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!isStarted) { // nicht zeichnen, wenn start-button in menupanel nicht betätigt wurde
			return;
		} else {
			int numColors = (int) boardPanel.getMenuPanel().getNumberFarbenBox().getSelectedItem(); // so wird sichergestellt,
																						// dass nur so viele Quadrate
																						// erzeugt werden, wie Anzahl
																						// der Farben im MenuPanel
																						// ausgewählt sind
			int squareSize = Math.min(getWidth() / numColors, getHeight());
			offsetX = (getWidth() - (squareSize * numColors)) / 2;
			offsetY = (getHeight() - squareSize) / 2; // Berechne den Y-Offset, um die Quadrate zu zentrieren

			g.setFont(new Font("Arial", Font.BOLD, squareSize / 3)); // Set the font size based on square size

			for (int i = 0; i < numColors; i++) {
				g.setColor(colors[i]);
				g.fillRect(offsetX + (i * squareSize), offsetY, squareSize, squareSize);
				g.setColor(Color.BLACK);
				g.drawRect(offsetX + (i * squareSize), offsetY, squareSize, squareSize);

				// Draw the number in the middle of the square
				int number = i + 1; // Start the numbering from 1
				String strNumber = String.valueOf(number);

				// Calculate the size of the string to center it
				FontMetrics metrics = g.getFontMetrics();
				int x = offsetX + (i * squareSize) + (squareSize - metrics.stringWidth(strNumber)) / 2;
				int y = offsetY + squareSize / 2; // Berücksichtige den Y-Offset beim Zeichnen der Zahlen

				g.drawString(strNumber, x, y);
			}
		}
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public void setBoardPanel(BoardPanel boardPanel) {
		this.boardPanel = boardPanel;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public MouseListener getCustomMouseListener() {
		return customMouseListener;
	}

	public void setCustomMouseListener(MouseListener customMouseListener) {
		this.customMouseListener = customMouseListener;
	}

	public KeyListener getCustomKeyListener() {
		return customKeyListener;
	}

	public void setCustomKeyListener(KeyListener customKeyListener) {
		this.customKeyListener = customKeyListener;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;}
	
	
	
}
