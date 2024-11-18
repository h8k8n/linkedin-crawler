package com.obss.softwarecrafter.service.crawling.common;

import com.obss.softwarecrafter.configuration.CrawlerConfiguration;
import com.obss.softwarecrafter.model.dto.LinkedinAccountDto;
import com.obss.softwarecrafter.service.LinkedinAccountFactory;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CrawlerLoginService {
    private final CrawlerConfiguration crawlerConfiguration;

    public void login(WebDriver driver, String username, String password) {
        driver.get(crawlerConfiguration.getLinkedinParams().getLoginUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement usernameField = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("username"))
        );
        WebElement passwordField = driver.findElement(By.id("password"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        passwordField.submit();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("global-nav")));
    }
}
