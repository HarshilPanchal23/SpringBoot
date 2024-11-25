package com.example.springSecurity.exception;


import com.example.springSecurity.dto.ApiResponse;
import com.example.springSecurity.dto.ErrorDTO;
import com.example.springSecurity.enums.ExceptionEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h1>GlobalExceptionHandlerController</h1>
 * <p>
 * This GlobalExceptionHandlerController will be used to handle exceptions and
 * provide custom response
 * </p>
 *
 * @author Softvan Nester
 * @version 1.0
 * @since 14-05-2020
 */
@RestControllerAdvice
public class GlobalExceptionHandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);
    
	/**
	 * <p>
	 * This handles CustomException and returns ApiResponse
	 * </p>
	 *
	 * @param req httpServletRequest request
	 * @param e   CustomException
	 * @return ResponseEntity &lt;ApiResponse&gt;
	 * @see CustomException
	 * @see ApiResponse
	 */
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleCustomException(HttpServletRequest req, CustomException e) {
      LOGGER.info("handleCustomException message:: {}",e.getMessage());
      HttpStatus httpStatus = e.getHttpStatus();
      ErrorDTO errorDTO = new ErrorDTO(httpStatus, new Date().getTime(), e.getMessage(), req.getServletPath());
      errorDTO.setError(e.getMessage());
      errorDTO.setMessage(httpStatus.name());
      ApiResponse apiResponse = new ApiResponse(httpStatus, e.getMessage(), errorDTO);
      return ResponseEntity.status(httpStatus).body(apiResponse);
    }

	/**
	 * <p>
	 * This handles AccessDeniedException and gives custom response
	 * </p>
	 *
	 * @param req httpServletRequest request
	 * @param e   AccessDeniedException
	 * @return ResponseEntity &lt;ApiResponse&gt;
	 * @see AccessDeniedException
	 * @see ApiResponse
	 */
	@ExceptionHandler(java.nio.file.AccessDeniedException.class)
	public ResponseEntity<ApiResponse> handleAccessDeniedException(HttpServletRequest req, java.nio.file.AccessDeniedException e) {
		HttpStatus httpStatus = HttpStatus.FORBIDDEN;
		ErrorDTO errorDTO = new ErrorDTO(httpStatus, new Date().getTime(), ExceptionEnum.ACCESS_DENIED.getValue(),
				req.getServletPath());
		ApiResponse apiResponse = new ApiResponse(httpStatus, e.getMessage(), errorDTO);
		return ResponseEntity.status(httpStatus).body(apiResponse);
	}

	/**
	 * <p>
	 * This handles IllegalArgumentException and gives custom response
	 * </p>
	 *
	 * @param req httpServletRequest request
	 * @param e   IllegalArgumentException
	 * @return ResponseEntity &lt;ApiResponse&gt;
	 * @see IllegalArgumentException
	 * @see ApiResponse
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> handleIllegalArgumentException(HttpServletRequest req,
			IllegalArgumentException e) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorDTO errorDTO = new ErrorDTO(httpStatus, new Date().getTime(),
				ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), req.getServletPath());
		ApiResponse apiResponse = new ApiResponse(httpStatus, e.getMessage(), errorDTO);
		return ResponseEntity.status(httpStatus).body(apiResponse);
	}

	/**
	 * <p>
	 * This handles Exception and gives custom response
	 * </p>
	 *
	 * @param req httpServletRequest request
	 * @param e   Exception
	 * @return ResponseEntity &lt;ApiResponse&gt;
	 * @see Exception
	 * @see ApiResponse
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleException(HttpServletRequest req, Exception e) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorDTO errorDTO = new ErrorDTO(httpStatus, new Date().getTime(),
				ExceptionEnum.SOMETHING_WENT_WRONG.getValue(), req.getServletPath());
		ApiResponse apiResponse = new ApiResponse(httpStatus, e.getMessage(), errorDTO);
		return ResponseEntity.status(httpStatus).body(apiResponse);
	}

	/**
	 * <p>
	 * This handles MethodArgumentNotValidException and gives custom response which
	 * contains list of errors
	 * </p>
	 *
	 * @param req httpServletRequest request
	 * @param e   MethodArgumentNotValidException
	 * @return ResponseEntity &lt;ApiResponse&gt;
	 * @see MethodArgumentNotValidException
	 * @see ApiResponse
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(HttpServletRequest req,
			MethodArgumentNotValidException e) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		List<String> errorMessages = new ArrayList<>();
		e.getBindingResult().getAllErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
		ErrorDTO errorDTO = new ErrorDTO(httpStatus, new Date().getTime(), httpStatus.name(), req.getServletPath(),
				errorMessages);
		ApiResponse apiResponse = new ApiResponse(httpStatus, httpStatus.name(), errorDTO);
		return ResponseEntity.status(httpStatus).body(apiResponse);
	}

	/**
	 * <p>
	 * This handles PropertyReferenceException and gives custom response
	 * </p>
	 *
	 * <p>
	 * This exception occurs when we receive illegal column from user/ui side
	 * </p>
	 *
	 * @param req httpServletRequest request
	 * @param e   PropertyReferenceException
	 * @return ResponseEntity &lt;ApiResponse&gt;
	 * @see PropertyReferenceException
	 * @see ApiResponse
	 */
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<ApiResponse> handlePropertyReferenceException(HttpServletRequest req,
			PropertyReferenceException e) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		String message = String.format(ExceptionEnum.NO_PROPERTY_WITH_NAME_FOUND_IN_DATABASE.getValue(),
				e.getPropertyName());
		ErrorDTO errorDTO = new ErrorDTO(httpStatus, new Date().getTime(), message, req.getServletPath());
		ApiResponse apiResponse = new ApiResponse(httpStatus, message, errorDTO);
		return ResponseEntity.status(httpStatus).body(apiResponse);
	}
}
