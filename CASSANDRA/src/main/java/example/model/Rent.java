package example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class Rent {
    private String rentID;
    private String clientID;
    private String vmID;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private double rentCost;
}
