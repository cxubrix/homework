package fm.ask.kplavins.service;

public interface FloodPreventionService {

    /**
     * Count a single item for current time slot for a spceific country
     * 
     * @param country
     *            not null country code.
     */
    void tick(String country);

    /**
     * Get currently calculated flood ratio for a particular country.
     * 
     * @param country
     * @return less than 1.0 if there is no flood. Everything above 1.0 means
     *         that the service is flooded.
     */
    double getFloodRatio(String country);

}
