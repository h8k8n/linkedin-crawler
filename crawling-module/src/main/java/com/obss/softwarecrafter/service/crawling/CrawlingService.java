package com.obss.softwarecrafter.service.crawling;

import com.obss.softwarecrafter.configuration.CrawlerConfiguration;
import com.obss.softwarecrafter.exception.LinkedInAccountNotFoundException;
import com.obss.softwarecrafter.exception.ProxyServerNotFoundException;
import com.obss.softwarecrafter.model.dto.CrawlResultDto;
import com.obss.softwarecrafter.model.dto.LinkedinAccountDto;
import com.obss.softwarecrafter.model.dto.proxy.ProxyServerDto;
import com.obss.softwarecrafter.model.dto.result.ServiceResult;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import com.obss.softwarecrafter.service.LinkedinAccountFactory;
import com.obss.softwarecrafter.service.ProxyServerFactory;
import com.obss.softwarecrafter.service.crawling.common.CrawlerLoginService;
import com.obss.softwarecrafter.service.crawling.scraping.ProfileScrapingService;
import com.obss.softwarecrafter.service.crawling.search.CrawlerSearchService;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlingService {
    private final CrawlerLoginService crawlerLoginService;
    private final ProfileScrapingService profileScrapingService;
    private final CrawlerSearchService crawlerSearchService;
    private final CrawlerConfiguration crawlerConfiguration;
    private final ProxyServerFactory proxyServerFactory;
    private final LinkedinAccountFactory linkedinAccountFactory;

    private WebDriver initializeDriver(ProxyServerDto proxyServer) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=1920,1080");
        options.setAcceptInsecureCerts(true);

        setProxy(proxyServer, options);

        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private void setProxy(ProxyServerDto proxyServer, ChromeOptions options) {
        Proxy proxy = new Proxy();

        String proxyString = String.format("%s:%d", proxyServer.getIp(), proxyServer.getPort());
        proxy.setSocksProxy(proxyString);
        if ("SOCKS4".equals(proxyServer.getType())) {
            proxy.setSocksProxy(proxyString);
            proxy.setSocksVersion(4);
        } else {
            proxy.setSocksProxy(proxyString);
            proxy.setSocksVersion(5);
        }
        if (proxyServer.getUsername() != null && proxyServer.getPassword() != null) {
            String proxyAuth = String.format("--proxy-server=socks%d://%s:%s@%s:%d",
                    proxy.getSocksVersion(),
                    proxyServer.getUsername(),
                    proxyServer.getPassword(),
                    proxyServer.getIp(),
                    proxyServer.getPort()
            );
            log.info("kullanılan proxy: "+proxyAuth);
            options.addArguments(proxyAuth);
            options.setCapability("proxy", proxy);
        } else {
            String proxyArg = String.format("--proxy-server=socks%d://%s:%d",
                    proxy.getSocksVersion(),
                    proxyServer.getIp(),
                    proxyServer.getPort()
            );
            options.addArguments(proxyArg);
        }
    }

    public ServiceResult<List<String>> findProfileUrlList(CrawlingTypeEnum crawlingType, String target, Map<String, String[]> filterMap) {
        log.info("findProfileIdList başladı: " + crawlingType + " - " + target);
        if (crawlingType == CrawlingTypeEnum.PERSON || crawlingType == CrawlingTypeEnum.SYSTEM) {
            String url = crawlerConfiguration.getLinkedinParams().getTargetPrefixUrl().get(CrawlingTypeEnum.PERSON.name().toLowerCase()) + target;
            return ServiceResult.success(List.of(url));
        } else {
            WebDriver driver = null;
            String proxyServerId = null;
            String linkedinAccountId = null;
            try {
                LinkedinAccountDto linkedinAccount = linkedinAccountFactory.getLinkedinAccount();
                linkedinAccountId = linkedinAccount.getId();

                ProxyServerDto proxyServer = proxyServerFactory.getProxyServer();
                proxyServerId = proxyServer.getId();

                driver = initializeDriver(proxyServer);
                crawlerLoginService.login(driver, linkedinAccount.getUsername(), linkedinAccount.getPassword());

                String url = crawlerConfiguration.getLinkedinParams().getTargetPrefixUrl().get(crawlingType.name().toLowerCase()) + target;
                driver.get(url);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                WebElement webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("org-top-card-summary-info-list")));

                List<String> profileUrls = crawlerSearchService.search(driver, wait, webElement, filterMap);
                driver.quit();
                return ServiceResult.success(profileUrls);
            } catch (Exception e) {
                if (Objects.nonNull(driver)) {
                    driver.quit();
                }
                disabledAccounts(linkedinAccountId, proxyServerId);

                if (e instanceof LinkedInAccountNotFoundException) {
                    log.warn("aktif linkedin hesabı bulunamadığı için search işlemi yapılmadı, target: " + target);
                    return ServiceResult.error(e);
                } else if (e instanceof ProxyServerNotFoundException) {
                    log.warn("aktif proxy server bulunamadığı için search işlemi yapılmadı, target: " + target);
                    return ServiceResult.error(e);
                } else {
                    return findProfileUrlList(crawlingType, target, filterMap);
                }
            }
        }
    }

    public ServiceResult<List<CrawlResultDto>> crawlProfiles(List<String> profileUrls, int recursiveScrapingParam) {
        List<CrawlResultDto> crawlResultDtoList = new ArrayList<>(profileUrls.size());
        int count = 0;
        WebDriver driver = null;
        String proxyServerId = null;
        String linkedinAccountId = null;
        try {
            LinkedinAccountDto linkedinAccount = linkedinAccountFactory.getLinkedinAccount();
            linkedinAccountId = linkedinAccount.getId();

            ProxyServerDto proxyServer = proxyServerFactory.getProxyServer();
            proxyServerId = proxyServer.getId();

            driver = initializeDriver(proxyServer);
            crawlerLoginService.login(driver, linkedinAccount.getUsername(), linkedinAccount.getPassword());

            for (String url : profileUrls) {
                profileScrapingService.scrapeProfile(driver, url, recursiveScrapingParam, 1);
                Thread.sleep(2000); // Rate limiting için bekleme, random olsa iyi olur, configten çekelim
                count++;
            }
            driver.quit();
        } catch (Exception e) {
            if (Objects.nonNull(driver)) {
                driver.quit();
            }
            disabledAccounts(linkedinAccountId, proxyServerId);
            List<String> remainingList = profileUrls.subList(count, profileUrls.size());
            if (e instanceof LinkedInAccountNotFoundException) {
                remainingList.forEach(s -> log.warn("aktif linkedin hesabı bulunamadığı için bu profil crawl edilemedi, url: " + s));
                return ServiceResult.error(e);
            } else if (e instanceof ProxyServerNotFoundException) {
                remainingList.forEach(s -> log.warn("aktif proxy server bulunamadığı için bu profil crawl edilemedi, url: " + s));
                return ServiceResult.error(e);
            } else {
                crawlProfiles(remainingList, recursiveScrapingParam);
            }
        }

        return ServiceResult.success(crawlResultDtoList);
    }

    private void disabledAccounts(String linkedinAccountId, String proxyServerId){
        if(Objects.nonNull(linkedinAccountId)){
            linkedinAccountFactory.disable(linkedinAccountId);
        }

        if(Objects.nonNull(proxyServerId)){
            proxyServerFactory.disable(proxyServerId);
        }
    }

    public void testProxy(String address) {
        WebDriver webDriver = initializeDriver(proxyServerFactory.getProxyServer());
        try {
            webDriver.get(address);
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
            WebElement webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ipv4")));
            log.info("ipv4: " + webElement.getText());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

    }

    public void controlBeforeRun() {
        linkedinAccountFactory.getLinkedinAccount();
        proxyServerFactory.getProxyServer();
    }
}
