package EdTech.Course.dto;

import java.util.List;

import EdTech.Course.model.CourseMaterial;
import EdTech.Course.model.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	
	private Long id;
    private String name;
    private String username;
    private String email;
    private Long contact;
    private String address;
    private String role;
    private String password;
}
