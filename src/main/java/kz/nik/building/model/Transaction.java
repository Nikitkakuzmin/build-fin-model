package kz.nik.building.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal buildingCost;  // Строительство зданий
    private BigDecimal roadConstructionCost;  // Строительство дорог
    private BigDecimal taxes;  // Налоги
    private BigDecimal otherCosts;  // Прочие затраты
    private BigDecimal landPurchaseCost;  // Цена покупки участка
    private BigDecimal landSaleIncome;  // Доход от продажи участка

    private LocalDate date;  // Дата затрат

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;  // Связь с проектом

    // Метод для подсчета общих расходов для транзакции
    public BigDecimal getTotalCost() {
        return buildingCost.add(roadConstructionCost)
                .add(taxes)
                .add(otherCosts)
                .add(landPurchaseCost);
    }

    // Метод для подсчета дохода от продажи участка
    public BigDecimal getTotalIncome() {
        return landSaleIncome;
    }
}