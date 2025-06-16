// Name: Okan Ayhan
// Matrikelnummer:7380423

package testing;


import java.util.ArrayList;

/*
 * Siehe Hinweise auf dem Aufgabenblatt. 
 */

import logic.Field;

public class Testing {

    private Field[][] board;
    private ArrayList<Field> neighbors;
    private ArrayList<Field> sameColorNeighborsKomponenteSpieler1;
    private ArrayList<Field> komponentenSpieler1;
    private ArrayList<Field> komponentenSpieler1Alt;
    private ArrayList<Field> komponentenSpieler2;
    private ArrayList<Field> komponentenSpieler2Alt;

    public Testing(Field[][] initBoard) {
        this.board = initBoard;

    }

    // Hilfsmethoden

    public boolean isStartklar() {
        Field[][] boardCopy = createBoardCopy(board);

        if (boardCopy == null || boardCopy.length == 0) {
            return false; // Spielbrett ist leer oder nicht initialisiert
        }

        int numRows = boardCopy.length;
        int numCols = boardCopy[0].length;

        // Überprüfe Bedingung (3)
        if (boardCopy[numRows - 1][0].getColor() == boardCopy[0][numCols - 1].getColor()) {
            return false; // Felder in den Ecken haben die gleiche Farbe
        }

        // Überprüfe Bedingung (1) und (2)
        int[] colorCount = new int[7]; // Annahme: Farben sind Ganzzahlen von 1 bis 6, colorCount[0] ist ungenutzt da
                                       // boardCopy[][].getColor immer >=1
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int color = boardCopy[i][j].getColor(); // fangen dieColor die gegettet werden bei 0 oder 1 an?
                colorCount[color]++;

                // Überprüfe Nachbarn
                if ((i > 0 && boardCopy[i - 1][j].getColor() == color) || // oben
                        (i < numRows - 1 && boardCopy[i + 1][j].getColor() == color) || // unten
                        (j > 0 && boardCopy[i][j - 1].getColor() == color) || // links
                        (j < numCols - 1 && boardCopy[i][j + 1].getColor() == color)) { // rechts
                    return false; // Nachbarn haben die gleiche Farbe
                }
            }
        }

        // Überprüfe, ob die Anzahl der Farben auf dem Spielbrett der Anzahl der
        // tatsächlichen Farben entspricht
        int numColors = 0;
        for (int i = 1; i < colorCount.length; i++) { // i=0 ist leer deswegen überspringen
            if (colorCount[i] > 0) { // prüfe ob jedes Feld von colorCount mind. ein Element besitzt-> wenn ja wissen
                                     // wir dass alle Farben abgedeckt wurden
                numColors++;
            }
        }
        if (numColors != colorCount.length - 1) {
            return false; // Anzahl der Farben auf dem Spielbrett stimmt nicht mit der tatsächlichen
                          // Anzahl der Farben überein
        }

        return true; // Alle Bedingungen sind erfüllt
    }

    public boolean isEndConfig() {
        Field[][] boardCopy = createBoardCopy(board);

        // Ermittle zuerst die Boardgröße
        int boardGröße = boardCopy.length * boardCopy[0].length;
        

        // Ermittle die Felder von Spieler 1
        this.ermittleAlleFelderVonKomponenteS1(boardCopy);
        this.ermittleAlleFelderVonKomponenteS2(boardCopy);

        int aktuelleGrößeSpieler1 = komponentenSpieler1.size();
        int aktuelleGrößeSpieler2 = komponentenSpieler2.size();


        if (aktuelleGrößeSpieler1 + aktuelleGrößeSpieler2 == boardGröße) {
            return true;
        }
        return false;
    }

    public int testStrategy01() {
        Field[][] boardCopy = createBoardCopy(board);

        int minColor = Integer.MAX_VALUE;
        // minimale Größe der neuen Komponente
        int minSize = Integer.MAX_VALUE;
        // aktuelle Farbe der Komponente(n) vom Computer
        int colorPlayer1 = boardCopy[boardCopy.length - 1][0].getColor();
        // aktuelle Farbe der Komponente(n) vom Mensch
        int colorPlayer2 = boardCopy[0][boardCopy[0].length - 1].getColor();

        this.ermittleAlleFelderVonKomponenteS2(boardCopy);

        // ab diesem Punkt hat man alle aktuellen Felder der Komponente von Spieler 2 in
        // der Arraylist komponentenSpieler2

        // stagnations-Methoe ausführen

        for (int color = 1; color <= 6; color++) {
            if (color == colorPlayer1 || color == colorPlayer2)
                continue; // Bedingung das eigene Farbe und die des Menschen nicht gewählt werden darf

            ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, color, boardCopy);
            if (newFields.size() < minSize) {
                minSize = newFields.size();
                minColor = color;
            }

        }

        return minColor;
    }

    public int testStrategy02() {
        Field[][] boardCopy = createBoardCopy(board);

        int maxColor = Integer.MIN_VALUE;
        // minimale Größe der neuen Komponente
        int maxSize = Integer.MIN_VALUE;
        // aktuelle Farbe der Komponente(n) vom Computer
        int colorPlayer1 = boardCopy[boardCopy.length - 1][0].getColor();
        // aktuelle Farbe der Komponente(n) vom Mensch
        int colorPlayer2 = boardCopy[0][boardCopy[0].length - 1].getColor();

        this.ermittleAlleFelderVonKomponenteS2(boardCopy);

    
        // ab diesem Punkt hat man alle aktuellen Felder der Komponente von Spieler 2 in
        // der Arraylist komponentenSpieler2

        // Greedy-Strategie
        for (int color = 1; color <= 6; color++) {
            if (color == colorPlayer1 || color == colorPlayer2)
                continue; // Bedingung das eigene Farbe und die des Menschen nicht gewählt werden darf

            ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler2, color, boardCopy);
            if (newFields.size() > maxSize) {
                maxSize = newFields.size();
                maxColor = color;
            }

        }

        return maxColor;
    }

    public int testStrategy03() {
        Field[][] boardCopy = createBoardCopy(board);

        int maxColor = Integer.MIN_VALUE;
        // minimale Größe der neuen Komponente
        int maxSize = Integer.MIN_VALUE;
        // aktuelle Farbe der Komponente(n) vom Computer
        int colorPlayer1 = boardCopy[boardCopy.length - 1][0].getColor();
        // aktuelle Farbe der Komponente(n) vom Mensch
        int colorPlayer2 = boardCopy[0][boardCopy[0].length - 1].getColor();

        this.ermittleAlleFelderVonKomponenteS1(boardCopy);

        
        // ab diesem Punkt hat man alle aktuellen Felder der Komponente von Spieler 2 in
        // der Arraylist komponentenSpieler2

        for (int color = 1; color <= 6; color++) {
            if (color == colorPlayer1 || color == colorPlayer2)
                continue; // Bedingung das eigene Farbe und die des Menschen nicht gewählt werden darf

            ArrayList<Field> newFields = selectSameColorNeighbors(this.komponentenSpieler1, color, boardCopy);
            if (newFields.size() > maxSize) {
                maxSize = newFields.size();
                maxColor = color;
            }

        }

        return maxColor;
    }

