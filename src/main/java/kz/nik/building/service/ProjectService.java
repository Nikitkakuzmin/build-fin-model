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
import java.util.stream.Collectors;

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

    // Группировка транзакций по месяцам (по году и месяцу)
    public Map<String, Map<String, BigDecimal>> getTransactionsByMonth(Long projectId) {
        List<Transaction> transactions = transactionRepository.findByProjectIdOrderByDateAsc(projectId);

        // Группируем транзакции по году и месяцу
        Map<String, List<Transaction>> groupedByMonth = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getDate().getYear() + "-" + String.format("%02d", transaction.getDate().getMonthValue())
                ));

        // Для каждого месяца вычисляем суммы по категориям
        Map<String, Map<String, BigDecimal>> monthDetailsMap = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : groupedByMonth.entrySet()) {
            String month = entry.getKey();
            List<Transaction> monthTransactions = entry.getValue();

            Map<String, BigDecimal> categorySums = new HashMap<>();
            categorySums.put("BuildingCost", BigDecimal.ZERO);
            categorySums.put("RoadCost", BigDecimal.ZERO);
            categorySums.put("Taxes", BigDecimal.ZERO);
            categorySums.put("OtherCosts", BigDecimal.ZERO);
            categorySums.put("LandPurchaseCost", BigDecimal.ZERO);
            categorySums.put("LandSaleIncome", BigDecimal.ZERO);

            // Суммируем по категориям
            for (Transaction transaction : monthTransactions) {
                categorySums.put("BuildingCost", categorySums.get("BuildingCost").add(transaction.getBuildingCost()));
                categorySums.put("RoadCost", categorySums.get("RoadCost").add(transaction.getRoadConstructionCost()));
                categorySums.put("Taxes", categorySums.get("Taxes").add(transaction.getTaxes()));
                categorySums.put("OtherCosts", categorySums.get("OtherCosts").add(transaction.getOtherCosts()));
                categorySums.put("LandPurchaseCost", categorySums.get("LandPurchaseCost").add(transaction.getLandPurchaseCost()));
                categorySums.put("LandSaleIncome", categorySums.get("LandSaleIncome").add(transaction.getLandSaleIncome()));
            }

            // Сохраняем суммы для этого месяца
            monthDetailsMap.put(month, categorySums);
        }

        return monthDetailsMap;
    }

}