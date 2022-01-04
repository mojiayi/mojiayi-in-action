package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.sort.impl.BubbleSortingExclusiveOr;
import com.mojiayi.action.algorithm.sort.impl.BubbleSortingNoTempDataSwap;
import com.mojiayi.action.algorithm.sort.impl.InsertSortingRememberInsertPoint;
import com.mojiayi.action.algorithm.sort.impl.InsertSortingTraditional;
import com.mojiayi.action.algorithm.sort.impl.SelectionSortingOnlyOneArray;
import com.mojiayi.action.algorithm.sort.impl.SelectionSortingTraditional;
import com.mojiayi.action.algorithm.sort.impl.BubbleSortingTraditionalSwap;
import org.junit.Assert;
import org.junit.Test;

public class SortingTest {
    private static final int[] inputData = {7, 4, 8, 2, 3, 5, 6, 1};

    @Test
    public void testBubbleSortingTraditionalSwap() {
        BubbleSortingTraditionalSwap sortingAlgorithm = new BubbleSortingTraditionalSwap();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testBubbleSortingNoTempDataSwap() {
        BubbleSortingNoTempDataSwap sortingAlgorithm = new BubbleSortingNoTempDataSwap();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testBubbleSortingExclusiveOr() {
        BubbleSortingExclusiveOr sortingAlgorithm = new BubbleSortingExclusiveOr();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testInsertSortingTraditional() {
        InsertSortingTraditional sortingAlgorithm = new InsertSortingTraditional();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testInsertSortingRememberInsertPoint() {
        InsertSortingRememberInsertPoint sortingAlgorithm = new InsertSortingRememberInsertPoint();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testSelectionSortingTraditional() {
        SelectionSortingTraditional sortingAlgorithm = new SelectionSortingTraditional();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testSelectionSortingOnlyOneArray() {
        SelectionSortingOnlyOneArray sortingAlgorithm = new SelectionSortingOnlyOneArray();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }
    private void verifySortResult(int[] sortedData) {
        int expected = 1;
        Assert.assertEquals(expected, sortedData[expected - 1]);
        expected = 3;
        Assert.assertEquals(expected, sortedData[expected - 1]);
        expected = 5;
        Assert.assertEquals(expected, sortedData[expected - 1]);
        expected = 7;
        Assert.assertEquals(expected, sortedData[expected - 1]);
        expected = 6;
        Assert.assertEquals(expected, sortedData[expected - 1]);
    }
}
