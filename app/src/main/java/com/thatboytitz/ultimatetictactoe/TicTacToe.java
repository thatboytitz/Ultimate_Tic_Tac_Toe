package com.thatboytitz.ultimatetictactoe;

import java.util.ArrayList;

/**
 * Created by Raul on 5/7/2016.
 */
public class TicTacToe {

    private static final int NUM_TILES = 3;
    private ArrayList<Tile> tiles;

    TicTacToe() {
        tiles = new ArrayList<>();

        for(int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            Tile tile = new Tile();
            tiles.add(tile);
        }
    }

    public void setTile (int index, int value) {
        tiles.get(index).setValue(value);
    }

    public boolean emptyTile (int index) {
        boolean isEmpty = false;

        if (tiles.get(index).getValue() == 0) {
            isEmpty = true;
        }

        return isEmpty;
    }

    public int gameStatus () {
        int gameValue = 0;
        int rowSum = 0;
        int colSum = 0;
        int diagSum = 0;
        boolean full = true;

        //row
        for(int i = 0; i < 3; i++){
            rowSum = 0;
            for(int j = 0; j < 3; j++){
                int row = i * 3;
                rowSum += tiles.get(row + j).getValue();
            }
            if(rowSum == 3) return 1;
            else if (rowSum == -3) return -1;
        }

        //column
        for(int i = 0; i < 3; i++) {
            colSum = 0;
            for(int j = 0; j < 3; j++) {
                int col = j * 3;
                colSum += tiles.get(col + i).getValue();
            }
            if(colSum == 3) return 1;
            else if(colSum == -3) return -1;
        }

        //diag \
        diagSum = 0;
        for(int i = 0; i < 3; i++){
            int diag = i * 4;
            diagSum+= tiles.get(diag).getValue();
        }

        if(diagSum == 3) return 1;
        else if(diagSum == -3) return -1;

        diagSum = 0;

        //diag /
        for(int i = 1; i < 4; i++){
            int diag = i * 2;
            diagSum+= tiles.get(diag).getValue();
        }

        if(diagSum == 3) return 1;
        else if(diagSum == -3) return -1;

        //empty
        for(int i =0; i<9;i++){
            if(emptyTile(i)) full = false;
        }
        if(full) return 2;
        
        return gameValue;
    }

    public ArrayList<Integer> WinnableTiles(){
        ArrayList<Integer> winnable = new ArrayList<Integer>();
        int rowSum = 0;
        for(int i = 0; i < 3; i++){
            rowSum = 0;
            for(int j = 0; j < 3; j++){
                int row = i * 3;
                rowSum += tiles.get(row + j).getValue();
            }
            if(rowSum == 2 || rowSum == -2) {
                for(int j = 0; j < 3; j++){
                    int row = i * 3;
                    if(emptyTile(row + j)) winnable.add(row + j);
                }
            }
        }

        int colSum = 0;
        for(int i = 0; i < 3; i++) {
            colSum = 0;
            for(int j = 0; j < 3; j++) {
                int col = j * 3;
                colSum += tiles.get(col + i).getValue();
            }
            if(colSum == 2 ||colSum == -2){
                for(int j = 0; j < 3; j++) {
                    int col = j * 3;
                    if(emptyTile(col + i)) winnable.add(col + i);
                }
            }
        }

        int diagSum = 0;

        for(int i = 0; i < 3; i++){
            int diag = i * 4;
            diagSum+= tiles.get(diag).getValue();
        }
        if(diagSum == 2 || diagSum == -2) {
            for(int i = 0; i < 3; i++){
                int diag = i * 4;
                if(emptyTile(diag)) winnable.add(diag);
            }
        }

        diagSum = 0;

        for(int i = 1; i < 4; i++){
            int diag = i * 2;
            diagSum+= tiles.get(diag).getValue();
        }
        if(diagSum == 2 || diagSum == -2) {
            for(int i = 1; i < 4; i++){
                int diag = i * 2;
                if(emptyTile(diag)) winnable.add(diag);
            }
        }
        return winnable;
    }

    public ArrayList<Integer> WinnableTiles(int player){
        ArrayList<Integer> winnable = new ArrayList<>();
        int rowSum = 0;
        for(int i = 0; i < 3; i++){
            rowSum = 0;
            for(int j = 0; j < 3; j++){
                int row = i * 3;
                rowSum += tiles.get(row + j).getValue();
            }
            if(rowSum == (2 * player)) {
                for(int j = 0; j < 3; j++){
                    int row = i * 3;
                    if(emptyTile(row + j)) winnable.add(row + j);
                }
            }
        }

        int colSum = 0;
        for(int i = 0; i < 3; i++) {
            colSum = 0;
            for(int j = 0; j < 3; j++) {
                int col = j * 3;
                colSum += tiles.get(col + i).getValue();
            }
            if(colSum == (2 * player)){
                for(int j = 0; j < 3; j++) {
                    int col = j * 3;
                    if(emptyTile(col + i)) winnable.add(col + i);
                }
            }
        }

        int diagSum = 0;

        for(int i = 0; i < 3; i++){
            int diag = i * 4;
            diagSum+= tiles.get(diag).getValue();
        }
        if(diagSum == (2 * player)) {
            for(int i = 0; i < 3; i++){
                int diag = i * 4;
                if(emptyTile(diag)) winnable.add(diag);
            }
        }

        diagSum = 0;

        for(int i = 1; i < 4; i++){
            int diag = i * 2;
            diagSum+= tiles.get(diag).getValue();
        }
        if(diagSum == (2 * player)) {
            for(int i = 1; i < 4; i++){
                int diag = i * 2;
                if(emptyTile(diag)) winnable.add(diag);
            }
        }
        return winnable;
    }



    public ArrayList<Integer> WinningTiles(){
        ArrayList<Integer> win = new ArrayList<Integer>();
        int rowSum = 0;
        for(int i = 0; i < 3; i++){
            rowSum = 0;
            for(int j = 0; j < 3; j++){
                int row = i * 3;
                rowSum += tiles.get(row + j).getValue();
            }
            if(rowSum == 3 || rowSum == -3) {
                for(int j = 0; j < 3; j++){
                    int row = i * 3;
                    win.add(row + j);
                }
            }
        }

        int colSum = 0;
        for(int i = 0; i < 3; i++) {
            colSum = 0;
            for(int j = 0; j < 3; j++) {
                int col = j * 3;
                colSum += tiles.get(col + i).getValue();
            }
            if(colSum == 3 ||colSum == -3){
                for(int j = 0; j < 3; j++) {
                    int col = j * 3;
                    win.add(col + i);
                }
            }
        }

        int diagSum = 0;

        for(int i = 0; i < 3; i++){
            int diag = i * 4;
            diagSum+= tiles.get(diag).getValue();
        }
        if(diagSum == 3 || diagSum == -3) {
            for(int i = 0; i < 3; i++){
                int diag = i * 4;
                win.add(diag);
            }
        }

        diagSum = 0;

        for(int i = 1; i < 4; i++){
            int diag = i * 2;
            diagSum+= tiles.get(diag).getValue();
        }
        if(diagSum == 3 || diagSum == -3) {
            for(int i = 1; i < 4; i++){
                int diag = i * 2;
                win.add(diag);
            }
        }
        return win;
    }

    public ArrayList<Integer> getEmpty(){
        ArrayList<Integer> empty = new ArrayList<>();
        for(int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            if(emptyTile(i)) empty.add(i);
        }
        return empty;
    }

}