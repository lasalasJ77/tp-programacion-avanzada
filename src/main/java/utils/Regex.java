package utils;

import Exceptions.AddressException;
import Exceptions.DNIException;
import Exceptions.EmailException;
import Exceptions.LastNameException;
import Exceptions.NameException;
import Exceptions.PhoneException;
import model.Student;

public class Regex {
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String REGEX_DNI = "^\\d{7,8}$";
    public static final String REGEX_TELEFONO = "^\\d{8,15}$";
    public static final String REGEX_NAME_LAST_NAME = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s'-]{2,50}$";
    public static final String REGEX_ADDRESS = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s'-]{2,30}$";
    
    public static boolean isEmailValid(String email) {
        return email != null && email.matches(REGEX_EMAIL);
    }

    public static boolean esDniValid(Integer dni) {
        return dni != null && dni.toString().matches(REGEX_DNI);
    }

    public static boolean isPhoneValid(String phone) {
        return phone != null && phone.matches(REGEX_TELEFONO);
    }
    
    public static boolean isNameValid(String name) {
        return name != null && name.matches(REGEX_NAME_LAST_NAME);
    }
    
    public static boolean isLastNameValid(String lastName) {
        return lastName != null && lastName.matches(REGEX_NAME_LAST_NAME);
    }
    
    public static boolean isAddressValid(String address) {
        return address != null && address.matches(REGEX_ADDRESS);
    }
    
    public static void checkStudent(Student student) throws EmailException, DNIException, PhoneException, NameException, LastNameException, AddressException {
        if (!esDniValid(student.getDni())) {
            throw new DNIException("DNI inválido");
        }
        if (!isAddressValid(student.getAddress())) {
            throw new AddressException("Dirección inválido");
        }
        if (!isNameValid(student.getName())) {
            throw new NameException("Nombre inválido");
        }
        if (!isLastNameValid(student.getLastName())) {
            throw new LastNameException("Nombre inválido");
        }
        if (!isPhoneValid(student.getPhone())) {
            throw new PhoneException("Teléfono inválido");
        }
        if (!isEmailValid(student.getEmail())) {
            throw new EmailException("Email inválido");
        }
    }
}
