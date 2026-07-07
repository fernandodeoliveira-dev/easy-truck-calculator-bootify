package br.com.fdo.easy_truck_calculator.item_category;

import br.com.fdo.easy_truck_calculator.events.BeforeDeleteItemCategory;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ApplicationEventPublisher publisher;
    private final ItemCategoryMapper itemCategoryMapper;

    public ItemCategoryService(final ItemCategoryRepository itemCategoryRepository,
            final ApplicationEventPublisher publisher,
            final ItemCategoryMapper itemCategoryMapper) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.publisher = publisher;
        this.itemCategoryMapper = itemCategoryMapper;
    }

    public Page<ItemCategoryDTO> findAll(final String filter, final Pageable pageable) {
        Page<ItemCategory> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = itemCategoryRepository.findAllById(longFilter, pageable);
        } else {
            page = itemCategoryRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(itemCategory -> itemCategoryMapper.updateItemCategoryDTO(itemCategory, new ItemCategoryDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public ItemCategoryDTO get(final Long id) {
        return itemCategoryRepository.findById(id)
                .map(itemCategory -> itemCategoryMapper.updateItemCategoryDTO(itemCategory, new ItemCategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ItemCategoryDTO itemCategoryDTO) {
        final ItemCategory itemCategory = new ItemCategory();
        itemCategoryMapper.updateItemCategory(itemCategoryDTO, itemCategory);
        return itemCategoryRepository.save(itemCategory).getId();
    }

    public void update(final Long id, final ItemCategoryDTO itemCategoryDTO) {
        final ItemCategory itemCategory = itemCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        itemCategoryMapper.updateItemCategory(itemCategoryDTO, itemCategory);
        itemCategoryRepository.save(itemCategory);
    }

    public void delete(final Long id) {
        final ItemCategory itemCategory = itemCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteItemCategory(id));
        itemCategoryRepository.delete(itemCategory);
    }

}
