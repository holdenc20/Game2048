package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

public class Game2048 extends World {
  int dimensions = 4;
  ArrayList<ArrayList<Integer>> tiles = new ArrayList<>();
  Random r = new Random();
  boolean simulation;
  ArrayList<Game2048> children = new ArrayList<Game2048>();

  int val;
  int oneDirectional;
  int empty;
  int smoothness;

  Game2048(boolean simulation, int val, int oneDirectional, int empty, int smoothness) {
    this.val = val;
    this.oneDirectional = oneDirectional;
    this.empty = empty;
    this.smoothness = smoothness;
    this.populateTiles();
    //this.makeScene();
    this.simulation = simulation;
  }

  Game2048(boolean simulation, ArrayList<ArrayList<Integer>> tiles, int val, int oneDirectional,
      int empty, int smoothness) {
    this.val = val;
    this.oneDirectional = oneDirectional;
    this.empty = empty;
    this.smoothness = smoothness;
    if (!simulation) {
      //this.populateTiles();
      //this.makeScene();
    }
    this.tiles = new ArrayList<>();
    this.simulation = simulation;
    for (int row = 0; row < this.dimensions; row++) {
      ArrayList<Integer> temp = new ArrayList<>();
      for (int col = 0; col < this.dimensions; col++) {
        temp.add(tiles.get(row).get(col));
      }
      this.tiles.add(temp);
    }
  }

