/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Programmer
 */
public class MoveTreeNode {
    MoveTreeNode parent;
    Move element;
    List<MoveTreeNode> children;
    boolean root;

   
    public MoveTreeNode() {
       this.parent = null;
       this.element = null;
       this.children = new ArrayList<>();
    }

    public void addChild(MoveTreeNode node) {
        children.add(node);
        
    }
    
    

    public Move getElement() {
        return element;
    }

    public void setElement(Move element) {
        this.element = element;
    }

    public Iterator<MoveTreeNode> getChildren() {
        if(children == null || children.isEmpty()) {
           return Collections.emptyIterator();
        }
        return children.iterator();
    }

    public void setChildren(List<MoveTreeNode> children) {
        this.children = children;
    }

    public MoveTreeNode getParent() {
        return parent;
    }

    public void setParent(MoveTreeNode parent) {
        root = true;
        this.parent = parent;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
    
    
}
