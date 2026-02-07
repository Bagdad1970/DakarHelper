package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.service.interfaces.HeaderCellService;
import io.github.bagdad.models.request.headercell.HeaderCellCreateRequest;
import io.github.bagdad.models.request.headercell.HeaderCellDeleteRequest;
import io.github.bagdad.models.request.headercell.HeaderCellUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/header-cells")
public class HeaderCellController {

    private final HeaderCellService service;

    public HeaderCellController(HeaderCellService service) {
        this.service = service;
    }

    @PostMapping
    public HeaderCell create(@RequestBody HeaderCellCreateRequest request) {
        HeaderCell headerCell = new HeaderCell();

        headerCell.setSubcategoryId(request.getSubcategoryId());
        headerCell.setOriginalName(request.getOriginalName());
        headerCell.setNormalizedName(request.getNormalizedName());
        headerCell.setCategory(request.getCategory());
        headerCell.setCellStatus(request.getCellStatus());

        return service.create(headerCell);
    }

    @PutMapping
    public HeaderCell update(@RequestBody HeaderCellUpdateRequest request) {
        HeaderCell headerCell = new HeaderCell();

        headerCell.setId(request.getId());
        headerCell.setSubcategoryId(request.getSubcategoryId());
        headerCell.setOriginalName(request.getOriginalName());
        headerCell.setNormalizedName(request.getNormalizedName());
        headerCell.setCategory(request.getCategory());
        headerCell.setCellStatus(request.getCellStatus());

        return service.update(headerCell);
    }

    @GetMapping
    public List<HeaderCell> findAll() {
        return service.findAll();
    }

    @DeleteMapping
    public void delete(@RequestBody HeaderCellDeleteRequest request) {
        Long id = request.getId();

        service.delete(id);
    }

}
