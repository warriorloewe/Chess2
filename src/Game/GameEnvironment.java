package Game;

import Library.Menu;
import Overlay.Display;
import Pieces.*;

import java.util.ArrayList;

public class GameEnvironment implements Runnable {
    public static GameState gameState;
    private final Square[] board;
    private Menu activeMenu;
    private boolean gameOver = false;
    private boolean whitesMove;
    private boolean whiteLongCastle;
    private boolean whiteShortCastle;
    private boolean blackLongCastle;
    private boolean blackShortCastle;
    private Display display;
    private InputManager input;
    private Piece selectedPiece;
    private Square selectedSquare;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private King whiteKing;
    private King blackKing;
    private double whiteTimer = 180;
    private double blackTimer = 180;

    public GameEnvironment() {
        gameState = GameState.MAIN_MENU;
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        board = new Square[64];
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        while (true) {
            display.repaint();
            switch (gameState) {
                case BOARD -> {
                    long deltaTime = System.currentTimeMillis() - currentTime;
                    if(deltaTime > 100) {
                        if(whitesMove) {
                            whiteTimer -= (double) deltaTime / 1000;
                        } else {
                            blackTimer -= (double) deltaTime / 1000;
                        }
                        currentTime = System.currentTimeMillis();
                    }
                }
                case MAIN_MENU, SETTINGS -> currentTime = System.currentTimeMillis();
            }
        }
    }

    public void move(Piece p, Square start, Square end, boolean enPassant, boolean pseudoMove) {
        if(enPassant) {
            int direction = p.isWhite() ? 8 : -8;
            board[end.getPosition() + direction].setPiece(null);
        }
        Piece capturedPiece = end.getPiece();
        if(capturedPiece != null) {
            capturedPiece.setPosition(-100);
        }
        start.setPiece(null);
        end.setPiece(p);
        p.setPosition(end.getPosition());
        if(!pseudoMove) {
            if(capturedPiece != null) {
                if(p.isWhite()) {
                    blackPieces.remove(capturedPiece);
                } else {
                    whitePieces.remove(capturedPiece);
                }
            }
            selectedPiece = null;
            selectedSquare = null;
            update();
        }
        if(p instanceof Pawn && !p.hasMoved()) {
            ((Pawn) p).setEnPassant(Math.abs(start.getY() - end.getY()) > 1);
        } else if(p instanceof King && Math.abs(start.getX() - end.getX()) > 1) {
            boolean shortCastle = start.getX() - end.getX() == -2;
            Square startR = shortCastle ? board[p.getPosition() + 1] : board[p.getPosition() - 2];
            Square endR = shortCastle ? board[p.getPosition() - 1] : board[p.getPosition() + 1];
            Rook r = (Rook) startR.getPiece();
            startR.setPiece(null);
            endR.setPiece(r);
            r.setPosition(p.getPosition() - 1);
            r.setMoved(true);
        }
        p.setMoved(true);
    }

    private boolean isLegalMove(Piece p, Square start, Square end, boolean enPassant) {
        boolean legal = true;
        boolean castled = p instanceof King && Math.abs(start.getX() - end.getX()) > 1;
        boolean moved = p.hasMoved();
        Piece capturedPiece = end.getPiece();
        Pawn enPassantPawn = null;
        if(enPassant) {
            enPassantPawn = (Pawn) (p.isWhite() ? board[end.getPosition() + 8].getPiece() : board[end.getPosition() - 8].getPiece());
        }
        move(p, start, end, enPassant, true);

        ArrayList<Piece> pieces = whitesMove ? blackPieces : whitePieces;
        int kingPos = whitesMove ? whiteKing.getPosition() : blackKing.getPosition();
        for(Piece enemyP : pieces) {
            if(enemyP.getReachableFields(board).contains(board[kingPos])) {
                legal = false;
                break;
            }
        }
        unmakeMove(p, end, start, capturedPiece, enPassant, enPassantPawn, castled);
        p.setMoved(moved);
        return legal;
    }

    private void unmakeMove(Piece p, Square start, Square end, Piece capturedPiece, boolean enPassant, Pawn enPassantPawn, boolean castled) {
        if(enPassant) {
            int direction = enPassantPawn.getPosition() % 8 - end.getPosition() % 8;
            board[end.getPosition() + direction].setPiece(enPassantPawn);
        }
        start.setPiece(capturedPiece);
        end.setPiece(p);
        p.setPosition(end.getPosition());
        if(capturedPiece != null) {
            capturedPiece.setPosition(start.getPosition());
        }
        if(p instanceof Pawn && !p.hasMoved() && Math.abs(start.getY() - end.getY()) > 1) {
            ((Pawn) p).setEnPassant(false);
        } else if(castled) {
            boolean shortCastle = start.getX() - end.getX() == 2;
            Square startR = shortCastle ? board[p.getPosition() + 1] : board[p.getPosition() - 1];
            Square endR = shortCastle ? board[p.getPosition() + 3] : board[p.getPosition() - 4];
            Rook r = (Rook) startR.getPiece();
            startR.setPiece(null);
            endR.setPiece(r);
            r.setPosition(p.getPosition() - 1);
            r.setMoved(true);
        }
    }

    private boolean canCastle(Square start, Square end) {
        int delta = start.getPosition() - end.getPosition();
        int direction = Math.abs(delta) != delta ? 1 : -1;
        ArrayList<Piece> pieces = whitesMove ? blackPieces : whitePieces;
        for(Piece enemyP : pieces) {
            ArrayList<Square> reachableFields = enemyP.getReachableFields(board);
            for(int i = 0; i <= Math.abs(delta); i++) {
                if(reachableFields.contains(board[start.getPosition() + i * direction])) {
                    return false;
                }
            }
        }
        return true;
    }

