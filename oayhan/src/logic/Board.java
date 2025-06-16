// Name: Okan Ayhan
// Matrikelnummer:7380423

package logic;

import java.util.*;

import javax.swing.JOptionPane;

import gui.BoardPanel;
import gui.MenuPanel;

public class Board {

    private MenuPanel menuPanel; // benötigen wir für Anzahl ausgewählter Farben und Punktestand des Computers im Menü
    private BoardPanel boardPanel; 
    private Field[][] fields;
    private Random random;
    private List<Integer> colorsList; // Liste der Farben, die auf das Brett gesetzt werden sollen.
    private ArrayList<Field> komponentenSpieler1;
    private ArrayList<Field> komponentenSpieler1Alt;
    private ArrayList<Field> komponentenSpieler2;
    private ArrayList<Field> komponentenSpieler2Alt;
    private ArrayList<Field> sameColorNeighbors;

    private boolean spieler1AmZug = true;
    private int selectedStrategyNumber;
    private int counterPruefung=0;

    public Board(int rows, int cols, int numColors, MenuPanel menuPanel, BoardPanel boardPanel) {
        this.menuPanel = menuPanel;
        this.boardPanel = boardPanel;
        fields = new Field[rows][cols];
        random = new Random();
        komponentenSpieler1 = new ArrayList<>();
        komponentenSpieler2 = new ArrayList<>();
        sameColorNeighbors = new ArrayList<>();
        initializeColorsList(numColors);
        initializeBoard(rows, cols, numColors);
        komponentenSpieler1.add(this.fields[rows - 1][0]);
        komponentenSpieler2.add(this.fields[0][cols - 1]);
        komponentenSpieler1Alt = new ArrayList<>(komponentenSpieler1);
        

    }



    public void initializeColorsList(int numColors) {
        colorsList = new ArrayList<>();
        for (int i = 0; i < numColors; i++) {
            colorsList.add(i);
        }
        Collections.shuffle(colorsList); // Die Farbenliste zufällig mischen, sonst hätten die ersten (8) Felder immer
                                         // gleiche Farben
    }

