package model;

import org.semanticweb.owlapi.model.AxiomType;

public class MetaAxiom {
    
    private AxiomType axiomType;
    private Enum category;

    public AxiomType getAxiomType()
    {
        return axiomType;
    }

    public void setAxiomType(AxiomType axiomType)
    {
        this.axiomType = axiomType;
    }

    public Enum getCategory()
    {
        return category;
    }

    public void setCategory(Enum category)
    {
        this.category = category;
    }

    public MetaAxiom(AxiomType axiomType, Enum category)
    {
        this.axiomType = axiomType;
        this.category = category;
    }

    
}
