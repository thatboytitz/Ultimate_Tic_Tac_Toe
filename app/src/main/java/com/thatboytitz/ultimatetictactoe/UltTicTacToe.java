package com.thatboytitz.ultimatetictactoe;

import java.util.ArrayList;

/**
 * Created by thatb on 6/8/2016.
 */

public class UltTicTacToe extends TicTacToe{

    private static final int NUM_TILES = 9;
    public ArrayList<TicTacToe> miniBoard;

    UltTicTacToe() {
        miniBoard = new ArrayList<>();
    }

    UltTicTacToe(ArrayList<TicTacToe> miniBoard) {
        this.miniBoard = miniBoard;
    }

    @Override
    public boolean emptyTile(int index) {
        return gameStatus(index) == 0;
    }

    @Override
    public int gameStatus() {
        int rowSum = 0;
        int colSum = 0;
        int diagSum = 0;
        int x = 0;
        int o = 0;
        int draw = 0;
        boolean empty = false;
        boolean full = true;

        for(int i = 0; i < 3; i++){
            x = 0;
            o = 0;
            draw = 0;
            empty = false;
            for(int j = 0; j < 3; j++) {
                int row = i * 3;
                switch (gameStatus(row + j)){
                    case 1:
                        o++;
                        break;
                    case -1:
                        x++;
                        break;
                    case 2:
                        draw++;
                        break;
                    case 0:
                        empty = true;
                        break;
                    default:
                        rowSum *= 0;
                        break;
                }
            }
            if(!empty && o + draw == 3) return 1;
            else if(!empty && x + draw == 3) return -1;
        }

        for(int i = 0; i < 3; i++) {
            x = 0;
            o = 0;
            draw = 0;
            empty = false;
            for(int j = 0; j < 3; j++) {
                int col = j * 3;
                switch (gameStatus(col + i)){
                    case 1:
                        o++;
                        break;
                    case -1:
                        x++;
                        break;
                    case 2:
                        draw++;
                        break;
                    case 0:
                        empty = true;
                        break;
                    default:
                        rowSum *= 0;
                        break;
                }
            }
            if(!empty && o + draw == 3) return 1;
            else if(!empty && x + draw == 3) return -1;
        }

        //diag \
        diagSum = 0;
        x = 0;
        o = 0;
        draw = 0;
        empty = false;
        for(int i = 0; i < 3; i++){
            int diag = i * 4;
            switch (gameStatus(diag)){
                case 1:
                    o++;
                    break;
                case -1:
                    x++;
                    break;
                case 2:
                    draw++;
                    break;
                case 0:
                    empty = true;
                    break;
                default:
                    rowSum *= 0;
                    break;
            }
        }
        if(!empty && o + draw == 3) return 1;
        else if(!empty && x + draw == 3) return -1;

        diagSum = 0;
        x = 0;
        o = 0;
        draw = 0;
        empty = false;

        //diag /
        for(int i = 1; i < 4; i++){
            int diag = i * 2;
            switch (gameStatus(diag)){
                case 1:
                    o++;
                    break;
                case -1:
                    x++;
                    break;
                case 2:
                    draw++;
                    break;
                case 0:
                    empty = true;
                    break;
                default:
                    rowSum *= 0;
                    break;
            }
        }
        if(!empty && o + draw == 3) return 1;
        else if(!empty && x + draw == 3) return -1;

        //draw
        for(int i = 0; i < NUM_TILES && full; i++){
            if(gameStatus(i) == 0) full = false;
        }
        if(full) return 2;

        //nonfinished boards
        return 0;
    }

    public int gameStatus(int board) {
        return miniBoard.get(board).gameStatus();
    }

    @Override
    public ArrayList<Integer> WinnableTiles() {
        return super.WinnableTiles();
    }

    @Override
    public ArrayList<Integer> getEmpty() {
        ArrayList<Integer> empty = new ArrayList<Integer>();
        for(int i = 0; i < 9; i++){
            if(gameStatus(i) == 0) empty.add(i);
        }
        return empty;
    }

    @Override
    public ArrayList<Integer> WinningTiles() {
        return super.WinningTiles();
    }

    public ArrayList<Integer> GetFinished() {
        ArrayList<Integer> finished = new ArrayList<Integer>();
        for(int i = 0; i < 9; i++){
            if(gameStatus(i) != 0) finished.add(i);
        }
        return finished;
    }
}
