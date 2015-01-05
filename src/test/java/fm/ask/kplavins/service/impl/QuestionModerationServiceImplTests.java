package fm.ask.kplavins.service.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fm.ask.kplavins.data.GeoIpInfo;
import fm.ask.kplavins.data.Question;
import fm.ask.kplavins.data.QuestionState;
import fm.ask.kplavins.repo.QuestionRepository;
import fm.ask.kplavins.service.BlacklistService;
import fm.ask.kplavins.service.FloodPreventionService;
import fm.ask.kplavins.service.GeoIpService;
import fm.ask.kplavins.service.QuestionModerationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles("unittests") // to hide from integration test 
public class QuestionModerationServiceImplTests {

    private static final String LV = "LV";
    private static final String IP = "127.0.0.1";
    private static final String MESSAGE = "Question?";

    @Autowired
    private QuestionModerationService questionModerationService;

    @Autowired
    private FloodPreventionService floodPreventionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private BlacklistService blacklistService;

    @Profile("unittests")    
    @Configuration
    static class ContextConfiguration {

        @Bean
        public QuestionModerationService questionModerationService() {
            return new QuestionModerationServiceImpl();
        }

        @Bean
        public GeoIpService geoIpService() {
            GeoIpService mock = mock(GeoIpService.class);
            GeoIpInfo geoIpInfo = mock(GeoIpInfo.class);
            when(geoIpInfo.getCountry_code()).thenReturn(LV);
            when(mock.getGeoIpInfo(eq(IP))).thenReturn(geoIpInfo);
            return mock;
        }

        @Bean
        public QuestionRepository questionRepository() {
            QuestionRepository mock = mock(QuestionRepository.class);
            return mock;
        }

        @Bean
        public FloodPreventionService floodPreventionService() {
            FloodPreventionService mock = mock(FloodPreventionService.class);
            return mock;
        }

        @Bean
        public BlacklistService blacklistService() {
            BlacklistService mock = mock(BlacklistService.class);
            return mock;
        }
    }

    private Question question;

    @Before
    public void initMocks() {
        reset(floodPreventionService, blacklistService, questionRepository); // reset
                                                                             // interactions
        question = new Question();
        question.setIp(IP);
        question.setContent(MESSAGE);
    }

    @Test
    public void moderateAccept() {

        when(blacklistService.containsBlacklistedWords(eq(MESSAGE))).thenReturn(false); // fine
        when(floodPreventionService.getFloodRatio(eq(LV))).thenReturn(0.1); // fine

        questionModerationService.moderate(question);

        Assert.assertEquals(LV, question.getCountry());
        Assert.assertEquals(QuestionState.ACCEPTED, question.getState());

        verify(floodPreventionService).tick(eq(LV));
        verify(blacklistService).containsBlacklistedWords(eq(MESSAGE));
        verify(floodPreventionService).getFloodRatio(eq(LV));
        verify(questionRepository).save(eq(question));
    }

    @Test
    public void moderateBlacklisted() {

        when(blacklistService.containsBlacklistedWords(eq(MESSAGE))).thenReturn(true); // contains
                                                                                       // blacklisted
                                                                                       // word
        when(floodPreventionService.getFloodRatio(eq(LV))).thenReturn(0.1);

        questionModerationService.moderate(question);

        Assert.assertEquals(LV, question.getCountry());
        Assert.assertEquals(QuestionState.REJECTEC_BY_BLACKLIST, question.getState());

        verify(floodPreventionService).tick(eq(LV));
        verify(blacklistService).containsBlacklistedWords(eq(MESSAGE));
        verify(floodPreventionService).getFloodRatio(eq(LV));
        verify(questionRepository).save(eq(question));
    }

    @Test
    public void moderateFlood() {

        when(floodPreventionService.getFloodRatio(eq(LV))).thenReturn(1.1); // flood
                                                                            // ration
                                                                            // exceeded

        questionModerationService.moderate(question);

        Assert.assertEquals(LV, question.getCountry());
        Assert.assertEquals(QuestionState.REJECTED_BY_FLOOD, question.getState());

        verify(floodPreventionService).tick(eq(LV));
        verify(blacklistService, never()).containsBlacklistedWords(anyString());
        verify(floodPreventionService).getFloodRatio(eq(LV));
        verify(questionRepository).save(eq(question));
    }
}
