package EdTech.Course.dto;

import EdTech.Course.model.CourseMaterial;
import EdTech.Course.model.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
	private String name;
    private String description;
    private String instructor;
    private Long amount;
    private List<CourseMaterial> courseMaterial;
    private List<Enrollment> enrollments;
}
