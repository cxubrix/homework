package fm.ask.kplavins.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fm.ask.kplavins.repo.BlacklistedWordRepository;
import fm.ask.kplavins.service.BlacklistService;
import fm.ask.kplavins.service.IPatternMatcher;

/**
 * A simple wrapper for {@link PatternMatcher} to expose it as a service and
 * initialize it with blacklisted words from database
 *
 */
@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistedWordRepository blacklistedWordRepository;

    private IPatternMatcher patternMatcher;

    /**
     * Load all blacklisted words from repository
     */
    @PostConstruct
    public void initializePatternMatcher() {
        patternMatcher = new PatternMatcher();
        blacklistedWordRepository.findAll().stream().forEach(b -> {
            patternMatcher.addPattern(b.getPattern());
        });
    }

    /**
     * 
     * @see BlacklistService#containsBlacklistedWords(String)
     */
    @Override
    public boolean containsBlacklistedWords(String message) {
        String found = patternMatcher.checkText(message);
        return found != null;
    }

}
