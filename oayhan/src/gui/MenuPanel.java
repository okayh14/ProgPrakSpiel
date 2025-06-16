// Name: Okan Ayhan
// Matrikelnummer:7380423

package gui;

import javax.swing.*;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




public class MenuPanel extends JPanel{

private GameWindow gw;

private Integer[] numbersFarben = {4, 5, 6, 7, 8, 9};
private Integer[] numbersSpielbrett = {3, 4, 5, 6, 7, 8, 9, 10};
private String[] players = {"Mensch (Spieler 1)", "Computer (Spieler 2)"};    
private String[] strategy = {"Stagnation", "Greedy", "Blocking"};

private int startStopButtonZustand; // 0=Start, 1=Stop -> im Startzustand wird kein Feld angezeigt
private JButton startStopButton;
private int playPauseButtonZustand; // 0=Play, 1=Pause 
private JButton playPauseButton;

private JComboBox<Integer> numberFarbenBox;
private JComboBox<Integer> numberColBox;
private JComboBox<Integer> numberRowBox;
private JComboBox<String> numberPlayerBox;
private JComboBox<String> numberStrategyBox ;


private int punktestandMensch=1;    //beginnt mit 1 da dies der Anfangskomponente entspricht
private int punktestandComputer=1;;
private JLabel player1ScoreLabel;
private JLabel player2ScoreLabel;

private Integer lastSelectedNumberFarben = null;
private Integer lastSelectedNumberCols = null;
private Integer lastSelectedNumberRows = null;
private String lastSelectedNumberPlayer = null;
private String lastSelectedNumberStrategy = null;


private Timer timer;
private int seconds;
private JLabel timerLabel;

public MenuPanel(GameWindow gw) {
      this.gw = gw;
         
      setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Start Button
        startStopButton = new JButton("Start");
        startStopButtonZustand = 0; // Initialwert des Buttons = 0 da wir im Startzustand starten
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(startStopButton, gbc);

        // Play Button
        playPauseButton = new JButton("Play");
        playPauseButtonZustand = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(playPauseButton, gbc);

        // Bedienungsanleitung Button
        JButton instructionButton = new JButton("Bedienungsanleitung");
        instructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(MenuPanel.this,
            "Bevor auf Start geklickt wird können die Spielbedingungen im Menü festgelegt werden\n\n" +
            "Durch das Drücken auf Play startet der Timer und das Spiel kann nun gespielt werden\n" +
            "Durch das Drücken auf Pause kann das Spiel, sobald Sie am Zug sind, unterbrochen werden\n" +
            "Durch das Drücken auf Stop kann das Spiel jederzeit beendet werden\n\n" +
            "Es gibt 3 unterschiedliche Möglichkeiten, wie Sie die Farbe während des Spiel ändern können sobald Sie am Zug sind:\n\n" +
            "1) Klicken Sie auf eine entsprechende Farbe auf dem Spielbrett\n" +
            "2) Klicken Sie auf eine entsprechende Farbe in der Legende unter dem Spielbrett\n" +
            "3) Drücken Sie auf die entsprechende Zahl auf Ihrer Tastatur. Orientieren Sie sich dabei an den Farbzahlen in der Legende unter dem Spielbrett",
            "Bedienungsanleitung",
            JOptionPane.INFORMATION_MESSAGE);
        }});
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(instructionButton, gbc);

     // Anzahl Farben
        JLabel labelFarben = new JLabel("Anzahl der Farben");
        numberFarbenBox = new JComboBox<>(numbersFarben);
        numberFarbenBox.setPreferredSize(new Dimension(150, numberFarbenBox.getPreferredSize().height));
        numberFarbenBox.setSelectedItem(5);
         numberFarbenBox.addActionListener(new ActionListener() { //sorgt dafür dass Anzahl Farben nach Generierugn des Spielfelds nicht geändert wird
            @Override
            public void actionPerformed(ActionEvent e) {
                // If game has started and selection is changed
                if (startStopButtonZustand == 1 && lastSelectedNumberFarben != null && !lastSelectedNumberFarben.equals(numberFarbenBox.getSelectedItem())) {
                    // Revert back to the value before game start
                    numberFarbenBox.setSelectedItem(lastSelectedNumberFarben);
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Sie können die Anzahl der Farben nicht ändern, nachdem das Spielfeld generiert wurde. Klicke auf Stop, um danach ein neues Spiel zu generieren.",
                            "Warnung",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(labelFarben, gbc);
        gbc.gridy = 4;
        add(numberFarbenBox, gbc);

        // Anzahl Zeilen
        JLabel labelCols = new JLabel("Anzahl der Spalten");
        numberColBox = new JComboBox<>(numbersSpielbrett);
        numberColBox.setPreferredSize(new Dimension(150, numberColBox.getPreferredSize().height));
        numberColBox.setSelectedItem(6);
        numberColBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If game has started and selection is changed
                if (startStopButtonZustand == 1 && lastSelectedNumberCols != null && !lastSelectedNumberCols.equals(numberColBox.getSelectedItem())) {
                    // Revert back to the value before game start
                    numberColBox.setSelectedItem(lastSelectedNumberCols);
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Sie können die Anzahl der Spalten nicht ändern, nachdem das Spielfeld generiert wurde. Klicke auf Stop, um danach ein neues Spiel zu generieren.",
                            "Warnung",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gbc.gridy = 5;
        add(labelCols, gbc);
        gbc.gridy = 6;
        add(numberColBox, gbc);

        // Anzahl Spalten
        JLabel labelRows = new JLabel("Anzahl der Zeilen");
        numberRowBox = new JComboBox<>(numbersSpielbrett);
        numberRowBox.setPreferredSize(new Dimension(150, numberRowBox.getPreferredSize().height));
        numberRowBox.setSelectedItem(6);
        numberRowBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If game has started and selection is changed
                if (startStopButtonZustand == 1 && lastSelectedNumberRows != null && !lastSelectedNumberRows.equals(numberRowBox.getSelectedItem())) {
                    // Revert back to the value before game start
                    numberRowBox.setSelectedItem(lastSelectedNumberRows);
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Sie können die Anzahl der Zeilen nicht ändern, nachdem das Spielfeld generiert wurde. Klicke auf Stop, um danach ein neues Spiel zu generieren.",
                            "Warnung",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gbc.gridy = 7;
        add(labelRows, gbc);
        gbc.gridy = 8;
        add(numberRowBox, gbc);

        // Wer beginnt das Spiel?
        JLabel labelPlayer = new JLabel("Wer beginnt das Spiel");
        numberPlayerBox = new JComboBox<>(players);
        numberPlayerBox.setPreferredSize(new Dimension(150, numberPlayerBox.getPreferredSize().height));
        numberPlayerBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If game has started and selection is changed
                if (startStopButtonZustand == 1 && lastSelectedNumberPlayer != null && !lastSelectedNumberPlayer.equals(numberPlayerBox.getSelectedItem())) {
                    // Revert back to the value before game start
                    numberPlayerBox.setSelectedItem(lastSelectedNumberPlayer);
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Sie können den anfänglichen Spieler nicht ändern, nachdem das Spiel gestartet wurde. Klicke auf Stop, um danach ein neues Spiel zu generieren.",
                            "Warnung",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gbc.gridy = 9;
        add(labelPlayer, gbc);
        gbc.gridy = 10;
        add(numberPlayerBox, gbc);

        // Welche Strategie spielt der Computer?
        JLabel labelStrategy = new JLabel("Strategie des Computers");
        numberStrategyBox = new JComboBox<>(strategy);
        numberStrategyBox.setPreferredSize(new Dimension(150, numberStrategyBox.getPreferredSize().height));
        numberStrategyBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If game has started and selection is changed
                if (startStopButtonZustand == 1 && lastSelectedNumberStrategy != null && !lastSelectedNumberStrategy.equals(numberStrategyBox.getSelectedItem())) {
                    // Revert back to the value before game start
                    numberStrategyBox.setSelectedItem(lastSelectedNumberStrategy);
                    JOptionPane.showMessageDialog(MenuPanel.this,
                            "Sie können die Strategie des Computers nicht ändern, nachdem das Spiel gestartet wurde. Klicke auf Stop, um danach ein neues Spiel zu generieren.",
                            "Warnung",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gbc.gridy = 11;
        add(labelStrategy, gbc);
        gbc.gridy = 12;
        add(numberStrategyBox, gbc);

        
        // Punktestand des Spielers 1
        player1ScoreLabel = new JLabel("Punktestand Spieler 1: " + punktestandMensch);
        gbc.gridy = 13;
        add(player1ScoreLabel, gbc);

        // Punktestand des Spielers 2
        player2ScoreLabel = new JLabel("Punktestand Spieler 2: " + punktestandComputer);
        gbc.gridy = 14;
        add(player2ScoreLabel, gbc);

        // Timer Label
        seconds = 0;
        timerLabel = new JLabel(formatSeconds(seconds));
        timerLabel.setFont(new Font("Serif", Font.PLAIN, 40)); // Set font size
        gbc.gridy = 15;
        gbc.weighty = 1; // This will make the timerLabel take up the remaining space
        add(timerLabel, gbc);

        // Timer Initialization
        timer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText(formatSeconds(seconds));
        });

        // Fügt eine unsichtbare Box am Ende hinzu, die den restlichen vertikalen Platz einnimmt -> Buttons verschieben sich nach oben
         gbc.gridx = 0;
         gbc.gridy = 16;
         gbc.weighty = 1;
         add(Box.createVerticalGlue(), gbc);};
        
        
    @Override
    public Dimension getPreferredSize(){
        return new Dimension((int)(gw.getWidth()*0.35), gw.getHeight());
    }

  // Format seconds into "MM:SS"
    private String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Method to start the timer
    public void startTimer() {
        timer.start();
    }

    // Method to stop the timer
    public void stopTimer() {
        timer.stop();
    }
    // Method to reset the timer
    public void resetTimer() {
        timer.stop();
        seconds = 0;
        timerLabel.setText(formatSeconds(seconds));
    }  

    // Method to update the score of player 1
    public void updatePlayer1Score(int score) {
    punktestandMensch = score;
    player1ScoreLabel.setText("Punktestand Spieler 1: " + punktestandMensch);
}

// Method to update the score of player 2
public void updatePlayer2Score(int score) {
    punktestandComputer = score;
    player2ScoreLabel.setText("Punktestand Spieler 2: " + punktestandComputer);
}

public void resetPlayer1Score(){
    punktestandMensch = 1;
    player1ScoreLabel.setText("Punktestand Spieler 1: " + punktestandMensch);
}

public void resetPlayer2Score(){
    punktestandComputer = 1;
    player2ScoreLabel.setText("Punktestand Spieler 2: " + punktestandComputer);
}

  public void storeComboBoxValues() {
        lastSelectedNumberFarben = (Integer) numberFarbenBox.getSelectedItem();
        lastSelectedNumberCols = (Integer) numberColBox.getSelectedItem();
        lastSelectedNumberRows = (Integer) numberRowBox.getSelectedItem();
        lastSelectedNumberPlayer = (String) numberPlayerBox.getSelectedItem();
        lastSelectedNumberStrategy = (String) numberStrategyBox.getSelectedItem();
    }

    // Call this method when Stop button is clicked
    public void clearStoredComboBoxValues() {
        lastSelectedNumberFarben = null;
        lastSelectedNumberCols = null;
        lastSelectedNumberRows = null;
        lastSelectedNumberPlayer = null;
        lastSelectedNumberStrategy = null;
    }



public Integer[] getNumbersFarben() {
    return numbersFarben;
}

public void setNumbersFarben(Integer[] numbersFarben) {
    this.numbersFarben = numbersFarben;
}

public Integer[] getNumbersSpielbrett() {
    return numbersSpielbrett;
}

public void setNumbersSpielbrett(Integer[] numbersSpielbrett) {
    this.numbersSpielbrett = numbersSpielbrett;
}

public String[] getPlayers() {
    return players;
}

public void setPlayers(String[] players) {
    this.players = players;
}

public String[] getStrategy() {
    return strategy;
}

public void setStrategy(String[] strategy) {
    this.strategy = strategy;
}

public int getStartStopButtonZustand() {
    return startStopButtonZustand;
}

public void setStartStopButtonZustand(int startStopButtonZustand) {
    this.startStopButtonZustand = startStopButtonZustand;
}

public JButton getStartStopButton() {
    return startStopButton;
}

public void setStartStopButton(JButton startStopButton) {
    this.startStopButton = startStopButton;
}

public JComboBox<Integer> getNumberFarbenBox() {
    return numberFarbenBox;
}

public void setNumberFarbenBox(JComboBox<Integer> numberFarbenBox) {
    this.numberFarbenBox = numberFarbenBox;
}

public JComboBox<Integer> getNumberColBox() {
    return numberColBox;
}

public void setNumberColBox(JComboBox<Integer> numberColBox) {
    this.numberColBox = numberColBox;
}

public JComboBox<Integer> getNumberRowBox() {
    return numberRowBox;
}

public void setNumberRowBox(JComboBox<Integer> numberRowBox) {
    this.numberRowBox = numberRowBox;
}

public JComboBox<String> getNumberPlayerBox() {
    return numberPlayerBox;
}

public void setNumberPlayerBox(JComboBox<String> numberPlayerBox) {
    this.numberPlayerBox = numberPlayerBox;
}

public JComboBox<String> getNumberStrategyBox() {
    return numberStrategyBox;
}

public void setNumberStrategyBox(JComboBox<String> numberStrategyBox) {
    this.numberStrategyBox = numberStrategyBox;
}

public int getPlayPauseButtonZustand() {
    return playPauseButtonZustand;
}

public void setPlayPauseButtonZustand(int playPauseButtonZustand) {
    this.playPauseButtonZustand = playPauseButtonZustand;
}

public JButton getPlayPauseButton() {
    return playPauseButton;
}

public void setPlayPauseButton(JButton playPauseButton) {
    this.playPauseButton = playPauseButton;
}

public int getPunktestandMensch() {
    return punktestandMensch;
}

public void setPunktestandMensch(int punktestandMensch) {
    this.punktestandMensch = punktestandMensch;
}

public int getPunktestandComputer() {
    return punktestandComputer;
}

public void setPunktestandComputer(int punktestandComputer) {
    this.punktestandComputer = punktestandComputer;
}

public JLabel getPlayer1ScoreLabel() {
    return player1ScoreLabel;
}

public void setPlayer1ScoreLabel(JLabel player1ScoreLabel) {
    this.player1ScoreLabel = player1ScoreLabel;
}

public JLabel getPlayer2ScoreLabel() {
    return player2ScoreLabel;
}

public void setPlayer2ScoreLabel(JLabel player2ScoreLabel) {
    this.player2ScoreLabel = player2ScoreLabel;
}

public Timer getTimer() {
    return timer;
}

public void setTimer(Timer timer) {
    this.timer = timer;
}

public int getSeconds() {
    return seconds;
}

public void setSeconds(int seconds) {
    this.seconds = seconds;
}

public JLabel getTimerLabel() {
    return timerLabel;
}

public void setTimerLabel(JLabel timerLabel) {
    this.timerLabel = timerLabel;
}


public GameWindow getGw() {
    return gw;
}


public void setGw(GameWindow gw) {
    this.gw = gw;
}


public Integer getLastSelectedNumberFarben() {
    return lastSelectedNumberFarben;
}


public void setLastSelectedNumberFarben(Integer lastSelectedNumberFarben) {
    this.lastSelectedNumberFarben = lastSelectedNumberFarben;
}


public Integer getLastSelectedNumberCols() {
    return lastSelectedNumberCols;
}


public void setLastSelectedNumberCols(Integer lastSelectedNumberCols) {
    this.lastSelectedNumberCols = lastSelectedNumberCols;
}


public Integer getLastSelectedNumberRows() {
    return lastSelectedNumberRows;
}


public void setLastSelectedNumberRows(Integer lastSelectedNumberRows) {
    this.lastSelectedNumberRows = lastSelectedNumberRows;
}


public String getLastSelectedNumberPlayer() {
    return lastSelectedNumberPlayer;
}


public void setLastSelectedNumberPlayer(String lastSelectedNumberPlayer) {
    this.lastSelectedNumberPlayer = lastSelectedNumberPlayer;
}


public String getLastSelectedNumberStrategy() {
    return lastSelectedNumberStrategy;
}


public void setLastSelectedNumberStrategy(String lastSelectedNumberStrategy) {
    this.lastSelectedNumberStrategy = lastSelectedNumberStrategy;
}

 


}


    

