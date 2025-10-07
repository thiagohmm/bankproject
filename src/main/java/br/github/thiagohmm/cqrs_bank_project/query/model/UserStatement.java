package br.github.thiagohmm.cqrs_bank_project.query.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("user_statements")
@Data
@NoArgsConstructor
public class UserStatement {
    @Id
    private String userId;
    private BigDecimal totalBalance;
    private List<HistoryItem> history;

    public UserStatement(String userId) {
        this.userId = userId;
        this.totalBalance = BigDecimal.ZERO;
        this.history = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    public static class HistoryItem {
        private String type;
        private BigDecimal value;
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime date;
    }
}