// Name: Okan Ayhan
// Matrikelnummer:7380423

package gui;

import javax.swing.*;

import logic.Field;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import java.awt.*;
import logic.Board;

public class GameBoardPanel extends JPanel {

    private Color[] colors;
    private Board board;
    private int offsetX;
    private int offsetY;
    private int boardHeight;
    private int boardWidth;
    private int cellSize;
    private MouseListener customMouseListener;
    private MenuPanel menuPanel; // wird benötigt um den Punktestand im MenuPanel zu aktualisieren
    private Timer timer;

    public GameBoardPanel(Board board, MenuPanel menuPanel) {
        this.board = board; // 2D-Field-Array [row][col]
        this.menuPanel = menuPanel;
        this.colors = new Color[9];
        populateColors(); // lediglich Zuweisung der Farben

    }

    public void populateColors() {
        colors[0] = Color.RED;
        colors[1] = Color.GREEN;
        colors[2] = Color.BLUE;
        colors[3] = Color.YELLOW;
        colors[4] = Color.ORANGE;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.PINK;
        colors[7] = Color.LIGHT_GRAY;
        colors[8] = Color.DARK_GRAY;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board != null) {
            cellSize = Math.min(getWidth() / board.getCols(), getHeight() / board.getRows()); // Berechnet die Größe
                                                                                                  // der quadratischen
                                                                                                  // Zellen basierend
                                                                                                  // auf der Größe des
                                                                                                  // GameBoardPanel und
                                                                                                  // der Anzahl der
                                                                                                  // Zeilen und Spalten
                                                                                                  // auf dem Brett
            boardWidth = cellSize * board.getCols(); // Berechnet die Gesamtbreite des Spielbretts
            boardHeight = cellSize * board.getRows(); // Berechnet die Gesamthöhe des Spielbretts
            offsetX = (getWidth() - boardWidth) / 2; // Berechnet den horizontalen Abstand vom linken Rand des
                                                     // GameBoardPanel bis zum Anfang des Spielbretts
            offsetY = (getHeight() - boardHeight) / 2; // Berechnet den vertikalen Abstand vom oberen Rand des
                                                       // GameBoardPanel bis zum Anfang des Spielbretts

            for (int row = 0; row < board.getRows(); row++) {
                for (int col = 0; col < board.getCols(); col++) {
                    g.setColor(colors[board.getField(row, col).getColor()]);
                    g.fillRect(offsetX + col * cellSize, offsetY + row * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(offsetX + col * cellSize, offsetY + row * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    // MouseListener erst dann hinzufügen sobald Board erstellt wird da sonst
    // NullPointerException
    public void addCustomMouseListener() {
        customMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (board != null) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    
                    int clickedRow = (mouseY - getOffsetY()) / cellSize;
                    int clickedCol = (mouseX - getOffsetX()) / cellSize;
                    
                    if (mouseX > offsetX && mouseX <= offsetX + boardWidth && mouseY > offsetY && mouseY <= offsetY + boardHeight){
                    Field clickedField = board.getField(clickedRow, clickedCol);
                    boolean hasMatch = false;

                    ArrayList<Field> komponentenSpieler1Copy = new ArrayList<>(board.getKomponentenSpieler1()); // Kopie
                                                                                                                // der
                                                                                                                // ArrayList
                                                                                                                // erstellen

                    for (Field komponentenField : komponentenSpieler1Copy) {
                        if (komponentenField.getColor() == clickedField.getColor()) {
                            JOptionPane.showMessageDialog(GameBoardPanel.this,
                                    "Du kannst nicht die aktuelle Farbe deiner eigenen Komponente wählen");
                                    
                            hasMatch = true;
                            break;
                        }

                        if (clickedField.getColor() == board.getField(0, board.getCols() - 1).getColor()) {
                            JOptionPane.showMessageDialog(GameBoardPanel.this,
                                    "Du kannst nicht die aktuelle Farbe der Komponente des Computers wählen.");
                            hasMatch = true;
                            break;
                        }
                    }

                    if (!hasMatch && board.isSpieler1AmZug()) {

                        board.setKomponentenSpieler1Alt(komponentenSpieler1Copy);
                        

                        ArrayList<Field> newFields = board.selectSameColorNeighbors(board.getKomponentenSpieler1(),clickedField.getColor());
                        board.getKomponentenSpieler1().addAll(newFields); //aktualisiere Komponenten von Spieler 1
                        board.floodField(clickedField.getColor(), board.getKomponentenSpieler1()); //flute die Felder
                        repaint();
                        menuPanel.updatePlayer1Score(board.getKomponentenSpieler1().size());
                        
                    
                        
                        boolean keineVeraenderung = board.pruefeObKeineVeraenderung(); //speichere boolean in Variable da bei erneutem Aufruf in Z.134 sonst NullPointerException da 2. if Fall in pruefe-Methode ausgeführt wird
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
        }};
        this.addMouseListener(customMouseListener);
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
        this.addMouseListener(customMouseListener);
    }

    // Setzt das Board für das Panel
    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
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
        this.offsetY = offsetY;
    }

    public MouseListener getCustomMouseListener() {
        return customMouseListener;
    }

    public void setCustomMouseListener(MouseListener mouseListener) {
        this.customMouseListener = mouseListener;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    

}
