package br.com.fdo.easy_truck_calculator.unit;

import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public UnitService(final UnitRepository unitRepository, final UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    public Page<UnitDTO> findAll(final String filter, final Pageable pageable) {
        Page<Unit> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = unitRepository.findAllById(uuidFilter, pageable);
        } else {
            page = unitRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(unit -> unitMapper.updateUnitDTO(unit, new UnitDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public UnitDTO get(final UUID id) {
        return unitRepository.findById(id)
                .map(unit -> unitMapper.updateUnitDTO(unit, new UnitDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UnitDTO unitDTO) {
        final Unit unit = new Unit();
        unitMapper.updateUnit(unitDTO, unit);
        return unitRepository.save(unit).getId();
    }

    public void update(final UUID id, final UnitDTO unitDTO) {
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        unitMapper.updateUnit(unitDTO, unit);
        unitRepository.save(unit);
    }

    public void delete(final UUID id) {
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        unitRepository.delete(unit);
    }

}
