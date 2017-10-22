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

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Swing version of the view.
 * <p>
 * It merely instantiates the components, configures them and lays them out.
 */
public class SwingView {

    private final JFrame frame;

    private final JTextField usernameTextField;

    private final JPasswordField passwordField;

    private final JButton loginButton;

    private final JButton logoutButton;

    private final JLabel statusLabel;

    public SwingView() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Example");

        JPanel contentPane = new JPanel(new MigLayout("fill, wrap 2",
                "[pref]related[]",
                "[pref]related[]unrelated[]unrelated[]"));
        frame.setContentPane(contentPane);

        contentPane.add(new JLabel("Username:"));
        usernameTextField = new JTextField();
        usernameTextField.setColumns(10);
        contentPane.add(usernameTextField);

        contentPane.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        contentPane.add(passwordField);

        statusLabel = new JLabel("Status");
        contentPane.add(statusLabel, "span 2");

        logoutButton = new JButton("Log out");
        contentPane.add(logoutButton, "left");
        loginButton = new JButton("Log in");
        contentPane.add(loginButton, "right");

        frame.pack();
        frame.setResizable(false);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}
