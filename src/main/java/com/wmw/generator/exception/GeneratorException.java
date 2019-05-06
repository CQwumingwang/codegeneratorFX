package com.wmw.generator.exception;

/**
 * @ClassName GeneratorException
 * @Description TODO
 * @Author wumingwang
 * @Date 2019/5/6 9:51
 * @Version 1.0
 */
public class GeneratorException extends RuntimeException{

    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = 4069423741846231410L;

    /**
     * 错误信息
     */
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GeneratorException(){
        super();
    }

    public GeneratorException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
