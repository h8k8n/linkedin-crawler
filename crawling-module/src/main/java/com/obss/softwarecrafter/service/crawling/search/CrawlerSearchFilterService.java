package com.obss.softwarecrafter.service.crawling.search;

import com.obss.softwarecrafter.configuration.CrawlerConfiguration;
import com.obss.softwarecrafter.configuration.FilterConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrawlerSearchFilterService {
    private final FilterConfiguration filterConfiguration;

    public WebElement applyFilters(WebDriver driver, WebDriverWait wait, Map<String, String[]> filterMap) {
        if (!filterMap.isEmpty()){
            // Tüm filtreleri aç
            WebElement allFiltersButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.className("search-reusables__all-filters-pill-button")));
            allFiltersButton.click();

            Map<String, WebElement> filterSectionMap = driver.findElements(By.className("search-reusables__secondary-filters-filter")).stream()
                    .collect(Collectors.toMap(webElement -> webElement.findElement(By.tagName("h3")).getText().trim(), webElement -> webElement));

            Map<String, FilterConfiguration.FilterDefinition> filterDefinitionMap = filterConfiguration.getFilterDefinitions().stream()
                    .filter(FilterConfiguration.FilterDefinition::isActive)
                    .collect(Collectors.toMap(FilterConfiguration.FilterDefinition::getKey, filterDefinition -> filterDefinition));

            filterMap.forEach((key, filterValues) -> {
                if (filterDefinitionMap.containsKey(key)) {
                    FilterConfiguration.FilterDefinition filterDefinition = filterDefinitionMap.get(key);
                    if (filterSectionMap.containsKey(filterDefinition.getLabel())) {
                        WebElement webElement = filterSectionMap.get(filterDefinition.getLabel());
                        for (String filterValue : filterValues) {
                            applyFilter(webElement, wait, ArrayUtils.isNotEmpty(filterDefinition.getValues()), filterValue);
                        }
                    } else {
                        System.out.println("Ekranda filtre başlığı bulunamadığı için filtre uygulanamadı, bulunamayan filtre başlığı " + filterDefinition.getLabel());
                    }
                } else {
                    System.out.println("Girilen Filtre key değeri ayar dosyasındaki aktif filtre değerleriyle uyuşmadığı için filtre uygulanamadı, girilen key değeri: " + key);
                }
            });

            // Filtreleri uygula
            WebElement applyButton = driver.findElement(By.className("search-reusables__secondary-filters-show-results-button"));
            applyButton.click();
        }

        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search-results-container")));
    }

    private void applyFilter(WebElement webElement, WebDriverWait wait, boolean useCheckbox, String filterValue) {
        try {
            if (useCheckbox) {
                List<WebElement> label = webElement.findElements(By.tagName("label"));
                label.stream()
                        .filter(labelElement -> labelElement.getText().contains(filterValue))
                        .findAny()
                        .ifPresentOrElse(WebElement::click,
                                () -> System.out.printf("%s bu filtre uygulanamadı%n", filterValue));
            } else {
                webElement.findElement(By.className("reusable-search-filters-advanced-filters__add-filter-button")).click();
                WebElement locationInput = wait.until(ExpectedConditions.elementToBeClickable(
                        By.className("search-basic-typeahead"))).findElement(By.tagName("input"));
                locationInput.sendKeys(filterValue);
                ThreadUtils.sleep(Duration.ofSeconds(2));//waiting for search
                locationInput.sendKeys(Keys.ARROW_DOWN);
                locationInput.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            System.out.printf("%s bu filtre uygulanamadı%n, beklenmeyen bir hata oluştu", filterValue);
        }
    }
}
