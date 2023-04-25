package kh.gov.camdx.disig.wrapperservice.utility;

import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class ErrorLogUtility {
    public ErrorLogUtility() {
    }

    public String getErrorStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String errorStackTrace = stringWriter.toString();
        printWriter.close();
        return errorStackTrace;
    }
}