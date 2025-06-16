// Name: Okan Ayhan
// Matrikelnummer:7380423

package gui;
import javax.swing.*;
import java.awt.*;
import logic.Board;

public class BoardPanel extends JPanel {

      
    private Board board; 
    private GameBoardPanel gameBoardPanel;
    private ColorPanel colorPanel;
    private MenuPanel menuPanel;

    public BoardPanel(MenuPanel menuPanel) { //MenuPanel im Konstruktor um dem GameBoardPanel-Objekt Informationen der Auswahl in Menütafeln "weiterzugeben"
    this.menuPanel = menuPanel;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    setLayout(layout);
    gameBoardPanel = new GameBoardPanel(null,this.menuPanel);
    colorPanel = new ColorPanel(gameBoardPanel.getColors(),this); // gameBoardPanel.getColors() übergibt das colors-Array aus GameBoardPanel-Klasse, benötigen kein this da colors-Array in jedem gameBoardPanel-Objekt gleich ist
   

    
    gbc.gridx=0;
    gbc.gridy=0;
    gbc.gridwidth=1;
    gbc.gridheight=1;
    gbc.weightx = 1;
    gbc.weighty =0.8;
    gbc.fill = GridBagConstraints.BOTH;
    layout.setConstraints(gameBoardPanel, gbc);
    add(gameBoardPanel);

    
    gbc.gridx=0;
    gbc.gridy=1;
    gbc.gridwidth=1;
    gbc.gridheight=1;
    gbc.weightx = 1;
    gbc.weighty =0.2;
    gbc.fill = GridBagConstraints.BOTH;
    layout.setConstraints(colorPanel, gbc);
    add(colorPanel);

    
}


public Board getBoard() {
    return board;
}


public void setBoard(Board board) {
    this.board = board;
}


public GameBoardPanel getGameBoardPanel() {
    return gameBoardPanel;
}


public void setGameBoardPanel(GameBoardPanel gameBoardPanel) {
    this.gameBoardPanel = gameBoardPanel;
}


public ColorPanel getColorPanel() {
    return colorPanel;
}


public void setColorPanel(ColorPanel colorPanel) {
    this.colorPanel = colorPanel;
}


public MenuPanel getMenuPanel() {
    return menuPanel;
}


public void setMenuPanel(MenuPanel menuPanel) {
    this.menuPanel = menuPanel;
}


}

    

