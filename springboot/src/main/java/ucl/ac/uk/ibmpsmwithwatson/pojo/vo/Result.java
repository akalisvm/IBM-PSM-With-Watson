package ucl.ac.uk.ibmpsmwithwatson.pojo.vo;

import lombok.Data;

@Data
public class Result<T> {

    private String code;
    private String msg;
    private T data;

    public Result() {}

    public Result(T data) { this.data = data; }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode("10000");
        result.setMsg("success");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode("10000");
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
