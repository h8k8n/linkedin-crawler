package com.obss.softwarecrafter.service.crawling.scraping;

import com.obss.softwarecrafter.configuration.CrawlerConfiguration;
import com.obss.softwarecrafter.kafka.ProducerService;
import com.obss.softwarecrafter.model.event.AnalyzeCrawlingDataEvent;
import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import com.obss.softwarecrafter.service.crud.CrawlingResultCrudService;
import com.obss.softwarecrafter.utilities.HtmlUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileScrapingService {
    private final CrawlerConfiguration crawlerConfiguration;
    private final CrawlingResultCrudService crawlingResultCrudService;
    private final ProducerService producerService;

    public void scrapeProfile(WebDriver driver, String profileUrl, int recursiveScrapingParam, int currentRecursiveLevel) {
        //String url=crawlerConfiguration.getLinkedinParams().getProfilePrefixUrl() + profileId;
        System.out.println("scrapeProfile başladı %s - %s - %s".formatted(profileUrl, recursiveScrapingParam, currentRecursiveLevel));

        driver.get(profileUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            //TODO mantıklı bir bekleme olmalı
            By profileMainLocator = By.cssSelector(".pv-profile-card");
            wait.until(ExpectedConditions.presenceOfElementLocated(profileMainLocator));
            ThreadUtils.sleep(Duration.ofSeconds(10));

            Document doc = Jsoup.parse(driver.getPageSource());
            Element mainElement = doc.getElementsByClass("scaffold-layout__main").get(0);
            Elements sections = mainElement.getElementsByTag("section");

            Element nameElement = mainElement.selectFirst("h1");
            String fullName = nameElement != null ? nameElement.text() : "";

            sections.removeIf(section -> {
                Elements elementsByClass = section.getElementsByClass("pv-profile-card__anchor");
                if (!elementsByClass.isEmpty()) {
                    return StringUtils.equalsAny(elementsByClass.get(0).id(), crawlerConfiguration.getUnusedSections().toArray(new String[0]));
                }
                return false;
            });

            sections.forEach(section ->
                    checkDetail(driver, section)
            );

            String profileId = HtmlUtils.getProfileIdFromUrl(profileUrl);
            System.out.println("scrapeProfile bitti, kayıt yapılcak %s - %s".formatted(profileId, fullName));
            CrawlingResultEntity crawlingResultEntity = crawlingResultCrudService.getByProfileId(profileId).orElse(new CrawlingResultEntity());
            crawlingResultEntity.setProfileId(profileId);
            crawlingResultEntity.setRawDataResult(mainElement.html());
            crawlingResultEntity.setCrawlDate(LocalDateTime.now());
            crawlingResultEntity.setFullName(fullName);
            CrawlingResultEntity createdEntity = crawlingResultCrudService.create(crawlingResultEntity);

            producerService.sendMessage(AnalyzeCrawlingDataEvent.builder()
                    .profileId(createdEntity.getProfileId())
                    .crawlDate(createdEntity.getCrawlDate())
                    .id(createdEntity.getId().toString())
                    .rawData(crawlingResultEntity.getRawDataResult())
                    .build());

            //benzer profillere bak
            if (recursiveScrapingParam > 0 && (recursiveScrapingParam >= currentRecursiveLevel)) {
                System.out.println("benzer profillere bakılcak %s".formatted(profileUrl));
                driver.get(profileUrl);
                WebElement asideElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("scaffold-layout__aside")));
                List<WebElement> asideSectionElements = asideElement.findElements(By.tagName("section"));
                ((JavascriptExecutor) driver)
                        .executeScript("window.scrollTo(0, document.body.scrollHeight)");
                ThreadUtils.sleep(Duration.ofSeconds(1));
                asideSectionElements.stream()
                        .filter(webElement -> {
                            try {
                                webElement.findElement(By.id("browsemap_recommendation"));
                                return true;
                            } catch (NoSuchElementException e) {
                                System.out.println("browsemap_recommendation bulunamadı");
                                return false;
                            }
                        })
                        .findAny()
                        .ifPresent(webElement -> {
                            driver.get(webElement.findElement(By.id("navigation-overlay-section-see-more")).getAttribute("href"));
                            WebElement modalElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("artdeco-modal__content")));
                            Set<String> otherProfileUrls = modalElement.findElements(By.tagName("a")).stream()
                                    .map(webElement1 -> webElement1.getAttribute("href")).
                                    collect(Collectors.toSet());
                            int newRecursiveLevel = currentRecursiveLevel + 1;
                            otherProfileUrls.forEach(url -> scrapeProfile(driver, url, recursiveScrapingParam, newRecursiveLevel));
                        });
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private static void checkDetail(WebDriver driver, Element section) {
        try {
            Elements elementOfFooter = section.getElementsByClass("pvs-list__footer-wrapper");
            if (!elementOfFooter.isEmpty()) {
                Element mainUlTag = section.getElementsByTag("ul").get(0);
                //detay çek
                String detailUrl = elementOfFooter.get(0).getElementsByTag("a").get(0).attribute("href").getValue();
                driver.get(detailUrl);
                ThreadUtils.sleep(Duration.ofSeconds(5));
                Document docDetail = Jsoup.parse(driver.getPageSource());
                Element detailMain = docDetail.getElementsByClass("scaffold-layout__main").get(0);
                Element detailUlTag = detailMain.getElementsByTag("ul").get(0);
                mainUlTag.replaceWith(detailUlTag);
            }
        } catch (Exception e) {
            //TODO log
            e.printStackTrace();
        }
    }
}