public boolean toBoard(Field[][] anotherBoard, int moves) {
    return canReachInMoves(createBoardCopy(board), anotherBoard, moves, true);
}

public boolean canReachInMoves(Field[][] currentBoard, Field[][] anotherBoard, int moves, boolean isS1Turn) {
    if (moves < 0) { // wenn moves negativ ist definitiv false
        return false;
    }

    if (boardsAreEqual(currentBoard, anotherBoard)) { // wenn identisches Board gebe direkt true zurück
        return true;
    }

    if (moves == 0) { // wenn Boards nicht identisch sind gibts keinen Weg wie man mit 0 Schritten das gleiche Board erhalten kann
        return false;
    }
    if (currentBoard.length != anotherBoard.length || currentBoard[0].length != anotherBoard[0].length){ // Zeilen und Spalten der beiden Boards müssen immer gleich sein
        return false;
    }

    
    for (int color = 1; color <= 6; color++) {
        Field[][] boardCopy = createBoardCopy(currentBoard);
        ArrayList<Field> komponenten;

        if (isS1Turn) {
            ermittleAlleFelderVonKomponenteS1(boardCopy);
            komponenten = komponentenSpieler1;
        } else {
            ermittleAlleFelderVonKomponenteS2(boardCopy);
            komponenten = komponentenSpieler2;
        }

    
        floodField(color, komponenten);


        if (canReachInMoves(boardCopy, anotherBoard, moves - 1, !isS1Turn)) {
            return true;
        }
    }

    return false;
}

