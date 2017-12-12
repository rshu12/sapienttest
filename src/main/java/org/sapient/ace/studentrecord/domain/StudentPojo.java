package org.sapient.ace.studentrecord.domain;

public class StudentPojo {
	
	private Students Students;

    public Students getStudents ()
    {
        return Students;
    }

    public void setStudents (Students Students)
    {
        this.Students = Students;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Students = "+Students+"]";
    }

}
