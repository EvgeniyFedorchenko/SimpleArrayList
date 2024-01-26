package com.evgeniyfedorchenko.simplearraylist;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        SimpleArrayList myList = new SimpleArrayList(10);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("q");
        myList.add("0");
        myList.add("1");
        myList.add("2");
        myList.add("3");
        myList.add("4");
        myList.add("5");
        myList.add("6");
        myList.add("7");
        myList.add("8");
        myList.add("9");
        myList.add("10");
        myList.add("11");

        myList.add(4, "999");
        myList.add("12");
        myList.add("13");
        myList.add("14");
        myList.add(8, "999");
        System.out.println(myList);
    }
}
