package com.game.service;

import com.game.entity.Player;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;

public class PlayerValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        // Required fields
        ValidationUtils.rejectIfEmpty(errors, "name", "name.required");
        ValidationUtils.rejectIfEmpty(errors, "title", "title.required");
        ValidationUtils.rejectIfEmpty(errors, "race", "race.required");
        ValidationUtils.rejectIfEmpty(errors, "profession", "profession.required");
        ValidationUtils.rejectIfEmpty(errors, "birthday", "birthday.required");
        ValidationUtils.rejectIfEmpty(errors, "experience", "experience.required");

        if (errors.hasErrors()) {
            return;
        }

        Player player = (Player) target;

        // Name
        if (player.getName().length() > 12 || "".equals(player.getName())) {
            errors.rejectValue("name", "name.out.of.bound");
        }

        // Title
        if (player.getTitle().length() > 30) {
            errors.rejectValue("title", "title.out.of.bound");
        }

        // Experience

        if (player.getExperience() < 0 || player.getExperience() > 10_000_000) {
            errors.rejectValue("experience", "experience.out.of.bound");
        }

        // Birthday
        Calendar playerBirthday = Calendar.getInstance();
        playerBirthday.setTime(player.getBirthday());
        if (playerBirthday.get(Calendar.YEAR) < 2000 || playerBirthday.get(Calendar.YEAR) > 3000) {
            errors.rejectValue("birthday", "birthday.out.of.bound");
        }
    }
}
