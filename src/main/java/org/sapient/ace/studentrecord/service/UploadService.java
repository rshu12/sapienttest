package org.sapient.ace.studentrecord.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sapient.ace.studentrecord.exception.DataException;
import org.sapient.ace.studentrecord.model.Student;
import org.sapient.ace.studentrecord.model.Subject;
import org.sapient.ace.studentrecord.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class UploadService {

	// @Autowired
	// private UploadDao uploadDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
	private static String UPLOADED_FOLDER = "E://rajat//file//xml_upload";

	private final StudentRepository studentRepository;

	@Inject
	public UploadService(final StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public Boolean uploadOperation(MultipartFile file)
			throws DataException, ParserConfigurationException, SAXException, IOException {

		// checkData()
		Path path = uploadFileLocal(file);
		processDataIntoDB(path);

		return false;
	}

	private void processDataIntoDB(Path path) throws ParserConfigurationException, SAXException, IOException {

		File file = path.toFile();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);

		doc.getDocumentElement().normalize();
		
		List<Student> students = createStudent(doc);

//		studentRepository.createStudentDB(nList);
    	studentRepository.save(students);

	}

	private List<Student> createStudent(Document doc) {
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		
		List<Student> students = new ArrayList<>();

		NodeList nList = doc.getElementsByTagName("Student");
		for(int i = 0; i< nList.getLength();i++) {
			Student student = new Student();
			Node node = nList.item(i);
			System.out.println("Current Element :" + node.getNodeName());
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				
				Element element = (Element)node;
				System.out.println("Student Id" + element.getAttribute("ID"));
				student.setId(element.getAttribute("ID"));

				System.out.println("Name :" + element.getElementsByTagName("Name").item(0).getTextContent());
				student.setName(element.getElementsByTagName("Name").item(0).getTextContent());
				System.out.println("Class :" + element.getElementsByTagName("Class").item(0).getTextContent());
				
				student.setClas(element.getElementsByTagName("Class").item(0).getTextContent());
				
				System.out.println("Subjects :" + element.getElementsByTagName("Subjects").item(0).getTextContent());
				NodeList sList = element.getElementsByTagName("Subjects");
				System.out.println("Total subjects ........."+sList.getLength());
				Set<Subject> subjects = new HashSet<>();
				for(int j = 0;j < sList.getLength(); j++) {
					Node sNode = sList.item(j);
					System.out.println("Current Element :" + sNode.getNodeName());
					if(sNode.getNodeType() == Node.ELEMENT_NODE) {
						Element element2 = (Element) sNode;
						String id = element2.getElementsByTagName("id").item(0).getTextContent();
						String sName = element2.getElementsByTagName("name").item(0).getTextContent();
						String sMarks = element2.getElementsByTagName("marks").item(0).getTextContent();
						System.out.println("Subject ID: " + id);
						System.out.println("Subject Name : " + sName);
						System.out.println("Subject  Marks: " + sMarks);
						Subject subject = new Subject(id,sName,sMarks,student);
						subjects.add(subject);
					}
				}
				
				student.setSubject(subjects);
				
				
				students.add(student);
			}
//			Student s = new Student();
//			System.out.println(doc.g);
		}
		

		System.out.println("----------------------------");
		System.out.println(nList.getLength());
		return students;
	}

	private Path uploadFileLocal(MultipartFile file) throws DataException {
		try {

			byte[] bytes = file.getBytes();
			File file2 = new File(file.getOriginalFilename());

			Path path = Paths.get(UPLOADED_FOLDER + File.separator + file2.getName());
			Files.write(path, bytes);

			LOGGER.info("File loaded sussessfully::Server File Location=" + path.getFileName());

			return path;

		} catch (Exception e) {
			String msg = new StringBuilder().append("You failed to upload => ").append(e.getMessage()).toString();
			LOGGER.error(msg, e);
			throw new DataException(msg);

		}
	}

}
