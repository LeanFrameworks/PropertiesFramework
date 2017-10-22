/*-
 * #%L
 * PropertiesFramework :: Experiments
 * %%
 * Copyright (C) 2017 LeanFrameworks
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package com.github.leanframeworks.propertiesframework.demo.injection.common;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.ReadableWritableProperty;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeA;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRO;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.ExposeRW;
import com.github.leanframeworks.propertiesframework.base.property.CompositeReadableProperty;
import com.github.leanframeworks.propertiesframework.base.property.simple.SimpleProperty;

import static com.github.leanframeworks.propertiesframework.base.binding.Binder.from;
import static com.github.leanframeworks.propertiesframework.base.binding.Bindings.and;
import static com.github.leanframeworks.propertiesframework.base.binding.Bindings.isNotEmpty;
import static com.github.leanframeworks.propertiesframework.base.binding.Bindings.not;

/**
 * Presentation model.
 * <p>
 * It exposes all properties representing the state of the UI, as well as actions that can potentially mutate the state.
 * <p>
 * Consumers of the exposed properties are typically the presenters and other models. Consumers of the exposed actions
 * are typically the presenters.
 */
public class LoginModel {

    @ExposeRW("username")
    private final ReadableWritableProperty<String> username = new SimpleProperty<>("");

    @ExposeRW("password")
    private final ReadableWritableProperty<String> password = new SimpleProperty<>("");

    @ExposeRO("enterUsernameAllowed")
    @ExposeRO("enterPasswordAllowed")
    private final ReadableProperty<Boolean> enterCredentialsAllowed;

    @ExposeRO("loggingIn")
    private final ReadableWritableProperty<Boolean> loggingIn = new SimpleProperty<>(false);

    @ExposeRO("loggedIn")
    @ExposeRO("logoutAllowed")
    private final ReadableWritableProperty<Boolean> loggedIn = new SimpleProperty<>(false);

    @ExposeRO("loginAllowed")
    private final ReadableProperty<Boolean> loginAllowed;

    @ExposeA("doLogin")
    private final Runnable doLogin = this::submitCredentials;

    @ExposeA("doLogout")
    private final Runnable doLogout = this::logout;

    @ExposeRO("status")
    private final ReadableWritableProperty<String> status = new SimpleProperty<>("");

    public LoginModel() {
        enterCredentialsAllowed = and(not(loggingIn), not(loggedIn));
        loginAllowed = and(isNotEmpty(username), isNotEmpty(password), not(loggingIn), not(loggedIn));

        from(new CompositeReadableProperty<>(loggingIn, loggedIn))
                .transform(i -> {
                    String status;
                    if (loggingIn.getValue()) {
                        status = "Logging in...";
                    } else if (loggedIn.getValue()) {
                        status = "Logged in as '" + username.getValue() + "'";
                    } else {
                        status = "Logged out";
                    }
                    return status;
                })
                .to(status);
    }

    private void submitCredentials() {
        loggingIn.setValue(true);
        loggedIn.setValue(true);
        loggingIn.setValue(false);
    }

    private void logout() {
        loggedIn.setValue(false);
    }
}
