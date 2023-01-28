package com.mojiayi.action.concurrent;

import com.mojiayi.action.javabasis.concurrent.SyncDataConcurrently;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author guangri.li
 * @since 2023/1/28 21:31
 */
public class SyncDataConcurrentlyTest {
    @Test
    public void testSyncData() {
        // 初始化待同步数据
        List<String> productIdList = initProductList();
        SyncDataConcurrently instance = new SyncDataConcurrently();
        instance.syncData(productIdList);
    }

    private List<String> initProductList() {
        int size = 200;
        List<String> productIdList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            productIdList.add(UUID.randomUUID().toString().toLowerCase().replace("-", ""));
        }

        return productIdList;
    }
}
