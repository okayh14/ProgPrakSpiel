// Name: Okan Ayhan
// Matrikelnummer:7380423

package gui;

import javax.swing.*;



import logic.Board;

import java.awt.*;

public class GameWindow extends JFrame  {

    private BoardPanel boardPanel;
    private MenuPanel menuPanel;
    private Board board; // Definieren Sie das Board außerhalb der Konstruktorfunktion
    private int rows;
    private int cols;
    private int numColors;
    private String selectedPlayer;
    private String selectedStrategyString;
    private Timer timer;
    
    
    public GameWindow() {

    // Initialisiere board als null, anstatt ihm ein neues Board-Objekt zuzuweisen
    this.board = null;

    // Das Panel braucht kein Board zur Initialisierung
    menuPanel = new MenuPanel(this);
    boardPanel = new BoardPanel(menuPanel); //hierdurch greift BoardPanel-Objekt auf die ausgewählte Anzahl Farben der MenuPanel JCOMBOBox zu
    BorderLayout layout = new BorderLayout();
    setLayout(layout);    
    setTitle("ProgPrak Spiel");
    setSize(600, 600);
    setMinimumSize(new Dimension(600, 600));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    menuPanel.setPreferredSize(new Dimension((int)(getWidth()*0.35), getHeight()));
    boardPanel.setPreferredSize(new Dimension((int)(getWidth()*0.65), getHeight()));

    add(BorderLayout.EAST,menuPanel);
    add(BorderLayout.CENTER,boardPanel);

    menuPanel.getStartStopButton().addActionListener(e -> {
        if (menuPanel.getStartStopButtonZustand() == 0) {
            // Erstelle das Board, wenn der StartStopButtonZustand = 0 ist
            rows = (int)menuPanel.getNumberRowBox().getSelectedItem(); //erstelle Board auf Basis der Auswahl im Menü
            cols = (int)menuPanel.getNumberColBox().getSelectedItem();
            numColors = (int)menuPanel.getNumberFarbenBox().getSelectedItem();
            selectedPlayer = (String) menuPanel.getNumberPlayerBox().getSelectedItem();
            selectedStrategyString = (String) menuPanel.getNumberStrategyBox().getSelectedItem();
            board = new Board(rows, cols, numColors, menuPanel, boardPanel); //erstelle neues Board-Objekt damit nicht mehr =null
            boardPanel.getGameBoardPanel().setBoard(board); // GameboardPanel hat board-Variable im Konstruktor -> mit setBoard-Methode wird neues Board GameBoardPanel-Attribut des BoardPanel-Objekts übergeben
            boardPanel.getColorPanel().start(); // Startet das Zeichnen der Farben auf dem ColorPanel
            menuPanel.storeComboBoxValues();
            boardPanel.getGameBoardPanel().repaint(); // Wiederholen Sie die Methode paintComponent in BoardPanel, um das neue Board zu zeichnen //Info: repaint() führt paint-Methode der Ober und Unterkomponente (GameBoardPanel) aus
            menuPanel.setStartStopButtonZustand(1);
            menuPanel.getStartStopButton().setText("Stop");
            if (selectedPlayer.equals("Computer (Spieler 2)")){
                boardPanel.getGameBoardPanel().getBoard().setSpieler1AmZug(false);;
            }
            if (selectedStrategyString.equals("Stagnation")){
                boardPanel.getGameBoardPanel().getBoard().setSelectedStrategyNumber(1); //setzte Strategienummer der fuehreComputerzugAus-Methode auf Basis der Auswahl im Menü
            }
            if (selectedStrategyString.equals("Greedy")){
                boardPanel.getGameBoardPanel().getBoard().setSelectedStrategyNumber(2);
            }
              if (selectedStrategyString.equals("Blocking")){
                boardPanel.getGameBoardPanel().getBoard().setSelectedStrategyNumber(3);
            }           
            
        } else {
            // Stoppen Sie das Spiel, wenn der StartStopButtonZustand = 1 ist
            board.resetGame(); // stop the timer when the game stops
        }
    });

    menuPanel.getPlayPauseButton().addActionListener(e -> {
        if (menuPanel.getPlayPauseButtonZustand() == 0 && menuPanel.getStartStopButtonZustand() == 1) { // Wechsel von 0=Play auf 1=Pause -> bedeutet es wurde gerade auf Play gedrückt ergo Spiel soll gespielt werden
            // Wenn noch keine Mouselistener und kein KeyListener erstellt wurde erstelle neue Mouselistener und KeyLISTENER
            if (boardPanel.getColorPanel().getCustomMouseListener() == null && boardPanel.getGameBoardPanel().getCustomMouseListener() == null ){
                boardPanel.getColorPanel().addCustomMouseListener(boardPanel.getGameBoardPanel().getBoard());
                boardPanel.getColorPanel().addCustomKeyListener(boardPanel.getGameBoardPanel().getBoard());
                boardPanel.getGameBoardPanel().addCustomMouseListener();
                
            }
            else {
                boardPanel.getGameBoardPanel().enableMouseListener(); //wenn bereits Mouselistener initialisert wurde aktiviere den vorher deaktivierten Mouselistener wieder
                boardPanel.getColorPanel().enableMouseListener();
                boardPanel.getColorPanel().enableKeyListener();
            } 
        if (!boardPanel.getGameBoardPanel().getBoard().isSpieler1AmZug()){ // wenn Computer als Spieler mit ersten Zug ausgewählt wird führe die fuehreComputerzugAus-Methode aus
            timer = new Timer(1000, m -> { //Computer spielt immer mit Verzögerung
			boardPanel.getGameBoardPanel().getBoard().fuehreComputerzugAus();
			boardPanel.getGameBoardPanel().repaint();
			});
			timer.setRepeats(false);
			timer.start();
        }    
            
            menuPanel.setPlayPauseButtonZustand(1);
            menuPanel.getNumberFarbenBox().setEnabled(false);
            menuPanel.getNumberColBox().setEnabled(false);
            menuPanel.getNumberRowBox().setEnabled(false);
            menuPanel.getNumberPlayerBox().setEnabled(false);
            menuPanel.getNumberStrategyBox().setEnabled(false);
            menuPanel.getPlayPauseButton().setText("Pause"); 
            menuPanel.startTimer();  // resume the timer when the game unpauses
        } else {
            // Stoppen das Spiel, wenn man von 1=Pause auf 0=Play wechselt
            boardPanel.getGameBoardPanel().disableMouseListener();
            boardPanel.getColorPanel().disableMouseListener();
            boardPanel.getColorPanel().disableKeyListener();
            menuPanel.setPlayPauseButtonZustand(0);
            
            menuPanel.getPlayPauseButton().setText("Play");
            menuPanel.stopTimer();  // pause the timer when the game pauses
        }
    });


    setVisible(true);
    }


    public BoardPanel getBoardPanel() {
        return boardPanel;
    }


    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }


    public MenuPanel getMenuPanel() {
        return menuPanel;
    }


    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }


    public Board getBoard() {
        return board;
    }


    public void setBoard(Board board) {
        this.board = board;
    }


    public int getRows() {
        return rows;
    }


    public void setRows(int rows) {
        this.rows = rows;
    }


    public int getCols() {
        return cols;
    }


    public void setCols(int cols) {
        this.cols = cols;
    }


    public int getNumColors() {
        return numColors;
    }


    public void setNumColors(int numColors) {
        this.numColors = numColors;
    }


    public String getSelectedPlayer() {
        return selectedPlayer;
    }


    public void setSelectedPlayer(String selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }


    public String getSelectedStrategyString() {
        return selectedStrategyString;
    }


    public void setSelectedStrategyString(String selectedStrategyString) {
        this.selectedStrategyString = selectedStrategyString;
    }


    public Timer getTimer() {
        return timer;
    }


    public void setTimer(Timer timer) {
        this.timer = timer;
    }


    
}