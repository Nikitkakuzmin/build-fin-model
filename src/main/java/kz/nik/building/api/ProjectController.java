package kz.nik.building.api;

/*import kz.nik.building.model.Cost;
import kz.nik.building.model.Income;*/

import kz.nik.building.model.Project;
import kz.nik.building.model.Transaction;
import kz.nik.building.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;


    @GetMapping("/")
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "index";  // Отображает список проектов
    }

    @GetMapping("/projects/create")
    public String createProjectForm() {
        return "create";  // Страница создания проекта
    }

    @PostMapping("/projects")
    public String createProject(@RequestParam String name) {
        projectService.createProject(name);
        return "redirect:/";  // После создания проекта перенаправляем на главную страницу
    }

    @GetMapping("/projects/{id}/transactions")
    public String addTransactionForm(@PathVariable Long id, Model model) {
        Project project = projectService.getProject(id);
        model.addAttribute("project", project);
        return "add-transaction";  // Страница добавления затрат
    }

    @PostMapping("/projects/{id}/transactions")
    public String addTransaction(@PathVariable Long id,
                                 @RequestParam BigDecimal buildingCost,
                                 @RequestParam BigDecimal roadConstructionCost,
                                 @RequestParam BigDecimal taxes,
                                 @RequestParam BigDecimal otherCosts,
                                 @RequestParam BigDecimal landPurchaseCost,
                                 @RequestParam BigDecimal landSaleIncome,
                                 @RequestParam String date) {
        projectService.addTransaction(id, buildingCost, roadConstructionCost, taxes, otherCosts, landPurchaseCost,
                landSaleIncome, LocalDate.parse(date));
        return "redirect:/";
    }

    @GetMapping("/projects/{id}/monthly-details")
    public String getMonthlyDetails(@PathVariable Long id, Model model) {
        Project project = projectService.getProject(id); // Получаем проект
        List<Transaction> transactions = projectService.getMonthlyTransactions(id);
        Map<String, Map<String, BigDecimal>> transactionsByMonth = projectService.getTransactionsByMonth(id); // Получаем сгруппированные и подсчитанные транзакции по месяцам

        model.addAttribute("project", project);
        model.addAttribute("transactions", transactions);
        model.addAttribute("transactionsByMonth", transactionsByMonth);

        return "monthly-details";  // Переход на страницу с таблицей
    }

}
