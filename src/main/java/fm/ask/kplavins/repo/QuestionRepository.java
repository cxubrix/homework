package fm.ask.kplavins.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import fm.ask.kplavins.data.Question;
import fm.ask.kplavins.data.QuestionState;

/**
 * Spring data will generate all CRUD methods and implementation for methods by
 * their name, we just provide entity class and primary key type and parameter
 * names for methods
 *
 */
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {

    Iterable<Question> findByCountryIgnoringCaseAndState(@Param("country") String country,
            @Param("state") QuestionState state);

    Iterable<Question> findByState(@Param("state") QuestionState state);

}
