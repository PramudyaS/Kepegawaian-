package pravin.kepegawaian.Model;

public class User {
    String id_employee,name,gender,birth_place,birth_date,email,photo
            ,id_job,id_division,contract_begin,contract_end,number_of_leave,password,job,division;

    public String getId_employee()
    {
        return id_employee;
    }

    public String getName()
    {
        return name;
    }

    public String getGender()
    {
        return gender;
    }

    public String getBirth_place()
    {
        return birth_place;
    }

    public String getBirth_date()
    {
        return birth_date;
    }

    public String getPhoto()
    {
        return photo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getJob(){
        return job;
    }

    public String getDivision()
    {
        return division;
    }
}
