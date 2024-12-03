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
@Table(name = "incomes")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal landSaleIncome;  // Доход от продажи участка

    private LocalDate date;  // Дата дохода

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;  // Связь с проектом
}
