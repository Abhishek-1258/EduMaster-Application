package EdTech.Course.service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import EdTech.Course.dto.CourseDto;
import EdTech.Course.dto.Payment;
import EdTech.Course.dto.UserDto;
import EdTech.Course.feign.PaymentService;
import EdTech.Course.feign.UserService;
import EdTech.Course.model.Course;
import EdTech.Course.model.CourseMaterial;
import EdTech.Course.model.Enrollment;
import EdTech.Course.repository.CourseRepository;
import EdTech.Course.repository.EnrollmentRepository;

@Service
public class CourseService {
	

	private final RestTemplate restTemplate;
	
	private final CourseRepository courseRepository;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	public CourseService(CourseRepository courseRepository,RestTemplate restTemplate) {
		this.courseRepository = courseRepository;
		this.restTemplate = restTemplate;
	}

	@Autowired
	EnrollmentRepository enrollmentRepository;

	public List<Course> getAllCourses(){
		return courseRepository.findAll();
	}

	public Course getCourseById(Long id) {
		return courseRepository.findById(id).orElse(null);
	}


	public Course getCourseByName(String name) {
		return courseRepository.findByName(name);
	}


	public List<CourseMaterial> getCourseMaterialByCourseId(Long id){
		List<CourseMaterial> courseMaterials = getCourseById(id).getCourseMaterial();
		return courseMaterials;
	}


	public Course getCourseByInstructor(String instructor) {
		return courseRepository.findByInstructor(instructor);
	}



	public void createCourse(CourseDto courseDto) {
		Course course = Course.builder().name(courseDto.getName())
				.description(courseDto.getDescription())
				.instructor(courseDto.getInstructor())
				.amount(courseDto.getAmount()).build();


		List<CourseMaterial> materials = courseDto.getCourseMaterial().stream()
				.map(dto -> CourseMaterial.builder()
						.type(dto.getType())
						.description(dto.getDescription())
						.course(course)
						.build()).collect(Collectors.toList());
		
		
		List<Enrollment> enrollments = courseDto.getEnrollments().stream()
				.map(dto -> Enrollment.builder()
						.userId(dto.getUserId())
						.course(course)
						.build()).collect(Collectors.toList());
		
		course.setCourseMaterial(materials);
		course.setEnrollment(enrollments);
		
		
		courseRepository.save(course);
//		
	}

	public void updateCourse(Long id, CourseDto updatedCourseDto) {
		Course existingCourse = getCourseById(id);
		if(existingCourse != null) {
			existingCourse.setName(updatedCourseDto.getName());
			existingCourse.setInstructor(updatedCourseDto.getInstructor());
			existingCourse.setAmount(updatedCourseDto.getAmount());
			existingCourse.setDescription(updatedCourseDto.getDescription());
		}
		courseRepository.save(existingCourse);
	}


	public void deleteCourse(Long id) {
		courseRepository.deleteById(id);
		
	}

	public void createEnrollmentForCourse(Long courseId, Long userId) {
		String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
		Object object = userService.getUserById(token,userId);
		
		if(object == null) {
			throw new RuntimeException();
		}
		Course course = getCourseById(courseId);
		if(course == null) {
			throw new RuntimeException();
		}
		
		Enrollment enrollment = Enrollment.builder()
				.userId(userId)
				.course(course)
				.build();
		enrollmentRepository.save(enrollment);
				
		Payment payment = Payment.builder()
				.amount(course.getAmount())
				.courseId(courseId)
				.date(new Date().toString())
				.userId(userId)
				.build();
			
		paymentService.createPayment(payment);
		
	}

}
