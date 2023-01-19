package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.entity.Page;

import java.util.List;

public class PaginationUtil {
    public static <T> Page pagination(List<T> list, Integer pageNum, Integer pageSize, Integer total) {
        Page page = new Page();
        page.setTotal(total);
        if(pageSize == 0) {
            page.setRecords(list);
            return page;
        }
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());
        page.setRecords(list.subList(fromIndex, toIndex));
        return page;
    }
}
