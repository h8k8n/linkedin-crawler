package com.obss.softwarecrafter.service.crawling.search;

import com.obss.softwarecrafter.model.dto.result.ServiceResult;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import com.obss.softwarecrafter.utilities.SeleniumUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ThreadUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CrawlerSearchService {
    private final CrawlerSearchFilterService crawlerSearchFilterService;

    public List<String> search(WebDriver driver, WebDriverWait wait, WebElement webElement, Map<String, String[]> filterMap) throws InterruptedException {
        List<String> profileUrls = new ArrayList<>();
        WebElement searchLinkElement = webElement.findElements(By.tagName("a"))
                .stream()
                .filter(webElement1 -> webElement1.getAttribute("href").contains("search/results/people"))
                .findAny()
                .orElseThrow();//TODO

        String searchLink = searchLinkElement.getAttribute("href");
        System.out.println("searchLink: " + searchLink);

        searchLinkElement.click();
        crawlerSearchFilterService.applyFilters(driver, wait, filterMap);
        WebElement mainWebElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search-results-container")));
        int pageCount = 1;
        do {
            getProfileUrlsOnPage(mainWebElement, profileUrls);
            System.out.println("pageCount: " + pageCount + " list size: " + profileUrls.size());
            mainWebElement = checkAndClickNext(driver, wait, mainWebElement);
        } while (Objects.nonNull(mainWebElement));

        return profileUrls;
    }

    private void getProfileUrlsOnPage(WebElement mainWebElement, List<String> profileUrls) {
        mainWebElement.findElements(By.tagName("li")).stream().map(webElement2 -> {
            try {
                String profileUrl = webElement2.findElement(By.tagName("a")).getAttribute("href");
                System.out.println("resultsText: " + profileUrl);
                return profileUrl;
            } catch (NoSuchElementException e) {
                System.out.println(webElement2.getText());
            }
            return "";
        }).filter(profileUrl -> profileUrl.contains("/in/")).forEach(profileUrls::add);
    }

    private WebElement checkAndClickNext(WebDriver driver, WebDriverWait wait, WebElement mainWebElement) throws InterruptedException {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        ThreadUtils.sleep(Duration.ofSeconds(1));


        Optional<WebElement> nextButtonOptional = SeleniumUtil.findElement(mainWebElement, By.className("artdeco-pagination__button--next"));
        WebElement webElement = null;
        if (nextButtonOptional.isPresent()) {
            nextButtonOptional.get().click();
            ((JavascriptExecutor) driver)
                    .executeScript("window.scrollTo(0, 0)");
            webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search-results-container")));
        }
        return webElement;
    }
}
