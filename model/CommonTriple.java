package model;

import org.semanticweb.owlapi.model.OWLObject;

// Referenced classes of package model:
//            Triple

public class CommonTriple extends Triple{
    
    private OWLObject subject;
    private OWLObject predicate;
    private OWLObject object;

    @Override
    public String toString()
    {
        return (new StringBuilder())
                .append("[")
                .append(getSubject().toString())
                .append(" ").append(getPredicate().toString())
                .append(" ").append(getObject().toString())
                .append("]").toString();
    }

    public CommonTriple(OWLObject subject, OWLObject predicate, OWLObject object)
    {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public OWLObject getObject()
    {
        return object;
    }

    public void setObject(OWLObject object)
    {
        this.object = object;
    }

    public OWLObject getPredicate()
    {
        return predicate;
    }

    public void setPredicate(OWLObject predicate)
    {
        this.predicate = predicate;
    }

    public OWLObject getSubject()
    {
        return subject;
    }

    public void setSubject(OWLObject subject)
    {
        this.subject = subject;
    }

    
}
