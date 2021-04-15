package org.labs.service;

import org.labs.mapper.EquipmentMapper;
import org.labs.model.Equipment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private EquipmentMapper equipmentMapper;

    public void save(Equipment saveDTO) {
        Equipment equipment = mapper.map(saveDTO, Equipment.class);
        equipmentMapper.save(equipment);
    }

    public void update(Equipment updateDTO) {
        Equipment equipment = mapper.map(updateDTO, Equipment.class);
        equipmentMapper.update(equipment);
    }

    public List<Equipment> selectAllEquipments() {
        return equipmentMapper.selectAllEquipments();
    }

    public Equipment selectEquipmentByNumber(Equipment selectDTO) {
        Equipment equipment = mapper.map(selectDTO, Equipment.class);
        return equipmentMapper.selectEquipmentByNumber(equipment);
    }

    public void delete(Equipment deleteDTO) {
        Equipment equipment = mapper.map(deleteDTO, Equipment.class);
        equipmentMapper.delete(equipment);
    }
}
