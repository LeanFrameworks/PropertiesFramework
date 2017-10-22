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

package com.github.leanframeworks.propertiesframework.demo.injection.javafx;

import com.github.leanframeworks.propertiesframework.api.property.ReadableProperty;
import com.github.leanframeworks.propertiesframework.api.property.WritableProperty;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.PostInject;
import com.github.leanframeworks.propertiesframework.base.injection.annotation.Require;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.github.leanframeworks.propertiesframework.base.binding.Bindings.not;
import static com.github.leanframeworks.propertiesframework.javafx.binding.FXBindings.fx;

/**
 * JavaFX version of the presenter.
 * <p>
 * It merely binds the properties injected from the presentation model to the view components.
 */
public class JavaFXLoginPresenter {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label statusLabel;

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

    @FXML
    private void initialize() {
        System.out.println("JavaFXLoginPresenter.initialize");
    }

    @PostInject
    public void postInitialize() {
        // Username
        usernameTextField.textProperty().addListener((p, o, n) -> username.setValue(n));
        usernameTextField.disableProperty().bind(fx(not(enterUsernameAllowed)));

        // Password
        passwordField.textProperty().addListener((p, o, n) -> password.setValue(n));
        passwordField.disableProperty().bind(fx(not(enterPasswordAllowed)));

        // Log in
        loginButton.disableProperty().bind(fx(not(loginAllowed)));

        // Log out
        logoutButton.disableProperty().bind(fx(not(logoutAllowed)));

        // Status
        statusLabel.textProperty().bind(fx(status));
    }

    @FXML
    private void doLogin() {
        doLogin.run();
    }

    @FXML
    private void doLogout() {
        doLogout.run();
    }
}
