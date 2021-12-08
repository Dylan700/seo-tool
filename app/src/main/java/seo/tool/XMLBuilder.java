package seo.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBuilder {

    private class Node {
        private String name;
        private String text;
        private Map<String, String> attributes = new HashMap<>();
        private Node parent;
        private List<Node> children = new ArrayList<Node>();

        public Node(String name) {
            this.name = name;
        }

        public void addAttribute(String name, String value) {
            attributes.put(name, value);
        }

        public String getName() {
            return name;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public String getText() {
            return text;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public void addText(String text) {
            this.text = text;
        }
    }

    private Node currentNode;

    public XMLBuilder(String name) {
        this.currentNode = new Node(name);
    }

    public XMLBuilder addElement(String name){
        Node child = new Node(name); 
        child.setParent(currentNode);
        currentNode.addChild(child);
        currentNode = child;
        return this;
    }

    public XMLBuilder addAttribute(String name, String value){
        currentNode.addAttribute(name, value);
        return this;
    }

    public XMLBuilder addText(String text){
        currentNode.addText(text);
        return this;
    }

    public XMLBuilder up(){
        if(currentNode.getParent() != null){
            currentNode = currentNode.getParent();
        }
        return this;
    }

    public String build(){
        while(currentNode.getParent() != null){
            currentNode = currentNode.getParent();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        build(sb, currentNode);
        return sb.toString();
    }

    // recursive method to build the XML string
    private void build(StringBuilder sb, Node node){
        addStartNode(sb, node);
        if(node.getChildren().size() > 0){
            for(Node child : node.getChildren()){
                build(sb, child);
            }
        }
        addEndNode(sb, node);
    }

    private void addStartNode(StringBuilder sb, Node node){
        sb.append("<");
        sb.append(node.getName());
        Map<String, String> attributes = node.getAttributes();
        for(Map.Entry<String, String> set : attributes.entrySet()){
            sb.append(" ");
            sb.append(set.getKey());
            sb.append("=\"");
            sb.append(set.getValue());
            sb.append("\"");
        }
        sb.append(">");
        if(node.getText() != null){
            sb.append(node.getText());
        }
    }

    private void addEndNode(StringBuilder sb, Node node){
        sb.append("</");
        sb.append(node.getName());
        sb.append(">");
    }
}
