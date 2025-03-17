import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SoloTestGame extends JFrame {
    private static final int SIZE = 7;
    private JButton[][] board = new JButton[SIZE][SIZE];
    private JButton selectedPeg = null;

    public SoloTestGame() {
        setTitle("Solo Test Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidPosition(i, j)) {
                    board[i][j] = new JButton();
                    board[i][j].addActionListener(new ButtonClickListener(i, j));
                    add(board[i][j]);
                } else {
                    board[i][j] = null;
                    add(new JLabel());  // Invalid positions are empty
                }
            }
        }

        // Initialize the board with pegs
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidPosition(i, j)) {
                    board[i][j].setBackground(Color.GREEN);
                    board[i][j].setText("O");  // Peg
                }
            }
        }
        board[SIZE / 2][SIZE / 2].setBackground(Color.WHITE);
        board[SIZE / 2][SIZE / 2].setText("");  // Central hole
    }

    private boolean isValidPosition(int i, int j) {
        if (i < 2 || i > 4) {
            return j >= 2 && j <= 4;
        } else {
            return true;
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int x, y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (selectedPeg == null) {
                if ("O".equals(button.getText())) {
                    selectedPeg = button;
                    button.setBackground(Color.RED);
                }
            } else {
                movePeg(selectedPeg, button);
            if("O".equals(button.getText()))
                selectedPeg.setBackground(Color.WHITE);
            else
                selectedPeg.setBackground(Color.GREEN);
                selectedPeg = null;
                checkGameStatus();
            }
        }
    }

    private void movePeg(JButton fromButton, JButton toButton) {
        int fromX = -1, fromY = -1, toX = -1, toY = -1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == fromButton) {
                    fromX = i;
                    fromY = j;
                }
                if (board[i][j] == toButton) {
                    toX = i;
                    toY = j;
                }
            }
        }

        if (isValidMove(fromX, fromY, toX, toY)) {
            toButton.setText("O");
            toButton.setBackground(Color.GREEN);
            fromButton.setText("");
            fromButton.setBackground(Color.WHITE);  // Set the previous position to white
            board[(fromX + toX) / 2][(fromY + toY) / 2].setText("");
            board[(fromX + toX) / 2][(fromY + toY) / 2].setBackground(Color.WHITE);

        }
    }

    private boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        if (!isValidPosition(toX, toY) || toX < 0 || toX >= SIZE || toY < 0 || toY >= SIZE || board[toX][toY] == null || !"".equals(board[toX][toY].getText())) {
            return false;
        }
        int dx = Math.abs(fromX - toX);
        int dy = Math.abs(fromY - toY);
        if ((dx == 2 && dy == 0) || (dx == 0 && dy == 2)) {
            int midX = (fromX + toX) / 2;
            int midY = (fromY + toY) / 2;
            return midX >= 0 && midX < SIZE && midY >= 0 && midY < SIZE && board[midX][midY] != null && "O".equals(board[midX][midY].getText());
        }
        return false;
    }

    private void checkGameStatus() {
        int pegCount = 0;
        boolean canMove = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidPosition(i, j) && board[i][j] != null && "O".equals(board[i][j].getText())) {
                    pegCount++;
                    if (canMovePeg(i, j)) {
                        canMove = true;
                    }
                }
            }
        }

        if (!canMove) {
            String message;
            switch (pegCount) {
                case 1:
                    message = "Zekisin! Oyun bitti. Kalan taş: 1";
                    break;
                case 2:
                    message = "Akıllısın! Oyun bitti. Kalan taş: 2";
                    break;
                case 3:
                    message = "Kurnazsın! Oyun bitti. Kalan taş: 3";
                    break;
                case 4:
                    message = "Tembelsin! Oyun bitti. Kalan taş: 4";
                    break;
                case 5:
                    message = "Salaksın! Oyun bitti. Kalan taş: 5";
                    break;
                case 6:
                    message = "Gerizekalısın! Oyun bitti. Kalan taş: 6";
                    break;
                case 7:
                    message = "Çalışman lazım! Oyun bitti. Kalan taş: 7";
                    break;
                case 8:
                    message = "Bir daha oynama! Oyun bitti. Kalan taş: 8";
                    break;
                default:
                    message = "Oyun bitti. Kalan taş: " + pegCount;
                    break;
            }

            JOptionPane.showMessageDialog(this, message, "Oyun Bitti", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean canMovePeg(int x, int y) {
        int[] dx = {-2, 2, 0, 0};
        int[] dy = {0, 0, -2, 2};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (newX >= 0 && newX < SIZE && newY >= 0 && newY < SIZE && isValidMove(x, y, newX, newY)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SoloTestGame().setVisible(true);
            }
        });
    }
}

