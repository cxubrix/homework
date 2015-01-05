package fm.ask.kplavins.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import fm.ask.kplavins.data.BlacklistEntry;

/**
 * Spring data will generate all CRUD methods, we just provide entity class and
 * primary key type
 *
 */
public interface BlacklistedWordRepository extends JpaRepository<BlacklistEntry, Long> {

}