  public int hashCode() {
    int count = 0;
    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col < this.dimensions; col++) {
        count = count + tiles.get(row).get(col) * (row + 1) * (col + 1);
      }
    }
    return count;
  }

  public boolean equals(Object o) {
    return this.hashCode() == o.hashCode();
  }

  public WorldImage draw(int value) {
    Color color = Color.gray;
    if (value == 0) {
      color = Color.gray;
    }
    if (value == 2) {
      color = Color.yellow;
    }
    else if (value == 4) {
      color = Color.orange;
    }
    else if (value == 8) {
      color = Color.pink;
    }
    else if (value == 16) {
      color = Color.red;
    }
    else if (value == 32) {
      color = Color.magenta;
    }
    else if (value == 64) {
      color = Color.blue;
    }
    else if (value == 128) {
      color = Color.cyan;
    }
    else if (value == 256) {
      color = Color.green;
    }
    else if (value == 512) {
      color = Color.red;
    }
    if (value == 1024) {
      color = Color.yellow;
    }
    else if (value == 2048) {
      color = Color.orange;
    }
    else if (value == 4096) {
      color = Color.pink;
    }
    return new OverlayImage(new TextImage("" + value, Color.black),
        new RectangleImage(100, 100, OutlineMode.SOLID, color));
  }

  private void populateTiles() {
    int tile1 = r.nextInt(this.dimensions * this.dimensions);
    int tile1r = tile1 / this.dimensions;
    int tile1c = tile1 % this.dimensions;

    int tileNum1 = r.nextInt(2);
    int tile2 = r.nextInt(this.dimensions * this.dimensions);

    int tileNum2 = r.nextInt(2);
    while (tile1 == tile2) {
      tile2 = r.nextInt(this.dimensions * this.dimensions);
    }
    int tile2r = tile2 / this.dimensions;
    int tile2c = tile2 % this.dimensions;
    // 0  1  2  3
    // 4  5  6  7
    // 8  9  10 11
    // 12 13 14 15

    for (int row = 0; row < this.dimensions; row++) {
      ArrayList<Integer> temp = new ArrayList<>();
      for (int col = 0; col < this.dimensions; col++) {
        if (tile1r == row && tile1c == col) {
          temp.add(2 * (tileNum1 + 1));
        }
        else if (tile2r == row && tile2c == col) {
          temp.add(2 * (tileNum2 + 1));
        }
        else {
          temp.add(0);
        }
      }
      tiles.add(temp);
    }
  }

  public WorldScene makeScene() {

    WorldImage grid = new EmptyImage();
    WorldScene s = this.getEmptyScene();
    for (ArrayList<Integer> rowTiles : tiles) {
      WorldImage tempImage = new EmptyImage();
      for (Integer tile : rowTiles) {
        tempImage = new BesideImage(tempImage, this.draw(tile));
      }
      grid = new AboveImage(grid, tempImage);
    }
    s.placeImageXY(grid, 80 * 2 / 3 * this.dimensions, 80 * 2 / 3 * this.dimensions);
    return s;

  }

  public void onTick() {
    this.generateBestMove();
  }

  public void onKeyEvent(String s) {
    //move down
    if (s.equals("w") || s.equals("up")) {
      boolean tileChanged = false;
      for (int col = 0; col < this.dimensions; col++) {
        for (int row = this.dimensions - 1; row >= 0; row--) {
          for (int row2 = row - 1; row2 >= 0; row2--) {
            //when the tile is 0
            if (tiles.get(row).get(col).equals(0)) {
              if (!tiles.get(row2).get(col).equals(0)) {
                tileChanged = true;
              }
              tiles.get(row).set(col, tiles.get(row2).get(col));
              tiles.get(row2).set(col, 0);

            }
            //when the tiles are the same they combine
            else if (tiles.get(row2).get(col).equals(tiles.get(row).get(col))) {
              tiles.get(row).set(col, tiles.get(row2).get(col) * 2);
              tiles.get(row2).set(col, 0);
              row2 = -10; // to end the loop
              tileChanged = true;
            }
            else if (!tiles.get(row2).get(col).equals(0)) {
              row2 = -10; // to end the loop
            }
          }
        }
      }
      if (tileChanged && !this.simulation) {
        this.addNewTile();
      }
    }

    //move up
    if (s.equals("s") || s.equals("down")) {
      boolean tileChanged = false;
      for (int col = 0; col < this.dimensions; col++) {
        for (int row = 0; row < this.dimensions - 1; row++) {
          for (int row2 = row + 1; row2 < this.dimensions; row2++) {
            //when the tile is 0
            if (tiles.get(row).get(col).equals(0)) {
              if (!tiles.get(row2).get(col).equals(0)) {
                tileChanged = true;
              }
              tiles.get(row).set(col, tiles.get(row2).get(col));
              tiles.get(row2).set(col, 0);
            }
            //when the tiles are the same they combine
            else if (tiles.get(row2).get(col).equals(tiles.get(row).get(col))) {
              tiles.get(row).set(col, tiles.get(row2).get(col) * 2);
              tiles.get(row2).set(col, 0);
              row2 = 10; // to end the loop
              tileChanged = true;
            }
            else if (!tiles.get(row2).get(col).equals(0)) {
              row2 = 10; // to end the loop
            }
          }
        }
      }
      if (tileChanged && !this.simulation) {
        this.addNewTile();
      }
    }
    //move right
    if (s.equals("d") || s.equals("right")) {
      boolean tileChanged = false;
      for (int row = 0; row < this.dimensions; row++) {
        for (int col = this.dimensions - 1; col >= 0; col--) {
          for (int col2 = col - 1; col2 >= 0; col2--) {
            //when the tile is 0
            if (tiles.get(row).get(col).equals(0)) {
              if (!tiles.get(row).get(col2).equals(0)) {
                tileChanged = true;
              }
              tiles.get(row).set(col, tiles.get(row).get(col2));
              tiles.get(row).set(col2, 0);
            }
            //when the tiles are the same they combine
            else if (tiles.get(row).get(col2).equals(tiles.get(row).get(col))) {
              tiles.get(row).set(col, tiles.get(row).get(col2) * 2);
              tiles.get(row).set(col2, 0);
              col2 = -10; // to end the loop
              tileChanged = true;
            }
            else if (!tiles.get(row).get(col2).equals(0)) {
              col2 = -10; // to end the loop
            }
          }
        }
      }
      if (tileChanged && !this.simulation) {
        this.addNewTile();
      }
    }

    //move left
    if (s.equals("a") || s.equals("left")) {
      boolean tileChanged = false;
      for (int row = 0; row < this.dimensions; row++) {
        for (int col = 0; col < this.dimensions - 1; col++) {
          for (int col2 = col + 1; col2 < this.dimensions; col2++) {
            //when the tile is 0
            if (tiles.get(row).get(col).equals(0)) {
              if (!tiles.get(row).get(col2).equals(0)) {
                tileChanged = true;
              }
              tiles.get(row).set(col, tiles.get(row).get(col2));
              tiles.get(row).set(col2, 0);

            }
            //when the tiles are the same they combine
            else if (tiles.get(row).get(col2).equals(tiles.get(row).get(col))) {
              tiles.get(row).set(col, tiles.get(row).get(col2) * 2);
              tiles.get(row).set(col2, 0);
              col2 = 10; // to end the loop
              tileChanged = true;
            }
            else if (!tiles.get(row).get(col2).equals(0)) {
              col2 = 10; // to end the loop
            }
          }
        }
      }
      if (tileChanged && !this.simulation) {
        this.addNewTile();
      }
    }
  }

  //counts the empty tiles and adds a new tile to the board
  private void addNewTile() {
    int emptyTiles = this.countEmptyTiles();
    int tileNum = r.nextInt(10);
    if (tileNum == 0) {
      tileNum = 4;
    }
    else {
      tileNum = 2;
    }
    int tile = r.nextInt(emptyTiles);
    int count = 0;
    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col < this.dimensions; col++) {
        if (tiles.get(row).get(col) == 0) {
          if (count == tile) {
            tiles.get(row).set(col, tileNum);
          }
          count++;
        }
      }
    }
  }

  public double generateBestMove() {
    //for the up options
    Game2048 tilesUp = new Game2048(true, tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    Game2048 tilesDown = new Game2048(true, tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    Game2048 tilesLeft = new Game2048(true, tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    Game2048 tilesRight = new Game2048(true, tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    //System.out.println("--------");

    tilesUp.onKeyEvent("w");
    tilesDown.onKeyEvent("s");
    tilesLeft.onKeyEvent("a");
    tilesRight.onKeyEvent("d");

    int countUp = 0;
    int countDown = 0;
    int countLeft = 0;
    int countRight = 0;

    if (this.equals(tilesUp) && this.equals(tilesDown) && this.equals(tilesLeft)
        && this.equals(tilesRight)) {
      //this.printMaxTile();
      //System.out.println(this.evaluateTiles()[0]);
      return this.valueOfTiles();
    }

    double averageUp = 0;
    if (this.equals(tilesUp)) {
      countUp = -10000;
    }
    else {
      averageUp = tilesUp.minimax(0, false, -1, 1000000000);

    }

    double averageDown = 0;
    if (this.equals(tilesDown)) {
      countDown = -1000;
    }
    else {
      averageDown = tilesDown.minimax(0, false, -1, 1000000000);
    }

    double averageLeft = 0;
    if (this.equals(tilesLeft)) {
      countLeft = -1000;
    }
    else {
      averageLeft = tilesLeft.minimax(0, false, -1, 1000000000);
    }

    double averageRight = 0;
    if (this.equals(tilesRight)) {
      countRight = -1000;
    }
    else {
      averageRight = tilesRight.minimax(0, false, -1, 1000000000);
    }

    if (averageUp >= averageDown) {
      countUp += 1;
    }
    else {
      countDown += 1;
    }
    if (averageUp >= averageLeft) {
      countUp += 1;
    }
    else {
      countLeft += 1;
    }
    if (averageUp > averageRight) {
      countUp += 1;
    }
    else {
      countRight += 1;
    }
    if (averageDown > averageLeft) {
      countDown += 1;
    }
    else {
      countLeft += 1;
    }
    if (averageDown > averageRight) {
      countDown += 1;
    }
    else {
      countRight += 1;
    }
    if (averageLeft > averageRight) {
      countLeft += 1;
    }
    else {
      countRight += 1;
    }
    
//    System.out.println("-------");
//    System.out.println(averageUp);
//    System.out.println(averageDown);
//    System.out.println(averageLeft);
//    System.out.println(averageRight);

    if (averageUp == Math.max(Math.max(averageUp, averageDown), Math.max(averageLeft, averageRight))) {
      //System.out.println("Up");
      this.onKeyEvent("w");
      this.setChildren();
    }
    else if (averageLeft == Math.max(Math.max(averageUp, averageDown), Math.max(averageLeft, averageRight))) {
      //System.out.println("left");
      this.onKeyEvent("a");
      this.setChildren();
    }
    else if (averageDown == Math.max(Math.max(averageUp, averageDown), Math.max(averageLeft, averageRight))) {
      //System.out.println("down");
      this.onKeyEvent("s");
      this.setChildren();
    }
    else if (averageRight == Math.max(Math.max(averageUp, averageDown), Math.max(averageLeft, averageRight))) {
      //System.out.println("right");
      this.onKeyEvent("d");
      this.setChildren();
    }

    //return this.generateBestMove();
    return 0;
  }

  private void setChildren() {
    for (int min = 0; min < this.children.size(); min++) {
      for (int max = 0; max < this.children.get(min).children.size(); max++) {
        if (this.children.get(min).children.get(max).equals(this)) {
          this.children = this.children.get(min).children.get(max).children;
          max = 999;
          min = 999;
        }
      }
    }

  }

  long minimax(int depth, Boolean maximizingPlayer, double alpha, double beta) {
    // Terminating condition. i.e
    // leaf node is reached
    Game2048 tempU2 = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    Game2048 tempD2 = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    Game2048 tempL2 = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    Game2048 tempR2 = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
        this.smoothness);
    tempU2.onKeyEvent("w");
    tempD2.onKeyEvent("s");
    tempL2.onKeyEvent("a");
    tempR2.onKeyEvent("d");
    boolean sampling = true;
    if (tempU2.equals(tempD2) && tempL2.equals(tempR2) && tempD2.equals(tempR2)) {
      return this.evaluateTiles();
      //return new double[] { 0, 0, 0, 0 };
    }
    if (depth == 5) {
      return this.evaluateTiles();
    }

    if (maximizingPlayer) {
      long best = -999999999;
      if (this.children.isEmpty()) {
        Game2048 tempU = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
            this.smoothness);
        tempU.onKeyEvent("w");
        children.add(tempU);
        if (!tempU.equals(this)) {
          long val = minimax(depth + 1, false, alpha, beta);

          if (val > best) {
            best = val;
          }
          if (best >= beta) {
            sampling = false;
          }
          else if (best > alpha) {
            alpha = best;
          }
        }

        Game2048 tempD = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
            this.smoothness);
        tempD.onKeyEvent("s");
        children.add(tempD);
        if (!tempD.equals(this) && sampling) {
          long val = minimax(depth + 1, false, alpha, beta);

          if (val > best) {
            best = val;
          }
          if (best >= beta) {
            sampling = false;
          }
          else if (best > alpha) {
            alpha = best;
          }
        }

        Game2048 tempL = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
            this.smoothness);
        tempL.onKeyEvent("a");
        children.add(tempL);
        if (!tempL.equals(this) && sampling) {
          long val = minimax(depth + 1, false, alpha, beta);

          if (val > best) {
            best = val;
          }
          if (best >= beta) {
            sampling = false;
          }
          else if (best > alpha) {
            alpha = best;
          }
        }

        Game2048 tempR = new Game2048(true, this.tiles, this.val, this.oneDirectional, this.empty,
            this.smoothness);
        children.add(tempR);
        tempR.onKeyEvent("d");
        if (!tempR.equals(this) && sampling) {
          long val = minimax(depth + 1, false, alpha, beta);

          if (val > best) {
            best = val;
          }
          if (best >= beta) {
            sampling = false;
          }
          else if (best > alpha) {
            alpha = best;
          }
        }

        return best;
      }
      else {
        for (Game2048 g : children) {
          long temp = g.minimax(depth + 1, false, alpha, beta);
          if (temp > best) {
            best = temp;
          }
          if (best >= beta) {
            return best;
          }
          else if (best > alpha) {
            alpha = best;
          }
        }
        return best;
      }
    }
    else {
      //double best = 1000000000;
      long best = 999999999;
      if (this.children.isEmpty()) {
        // Recur for left and
        // right children
        int count = 0;
        for (int row = 0; row < this.dimensions; row++) {
          for (int col = 0; col < this.dimensions; col++) {
            for (int x = 0; x < 2; x++) {
              if (this.tiles.get(row).get(col) == 0) {
                count++;
                Game2048 temp = new Game2048(true, this.tiles, this.val, this.oneDirectional,
                    this.empty, this.smoothness);
                children.add(temp);
                temp.addTileAtPosn(row, col, 2 * x + 2);
                long val = minimax(depth + 1, true, alpha, beta);

                if (val < best) {
                  best = val;
                }
                if (best <= alpha) {
                  row = this.dimensions;
                  col = row;
                  x = 3;
                }
                else if (best < alpha) {
                  beta = best;

                }
              }
            }
          }
        }
        if(count==0) {
          return this.evaluateTiles();
        }
      }

      else {
        for (Game2048 g : children) {
          long temp = g.minimax(depth + 1, true, alpha, beta);
          if (temp < best) {
            best = temp;
          }
          if (best <= alpha) {
            break;
          }
          else if (best < alpha) {
            beta = best;
          }
        }
      }

      return best;
    }
  }

  private long evaluateTiles() {
    long maxValue = 0;
    long empty = 0;
    long oneDirectional = this.evaluateDirectionality();
    long smoothness = 0;

    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col < this.dimensions; col++) {
        if (tiles.get(row).get(col) > maxValue) {
          maxValue = tiles.get(row).get(col);
        }

        if (tiles.get(row).get(col) == 0) {
          empty++;
        }

        //for smoothness
        if (tiles.get(row).get(col) != 0) {
          long value = (long) (Math.log(tiles.get(row).get(col)) / Math.log(2));
          for (int r = 0; r < this.dimensions; r++) {
            if (r != row) {
              if (tiles.get(r).get(col) != 0) {
                long target = (long) (Math.log(tiles.get(r).get(col)) / Math.log(2));
                smoothness -= Math.abs(value - target);
              }
            }
          }

          for (int c = 0; c < this.dimensions; c++) {
            if (c != col) {
              if (tiles.get(row).get(c) != 0) {
                long target = (long) (Math.log(tiles.get(row).get(c)) / Math.log(2));
                smoothness -= Math.abs(value - target);
              }
            }
          }
        }
      }
    }
    return (long) (maxValue * this.val + oneDirectional * this.oneDirectional
        + Math.log(1 + empty) * this.empty + smoothness * this.smoothness);

  }

  //determines how monotonic the grid is 
  //saw this online
  private long evaluateDirectionality() {
    long[] totals = { 0, 0, 0, 0 };
    for (int row = 0; row < this.dimensions; row++) {
      int current = 0;
      int next = current + 1;
      while (next < 4) {
        while (next < 4 && tiles.get(row).get(next) == 0) {
          next++;
        }
        if (next >= 4) {
          next--;
        }
        double currentVal = 0;
        if (tiles.get(row).get(current) != 0) {
          currentVal = Math.log((double) tiles.get(row).get(current)) / Math.log(2);
        }
        double nextVal = 0;
        if (tiles.get(row).get(next) != 0) {
          nextVal = Math.log((double) tiles.get(row).get(next)) / Math.log(2);
        }
        if (currentVal > nextVal) {
          totals[0] += nextVal - currentVal;
        }
        else if (nextVal > currentVal) {
          totals[1] += currentVal - nextVal;
        }
        current = next;
        next++;
      }
    }

    for (int col = 0; col < this.dimensions; col++) {
      int current = 0;
      int next = current + 1;
      while (next < 4) {
        while (next < 4 && tiles.get(next).get(col) == 0) {
          next++;
        }
        if (next >= 4) {
          next--;
        }
        double currentVal = 0;
        if (tiles.get(current).get(col) != 0) {
          currentVal = Math.log(tiles.get(current).get(col)) / Math.log(2);
        }
        double nextVal = 0;
        if (tiles.get(next).get(col) != 0) {
          nextVal = Math.log(tiles.get(next).get(col)) / Math.log(2);
        }
        if (currentVal > nextVal) {
          totals[2] += nextVal - currentVal;
        }
        else if (nextVal > currentVal) {
          totals[3] += currentVal - nextVal;
        }
        current = next;
        next++;
      }
    }

    return Math.max(totals[0], totals[1]) + Math.max(totals[2], totals[3]);
  }

  private void addTileAtPosn(int row, int col, int i) {
    tiles.get(row).set(col, i);
  }

  private double valueOfTiles() {
    int count = 0;
    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col < this.dimensions; col++) {
        count += tiles.get(row).get(col);

      }
    }
    return count;
  }

  private int countEmptyTiles() {
    int count = 0;
    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col < this.dimensions; col++) {
        if (tiles.get(row).get(col) == 0) {
          count++;
        }
      }
    }
    return count;
  }

  private void printB() {
    int count = 0;
    for (int row = 0; row < this.dimensions; row++) {
      for (int col = 0; col < this.dimensions; col++) {
        System.out.print(tiles.get(row).get(col));
      }
      System.out.println();
    }
  }
}