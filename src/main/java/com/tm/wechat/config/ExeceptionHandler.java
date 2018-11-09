package com.tm.wechat.config;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by LEO on 16/10/11.
 */
@ControllerAdvice
public class ExeceptionHandler {
    @ResponseStatus(value= HttpStatus.OK)
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Message> handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        Message message = new Message(MessageType.MSG_TYPE_ERROR, "非法参数异常");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.OK)
    @ExceptionHandler(value = MultipartException.class)
    @ResponseBody
    public ResponseEntity<Message> handleMultipartException(HttpServletRequest req, MultipartException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        Message message = new Message(MessageType.MSG_TYPE_ERROR, "文件上传异常");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.OK)
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseBody
    public ResponseEntity<Message> handleIllegalStateException(HttpServletRequest req, IllegalStateException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        Message message = new Message(MessageType.MSG_TYPE_ERROR, "非法状态异常");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.OK)
    @ExceptionHandler(value = FileUploadBase.SizeLimitExceededException.class)
    @ResponseBody
    public ResponseEntity<Message> handleSizeLimitExceededException(HttpServletRequest req, FileUploadBase.SizeLimitExceededException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        Message message = new Message(MessageType.MSG_TYPE_ERROR, "文件大小超出限制");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.OK)
    @ExceptionHandler(value = SQLException.class)
    @ResponseBody
    public ResponseEntity<Message> handleSQLException(HttpServletRequest req, IllegalArgumentException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        Message message = new Message(MessageType.MSG_TYPE_ERROR, "数据库操作异常");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
    @ResponseStatus(value= HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    private ResponseEntity<Message> illegalPostParamsExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        StringBuffer sb = new StringBuffer();
        errors.forEach(item -> sb.append(item.getDefaultMessage()).append(","));
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,
                sb.toString()), HttpStatus.OK);
    }
}

