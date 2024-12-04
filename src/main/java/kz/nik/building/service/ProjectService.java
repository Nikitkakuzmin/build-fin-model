package kz.nik.building.service;


/*import kz.nik.building.model.Cost;
import kz.nik.building.model.Income;*/
import kz.nik.building.model.Project;
import kz.nik.building.model.Transaction;
/*import kz.nik.building.repository.CostRepository;
import kz.nik.building.repository.IncomeRepository;*/
import kz.nik.building.repository.ProjectRepository;
import kz.nik.building.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final TransactionRepository transactionRepository;

    // Метод для создания проекта
    public Project createProject(String name) {
        Project project = Project.builder()
                .name(name)
                /*.transaction(BigDecimal.ZERO)*/
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
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    // Метод для добавления транзакции
    public void addTransaction(Long projectId, BigDecimal buildingCost, BigDecimal roadConstructionCost, BigDecimal taxes,
                               BigDecimal otherCosts, BigDecimal landPurchaseCost, BigDecimal landSaleIncome, LocalDate date) {
        // Получаем проект по его ID
        Project project = getProject(projectId);

        // Создание объекта транзакции
        Transaction transaction = Transaction.builder()
                .buildingCost(buildingCost)
                .roadConstructionCost(roadConstructionCost)
                .taxes(taxes)
                .otherCosts(otherCosts)
                .landPurchaseCost(landPurchaseCost)
                .landSaleIncome(landSaleIncome)
                .date(date)
                .project(project)
                .build();

        // Сохраняем транзакцию в базе данных
        transactionRepository.save(transaction);

        // Расчет общей суммы расходов
        BigDecimal totalCost = transaction.getTotalCost();

        // Расчет дохода (доход от продажи участка)
        BigDecimal totalIncome = transaction.getTotalIncome();

        // Обновляем данные проекта (доходы и расходы)
        project.addTransaction(totalCost, totalIncome);

        // Расчет прибыли: доход от продажи участка - общие расходы
        BigDecimal profit = totalIncome.subtract(totalCost);
        project.setProfit(project.getProfit().add(profit)); // Прибавляем к текущей прибыли проекта

        // Сохраняем обновленный проект в базе данных
        projectRepository.save(project);
    }


    public List<Transaction> getMonthlyTransactions(Long projectId) {
        return transactionRepository.findByProjectIdOrderByDateAsc(projectId);  // Сортировка затрат по дате
    }

}