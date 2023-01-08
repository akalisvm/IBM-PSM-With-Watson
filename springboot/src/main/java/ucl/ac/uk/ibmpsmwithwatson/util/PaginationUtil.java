package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.entity.Page;

import java.util.List;

public class PaginationUtil {
    public static <T> Page pagination(List<T> list, Integer pageNum, Integer pageSize, Integer total) {
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());
        Page page = new Page();
        page.setRecords(list.subList(fromIndex, toIndex));
        page.setTotal(total);
        return page;
    }
}
