import java.util.ArrayList;
 
public class Tree<T> {
        private Tree<T> parent;
        private ArrayList<Tree<T>> children;
        private T data;
 
        public Tree(Tree<T> parent, T data) {
                this.parent = parent;
                this.data = data;
                this.children = new ArrayList<Tree<T>>();

		if(parent != null) {
			parent.addChild(this);

		}
 
        }
 
        public void addChild(Tree<T> child) {
		child.setParent(this);
                children.add(child);
 
        }
 
        public ArrayList<Tree<T>> getChildren() {
                return children;
 
        }
 
        public void setData(T value) {
                this.data = value;
 
        }
 
        public T getData() {
                return data;
 
        }
 
        public void setParent(Tree<T> parent) {
                this.parent = parent;
 
        }
 
        public Tree<T> getParent() {
                return parent;
 
        }

	public String toString() {
		String str = "";
		str += data.toString();
		str += "\n-- tree --\n";
		for(Tree<T> t : getChildren()) {
			str += t.toString();

		}
		str += "\n-- --\n\n";

		return str;
	}
 
}
