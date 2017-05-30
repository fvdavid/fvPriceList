package fv.monster.repository;

import fv.monster.model.Materi;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
public interface MateriRepository extends CrudRepository<Materi, Long> {

    public Materi findByNama(String nama);

    public Materi getMateriById(Long id);

    // public Materi findByRangeHarga(BigDecimal dari, BigDecimal ke);
    public Materi findByStatus(String status);

    public Materi findByIdIn(Iterable<Long> ids);

}
