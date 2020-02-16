import com.alibaba.fastjson.JSON;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import stoenr.telastic.Application;
import stoenr.telastic.bean.Item;
import stoenr.telastic.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testCreateIndex() {
        elasticsearchTemplate.createIndex(Item.class);
    }

    @Test
    public void testDeleteIndex() {
        elasticsearchTemplate.deleteIndex(Item.class);
    }

    @Test
    public void insert() {
        Item item = new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.baidu.com/13123.jpg");
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);
        items.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.baidu.com/13123.jpg"));
        items.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.baidu.com/13123.jpg"));
        itemRepository.saveAll(items);
    }

    @Test
    public void query() {
        Iterable<Item> all = itemRepository.findAll(Sort.by("id").descending());
        System.out.println("all -> "+all.getClass());
        for (Item item : all) {
            System.out.println(item);
        }
    }

    @Test
    public void queryByFiled() {
        ArrayList<String> names = new ArrayList<>();
        names.add("锤子");
        names.add("小米");
        names.add("小米");

        Iterable<Item> all = itemRepository.findByBrandIn(names);
        for (Item item : all) {
            System.out.println(item);
        }
    }

    @Test
    public void queryPage() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("brand", "小米"));
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));
        // 分页：
        int page = 0;
        int size = 2;
        queryBuilder.withPageable(PageRequest.of(page,size));

        // 搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        // 总条数
        long total = items.getTotalElements();
        System.out.println("总条数 = " + total);
        // 总页数
        System.out.println("总页数 = " + items.getTotalPages());
        // 当前页
        System.out.println("当前页：" + items.getNumber());
        // 每页大小
        System.out.println("每页大小：" + items.getSize());

        System.out.println(JSON.toJSONString(items));
    }
}
