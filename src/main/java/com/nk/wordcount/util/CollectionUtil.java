package com.nk.wordcount.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author Created by niuyang on 2018/5/10.
 */
public class CollectionUtil {

    /**
     * 判断集合是否为空
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    /**
     * 分页处理idList
     *
     * @param idList      id列表
     * @param pageSize    每页大小
     * @param pageProcess 回调
     */
    public static <T> void split(List<T> idList, int pageSize, PageProcess<T> pageProcess) {
        int totalPage = (idList.size() + pageSize - 1) / pageSize;
        for (int i = 0; i < totalPage; i++) {
            int fromIndex = i * pageSize;
            int toIndex = Math.min((i + 1) * pageSize, idList.size());
            List<T> pageIdList = idList.subList(fromIndex, toIndex);
            pageProcess.process(pageIdList);
        }
    }

    /**
     * 分页回调
     *
     * @param <T>
     */
    public static interface PageProcess<T> {

        /**
         * 分页处理
         *
         * @param pageIdList
         */
        public void process(List<T> pageIdList);
    }
}
