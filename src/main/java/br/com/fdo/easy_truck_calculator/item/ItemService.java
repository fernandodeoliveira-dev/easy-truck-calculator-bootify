package br.com.fdo.easy_truck_calculator.item;

import br.com.fdo.easy_truck_calculator.events.BeforeDeleteItemCategory;
import br.com.fdo.easy_truck_calculator.item_category.ItemCategoryRepository;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import br.com.fdo.easy_truck_calculator.util.ReferencedException;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemMapper itemMapper;

    public ItemService(final ItemRepository itemRepository,
            final ItemCategoryRepository itemCategoryRepository, final ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemMapper = itemMapper;
    }

    public Page<ItemDTO> findAll(final String filter, final Pageable pageable) {
        Page<Item> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = itemRepository.findAllById(uuidFilter, pageable);
        } else {
            page = itemRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(item -> itemMapper.updateItemDTO(item, new ItemDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public ItemDTO get(final UUID id) {
        return itemRepository.findById(id)
                .map(item -> itemMapper.updateItemDTO(item, new ItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ItemDTO itemDTO) {
        final Item item = new Item();
        itemMapper.updateItem(itemDTO, item, itemCategoryRepository);
        return itemRepository.save(item).getId();
    }

    public void update(final UUID id, final ItemDTO itemDTO) {
        final Item item = itemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        itemMapper.updateItem(itemDTO, item, itemCategoryRepository);
        itemRepository.save(item);
    }

    public void delete(final UUID id) {
        final Item item = itemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        itemRepository.delete(item);
    }

    @EventListener(BeforeDeleteItemCategory.class)
    public void on(final BeforeDeleteItemCategory event) {
        final ReferencedException referencedException = new ReferencedException();
        final Item itemCategoryItem = itemRepository.findFirstByItemCategoryId(event.getId());
        if (itemCategoryItem != null) {
            referencedException.setKey("itemCategory.item.itemCategory.referenced");
            referencedException.addParam(itemCategoryItem.getId());
            throw referencedException;
        }
    }

}
