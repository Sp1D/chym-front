/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront.controllers;

import java.util.Properties;
import net.sp1d.chym.entities.User;
import net.sp1d.chymfront.Settings;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author sp1d
 */
@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return User.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required", "Username is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "Email is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required", "Password is required");

        Properties p = Settings.getSettings();
        int usernameMin = 4, usernameMax = 30, passwordMin = 6, passwordMax = 30;

        try {
            usernameMin = Integer.valueOf(p.getProperty("user.username.minsize"));
            usernameMax = Integer.valueOf(p.getProperty("user.username.maxsize"));
            passwordMin = Integer.valueOf(p.getProperty("user.password.minsize"));
            passwordMax = Integer.valueOf(p.getProperty("user.password.maxsize"));
        } catch (NumberFormatException e) {
        }

        User user = (User) o;

        if (user.getUsername().length() < usernameMin || user.getUsername().length() > usernameMax) {
            errors.rejectValue("username", "username.length", "Username must be " + usernameMin + " to " + usernameMax + " characters");
        }

        if (user.getPassword().length() < passwordMin || user.getPassword().length() > passwordMax) {
            errors.rejectValue("password", "password.length", "Password must be " + passwordMin + " to " + passwordMax + " characters");
        }

        if (!user.getPassword().equals(user.getPasswordCheck())) {
            errors.rejectValue("passwordCheck", "Passwords are not match", "Passwords are not match");
        }
    }

}
