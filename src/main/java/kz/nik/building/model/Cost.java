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
@Table(name = "costs")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal buildingCost;  // Строительство зданий
    private BigDecimal roadConstructionCost;  // Строительство дорог
    private BigDecimal taxes;  // Налоги
    private BigDecimal otherCosts;  // Прочие затраты
    private BigDecimal landPurchaseCost;  // Цена покупки участка

    private LocalDate date;  // Дата затрат

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;  // Связь с проектом
}