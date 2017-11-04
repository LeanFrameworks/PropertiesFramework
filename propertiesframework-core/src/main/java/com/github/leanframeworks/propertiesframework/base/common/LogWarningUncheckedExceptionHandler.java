/*
 * Copyright (c) 2017, LeanFrameworks
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.leanframeworks.propertiesframework.base.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unchecked exception handler logging an warning message to handled the runtime exceptions and errors.
 */
public class LogWarningUncheckedExceptionHandler implements ThrowableHandler<Throwable> {

    /**
     * Logger to be used.
     */
    private final Logger logger;

    /**
     * Constructor using a default logger.
     */
    public LogWarningUncheckedExceptionHandler() {
        this.logger = LoggerFactory.getLogger(LogWarningUncheckedExceptionHandler.class);
    }

    /**
     * Constructor.
     *
     * @param logger Logger to be used.
     */
    public LogWarningUncheckedExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    /**
     * @see ThrowableHandler#handleThrowable(Throwable)
     */
    @Override
    public void handleThrowable(Throwable throwable) {
        logger.warn("An exception or error occurred", throwable);
    }
}
