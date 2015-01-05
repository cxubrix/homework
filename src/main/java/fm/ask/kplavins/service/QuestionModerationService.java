package fm.ask.kplavins.service;

import fm.ask.kplavins.data.Question;

public interface QuestionModerationService {

    /**
     * Update {@link Question} based on moderation rules.
     * 
     * @param question
     *            to moderate
     */
    void moderate(Question question);

}