    public void update() {
        whitesMove = !whitesMove;
        for(Piece p : (whitePieces)) {
            if(p instanceof Pawn) {
                ((Pawn) p).setEnPassant(false);
            }
        }
        for(Piece p : (blackPieces)) {
            if(p instanceof Pawn) {
                ((Pawn) p).setEnPassant(false);
            }
        }
        updateMarkers();
    }

    /*
     * first clears all markers then if the selected figure isn't null
     * it sets the fields the figure can move to to marked
     * and the ones it can attack to attackable
     */
    public void updateMarkers() {
        for(Square s : board) {
            s.setMarked(false);
            s.setAttackable(false);
            s.setEnPassant(false);
        }
        if(selectedPiece == null) return;

        for(Square s : selectedPiece.getReachableFields(board)) {
            if(isLegalMove(selectedPiece, board[selectedPiece.getPosition()], s, false)) {
                if(s.getPiece() != null) {
                    s.setAttackable(true);
                } else {
                    s.setMarked(true);
                }
            }
        }
        int correctRank = selectedPiece.isWhite() ? 3 : 4;
        if(selectedPiece instanceof Pawn && selectedPiece.getPosition() / 8 == correctRank) {
            for(Square s : ((Pawn) selectedPiece).getEnPassantFields(board)) {
                if(isLegalMove(selectedPiece, board[selectedPiece.getPosition()], s, true)) {
                    s.setEnPassant(true);
                }
            }
        } else if(selectedPiece instanceof King && !selectedPiece.hasMoved()) {
            for(Square s : ((King) selectedPiece).getCastleFields(board)) {
                if(canCastle(selectedSquare, s)) {
                    s.setMarked(true);
                }
            }
        }
    }

    public void updateSelectedPiece(Piece p, Square s) {
        if(p != selectedPiece && whitesMove == p.isWhite()) {
            selectedPiece = p;
            selectedSquare = s;
            updateMarkers();
        } else if (selectedPiece != null) {
            selectedPiece = null;
            selectedSquare = null;
            updateMarkers();
        }
    }

    // creates a board from the standard FEN notation
    // https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
    public void createBoard(String boardFen) {
        int pos = 0;
        for (char c : boardFen.toCharArray()) {
            // after the board notation (pos > 63) comes player currently moving and castling rights
            if (pos > 63) {
                if (c == 'w' || c == 'b') whitesMove = c == 'w';
                else if (c == 'K') whiteShortCastle = true;
                else if (c == 'k') blackShortCastle = true;
                else if (c == 'Q') whiteLongCastle = true;
                else if (c == 'q') blackLongCastle = true;
                continue;
            }
            // if the character is a number we want to create that many empty squares except if it's a backslash (ASCII: / = 47)
            if (c < 57) {
                if (c > 47) {
                    int number = c - 48;
                    for (int i = 0; i < number; i++) {
                        board[pos] = new Square(pos, null);
                        pos++;
                    }
                }
                continue;
            }
            Piece p = null;
            // we want to create the piece according to what character is in that position
            // uppercase letters for white pieces lowercase letters for black pieces
            switch (c) {
                case 'r', 'R' -> {
                    p = new Rook(this, c == 'R', pos);
                }
                case 'n', 'N' -> {
                    p = new Knight(this, c == 'N', pos);
                }
                case 'b', 'B' -> {
                    p = new Bishop(this, c == 'B', pos);
                }
                case 'q', 'Q' -> {
                    p = new Queen(this, c == 'Q', pos);
                }
                case 'k', 'K' -> {
                    p = new King(this, c == 'K', pos);
                }
                case 'p', 'P' -> {
                    p = new Pawn(this, c == 'P', pos);
                }
            }
            if (p != null) {
                board[pos] = new Square(pos, p);
            }
            // go to the next position in the string
            pos++;
        }
        for(Square s : board) {
            Piece p = s.getPiece();
            if(p != null) {
                if(p.isWhite()) {
                    if(p instanceof King) {
                        whiteKing = (King) p;
                    }
                    whitePieces.add(p);
                } else {
                    if(p instanceof King) {
                        blackKing = (King) p;
                    }
                    blackPieces.add(p);
                }
            }
        }
    }

    public Square[] getBoard() {
        return board;
    }

    public Menu getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(Menu activeMenu) {
        this.activeMenu = activeMenu;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public void setInput(InputManager input) {
        this.input = input;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    public void setSelectedSquare(Square selectedSquare) {
        this.selectedSquare = selectedSquare;
    }

    public boolean canWhiteCasteLong() {
        return whiteLongCastle;
    }

    public boolean canWhiteCasteShort() {
        return whiteShortCastle;
    }

    public boolean canBlackCasteLong() {
        return blackLongCastle;
    }

    public boolean canBlackCasteShort() {
        return blackShortCastle;
    }

    public boolean isWhitesMove() {
        return whitesMove;
    }

    public String getTime(double time) {
        String minutes = String.valueOf((int) (time / 60));
        String seconds = time % 60 < 10 ? "0" + time % 60 : String.valueOf(time % 60);
        seconds = seconds.substring(0, 4);
        return minutes + ":" + seconds;
    }

    public String getWhiteTime() {
        return getTime(whiteTimer);
    }

    public String getBlackTime() {
        return getTime(blackTimer);
    }

    public enum GameState {
        MAIN_MENU,
        SETTINGS,
        BOARD
    }
}
