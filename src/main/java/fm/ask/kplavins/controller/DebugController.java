package fm.ask.kplavins.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fm.ask.kplavins.data.BlacklistEntry;
import fm.ask.kplavins.data.Question;
import fm.ask.kplavins.repo.BlacklistedWordRepository;
import fm.ask.kplavins.repo.QuestionRepository;

/**
 * Exposed only during development to see all question and blacklist entries in
 * db
 *
 */
@Profile("dev")
@RestController
public class DebugController {

    private static final Logger log = LoggerFactory.getLogger(DebugController.class.getName());

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private BlacklistedWordRepository blacklistedWordRepository;

    @RequestMapping(value = "/_questions", method = RequestMethod.GET)
    public Iterable<Question> getAllQuestions() {
        if (log.isDebugEnabled()) {
            log.debug("get all questions");
        }
        return questionRepository.findAll();
    }

    @RequestMapping(value = "/_blacklist", method = RequestMethod.GET)
    public Iterable<BlacklistEntry> getBlacklist() {
        if (log.isDebugEnabled()) {
            log.debug("get all blacklisted words");
        }
        return blacklistedWordRepository.findAll();
    }
}
