package com.mulesoft.tools.migration.task.step;

import com.mulesoft.tools.migration.exception.MigrationStepException;
import org.jdom2.Attribute;
import org.jdom2.Element;

import static com.mulesoft.tools.migration.report.ReportCategory.RULE_APPLIED;

public class MoveAttributeToNewChildNode extends MigrationStep {

    private String attribute;
    private String childNode;

    public MoveAttributeToNewChildNode(String attribute, String childNode) {
        setAttribute(attribute);
        setChildNode(childNode);
    }

    public MoveAttributeToNewChildNode(){}

    public void execute() throws Exception {
        try {
            for (Element node : getNodes()) {
                Attribute att = node.getAttribute(getAttribute());
                if (att != null) {
                    Element child = node.getChild(getChildNode(), node.getNamespace());
                    if (child != null) {
                        node.removeAttribute(att);
                        child.setAttribute(att);

                        getReportingStrategy().log("Moved attribute " + att.getName() + "=\""+ att.getValue() + "\" to child node <" + child.getQualifiedName() + ">", RULE_APPLIED);
                    }
                    else {
                        Element newChild = new Element(getChildNode(), node.getNamespace());
                        node.removeAttribute(att);
                        newChild.setAttribute(att);
                        node.addContent(newChild);

                        getReportingStrategy().log("Moved attribute " + att.getName() + "=\""+ att.getValue() + "\" to new child node <" + newChild.getQualifiedName() + ">", RULE_APPLIED);
                    }
                }
            }
        } catch (Exception ex) {
            throw new MigrationStepException("Move attribute exception. " + ex.getMessage());
        }
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getChildNode() {
        return childNode;
    }

    public void setChildNode(String childNode) {
        this.childNode = childNode;
    }
}