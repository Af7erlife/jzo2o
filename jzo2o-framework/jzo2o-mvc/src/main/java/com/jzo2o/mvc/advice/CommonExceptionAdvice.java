package com.jzo2o.mvc.advice;

import cn.hutool.core.collection.CollUtil;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.utils.*;
import com.jzo2o.mvc.constants.HeaderConstants;
import com.jzo2o.mvc.model.Result;
import com.jzo2o.mvc.utils.RequestUtils;
import com.jzo2o.mvc.utils.ResponseUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.jzo2o.mvc.constants.HeaderConstants.BODY_PROCESSED;

/**
 * @author itcast
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {


    /**
     * 捕获feign异常
     * @param e
     * @return
     */
    @ExceptionHandler({FeignException.class})
    public Result feignException(FeignException e) {
        ResponseUtils.setResponseHeader(BODY_PROCESSED, "1");
        Object headerValue = e.responseHeaders().get(HeaderConstants.INNER_ERROR);

        if(RequestUtils.getRequest().getRequestURL().toString().contains("/inner/")) {
            // 内部接口调用内部接口，异常抛出
            if(ObjectUtils.isNull(headerValue)) {
                throw new CommonException(ErrorInfo.Msg.REQUEST_FAILD);
            }else {
                String encodeMsg = JsonUtils.parseArray(headerValue).getStr(0);
                String[] msgs = Base64Utils.decodeStr(encodeMsg).split("\\|");
                throw new CommonException(NumberUtils.parseInt(msgs[0]), msgs[1]);
            }
        }else {
            // 外部接口调用内部接口异常捕获
            if(ObjectUtils.isNull(headerValue)) {
                return Result.error(ErrorInfo.Msg.REQUEST_FAILD);
            }else {
                String encodeMsg = JsonUtils.parseArray(headerValue).getStr(0);
                String[] msgs = Base64Utils.decodeStr(encodeMsg).split("\\|");
                return Result.error(NumberUtils.parseInt(msgs[0]), msgs[1]);
            }
        }
    }

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler({CommonException.class})
    public Result customException(CommonException e) {
        log.error("请求异常，message:{},e", e.getMessage(),e);
        // 标识异常已被处理
        ResponseUtils.setResponseHeader(BODY_PROCESSED, "1");
        if(RequestUtils.getRequest().getRequestURL().toString().contains("/inner/")) {
            CommonException commonException = new CommonException(e.getCode(), e.getMessage());
            ResponseUtils.setResponseHeader(HeaderConstants.INNER_ERROR, Base64Utils.encodeStr(e.getCode() + "|" + e.getMessage()));
            throw commonException;
        }
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("[MethodArgumentNotValidExceptionHandler]", ex);
        ResponseUtils.setResponseHeader(BODY_PROCESSED, "1");
        String errorMessage = null;
        if(RequestUtils.getRequest().getRequestURL().toString().contains("/inner/")) {
            CommonException commonException = new CommonException(ErrorInfo.Msg.REQUEST_FAILD);

            ResponseUtils.setResponseHeader(HeaderConstants.INNER_ERROR, Base64Utils.encodeStr( "500|" + ErrorInfo.Msg.REQUEST_FAILD));
            throw commonException;
        }
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            // 组合校验，参考自 https://t.zsxq.com/3HVTx
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                errorMessage = allErrors.get(0).getDefaultMessage();
            }
        } else {
            errorMessage = fieldError.getDefaultMessage();
        }
        return Result.error(String.format("请求参数不正确:%s",errorMessage ));
    }

    /**
     * 非自定义异常处理
     * @param e 异常
     * @return
     */
    @ExceptionHandler({Exception.class})
    public Result noCustomException(Exception e) {
        log.error("请求异常，", e);
        // 标识异常已被处理
        ResponseUtils.setResponseHeader(BODY_PROCESSED, "1");
        if(RequestUtils.getRequest().getRequestURL().toString().contains("/inner/")) {
            CommonException commonException = new CommonException(ErrorInfo.Msg.REQUEST_FAILD);

            ResponseUtils.setResponseHeader(HeaderConstants.INNER_ERROR, Base64Utils.encodeStr( "500|" + ErrorInfo.Msg.REQUEST_FAILD));
            throw commonException;
        }
        return Result.error(ErrorInfo.Msg.REQUEST_FAILD);
    }

}
