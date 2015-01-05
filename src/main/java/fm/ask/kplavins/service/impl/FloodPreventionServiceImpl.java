package fm.ask.kplavins.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fm.ask.kplavins.service.FloodPreventionService;

/**
 * Service to calculate flood ratio on predefined values. Flood ratio is below
 * 1.0 if the service received number of requests which is below food threshold.
 * With each reset period the flood ratio is reduced by 1.0 or less(in case if 0
 * is reached). In case of heavy flood it may take several reset periods to
 * recover flood ratio under 1.0
 * 
 * Current implementation uses constants: 5 questions per second and the reset
 * period is 2 seconds
 *
 */
@Service
public class FloodPreventionServiceImpl implements FloodPreventionService {

    private static final Logger log = LoggerFactory.getLogger(FloodPreventionServiceImpl.class.getName());

    public static final int QUESTIONS_PER_SECOND = 5;
    public static final int QUESTIONS_PER_SCHEDULLED_PERIOD = 2 * QUESTIONS_PER_SECOND;
    public static final int SCHEDULED_RESET_PERIOD = 2000;

    private final Map<String, AtomicInteger> statsPerCountry;

    public FloodPreventionServiceImpl() {
        statsPerCountry = new HashMap<String, AtomicInteger>();
    }

    @Override
    public void tick(String country) {
        AtomicInteger counter = getNullSafe(country);
        counter.incrementAndGet();
    }

    @Override
    public double getFloodRatio(String country) {
        double floodRatio = getNullSafe(country).doubleValue() / QUESTIONS_PER_SCHEDULLED_PERIOD;
        if (log.isDebugEnabled()) {
            log.debug("floodRatio: " + floodRatio);
        }
        return floodRatio;
    }

    private AtomicInteger getNullSafe(String country) {
        AtomicInteger tmp = statsPerCountry.get(country);
        if (tmp == null) { // check
            synchronized (statsPerCountry) { // sync
                tmp = statsPerCountry.get(country);
                if (tmp == null) { // check again
                    tmp = new AtomicInteger();
                    statsPerCountry.put(country, tmp);
                }
            }
        }
        return tmp;
    }

    /**
     * Double buffered reset period to avoid odd numbers
     */
    @Scheduled(fixedRate = SCHEDULED_RESET_PERIOD)
    public void resetCounter() {
        synchronized (statsPerCountry) {
            for (AtomicInteger counter : statsPerCountry.values()) {
                counter.set(Math.max(0, counter.get() - QUESTIONS_PER_SCHEDULLED_PERIOD));
            }
        }
    }

}
