package fm.ask.kplavins.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fm.ask.kplavins.data.Question;
import fm.ask.kplavins.data.QuestionState;
import fm.ask.kplavins.repo.QuestionRepository;
import fm.ask.kplavins.service.QuestionModerationService;

/**
 * Single controller for retrieving question, submitting question and listing
 * all accepted questions
 *
 */
@RestController
public class QuestionController {

    private static final Logger log = LoggerFactory.getLogger(QuestionController.class.getName());

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionModerationService moderationService;

    /**
     * Get question by it's id.
     * 
     * @param id
     * @return question or empty response if question is not found
     */
    @RequestMapping(value = "/questions/{id}", method = RequestMethod.GET)
    public Question getQuestionById(@PathVariable(value = "id") Long id) {
        if (log.isDebugEnabled()) {
            log.debug("get question by id: " + id);
        }
        return questionRepository.findOne(id);
    }

    /**
     * Get all accepted questions. Optionally filtered by country code
     * 
     * @param country
     *            code in uppercase e.g. LV
     * @return collection of questions or empty array if no questions found
     */
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public Iterable<Question> getAllAcceptedQuestions(@RequestParam(value = "country", required = false) String country) {
        if (log.isDebugEnabled()) {
            log.debug("get all accepted questions, country: " + country);
        }
        if (country != null) {
            return questionRepository.findByCountryIgnoringCaseAndState(country, QuestionState.ACCEPTED);
        }
        return questionRepository.findByState(QuestionState.ACCEPTED);
    }

    /**
     * Submit a question. Moderation of question will be performed. The new
     * question might have status NEW which means that it is still awaiting
     * moderation. Use GET with the id from response to check question status
     * after moderation. Question can get statuses 'ACCEPTED',
     * 'REJECTEC_BY_BLACKLIST' or 'REJECTED_BY_FLOOD'. It might be that the
     * moderation is performed immediately. Then instead of 'NEW' the response
     * will already have one of the last 3 statuses.
     * 
     * @param content
     *            question text
     * @param request
     *            injected by spring, used to getRemoteAddr
     * @return newly created question with id which can be used to track
     *         question status change
     */
    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    public Question askQuestion(@RequestParam("content") String content, HttpServletRequest request) {
        final String ip = request.getRemoteAddr();
        if (log.isDebugEnabled()) {
            log.debug("asking question: '" + content + "' form IP: " + ip);
        }
        final Question question = new Question();
        question.setContent(content);
        question.setIp(ip);
        final Question savedQuestion = questionRepository.save(question);
        moderationService.moderate(savedQuestion); // trigger async moderation
        return savedQuestion;
    }
}
