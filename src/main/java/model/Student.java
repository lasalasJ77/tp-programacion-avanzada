package model;

public class Student {
    public static final char DELIM = '\t';
    
    private Integer dni;
    private String lastName;
    private String name;
    private String address;
    private String phone;
    private String email;
    private boolean deleted;
    
    public Student() {}

    public Student(Integer dni, String lastName, String name, String email, String phone, String address, Boolean deleted) {
        this.dni = dni;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.deleted = deleted;
    }

    public Integer getDni() {
        return dni;
    }
    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return this.dni.toString() + '\t' + this.lastName + '\t' + this.name + '\t' + this.email + '\t' + this.phone + '\t' + this.address + '\t' + this.isDeleted();
    }
    
    public static Student str2Student(String[] fieldsStudent) {
        return new Student(
            Integer.parseInt(fieldsStudent[0]),
            fieldsStudent[1],
            fieldsStudent[2],
            fieldsStudent[3],
            fieldsStudent[4],
            fieldsStudent[5],
            Boolean.parseBoolean(fieldsStudent[6])
        );
    }
}
