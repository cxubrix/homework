package fm.ask.kplavins.service;

public interface BlacklistService {

    /**
     * Check if input contains any of blacklisted words. Blacklisted words and
     * their retrieval is implementation specific.
     * 
     * @param message
     *            to check
     * @return true if a blacklisted word is found, otherwise false
     */
    boolean containsBlacklistedWords(String message);

}
