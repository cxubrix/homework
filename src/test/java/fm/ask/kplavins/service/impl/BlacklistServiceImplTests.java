package fm.ask.kplavins.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fm.ask.kplavins.data.BlacklistEntry;
import fm.ask.kplavins.repo.BlacklistedWordRepository;
import fm.ask.kplavins.service.BlacklistService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BlacklistServiceImplTests {

    @Autowired
    private BlacklistService blacklistService;

    @Configuration
    static class ContextConfiguration {

        @Bean
        public BlacklistService blacklistService() {
            return new BlacklistServiceImpl();
        }

        @Bean
        public BlacklistedWordRepository blacklistedWordRepository() {
            BlacklistedWordRepository mock = mock(BlacklistedWordRepository.class);
            BlacklistEntry entry = new BlacklistEntry();
            entry.setId(1L);
            entry.setPattern("spam");
            when(mock.findAll()).thenReturn(Arrays.asList(entry));
            return mock;
        }
    }

    @Test
    public void testBlacklistGood() {
        assertFalse(blacklistService.containsBlacklistedWords("no blacklisted words?"));
    }

    @Test
    public void testBlacklistBad() {
        assertTrue(blacklistService.containsBlacklistedWords("how about fresh spam?"));
    }

}
