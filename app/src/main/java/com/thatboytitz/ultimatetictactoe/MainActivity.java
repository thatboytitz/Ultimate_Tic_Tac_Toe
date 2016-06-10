package com.thatboytitz.ultimatetictactoe;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final boolean DEBUG = false;
    private Button[][] grid;
    private UltTicTacToe ultGame;
    private Random random;
    private Integer currentBoard;
    private Integer previousBoard;
    private int choiceTile;
    private static final int NUM_TILES = 9;
    private static final String button = "button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentBoard = -1;
        previousBoard = -1;
        choiceTile = 0;

        grid = new Button[NUM_TILES][];
        ArrayList<TicTacToe> minigame;
        minigame = new ArrayList<>();
        for(int i = 0; i < NUM_TILES; i++) {
            minigame.add(new TicTacToe());
            grid[i] = new Button[NUM_TILES];
            for(int j = 0; j < NUM_TILES; j++){
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID,"id",getPackageName());
                findViewById(resID).setTag(buttonID);
                grid[i][j] = (Button) findViewById(resID);
            }
        }
        ultGame = new UltTicTacToe(minigame);

        random = new Random();
        boolean playerTurn = random.nextBoolean();
        if(!playerTurn) CPUTurn();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void MyClickFunction(View view) {
        DoAction((String) view.getTag());
    }

    public void DoAction(String id) {
        Integer board = Integer.parseInt(id.substring(6,7));
        Integer tile = Integer.parseInt(id.substring(7,8));
        grid[board][tile].setText("O");
        ultGame.miniBoard.get(board).setTile(tile,1);
        previousBoard = currentBoard;
        currentBoard = tile;
        if(!GameOver()) CPUTurn();
        else DrawBoard();
    }

    public void CPUTurn(){
        int choiceBoard = currentBoard;
        ArrayList<Integer> validTiles;
        ArrayList<Integer> validBoards;
        ArrayList<Integer> winningMovesPlayer = new ArrayList<>();
        ArrayList<Integer> winningMovesCPU = new ArrayList<>();
        ArrayList<Integer> nextCPUWinnable = new ArrayList<>();
        ArrayList<Integer> FinishedBoards = new ArrayList<>();

        if(choiceBoard < 0) {                                   //no previous move
            if(DEBUG) Log.d("debug","no previous move");
            validBoards = ultGame.getEmpty();
            choiceBoard = validBoards.get(random.nextInt(validBoards.size()));
            validTiles = ultGame.miniBoard.get(choiceBoard).getEmpty();
            choiceTile = validTiles.get(random.nextInt(validTiles.size()));
        }
        else if (!ultGame.emptyTile(choiceBoard)){              //current board finished
            if(DEBUG) Log.d("debug","board finished");
            ArrayList<Integer> winnableBoardsCPU = new ArrayList<>();
            ArrayList<Integer> winnableBoardsPlayer = new ArrayList<>();
            ArrayList<Integer> nextPlayerWinnable = new ArrayList<>();
            validBoards = new ArrayList<>();

            for(int i = 0; i < NUM_TILES; i++) {
                if(ultGame.emptyTile(i)) {
                    if (!ultGame.miniBoard.get(i).WinnableTiles(1).isEmpty())
                        winnableBoardsPlayer.add(i);
                    if(!ultGame.miniBoard.get(i).WinnableTiles(-1).isEmpty())
                        winnableBoardsCPU.add(i);
                    validBoards.add(i);
                }
            }

            if(!winnableBoardsCPU.isEmpty()) {
                choiceBoard = winnableBoardsCPU.get(random.nextInt(winnableBoardsCPU.size()));
                validTiles = ultGame.miniBoard.get(choiceBoard).WinnableTiles(-1);

                //DON'T MOVE TO CPU
                for(Integer tile: validTiles){
                    ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                    if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                    if (!CPUWinnable.isEmpty())
                        nextCPUWinnable.add(tile);
                }
                if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                if(validTiles.size() > FinishedBoards.size()){
                    if(validTiles.size() > FinishedBoards.size()){
                        for (Integer removeNextWinnable : FinishedBoards) {
                            validTiles.remove(removeNextWinnable);
                        }
                    }
                }
                if(validTiles.size() > nextCPUWinnable.size()){
                    if(validTiles.size() > nextCPUWinnable.size()){
                        for (Integer removeNextWinnable : nextCPUWinnable) {
                            validTiles.remove(removeNextWinnable);
                        }
                    }
                }
                //DON'T MOVE TO CPU

                choiceTile = validTiles.get(random.nextInt(validTiles.size()));
            }
            else if(!winnableBoardsPlayer.isEmpty()) {
                choiceBoard = winnableBoardsPlayer.get(random.nextInt(winnableBoardsPlayer.size()));
                validTiles = ultGame.miniBoard.get(choiceBoard).WinnableTiles(1);

                //DON'T MOVE TO CPU
                for(Integer tile: validTiles){
                    ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                    if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                    if (!CPUWinnable.isEmpty())
                        nextCPUWinnable.add(tile);
                }
                if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                if(validTiles.size() > FinishedBoards.size()){
                    if(validTiles.size() > FinishedBoards.size()){
                        for (Integer removeNextWinnable : FinishedBoards) {
                            validTiles.remove(removeNextWinnable);
                        }
                    }
                }
                if(validTiles.size() > nextCPUWinnable.size()){
                    if(validTiles.size() > nextCPUWinnable.size()){
                        for (Integer removeNextWinnable : nextCPUWinnable) {
                            validTiles.remove(removeNextWinnable);
                        }
                    }
                }
                //DON'T MOVE TO CPU

                choiceTile = validTiles.get(random.nextInt(validTiles.size()));

            }
            else {
                choiceBoard = validBoards.get(random.nextInt(validBoards.size()));
                validTiles = ultGame.miniBoard.get(choiceBoard).getEmpty();

                //DON'T MOVE TO CPU
                for(Integer tile: validTiles){
                    ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                    if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                    if (!CPUWinnable.isEmpty())
                        nextCPUWinnable.add(tile);
                }
                if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                if(validTiles.size() > FinishedBoards.size()){
                    if(validTiles.size() > FinishedBoards.size()){
                        for (Integer removeNextWinnable : FinishedBoards) {
                            validTiles.remove(removeNextWinnable);
                        }
                    }
                }
                if(validTiles.size() > nextCPUWinnable.size()){
                    if(validTiles.size() > nextCPUWinnable.size()){
                        for (Integer removeNextWinnable : nextCPUWinnable) {
                            validTiles.remove(removeNextWinnable);
                        }
                    }
                }
                //DON'T MOVE TO CPU

                choiceTile = validTiles.get(random.nextInt(validTiles.size()));
            }
        }
        else {                                                  //board in play
            if(DEBUG) Log.d("debug","current: " + currentBoard + "");

            winningMovesCPU = ultGame.miniBoard.get(currentBoard).WinnableTiles(-1);
            winningMovesPlayer = ultGame.miniBoard.get(currentBoard).WinnableTiles(1);

            if (!winningMovesCPU.isEmpty()) {
                if(DEBUG) Log.d("debug","CPU has winning move");
                ArrayList<Integer> nextPlayerWinnable = new ArrayList<>();

                for (Integer tile : winningMovesCPU) {
                    ArrayList<Integer> temp = ultGame.miniBoard.get(tile).WinnableTiles(1);
                    if (!temp.isEmpty())
                        nextPlayerWinnable.add(tile);
                }
                if(DEBUG) Log.d("debug", "nextPlayerWinnable: " + nextPlayerWinnable.toString());

                if (nextPlayerWinnable.isEmpty()) {

                    //DON'T MOVE TO CPU
                    for(Integer tile: winningMovesCPU){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(winningMovesCPU.size() > FinishedBoards.size()){
                        if(winningMovesCPU.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                winningMovesCPU.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(winningMovesCPU.size() > nextCPUWinnable.size()){
                        if(winningMovesCPU.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                winningMovesCPU.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = winningMovesCPU.get(random.nextInt(winningMovesCPU.size()));
                }
                else if (nextPlayerWinnable.size() < winningMovesCPU.size()) {
                    for (Integer removeNextWinnable : nextPlayerWinnable) {
                        winningMovesCPU.remove(removeNextWinnable);
                    }

                    //DON'T MOVE TO CPU
                    for(Integer tile: winningMovesCPU){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(winningMovesCPU.size() > FinishedBoards.size()){
                        if(winningMovesCPU.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                winningMovesCPU.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(winningMovesCPU.size() > nextCPUWinnable.size()){
                        if(winningMovesCPU.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                winningMovesCPU.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = winningMovesCPU.get(random.nextInt(winningMovesCPU.size()));
                }
                else {

                    //DON'T MOVE TO CPU
                    for(Integer tile: winningMovesCPU){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(winningMovesCPU.size() > FinishedBoards.size()){
                        if(winningMovesCPU.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                winningMovesCPU.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(winningMovesCPU.size() > nextCPUWinnable.size()){
                        if(winningMovesCPU.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                winningMovesCPU.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = winningMovesCPU.get(random.nextInt(winningMovesCPU.size()));
                }
            }
            else if (!winningMovesPlayer.isEmpty()) {
                if(DEBUG) Log.d("debug","Player has winning move");
                ArrayList<Integer> nextPlayerWinnable = new ArrayList<>();
                for (Integer tile : winningMovesPlayer) {
                    ArrayList<Integer> temp = ultGame.miniBoard.get(tile).WinnableTiles(1);
                    if (!temp.isEmpty())
                        nextPlayerWinnable.add(tile);
                }
                if(DEBUG) Log.d("debug", "nextPlayerWinnable: " + nextPlayerWinnable.toString());


                if (nextPlayerWinnable.isEmpty()) {

                    //DON'T MOVE TO CPU
                    for(Integer tile: winningMovesPlayer){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(winningMovesPlayer.size() > FinishedBoards.size()){
                        if(winningMovesPlayer.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                winningMovesPlayer.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(winningMovesPlayer.size() > nextCPUWinnable.size()){
                        if(winningMovesPlayer.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                winningMovesPlayer.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = winningMovesPlayer.get(random.nextInt(winningMovesPlayer.size()));
                }
                else if (nextPlayerWinnable.size() < winningMovesPlayer.size()) {
                    for (Integer removeNextWinnable : nextPlayerWinnable) {
                        winningMovesPlayer.remove(removeNextWinnable);
                    }

                    //DON'T MOVE TO CPU
                    for(Integer tile: winningMovesPlayer){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(winningMovesPlayer.size() > FinishedBoards.size()){
                        if(winningMovesPlayer.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                winningMovesPlayer.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(winningMovesPlayer.size() > nextCPUWinnable.size()){
                        if(winningMovesPlayer.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                winningMovesPlayer.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = winningMovesPlayer.get(random.nextInt(winningMovesPlayer.size()));
                }
                else {


                    //DON'T MOVE TO CPU
                    for(Integer tile: winningMovesPlayer){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(winningMovesPlayer.size() > FinishedBoards.size()){
                        if(winningMovesPlayer.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                winningMovesPlayer.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(winningMovesPlayer.size() > nextCPUWinnable.size()){
                        if(winningMovesPlayer.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                winningMovesPlayer.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = winningMovesPlayer.get(random.nextInt(winningMovesPlayer.size()));
                }

            }
            else {
                if(DEBUG) Log.d("debug","No winning move");
                validTiles = ultGame.miniBoard.get(choiceBoard).getEmpty();

                ArrayList<Integer> nextPlayerWinnable = new ArrayList<>();
                for (Integer tile : validTiles) {
                    ArrayList<Integer> temp = ultGame.miniBoard.get(tile).WinnableTiles(1);
                    if (!temp.isEmpty())
                        nextPlayerWinnable.add(tile);
                }
                if(DEBUG) Log.d("debug", "nextPlayerWinnable: " + nextPlayerWinnable.toString());

                if (nextPlayerWinnable.isEmpty()) {
                    for(Integer tile: validTiles){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(validTiles.size() > FinishedBoards.size()){
                        if(validTiles.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                validTiles.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(validTiles.size() > nextCPUWinnable.size()){
                        if(validTiles.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                validTiles.remove(removeNextWinnable);
                            }
                        }
                    }
                    choiceTile = validTiles.get(random.nextInt(validTiles.size()));
                }
                else if (nextPlayerWinnable.size() < validTiles.size()) {
                    for (Integer removeNextWinnable : nextPlayerWinnable) {
                        validTiles.remove(removeNextWinnable);
                    }

                    //DON'T MOVE TO CPU
                    for(Integer tile: validTiles){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(validTiles.size() > FinishedBoards.size()){
                        if(validTiles.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                validTiles.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(validTiles.size() > nextCPUWinnable.size()){
                        if(validTiles.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                validTiles.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU


                    choiceTile = validTiles.get(random.nextInt(validTiles.size()));
                }
                else {

                    //DON'T MOVE TO CPU
                    for(Integer tile: validTiles){
                        ArrayList<Integer> CPUWinnable = ultGame.miniBoard.get(tile).WinnableTiles(-1);
                        if(ultGame.gameStatus(tile) != 0) FinishedBoards.add(tile);
                        if (!CPUWinnable.isEmpty())
                            nextCPUWinnable.add(tile);
                    }
                    if(DEBUG) Log.d("debug", "nextCPUWinnable: " + nextCPUWinnable.toString());
                    if(DEBUG) Log.d("debug", "Finished: " + FinishedBoards.toString());
                    if(validTiles.size() > FinishedBoards.size()){
                        if(validTiles.size() > FinishedBoards.size()){
                            for (Integer removeNextWinnable : FinishedBoards) {
                                validTiles.remove(removeNextWinnable);
                            }
                        }
                    }
                    if(validTiles.size() > nextCPUWinnable.size()){
                        if(validTiles.size() > nextCPUWinnable.size()){
                            for (Integer removeNextWinnable : nextCPUWinnable) {
                                validTiles.remove(removeNextWinnable);
                            }
                        }
                    }
                    //DON'T MOVE TO CPU

                    choiceTile = validTiles.get(random.nextInt(validTiles.size()));
                }
            }
        }


        //CPU AI end

        grid[choiceBoard][choiceTile].setText("X");
        ultGame.miniBoard.get(choiceBoard).setTile(choiceTile,-1);

        previousBoard = choiceBoard;
        if(!ultGame.emptyTile(choiceTile)){
            currentBoard = -1;
        }
        else currentBoard = choiceTile;

        DrawBoard();
    }

    public void DisableBoard(){
        for(int i = 0; i < 9; i++){
            if(i != currentBoard) {
                for(int j = 0; j < NUM_TILES; j++) {
                    grid[i][j].setClickable(false);
                    grid[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
            else if(ultGame.gameStatus(i) == 0){
                for(int j = 0; j < NUM_TILES; j++){
                    if(ultGame.miniBoard.get(i).emptyTile(j)) {
                        grid[i][j].setClickable(true);
                    }
                    grid[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
    }

    public void EnableBoard(){
        for(int i = 0; i < 9; i++){
            if(ultGame.gameStatus(i) == 0){
                for(int j = 0; j < NUM_TILES; j++){
                    if(ultGame.miniBoard.get(i).emptyTile(j)) {
                        grid[i][j].setClickable(true);
                    }
                    grid[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
    }

    public void CompletedBoards(){
        for(int i = 0; i < NUM_TILES; i++){
            switch (ultGame.gameStatus(i)){
                case 1:
                    ultGame.setTile(i,1);
                    grid[i][1].getBackground().setColorFilter(Color.GREEN,PorterDuff.Mode.MULTIPLY);
                    grid[i][3].getBackground().setColorFilter(Color.GREEN,PorterDuff.Mode.MULTIPLY);
                    grid[i][5].getBackground().setColorFilter(Color.GREEN,PorterDuff.Mode.MULTIPLY);
                    grid[i][7].getBackground().setColorFilter(Color.GREEN,PorterDuff.Mode.MULTIPLY);

                    break;
                case -1:
                    ultGame.setTile(i,-1);
                    grid[i][0].getBackground().setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY);
                    grid[i][2].getBackground().setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY);
                    grid[i][4].getBackground().setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY);
                    grid[i][6].getBackground().setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY);
                    grid[i][8].getBackground().setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY);
                    break;
                case 2:
                    ultGame.setTile(i,2);
                    for(int j = 0; j < 9; j++){
                        if(j%2 == 0)
                            grid[i][j].getBackground().setColorFilter(Color.GREEN,PorterDuff.Mode.MULTIPLY);
                        else
                            grid[i][j].getBackground().setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY);
                    }
                    break;
            }

        }
    }

    public void DrawBoard() {
        //if (DEBUG) currentBoard = -1;
        if (GameOver()) {
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++){
                    grid[i][j].setClickable(false);
                    grid[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
            CompletedBoards();

        } else {
            if (currentBoard < 0) EnableBoard();
            else DisableBoard();
            CompletedBoards();
            if (previousBoard >= 0)
                grid[previousBoard][choiceTile].getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        }
    }

    public boolean GameOver() {
        boolean gameover = false;
        Toast toast;
        switch (ultGame.gameStatus()) {
            case 1:
                toast = Toast.makeText(getApplicationContext(),"You Win!", Toast.LENGTH_SHORT);
                toast.show();
                gameover = true;
                break;
            case -1:
                toast = Toast.makeText(getApplicationContext(),"You lose!", Toast.LENGTH_SHORT);
                toast.show();
                gameover = true;
                break;
        }
        return gameover;
    }

    public void Reset(View view) {
//        currentBoard = -1;
//        previousBoard = -1;
//        choiceTile = 0;
//        for(int i = 0; i < 9; i++) {
//            for(int j = 0; j < 9; j++){
//                grid[i][j].setClickable(true);
//                grid[i][j].setText("");
//                grid[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
//                ultGame.miniBoard.get(i).setTile(j,0);
//            }
//            ultGame.setTile(i,0);
//        }
        Reset();
    }

    public void Reset() {
        currentBoard = -1;
        previousBoard = -1;
        choiceTile = 0;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++){
                grid[i][j].setClickable(true);
                grid[i][j].setText("");
                grid[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                ultGame.miniBoard.get(i).setTile(j,0);
            }
            ultGame.setTile(i,0);
        }
        boolean playerTurn = random.nextBoolean();
        if(!playerTurn) CPUTurn();
    }
}


