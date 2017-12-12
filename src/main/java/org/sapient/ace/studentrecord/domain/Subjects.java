package org.sapient.ace.studentrecord.domain;

import org.sapient.ace.studentrecord.model.Subject;

public class Subjects {
	
	private Subject[] subject;

    public Subject[] getSubject ()
    {
        return subject;
    }

    public void setSubject (Subject[] subject)
    {
        this.subject = subject;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [subject = "+subject+"]";
    }

}
