package fm.ask.kplavins.service.impl;

import static fm.ask.kplavins.service.impl.FloodPreventionServiceImpl.QUESTIONS_PER_SCHEDULLED_PERIOD;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FloodPreventionServiceImplTests {

    private FloodPreventionServiceImpl floodPreventionService;

    @Before
    public void createFloodPreventionService() {
        floodPreventionService = new FloodPreventionServiceImpl();
    }

    @Test
    public void testTick() {
        double floodRatio = floodPreventionService.getFloodRatio("LV");
        Assert.assertEquals(0, (Double.compare(floodRatio, 0.0)));
        floodPreventionService.tick("LV");
        floodRatio = floodPreventionService.getFloodRatio("LV");
        Assert.assertTrue((Double.compare(floodRatio, 0.0)) > 0);
    }

    @Test
    public void testRatio() {
        IntStream.range(0, QUESTIONS_PER_SCHEDULLED_PERIOD + 1).parallel().forEach(c -> {
            floodPreventionService.tick("LV");
        });
        double floodRatio = floodPreventionService.getFloodRatio("LV");
        Assert.assertTrue((Double.compare(floodRatio, 1.0)) > 0);
        // simulate scheduled reset
        floodPreventionService.resetCounter();
        floodRatio = floodPreventionService.getFloodRatio("LV");
        Assert.assertTrue((Double.compare(floodRatio, 1.0)) < 0);

    }

}
