package kh.gov.camdx.disig.wrapperservice.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import kh.gov.camdx.disig.lib.dto.ApiResponse;
import kh.gov.camdx.disig.lib.exceptions.DisigException;
import kh.gov.camdx.disig.wrapperservice.utility.ErrorLogUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ResponseErrorHandlerAdvice {
    private final ErrorLogUtility errorLogUtility;

    public ResponseErrorHandlerAdvice(ErrorLogUtility errorLogUtility) {
        this.errorLogUtility = errorLogUtility;
    }

    @ExceptionHandler({DisigException.class})
    public ResponseEntity<ApiResponse> handleAddressBaseErrorObject(DisigException ex) {
        log.error(this.errorLogUtility.getErrorStackTrace(ex));
        return ResponseEntity.ok(new ApiResponse(501,ex.getMessage()));
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> handleGenericExceptionObject(Exception ex) {
        log.error(this.errorLogUtility.getErrorStackTrace(ex));
        return ResponseEntity.ok(new ApiResponse(501, ex.getMessage()));
    }

    private HashMap<String, String> getErrorFieldsFromException(Exception ex) {
        BindingResult result;
        if (ex instanceof BindException) {
            result = ((BindException)ex).getBindingResult();
        } else {
            if (!(ex instanceof WebExchangeBindException)) {
                return null;
            }

            result = ((WebExchangeBindException)ex).getBindingResult();
        }

        List<ObjectError> errorList = result.getAllErrors();
        HashMap<String, String> errorFields = new HashMap();
        Iterator var5 = errorList.iterator();

        while(var5.hasNext()) {
            ObjectError objectError = (ObjectError)var5.next();
            Class<?> objectErrorClass = objectError.getClass();
            Method getFieldMethod = null;

            try {
                getFieldMethod = objectErrorClass.getMethod("getField");
                errorFields.put((String)getFieldMethod.invoke(objectError), objectError.getDefaultMessage());
            } catch (Exception var10) {
                log.error("Scanning through error object in general response error handler advice.");
                log.error(this.errorLogUtility.getErrorStackTrace(var10));
            }

            if (getFieldMethod == null) {
                errorFields.put(objectError.getObjectName(), objectError.getDefaultMessage());
            }
        }

        return errorFields;
    }
}
