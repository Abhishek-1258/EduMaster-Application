package EdTech.Course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long userId;
    private Long courseId;
    private String date;
    private Long amount;
}
