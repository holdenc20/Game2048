package com.company;

import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.EmptyImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

public class Main {

  public static void main(String[] args) {

    int max = -1;
    int maxVal = -1;
    int maxOneDir = -1;
    int maxEmpty = -1;
    int maxSmooth = -1;
    int count = 0;
    /*
    for (int val = 0; val < 5; val++) {
      for (int empty = 0; empty < 5; empty++) {
        for (int oneDir = 0; oneDir < 10; oneDir++) {
          for (int smth = 0; smth < 10; smth++) {
            int value = 0;
            for (int i = 0; i < 1; i++) {
              Game2048 g = new Game2048(false, val, oneDir, empty, smth);
              value += g.generateBestMove();
              //g.bigBang(00, 00, .02);
            }
            count++;
            System.out.println(count + ":" + value);
            if (value > max) {
              max = value;
              maxVal = val;
              maxOneDir = oneDir;
              maxEmpty = empty;
              maxSmooth = smth;
            }
          }
        }
      }
    }
    */
     Game2048 g = new Game2048(false, 10, 10, 27, 1);
     //Game2048 g = new Game2048(false, 0,8,6,4,0);
     g.bigBang(430, 430, .02);
     
    System.out.println("Max: " + max + ", maxVal: " + maxVal + ", maxOnedir: " + maxOneDir
        + ", maxEmpty: " + maxEmpty + ", maxSmooth: " + maxSmooth);
  }
}
