package scripty;

import java.io.*;

public class FileService {

    public void saveEmployee(Employee employee, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(employee);
        }
    }

    public Employee loadEmployee(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Employee) ois.readObject();
        }
    }
}
