package fm.ask.kplavins.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import fm.ask.kplavins.data.GeoIpInfo;
import fm.ask.kplavins.data.Question;
import fm.ask.kplavins.data.QuestionState;
import fm.ask.kplavins.repo.QuestionRepository;
import fm.ask.kplavins.service.BlacklistService;
import fm.ask.kplavins.service.FloodPreventionService;
import fm.ask.kplavins.service.GeoIpService;
import fm.ask.kplavins.service.QuestionModerationService;

/**
 * Service for async moderation. Moderation is performed against blacklisted
 * words and flood prevention service per country. After moderation the question
 * state is updated and country code is added to the entity.
 *
 */
@Service
public class QuestionModerationServiceImpl implements QuestionModerationService {

    private static final Logger log = LoggerFactory.getLogger(QuestionModerationServiceImpl.class.getName());

    @Autowired
    private GeoIpService geoIpService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FloodPreventionService floodPreventionService;

    @Autowired
    private BlacklistService blacklistService;

    /**
     * Moderate question first against flood(no need to check blacklist if we
     * are flooded) then against blacklist
     */
    @Async
    @Override
    public void moderate(Question question) {
        final String country = getCountry(question.getIp());
        question.setCountry(country);

        floodPreventionService.tick(country); // count in

        question.setState(getModerateState(question));
        if (log.isDebugEnabled()) {
            log.debug("Question: " + question.getId() + " moderated to " + question.getState());
        }
        questionRepository.save(question);
    }

    private QuestionState getModerateState(Question question) {
        if (isFlooding(question.getCountry())) {
            return QuestionState.REJECTED_BY_FLOOD;
        }

        if (hasBlacklistedWords(question.getContent())) {
            return QuestionState.REJECTEC_BY_BLACKLIST;
        }

        return QuestionState.ACCEPTED;
    }

    private boolean isFlooding(String country) {
        double floodRatio = floodPreventionService.getFloodRatio(country);
        return Double.compare(floodRatio, 1) > 0;
    }

    private boolean hasBlacklistedWords(String content) {
        return blacklistService.containsBlacklistedWords(content);
    }

    private String getCountry(String ip) {
        GeoIpInfo geoIpInfo = geoIpService.getGeoIpInfo(ip);
        if (geoIpInfo != null && geoIpInfo.getCountry_code() != null) {
            return geoIpInfo.getCountry_code();
        } else {
            return GeoIpService.FALLBACK_COUNTRY_CODE;
        }
    }

}