public boolean boardsAreEqual(Field[][] board1, Field[][] board2) {
    for (int i = 0; i < board1.length; i++) {
        for (int j = 0; j < board1[0].length; j++) {
            if (board1[i][j].getColor() != board2[i][j].getColor()) {
                return false;
            }
        }
    }
    return true;
}



    public int minMoves(int row, int col) {

        int count = 0;
        boolean check;
        int farbeAnfangsfeldS1;
        int minSchritte = Integer.MAX_VALUE;
        this.ermittleAlleFelderVonKomponenteS1(board);

        if (komponentenSpieler1.contains(board[row][col])) {
            
            return 0;
        }

        for (int color = 1; color < 7; color++) {
            check = true;
            Field[][] boardCopy = createBoardCopy(board);
            this.ermittleAlleFelderVonKomponenteS1(boardCopy);
            this.ermittleAlleFelderVonKomponenteS2(boardCopy);
            farbeAnfangsfeldS1 = boardCopy[boardCopy.length - 1][0].getColor();
            if (farbeAnfangsfeldS1 == color) {
                continue;
            }

            int currentColor = color;
            
            while (check) {
                
                if (currentColor > 6) {
                    currentColor = 1;
                }
                
                sameColorNeighborsKomponenteSpieler1 = this.selectSameColorNeighbors(komponentenSpieler1, currentColor, boardCopy);
                
                for (Field fields : sameColorNeighborsKomponenteSpieler1){
                    if (komponentenSpieler2.contains(fields)){ // wenn selectet Nachbar Teil der Komponente
                        komponentenSpieler1.addAll(komponentenSpieler2); //
                        
                        break;
                    }
                }        
                
                if (sameColorNeighborsKomponenteSpieler1.size() == 0) {
                    
                }
                
                if (sameColorNeighborsKomponenteSpieler1.size() > 0) {


                    for (Field fields : sameColorNeighborsKomponenteSpieler1)
                    if (!komponentenSpieler1.contains(fields)){
                        komponentenSpieler1.add(fields);
                    }
                }
                
                this.floodField(currentColor, komponentenSpieler1);
                count++;

                if (komponentenSpieler1.contains(boardCopy[row][col])) {
            
                    check = false;
                    if (count < minSchritte) {
                        minSchritte = count;
                    
                    }

                }
                currentColor++;

            }
            count = 0;
        }

        return minSchritte;
    }

    public int minMovesFull() {

        int count = 0;
        boolean check;
        int farbeAnfangsfeldS1;
        int minSchritte = Integer.MAX_VALUE;

        if (pruefeObBoardEinfarbig(board)) {
            
            return 0;
        }

        for (int color = 1; color < 7; color++) {
            check = true;
            Field[][] boardCopy = createBoardCopy(board);
            this.ermittleAlleFelderVonKomponenteS1(boardCopy);
            this.ermittleAlleFelderVonKomponenteS2(boardCopy);
            farbeAnfangsfeldS1 = boardCopy[boardCopy.length - 1][0].getColor();
            if (farbeAnfangsfeldS1 == color) {
                continue;
            }

            int currentColor = color;
            
            while (check) {
                
                if (currentColor > 6) {
                    currentColor = 1;
                }
            
                sameColorNeighborsKomponenteSpieler1 = this.selectSameColorNeighbors(komponentenSpieler1, currentColor, boardCopy);
                    
                for (Field fields : sameColorNeighborsKomponenteSpieler1){
                    if (komponentenSpieler2.contains(fields)){ // wenn selectet Nachbar Teil der Komponente
                        komponentenSpieler1.addAll(komponentenSpieler2); //
                    
                        break;
                    }
                }        
                
                if (sameColorNeighborsKomponenteSpieler1.size() == 0) {
                    
                }
                
                if (sameColorNeighborsKomponenteSpieler1.size() > 0) {

                    for (Field fields : sameColorNeighborsKomponenteSpieler1)
                    if (!komponentenSpieler1.contains(fields)){
                        komponentenSpieler1.add(fields);
                    }
                }

                this.floodField(currentColor, komponentenSpieler1);
                count++;

                if (this.pruefeObBoardEinfarbig(boardCopy)) {
                    
                    check = false;
                    if (count < minSchritte) {
                        minSchritte = count;
                    
                    }

                }
                currentColor++;

            }
            count = 0;
        }

        return minSchritte;
    }

    // Hilfsmethoden

    public boolean hasColorOfS1(Field field, Field[][] boardCopy) {

        // Farbe von S1 ist die Farbe des Feldes in der unteren linken Ecke.
        int colorOfS1 = boardCopy[boardCopy.length - 1][0].getColor();
        return field.getColor() == colorOfS1;
    }

    public boolean hasColorOfS2(Field field, Field[][] boardCopy) {

        // Farbe von S2 ist die Farbe des Feldes in der oberen rechten Ecke.
        int colorOfS2 = boardCopy[0][boardCopy[0].length - 1].getColor();
        return field.getColor() == colorOfS2;
    }

    public boolean isPartOfS2(Field field, Field[][] boardCopy) {

        if (komponentenSpieler2.contains(field)){
            return true;
        }
        return false;
    }

    public ArrayList<Field> selectNeighbors(ArrayList<Field> komponenten, Field[][] boardCopy) {
        ArrayList<Field> neighbors = new ArrayList<>();

        for (Field field : komponenten) {
            int row = field.getRow();
            int col = field.getCol();

            int[] dx = { -1, 0, 1, 0 };
            int[] dy = { 0, 1, 0, -1 };

            for (int dir = 0; dir < 4; dir++) {
                int newRow = row + dx[dir];
                int newCol = col + dy[dir];

                if (isValidPosition(newRow, newCol, boardCopy)) {
                    neighbors.add(boardCopy[newRow][newCol]);
                }
            }
        }
        return neighbors;
    }

    public boolean isValidPosition(int row, int col, Field[][] boardCopy) {

        return row >= 0 && row < boardCopy.length && col >= 0 && col < boardCopy[0].length;
    }

    public ArrayList<Field> selectSameColorNeighbors(ArrayList<Field> komponenten, int color, Field[][] boardCopy) {
        ArrayList<Field> neighborSameColor = new ArrayList<>();

        for (Field field : komponenten) {
            int row = field.getRow();
            int col = field.getCol();

            int[] dx = { -1, 0, 1, 0 };
            int[] dy = { 0, 1, 0, -1 };

            for (int dir = 0; dir < 4; dir++) {
                int newRow = row + dx[dir];
                int newCol = col + dy[dir];

                if (isValidPosition(newRow, newCol, boardCopy) && boardCopy[newRow][newCol] != null
                        && boardCopy[newRow][newCol].getColor() == color
                        && !neighborSameColor.contains(boardCopy[newRow][newCol])) {
                    neighborSameColor.add(boardCopy[newRow][newCol]);
                }
            }
        }
        return neighborSameColor;
    }

    public void ermittleAlleFelderVonKomponenteS1(Field[][] boardCopy) {

        boolean check = true;
        komponentenSpieler1 = new ArrayList<>();
        Field startField = boardCopy[boardCopy.length - 1][0]; // Startfeld
        komponentenSpieler1.add(startField); // Startfeld ist auf jeden Fall immer in der Komponente

        // Ermittle zunächst alle Felder der aktuellen Komponente von Spieler2 bzw.
        // Computer
        while (check) {
            komponentenSpieler1Alt = new ArrayList<>(komponentenSpieler1); // für den vorher-nachher Vergleich
            neighbors = this.selectNeighbors(komponentenSpieler1, boardCopy);
            for (Field neighborField : neighbors) {
                if (hasColorOfS1(neighborField, boardCopy) && !komponentenSpieler1.contains(neighborField)) {
                    komponentenSpieler1.add(neighborField);
                }
            }
            if (komponentenSpieler1Alt.size() >= komponentenSpieler1.size()) {
                check = false;
            }
        }

    }

    public void ermittleAlleFelderVonKomponenteS2(Field[][] boardCopy) {

        boolean check = true;
        komponentenSpieler2 = new ArrayList<>();
        Field startField = boardCopy[0][boardCopy[0].length - 1]; // Startfeld
        komponentenSpieler2.add(startField); // Startfeld ist auf jeden Fall immer in der Komponente

        // Ermittle zunächst alle Felder der aktuellen Komponente von Spieler2 bzw.
        // Computer
        while (check) {
            komponentenSpieler2Alt = new ArrayList<>(komponentenSpieler2); // für den vorher-nachher Vergleich
            neighbors = this.selectNeighbors(komponentenSpieler2, boardCopy);
            for (Field neighborField : neighbors) {
                if (hasColorOfS2(neighborField, boardCopy) && !komponentenSpieler2.contains(neighborField)) {
                    komponentenSpieler2.add(neighborField);
                }
            }
            if (komponentenSpieler2Alt.size() >= komponentenSpieler2.size()) {
                check = false;
            }
        }

    }

    public Field[][] createBoardCopy(Field[][] originalBoard) {
        int numRows = originalBoard.length;
        int numCols = originalBoard[0].length;
        Field[][] copy = new Field[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Field originalField = originalBoard[i][j];
                Field copiedField = new Field(originalField.getRow(), originalField.getCol(), originalField.getColor());
                copy[i][j] = copiedField;
            }
        }

        return copy;
    }

    public boolean pruefeObBoardEinfarbig(Field[][] boardCopy) {

        int farbeAnfangsfeldS1 = boardCopy[boardCopy.length - 1][0].getColor();

        for (int i = 0; i < boardCopy.length; i++) {
            for (int j = 0; j < boardCopy[0].length; j++) {
                if (boardCopy[i][j].getColor() != farbeAnfangsfeldS1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void floodField(int color, ArrayList<Field> komponenten) {
        for (Field field : komponenten) {
            field.setColor(color);
        }
    }

    /*
     * Getter und Setter
     */
    public Field[][] getBoard() {
        return board;
    }

    public void setBoard(Field[][] board) {
        this.board = board;
    }

    public ArrayList<Field> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Field> neighbors) {
        this.neighbors = neighbors;
    }

    public ArrayList<Field> getSameColorNeighborsKomponenteSpieler1() {
        return sameColorNeighborsKomponenteSpieler1;
    }

    public void setSameColorNeighborsKomponenteSpieler1(ArrayList<Field> sameColorNeighborsKomponenteSpieler1) {
        this.sameColorNeighborsKomponenteSpieler1 = sameColorNeighborsKomponenteSpieler1;
    }

    public ArrayList<Field> getKomponentenSpieler1() {
        return komponentenSpieler1;
    }

    public void setKomponentenSpieler1(ArrayList<Field> komponentenSpieler1) {
        this.komponentenSpieler1 = komponentenSpieler1;
    }

    public ArrayList<Field> getKomponentenSpieler1Alt() {
        return komponentenSpieler1Alt;
    }

    public void setKomponentenSpieler1Alt(ArrayList<Field> komponentenSpieler1Alt) {
        this.komponentenSpieler1Alt = komponentenSpieler1Alt;
    }

    public ArrayList<Field> getKomponentenSpieler2() {
        return komponentenSpieler2;
    }

    public void setKomponentenSpieler2(ArrayList<Field> komponentenSpieler2) {
        this.komponentenSpieler2 = komponentenSpieler2;
    }

    public ArrayList<Field> getKomponentenSpieler2Alt() {
        return komponentenSpieler2Alt;
    }

    public void setKomponentenSpieler2Alt(ArrayList<Field> komponentenSpieler2Alt) {
        this.komponentenSpieler2Alt = komponentenSpieler2Alt;
    }

    

}
