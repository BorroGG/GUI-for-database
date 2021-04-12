package org.labs.service;

import org.labs.model.Equipment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private EquipmentService equipmentService;

    public void save(Equipment saveDTO) {
        Equipment equipment = mapper.map(saveDTO, Equipment.class);
        equipmentService.save(equipment);
    }

    public void update(Equipment updateDTO) {
        Equipment equipment = mapper.map(updateDTO, Equipment.class);
        equipmentService.update(equipment);
    }

    public List<Equipment> selectAllEquipments() {
        return equipmentService.selectAllEquipments();
    }

    public Equipment selectEquipmentById(Equipment selectDTO) {
        Equipment equipment = mapper.map(selectDTO, Equipment.class);
        return equipmentService.selectEquipmentById(equipment);
    }

    public void delete(Equipment deleteDTO) {
        Equipment equipment = mapper.map(deleteDTO, Equipment.class);
        equipmentService.delete(equipment);
    }
}
