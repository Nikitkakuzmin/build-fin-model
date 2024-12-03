package kz.nik.building.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Название проекта
    private BigDecimal income = BigDecimal.ZERO;  // Доходы
    private BigDecimal expenses = BigDecimal.ZERO;  // Все расходы
    private BigDecimal profit = BigDecimal.ZERO;  // Прибыль

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Cost> costs;  // Список затрат

    // Метод для добавления расходов
    public void addExpenses(BigDecimal amount) {
        this.expenses = this.expenses.add(amount);
    }

    // Метод для пересчета прибыли
    public void calculateProfit() {
        this.profit = this.income.subtract(this.expenses);
    }
}