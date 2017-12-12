package org.sapient.ace.studentrecord.domain;

import org.sapient.ace.studentrecord.model.Student;

public class Students {
	
	private Student[] Student;

    public Student[] getStudent ()
    {
        return Student;
    }

    public void setStudent (Student[] Student)
    {
        this.Student = Student;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Student = "+Student+"]";
    }

}
