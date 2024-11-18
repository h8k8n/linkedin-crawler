package com.obss.softwarecrafter.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class SeleniumUtil {

    public static Optional<WebElement> findElement(WebElement element, By by){
        try {
            return Optional.of(element.findElement(by));
        } catch (NoSuchElementException noSuchElementException){
            return Optional.empty();
        }
    }
}
