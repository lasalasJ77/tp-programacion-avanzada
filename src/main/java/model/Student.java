package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student {
    public static final char DELIM = ';';
    
    private Integer dni;
    private String lastName;
    private String name;
    private String address;
    private String phone;
    private String email;
    private boolean deleted;
    private LocalDate dateAdmission;
    private Double average;
    
    public Student() {}

    public Student(
        Integer dni,
        String lastName,
        String name,
        String email,
        String phone,
        String address,
        LocalDate dateAdmission,
        Double average,
        Boolean deleted
    ) {
        this.dni = dni;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.deleted = deleted;
        this.dateAdmission = dateAdmission;
        this.average = average;
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
    
    public LocalDate getDateAdmission() {
        return dateAdmission;
    }

    public void setDateAdmission(LocalDate dateAdmission) {
        this.dateAdmission = dateAdmission;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
    
    @Override
    public String toString() {
        return this.dni.toString() + Student.DELIM + 
            this.lastName + Student.DELIM +
            this.name + Student.DELIM +
            this.email + Student.DELIM +
            this.phone + Student.DELIM +
            this.address + Student.DELIM +
            this.dateAdmission + Student.DELIM +
            this.average + Student.DELIM +
            this.isDeleted();
    }
    
    
    
    private String ajust(String valor, int largo) {
        if (valor.length() > largo)
            return valor.substring(0, largo); // Truncar si excede
        return String.format("%-" + largo + "s", valor); // Rellenar con espacios
    }
    
    public String toFixedLengthString() {
        return String.join(String.valueOf(Student.DELIM),
            ajust(this.dni.toString(), 8),
            ajust(this.lastName, 50),
            ajust(this.name, 50),
            ajust(this.email, 100),
            ajust(this.phone, 15),
            ajust(this.address, 100),
            ajust(this.dateAdmission.toString(), 30),
            ajust(String.valueOf(this.average), 5),
            ajust(this.isDeleted() ? "true" : "false", 5)
        );
    }
    
    public static Student str2Student(String[] fieldsStudent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(fieldsStudent[6].trim(), formatter);
        return new Student(
            Integer.parseInt(fieldsStudent[0].trim()),
            fieldsStudent[1].trim(),
            fieldsStudent[2].trim(),
            fieldsStudent[3].trim(),
            fieldsStudent[4].trim(),
            fieldsStudent[5].trim(),
            date,
            Double.valueOf(fieldsStudent[7].trim()),
            Boolean.parseBoolean(fieldsStudent[8].trim())
        );
    }
}