    public void initializeBoard(int rows, int cols, int numColors) {
        // Initialisiere das Brett mit zufällig gefärbten Feldern.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int color;
                if (!colorsList.isEmpty()) { // wenn colorsList nicht leer ist
                    // Wähle die nächste Farbe aus der Liste (deswegen 0, denn 0 ist immer die erste
                    // Farbe in der aktuellen Liste), wenn sie nicht leer ist.
                    color = colorsList.remove(0); // entferne Farbzahl aus Liste und gebe sie der Variable Color, damit
                                                  // Feld diese Farbzahl erhält
                } else {
                    // Wähle eine zufällige Farbe, wenn alle Farben bereits einmal verwendet wurden.
                    color = random.nextInt(numColors);
                }
                fields[row][col] = new Field(row, col, color);
                while (hasSameColorNeighbour(row, col)) {
                    fields[row][col].setColor(random.nextInt(numColors));
                }
            }
        }

        // Stelle sicher, dass das untere linke Feld und das obere rechte Feld nicht die
        // gleiche Farbe haben.
        while (fields[rows - 1][0].getColor() == fields[0][cols - 1].getColor() || hasSameColorNeighbour(rows - 1, 0)) {
            fields[rows - 1][0].setColor((fields[rows - 1][0].getColor() + 1) % numColors);
        }
    }

    public boolean hasSameColorNeighbour(int row, int col) {
        // betrachte in dieser Reihenfolge: oben,rechts,unten,links
        int[] dx = { -1, 0, 1, 0 };
        int[] dy = { 0, 1, 0, -1 };
        for (int dir = 0; dir < 4; dir++) { // Ende = 4 für alle 4 Himmelsrichtungen
            int newRow = row + dx[dir];
            int newCol = col + dy[dir];
            // Ignoriere Felder, die noch nicht initialisiert wurden
            if (isValidPosition(newRow, newCol) && fields[newRow][newCol] != null
                    && fields[newRow][newCol].getColor() == fields[row][col].getColor()) { // fields[newRow][newCol] ==
                                                                                           // null, sollte Field-Objekt
                                                                                           // an dieser Stelle noch
                                                                                           // nicht erstellt worden
                                                                                           // sein, weil man sich bspw.
                                                                                           // noch zu Beginn der
                                                                                           // Betrachtung befindet
                return true;
            }
        }
        return false;
    }

    // prüft, ob betrachtete Zelle innerhalb des Spielfeldes liegt -> wenn bspw.
    // negative Zahl gebe false zurück
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < getRows() && col >= 0 && col < getCols(); // < statt <= da Indexierung bei 0 statt 1
                                                                           // anfängt und wir mit getRows/Cols die Länge
                                                                           // erhalten -> bei Länge 10 = von 0-9
    }

    public void floodField(int color, ArrayList<Field> komponenten) {
        for (Field field : komponenten) {
            field.setColor(color);
        }
    }

    public ArrayList<Field> selectSameColorNeighbors(ArrayList<Field> komponenten, int color) {
        sameColorNeighbors = new ArrayList<>(); // da bei jedem Aufruf neue Arraylist-Instanz erstellt wird müssen wir
                                                // sie später nicht wieder leeren

        // Betrachte jedes Feld in der übergebenen ArrayList einzeln, wenn Bedingungen
        // passen wird es in Arraylist sameColorsNeighbors aufgenommen
        for (Field field : komponenten) {

                int row = field.getRow();
                int col = field.getCol();

                // Betrachte die Nachbarfelder des aktuellen Feldes
                int[] dx = { -1, 0, 1, 0 };
                int[] dy = { 0, 1, 0, -1 };

                for (int dir = 0; dir < 4; dir++) {
                    int newRow = row + dx[dir];
                    int newCol = col + dy[dir];

                    // Überprüfe, ob das Nachbarfeld auf dem Spielfeld liegt und die gleiche Farbe
                    // hat
                    if (isValidPosition(newRow, newCol) && fields[newRow][newCol] != null && fields[newRow][newCol].getColor() == color 
                    && !sameColorNeighbors.contains(fields[newRow][newCol])) {
                        sameColorNeighbors.add(fields[newRow][newCol]);
                    }
                }
            
        }
        return sameColorNeighbors;
    }

    // Methode, um den Computerzug auszuführen
    public void fuehreComputerzugAus() {

        komponentenSpieler2Alt = new ArrayList<>(komponentenSpieler2);

        if (!spieler1AmZug && selectedStrategyNumber == 1) {
            stagnationStrategie();
        } else if (!spieler1AmZug && selectedStrategyNumber == 2) {
            greedyStrategie();
        } else {
            blockingStrategie();
        }

        boardPanel.getGameBoardPanel().repaint(); //aktualisiere Board vor Prüfung ob Spielende

        
        boolean check = pruefeObKeineVeraenderung();
    
        if (check){
            incrementCounterPruefung();
            
            
                if (counterPruefung== 4){
                    vorzeitigerAbbruch();
                    resetGame();
                    return;
                }
           
        }
        if (!check && counterPruefung>=1) {
            
            counterPruefung = 0;
        }
        

        if (isEndkonfiguration()){
            pruefeGewinner();
            resetGame();
            return;
        }

        spieler1AmZug = true;
    }

    public void pruefeGewinner(){
        if (menuPanel.getPunktestandMensch()>menuPanel.getPunktestandComputer()){
            JOptionPane.showMessageDialog(boardPanel.getGameBoardPanel(),
                                    "Spieler 1 hat gewonnen");
        }
        else if (menuPanel.getPunktestandMensch()<menuPanel.getPunktestandComputer()){
            JOptionPane.showMessageDialog(boardPanel.getGameBoardPanel(),
                                    "Spieler 2 hat gewonnen");
        }
        else {
            JOptionPane.showMessageDialog(boardPanel.getGameBoardPanel(),
                                    "Es gibt keinen Gewinner. Das Spiel endet unentschieden");
        }
    }

    public void vorzeitigerAbbruch (){
        JOptionPane.showMessageDialog(boardPanel.getGameBoardPanel(),
"Das Spiel wurde beendet, da sich die Komponenten der Spieler in den letzten 4 Spielzügen nicht verändert haben. Das Spiel endet unentschieden");
    }

    public void stagnationStrategie() {
        // repräsentiert minimale Farbe die bei Gleichstand ausgewählt werden soll, mit
        // Max Integer ansetzen damti sie definitiv überschrieben wird
        int minColor = Integer.MAX_VALUE;
        // minimale Größe der neuen Komponente
        int minSize = Integer.MAX_VALUE;
        // aktuelle Farbe der Komponente(n) vom Computer
        int currentColor = this.fields[0][getCols() - 1].getColor();
        // aktuelle Farbe der Komponente(n) vom Mensch
        int opponentColor = this.fields[getRows() - 1][0].getColor();

        // starte Schleife die max. bis ausgewählte Anzahl an Farben im Menü iteriert
        for (int color = 0; color < (int) menuPanel.getNumberFarbenBox().getSelectedItem(); color++) {
            if (color == currentColor || color == opponentColor)
                continue; // Bedingung das eigene Farbe und die des Menschen nicht gewählt werden darf
            ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, color); // speichere alle
                                                                                                    // Nachbarn der
                                                                                                    // aktuellen
                                                                                                    // Komponente(n)
                                                                                                    // welche die
                                                                                                    // gleiche Farbzahl
                                                                                                    // haben wie das
                                                                                                    // aktuell
                                                                                                    // iterierende color
                                                                                                    // -> Im Idealfall
                                                                                                    // erhält man hier 0

        

            if (newFields.size() < minSize) { // wenn weniger Felder in der gerade erhaltenen newFields-Arraylist als in
                                              // den bisherigen Arraylists
                minSize = newFields.size(); // ..., wird die kleinere Arraylist nun die neue minSize
                minColor = color; // und die neue minColor wir die color bzw. Farbzahl dieser neuen kleineren
                                  // Arraylist
            } 
            
        }
        
        // definiere nun final die selektierten Nachbarfelder mit der zuvor ermittelten
        // kleinstmöglichen Farbzahl
        ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, minColor);
        this.komponentenSpieler2.addAll(newFields);

        this.floodField(minColor, this.komponentenSpieler2);
        menuPanel.updatePlayer2Score(komponentenSpieler2.size()); // aktualisiere den Punktestand vom Computer
    }

    public void greedyStrategie() {
        // repräsentiert minimale Farbe die bei Gleichstand ausgewählt werden soll, mit
        // Max Integer ansetzen damti sie definitiv überschrieben wird
        int maxColor = Integer.MIN_VALUE;
        // minimale Größe der neuen Komponente
        int maxSize = Integer.MIN_VALUE;
        // aktuelle Farbe der Komponente(n) vom Computer
        int currentColor = this.fields[0][getCols() - 1].getColor();
        // aktuelle Farbe der Komponente(n) vom Mensch
        int opponentColor = this.fields[getRows() - 1][0].getColor();

        for (int color = 0; color < (int) menuPanel.getNumberFarbenBox().getSelectedItem(); color++) {
            if (color == currentColor || color == opponentColor)
                continue;
            ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, color);

            

            if (newFields.size() > maxSize) {

                maxSize = newFields.size();
                maxColor = color;
                // } else if (newFields.size() == maxSize && color < maxColor) { // bei
                // Gleichstand wähle immer die kleinere
                // // Farbzahl
                // maxColor = color;
            }

        }
        
        ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, maxColor);
        this.komponentenSpieler2.addAll(newFields);

        this.floodField(maxColor, this.komponentenSpieler2);
        menuPanel.updatePlayer2Score(komponentenSpieler2.size()); // aktualisiere den Punktestand vom Computer
    }

    public void blockingStrategie() {

        int maxColor = Integer.MIN_VALUE;
        int maxSize = Integer.MIN_VALUE;
        int currentColor = this.fields[0][getCols() - 1].getColor();
        int opponentColor = this.fields[getRows() - 1][0].getColor();

        for (int color = 0; color < (int) menuPanel.getNumberFarbenBox().getSelectedItem(); color++) {
            if (color == currentColor || color == opponentColor)
                continue;
            ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler1, color);


            if (newFields.size() > maxSize) {
                maxSize = newFields.size();
                maxColor = color;
            } 
        }

        
        ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, maxColor);
        this.komponentenSpieler2.addAll(newFields);
        this.floodField(maxColor, this.komponentenSpieler2);
        menuPanel.updatePlayer2Score(komponentenSpieler2.size());
    }

    public boolean isEndkonfiguration(){ //prüfe ob alle Felder von S1 und S2 besetzt wurden
        
        int felderBoard = (int)menuPanel.getNumberRowBox().getSelectedItem()*(int)menuPanel.getNumberColBox().getSelectedItem();
        int aktuelleGrößeSpieler1 = this.komponentenSpieler1.size();
        int aktuelleGrößeSpieler2 = this.komponentenSpieler2.size();
        if (felderBoard == aktuelleGrößeSpieler1+aktuelleGrößeSpieler2){
            return true;
        }
        return false;
    }

    public boolean pruefeObKeineVeraenderung(){
        
        if(spieler1AmZug){
            
            if (counterPruefung == 0 && komponentenSpieler1Alt.size() == komponentenSpieler1.size()){
                return true;
            }
            else if (counterPruefung >= 1 && komponentenSpieler1Alt.size() == komponentenSpieler1.size() && komponentenSpieler2Alt.size() == komponentenSpieler2.size()){
                return true;
            }
            else {
                return false;
            }
        }

        else{
            
            if (counterPruefung == 0 && komponentenSpieler2Alt.size() == komponentenSpieler2.size()){
                return true;
            }
            else if (counterPruefung >= 1 && komponentenSpieler1Alt.size() == komponentenSpieler1.size() && komponentenSpieler2Alt.size() == komponentenSpieler2.size()){
                return true;
            }
            else {
                return false;
            }    
    }

    }

    public void resetGame (){
    
            boardPanel.getGameBoardPanel().setBoard(null);
            boardPanel.getGameBoardPanel().removeCustomMouseListener();  // Entfernt den MouseListener, wenn der Stop-Button gedrückt wird
            boardPanel.getColorPanel().removeCustomMouseListener();
            boardPanel.getColorPanel().removeCustomKeyListener();
            boardPanel.getColorPanel().stop();
            boardPanel.getGameBoardPanel().repaint();  // Wiederholen Sie die Methode paintComponent in BoardPanel, um das gestoppte Board zu entfernen
            menuPanel.setStartStopButtonZustand(0);
            menuPanel.getStartStopButton().setText("Start");
            menuPanel.setPlayPauseButtonZustand(0); //setze auch den PlayPause-Button in Ursprungszustand, da Spiel ja beendet wurde
            menuPanel.getNumberFarbenBox().setEnabled(true); //macht nur Sinn, die Einstellungsmöglichkeiten dann wieder anzeigen zu lassen sobald wir im Startzustand sind und davor sind neues Feld zu initialisieren
            menuPanel.getNumberColBox().setEnabled(true);
            menuPanel.getNumberRowBox().setEnabled(true);
            menuPanel.getNumberPlayerBox().setEnabled(true);
            menuPanel.getNumberStrategyBox().setEnabled(true);
            menuPanel.clearStoredComboBoxValues();
            menuPanel.getPlayPauseButton().setText("Play");
            menuPanel.resetPlayer1Score();
            menuPanel.resetPlayer2Score();
            menuPanel.resetTimer();

            
    }

    // Getter-Methode für das Feld an einer bestimmten Position.
    public Field getField(int row, int col) {
        return fields[row][col];
    }

    // Getter-Methode für die Anzahl der Zeilen.
    public int getRows() {
        return fields.length;
    }

    // Getter-Methode für die Anzahl der Spalten.
    public int getCols() {
        return fields[0].length;
    }

    public int getNumColors() {
        return colorsList.size();
    }

    public Field[][] getFields() {
        return fields;
    }

    public void setFields(Field[][] fields) {
        this.fields = fields;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public List<Integer> getColorsList() {
        return colorsList;
    }

    public void setColorsList(List<Integer> colorsList) {
        this.colorsList = colorsList;
    }

    public ArrayList<Field> getKomponentenSpieler1() {
        return komponentenSpieler1;
    }

    public void setKomponentenSpieler1(ArrayList<Field> komponentenSpieler1) {
        this.komponentenSpieler1 = komponentenSpieler1;
    }

    public ArrayList<Field> getKomponentenSpieler2() {
        return komponentenSpieler2;
    }

    public void setKomponentenSpieler2(ArrayList<Field> komponentenSpieler2) {
        this.komponentenSpieler2 = komponentenSpieler2;
    }

    public ArrayList<Field> getSameColorNeighbors() {
        return sameColorNeighbors;
    }

    public void setSameColorNeighbors(ArrayList<Field> sameColorNeighbors) {
        this.sameColorNeighbors = sameColorNeighbors;
    }

    public boolean isSpieler1AmZug() {
        return spieler1AmZug;
    }

    public void setSpieler1AmZug(boolean spieler1AmZug) {
        this.spieler1AmZug = spieler1AmZug;
    }

    public int getSelectedStrategyNumber() {
        return selectedStrategyNumber;
    }

    public void setSelectedStrategyNumber(int selectedStrategyNumber) {
        this.selectedStrategyNumber = selectedStrategyNumber;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    public ArrayList<Field> getKomponentenSpieler1Alt() {
        return komponentenSpieler1Alt;
    }

    public void setKomponentenSpieler1Alt(ArrayList<Field> komponentenSpieler1Alt) {
        this.komponentenSpieler1Alt = komponentenSpieler1Alt;
    }

    public ArrayList<Field> getKomponentenSpieler2Alt() {
        return komponentenSpieler2Alt;
    }

    public void setKomponentenSpieler2Alt(ArrayList<Field> komponentenSpieler2Alt) {
        this.komponentenSpieler2Alt = komponentenSpieler2Alt;
    }

    public int getCounterPruefung() {
        return counterPruefung;
    }

    public void setCounterPruefung(int counterPruefung) {
        this.counterPruefung = counterPruefung;
    }

    public void incrementCounterPruefung() {
        this.counterPruefung++;
    }



    public BoardPanel getBoardPanel() {
        return boardPanel;
    }



    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    

    

}
