package br.com.fdo.easy_truck_calculator.pre_set;

import br.com.fdo.easy_truck_calculator.events.BeforeDeleteItem;
import br.com.fdo.easy_truck_calculator.item.ItemRepository;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class PreSetService {

    private final PreSetRepository preSetRepository;
    private final ItemRepository itemRepository;
    private final PreSetMapper preSetMapper;

    public PreSetService(final PreSetRepository preSetRepository,
            final ItemRepository itemRepository, final PreSetMapper preSetMapper) {
        this.preSetRepository = preSetRepository;
        this.itemRepository = itemRepository;
        this.preSetMapper = preSetMapper;
    }

    public Page<PreSetDTO> findAll(final String filter, final Pageable pageable) {
        Page<PreSet> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = preSetRepository.findAllById(uuidFilter, pageable);
        } else {
            page = preSetRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(preSet -> preSetMapper.updatePreSetDTO(preSet, new PreSetDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public PreSetDTO get(final UUID id) {
        return preSetRepository.findById(id)
                .map(preSet -> preSetMapper.updatePreSetDTO(preSet, new PreSetDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final PreSetDTO preSetDTO) {
        final PreSet preSet = new PreSet();
        preSetMapper.updatePreSet(preSetDTO, preSet, itemRepository);
        return preSetRepository.save(preSet).getId();
    }

    public void update(final UUID id, final PreSetDTO preSetDTO) {
        final PreSet preSet = preSetRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        preSetMapper.updatePreSet(preSetDTO, preSet, itemRepository);
        preSetRepository.save(preSet);
    }

    public void delete(final UUID id) {
        final PreSet preSet = preSetRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        preSetRepository.delete(preSet);
    }

    @EventListener(BeforeDeleteItem.class)
    public void on(final BeforeDeleteItem event) {
        // remove many-to-many relations at owning side
        preSetRepository.findAllByItemsId(event.getId()).forEach(preSet ->
                preSet.getItems().removeIf(item -> item.getId().equals(event.getId())));
    }

}
