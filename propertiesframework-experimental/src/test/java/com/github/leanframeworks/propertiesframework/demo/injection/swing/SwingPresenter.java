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

package com.github.leanframeworks.propertiesframework.demo.injection.swing;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.PostInject;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.Require;
import com.github.leanframeworks.propertiesframework.swing.property.ComponentEnabledProperty;
import com.github.leanframeworks.propertiesframework.swing.property.JLabelTextProperty;
import com.github.leanframeworks.propertiesframework.swing.property.JTextComponentTextProperty;

import static com.github.leanframeworks.propertiesframework.base.binding.Binder.from;

/**
 * Swing version of the presenter.
 * <p>
 * It merely binds the properties injected from the presentation model to the view components.
 */
public class SwingPresenter {

    @Require("enterUsernameAllowed")
    private ReadableProperty<Boolean> enterUsernameAllowed;

    @Require("username")
    private WritableProperty<String> username;

    @Require("enterPasswordAllowed")
    private ReadableProperty<Boolean> enterPasswordAllowed;

    @Require("password")
    private WritableProperty<String> password;

    @Require("loginAllowed")
    private ReadableProperty<Boolean> loginAllowed;

    @Require("loggingIn")
    private ReadableProperty<Boolean> loggingIn;

    @Require("loggedIn")
    private ReadableProperty<Boolean> loggedIn;

    @Require("logoutAllowed")
    private ReadableProperty<Boolean> logoutAllowed;

    @Require("status")
    private ReadableProperty<String> status;

    @Require("doLogin")
    private Runnable doLogin;

    @Require("doLogout")
    private Runnable doLogout;

    private SwingView view = null;

    public SwingView getView() {
        return view;
    }

    @PostInject
    private void initialize() {
        view = new SwingView();

        // Username
        from(new JTextComponentTextProperty(view.getUsernameTextField())).to(username);
        from(enterUsernameAllowed).to(new ComponentEnabledProperty(view.getUsernameTextField()));

        // Password
        from(new JTextComponentTextProperty(view.getPasswordField())).to(password);
        from(enterPasswordAllowed).to(new ComponentEnabledProperty(view.getPasswordField()));

        // Log in
        view.getLoginButton().addActionListener(e -> doLogin.run());
        from(loginAllowed).to(new ComponentEnabledProperty(view.getLoginButton()));

        // Log out
        view.getLogoutButton().addActionListener(e -> doLogout.run());
        from(logoutAllowed).to(new ComponentEnabledProperty(view.getLogoutButton()));

        // Status
        from(status).to(new JLabelTextProperty(view.getStatusLabel()));
    }
}
