package com.example.yunsystem.util;



public class ResultUtil {

    public static Result success(Object object) {
        Result result = new Result();
        result.setData(object);
        result.setCode(0);
        result.setMsg("查看成功");
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
