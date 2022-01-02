package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.sort.impl.ExclusiveOrBubbleSorting;
import com.mojiayi.action.algorithm.sort.impl.NoTempDataSwapBubbleSorting;
import com.mojiayi.action.algorithm.sort.impl.TraditionalInsertSorting;
import com.mojiayi.action.algorithm.sort.impl.TraditionalSwapBubbleSorting;
import org.junit.Assert;
import org.junit.Test;

public class SortingTest {
    private static final int[] inputData = {7, 4, 8, 2, 3, 5, 6, 1};

    @Test
    public void testTraditionalBubbleSorting() {
        TraditionalSwapBubbleSorting sortingAlgorithm = new TraditionalSwapBubbleSorting();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testNoTempDataBubbleSorting() {
        NoTempDataSwapBubbleSorting sortingAlgorithm = new NoTempDataSwapBubbleSorting();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testExclusiveOrBubbleSorting() {
        ExclusiveOrBubbleSorting sortingAlgorithm = new ExclusiveOrBubbleSorting();
        int[] sortedData = sortingAlgorithm.sort(inputData);
        verifySortResult(sortedData);
    }

    @Test
    public void testTraditionalInsertSorting() {
        TraditionalInsertSorting sortingAlgorithm = new TraditionalInsertSorting();
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
