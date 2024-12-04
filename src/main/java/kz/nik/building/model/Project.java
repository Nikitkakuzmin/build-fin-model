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
    /*private BigDecimal transaction = BigDecimal.ZERO;*/
    private BigDecimal totalIncome = BigDecimal.ZERO;  // Общий доход
    private BigDecimal totalExpenses = BigDecimal.ZERO;  // Общие расходы
    private BigDecimal profit = BigDecimal.ZERO;  // Прибыль

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Transaction> transactions;  // Список затрат


    public void addTransaction(BigDecimal totalCost, BigDecimal totalIncome) {
        if (this.totalExpenses == null) {
            this.totalExpenses = BigDecimal.ZERO;
        }
        if (this.totalIncome == null) {
            this.totalIncome = BigDecimal.ZERO;
        }

        this.totalExpenses = this.totalExpenses.add(totalCost);  // Обновляем общие расходы
        this.totalIncome = this.totalIncome.add(totalIncome);  // Обновляем общий доход
    }

}