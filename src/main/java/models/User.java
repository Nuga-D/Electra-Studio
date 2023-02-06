package models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "user_id")
    private Long id;

//    @NotEmpty(message = "User's first name cannot be empty")
    private String firstName;

//    @NotEmpty(message = "User's last name cannot be empty")
    private String lastName;

//    @NotEmpty(message = "User's email cannot be empty.")
//    @Email(message = "Please enter a valid Email Address")
    @Column(unique = true)
    private String email;

//    @NotEmpty(message = "User's password cannot be empty.")
//    @Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@.$%^&*-]).{8,}$",message=" Password length must be at least 8; must contain at least 1 uppercase and 1 special character")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "date_created")
    private String dateCreated;

//    @NotEmpty(message = "User's role cannot be empty.")
    private String role;

//    @Builder.Default
//    private Boolean disabled = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public Boolean getDisabled() {
//        return disabled;
//    }
//
//    public void setDisabled(Boolean disabled) {
//        this.disabled = disabled;
//    }
}
