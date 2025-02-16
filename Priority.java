package taskmanagement;
import java.util.ArrayList;
import java.util.List;


public class Priority {
    String PriorityName;
    List<Priority> priorities;


    public Priority() {
        priorities = new ArrayList<>(); // Αρχικοποίηση της λίστας
        priorities.add(this); // Προσθήκη του αντικειμένου ως default
    }

    public List<Priority> getPriorities() { // Getter για τη λίστα priorities
        return priorities;
    }

    public String getName() {
            return this.PriorityName;
    }

    public void setName(String name) {
        this.PriorityName = name;
    }

    //priorities.add(new Priorities());
//check chatgtp for information about priorities defaul
}