package kz.nik.building.service;


import kz.nik.building.model.Cost;
import kz.nik.building.model.Project;
import kz.nik.building.repository.CostRepository;
import kz.nik.building.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CostRepository costRepository;

    // Метод для создания проекта
    public Project createProject(String name) {
        Project project = Project.builder()
                .name(name)
                .income(BigDecimal.ZERO)
                .expenses(BigDecimal.ZERO)
                .profit(BigDecimal.ZERO)
                .build();
        return projectRepository.save(project);
    }

    // Метод для получения всех проектов
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Метод для получения конкретного проекта по id
    public Project getProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return project.get();
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    // Метод для добавления затрат в проект
    public void addCost(Long projectId, BigDecimal buildingCost, BigDecimal roadConstructionCost, BigDecimal taxes,
                        BigDecimal otherCosts, BigDecimal landPurchaseCost, LocalDate date) {
        Project project = getProject(projectId);

        // Создание объекта затрат
        Cost cost = Cost.builder()
                .buildingCost(buildingCost)
                .roadConstructionCost(roadConstructionCost)
                .taxes(taxes)
                .otherCosts(otherCosts)
                .landPurchaseCost(landPurchaseCost)
                .date(date)
                .project(project)
                .build();

        costRepository.save(cost);

        // Расчет и обновление расходов проекта
        BigDecimal totalCost = buildingCost.add(roadConstructionCost).add(taxes).add(otherCosts).add(landPurchaseCost);
        project.addExpenses(totalCost);
        project.calculateProfit();
        projectRepository.save(project);
    }
}