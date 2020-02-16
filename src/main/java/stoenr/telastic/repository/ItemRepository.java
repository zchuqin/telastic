package stoenr.telastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import stoenr.telastic.bean.Item;

import java.util.Collection;

public interface ItemRepository extends ElasticsearchRepository<Item, Long> {

    Iterable<Item> findByBrand(String brand);

    Iterable<Item> findByBrandIn(Collection<String> names);
}
