package com.example.umc9th.global.apiPayload.exception.handler;

import com.example.umc9th.global.apiPayload.code.BaseErrorCode;
import com.example.umc9th.global.apiPayload.exception.GeneralException;

public class ErrorHandler extends GeneralException {
    public ErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
